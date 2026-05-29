import torch
import itertools
from util.image_pool import ImagePool
from .base_model import BaseModel
from . import networks


class AttenEmbGANModel(BaseModel):
    @staticmethod
    def modify_commandline_options(parser, is_train=True):
        parser.set_defaults(no_dropout=True)  # default CycleGAN did not use dropout
        if is_train:
            parser.add_argument('--lambda_identity', type=float, default=0.5, help='use identity mapping. Setting lambda_identity other than 0 has an effect of scaling the weight of the identity mapping loss. For example, if the weight of the identity loss should be 10 times smaller than the weight of the reconstruction loss, please set lambda_identity = 0.1')
            parser.add_argument('--lambda_content', type=float, default=10.0, help='weight for struct loss.')

        return parser

    def __init__(self, opt):
        BaseModel.__init__(self, opt)
        # specify the training losses you want to print out. The training/test scripts will call <BaseModel.get_current_losses>
        self.loss_names = ['Dis', 'G', 'idt', 'con']
        # specify the images you want to save/display. The training/test scripts will call <BaseModel.get_current_visuals>
        visual_names = ['real_A', 'fake_B', 'o1_b', 'o2_b', 'o3_b', 'o4_b', 'o5_b', 'o6_b', 'o7_b', 'o8_b', 'o9_b', 'o10_b',
        'a1_b', 'a2_b', 'a3_b', 'a4_b', 'a5_b', 'a6_b', 'a7_b', 'a8_b', 'a9_b', 'a10_b', 'i1_b', 'i2_b', 'i3_b', 'i4_b', 'i5_b', 
        'i6_b', 'i7_b', 'i8_b', 'i9_b', 'con', 'tex', 'struct', 'org']
        if self.isTrain:
            if self.opt.lambda_identity > 0.0:  # if identity loss is used, we also visualize idt=Gen(B)
                visual_names.append('idt')
        if self.opt.saveDisk:
            self.visual_names = ['real_A', 'fake_B', 'a10_b', 'con', 'tex', 'struct', 'org']
        elif self.opt.saveFake:
            self.visual_names = ['fake_B']
        else:
            self.visual_names = visual_names  # combine visualizations for A and B
        # specify the models you want to save to the disk. The training/test scripts will call <BaseModel.save_networks> and <BaseModel.load_networks>.
        if self.isTrain:
            self.model_names = ['Gen', 'Dis']
        else:  # during test time, only load Gs
            self.model_names = ['Gen']

        # define networks (both Generators and discriminators)
        # The naming is different from those used in the paper.
        # Code (vs. paper): G_A (G), G_B (F), D_A (D_Y), D_B (D_X)
        self.netGen = networks.define_G(opt.input_nc, opt.output_nc, opt.ngf, 'our', opt.norm,
                                        not opt.no_dropout, opt.init_type, opt.init_gain, self.gpu_ids)

        if self.isTrain:  # define discriminators
            self.netDis = networks.define_D(opt.output_nc, opt.ndf, opt.netD,
                                            opt.n_layers_D, opt.norm, opt.init_type, opt.init_gain, self.gpu_ids)

        if self.isTrain:
            if opt.lambda_identity > 0.0:  # only works when input and output images have the same number of channels
                assert(opt.input_nc == opt.output_nc)
            self.fake_pool = ImagePool(opt.pool_size)  # create image buffer to store previously generated images
            # define loss functions
            self.criterionGAN = networks.GANLoss(opt.gan_mode).to(self.device)  # define GAN loss.
            self.criterionIdt = torch.nn.L1Loss()
            self.criterionCon = torch.nn.L1Loss()
            # initialize optimizers; schedulers will be automatically created by function <BaseModel.setup>.
            self.optimizer_G = torch.optim.Adam(itertools.chain(self.netGen.parameters()), lr=opt.lr, betas=(opt.beta1, 0.999))
            self.optimizer_D = torch.optim.Adam(itertools.chain(self.netDis.parameters()), lr=opt.lr, betas=(opt.beta1, 0.999))
            self.optimizers.append(self.optimizer_G)
            self.optimizers.append(self.optimizer_D)

    def set_input(self, input):
        """Unpack input data from the dataloader and perform necessary pre-processing steps.
        Parameters:
            input (dict): include the data itself and its metadata information.
        The option 'direction' can be used to swap domain A and domain B.
        """
        AtoB = self.opt.direction == 'AtoB'
        self.real_A = input['A' if AtoB else 'B'].to(self.device)
        self.real_B = input['B' if AtoB else 'A'].to(self.device)
        self.image_paths = input['A_paths' if AtoB else 'B_paths']

    def forward(self):
        """Run forward pass; called by both functions <optimize_parameters> and <test>."""
        self.fake_B, self.o1_b, self.o2_b, self.o3_b, self.o4_b, self.o5_b, self.o6_b, self.o7_b, self.o8_b, self.o9_b, self.o10_b, \
        self.a1_b, self.a2_b, self.a3_b, self.a4_b, self.a5_b, self.a6_b, self.a7_b, self.a8_b, self.a9_b, self.a10_b, \
        self.i1_b, self.i2_b, self.i3_b, self.i4_b, self.i5_b, self.i6_b, self.i7_b, self.i8_b, self.i9_b = self.netGen(self.real_A)  # G_A(A)

        # output
        self.con = self.o4_b + self.o5_b + self.o6_b + self.o7_b + self.o8_b + self.o9_b + self.o10_b
        self.struct = self.o4_b + self.o5_b + self.o6_b + self.o7_b + self.o8_b + self.o9_b
        self.tex = self.o1_b + self.o2_b + self.o3_b
        self.org = self.o10_b

    def backward_D_basic(self, netD, real, fake):
        """Calculate GAN loss for the discriminator
        Parameters:
            netD (network)      -- the discriminator D
            real (tensor array) -- real images
            fake (tensor array) -- images generated by a generator
        Return the discriminator loss.
        We also call loss_D.backward() to calculate the gradients.
        """
        # Real
        pred_real = netD(real)
        loss_D_real = self.criterionGAN(pred_real, True)
        # Fake
        pred_fake = netD(fake.detach())
        loss_D_fake = self.criterionGAN(pred_fake, False)
        # Combined loss and calculate gradients
        loss_D = (loss_D_real + loss_D_fake) * 0.5
        loss_D.backward()
        return loss_D

    def backward_Dis(self):
        """Calculate GAN loss for discriminator D_A"""
        fake_B = self.fake_pool.query(self.fake_B)
        self.loss_Dis = self.backward_D_basic(self.netDis, self.real_B, fake_B)

    def backward_G(self):
        """Calculate the loss for generator"""
        lambda_idt = self.opt.lambda_identity
        lambda_con = self.opt.lambda_content

        # Identity loss
        if lambda_idt > 0:
            # G_A should be identity if real_B is fed: ||G_A(B) - B||
            self.idt, _, _, _, _, _, _, _, _, _, _, \
            _, _, _, _, _, _, _, _, _, _, \
            _, _, _, _, _, _, _, _, _ = self.netGen(self.real_B)
            self.loss_idt = self.criterionIdt(self.idt, self.real_B) * lambda_idt
        else:
            self.loss_idt = 0

        # content loss
        if lambda_con > 0:
            self.loss_con = self.criterionCon(self.con, self.real_A) * lambda_con
        else:
            self.loss_con = 0

        # GAN loss D_A(G_A(A))
        self.loss_Gen = self.criterionGAN(self.netDis(self.fake_B), True)
        # combined loss and calculate gradients
        self.loss_G = self.loss_Gen + self.loss_idt + self.loss_con
        self.loss_G.backward()

    def optimize_parameters(self):
        """Calculate losses, gradients, and update network weights; called in every training iteration"""
        # forward
        self.forward()      # compute fake images and reconstruction images.
        # Gen
        self.set_requires_grad([self.netDis], False)  # Ds require no gradients when optimizing Gs
        self.optimizer_G.zero_grad()  # set Gen's gradients to zero
        self.backward_G()             # calculate gradients for Gen
        self.optimizer_G.step()       # update Gen's weights
        # D_A and D_B
        self.set_requires_grad([self.netDis], True)
        self.optimizer_D.zero_grad()   # set Dis's gradients to zero
        self.backward_Dis()      # calculate gradients for Dis
        self.optimizer_D.step()  # update Dis's weights
