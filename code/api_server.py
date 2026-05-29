"""
AI服务API - 刺绣图案生成服务
提供图片转刺绣和文字转刺绣的HTTP接口
"""
from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import os
import sys
import io
import base64
import requests
import tempfile
from PIL import Image, ImageDraw, ImageFont
import torch
import numpy as np
from pathlib import Path
import logging
import traceback

# 添加项目根目录到Python路径
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from options.test_options import TestOptions
from models import create_model
from util import util

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)  # 允许跨域请求

# 全局变量存储模型
model = None
opt = None

# 配置
TEMP_DIR = tempfile.gettempdir()
RESULTS_DIR = './results/api_results'
os.makedirs(RESULTS_DIR, exist_ok=True)


def init_model():
    """初始化AI模型"""
    global model, opt
    try:
        logger.info("初始化AI模型...")

        # 直接构造参数对象，不使用命令行解析
        import argparse
        opt = argparse.Namespace()

        # 从 test_opt.txt 设置所有必需参数
        opt.dataroot = './datasets/embs'  # 占位符，API模式不需要
        opt.name = 'AttnEmbGAN_st_10'
        opt.model = 'AttenEmbGAN'  # 使用 AttenEmbGAN 模型
        opt.checkpoints_dir = './checkpoints'
        opt.gpu_ids = []  # 使用 CPU 模式（如果有GPU，改为 [0]）
        opt.input_nc = 3
        opt.output_nc = 3
        opt.ngf = 64
        opt.ndf = 64
        opt.netG = 'resnet_9blocks'
        opt.netD = 'basic'
        opt.norm = 'instance'
        opt.no_dropout = True
        opt.init_type = 'normal'
        opt.init_gain = 0.02
        opt.dataset_mode = 'single'
        opt.direction = 'AtoB'
        opt.serial_batches = True
        opt.num_threads = 0
        opt.batch_size = 1
        opt.load_size = 1024
        opt.crop_size = 1024
        opt.preprocess = 'resize_and_crop'
        opt.no_flip = True
        opt.display_id = -1
        opt.epoch = 'latest'
        opt.load_iter = 0
        opt.verbose = False
        opt.isTrain = False
        opt.eval = True
        opt.saveDisk = True
        opt.saveFake = False
        opt.phase = 'test'
        opt.max_dataset_size = float('inf')
        opt.display_winsize = 256
        opt.aspect_ratio = 1.0
        opt.suffix = ''

        # 创建模型
        model = create_model(opt)
        model.setup(opt)
        model.eval()

        logger.info(f"✅ 模型加载成功: {opt.name}")
        return True

    except Exception as e:
        logger.error(f"❌ 模型初始化失败: {str(e)}")
        logger.error(traceback.format_exc())
        return False


def download_image_from_url(url):
    """
    从URL下载图片（支持阿里云OSS等）

    Args:
        url: 图片URL

    Returns:
        PIL.Image: 下载的图片对象
    """
    try:
        logger.info(f"正在从URL下载图片: {url}")

        # 设置超时和请求头
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        }

        response = requests.get(url, headers=headers, timeout=30)
        response.raise_for_status()

        # 从响应内容创建图片
        image = Image.open(io.BytesIO(response.content))
        logger.info(f"图片下载成功，尺寸: {image.size}")

        return image

    except requests.exceptions.Timeout:
        logger.error(f"下载图片超时: {url}")
        raise Exception("图片下载超时，请检查网络连接")
    except requests.exceptions.RequestException as e:
        logger.error(f"下载图片失败: {str(e)}")
        raise Exception(f"无法下载图片: {str(e)}")
    except Exception as e:
        logger.error(f"处理图片失败: {str(e)}")
        raise Exception(f"图片处理失败: {str(e)}")


def preprocess_image(image, target_size=(256, 256)):
    """
    预处理图片以适应模型输入

    Args:
        image: PIL.Image对象
        target_size: 目标尺寸

    Returns:
        处理后的图片
    """
    try:
        # 转换为RGB模式
        if image.mode != 'RGB':
            image = image.convert('RGB')

        # 调整大小（保持纵横比）
        image.thumbnail(target_size, Image.LANCZOS)

        # 创建白色背景
        background = Image.new('RGB', target_size, (255, 255, 255))

        # 居中粘贴
        offset = ((target_size[0] - image.size[0]) // 2,
                  (target_size[1] - image.size[1]) // 2)
        background.paste(image, offset)

        return background

    except Exception as e:
        logger.error(f"图片预处理失败: {str(e)}")
        raise Exception(f"图片预处理失败: {str(e)}")


def process_image_with_model(image_path):
    """
    使用AI模型处理图片

    Args:
        image_path: 输入图片路径

    Returns:
        str: 处理后的图片路径
    """
    try:
        if model is None:
            raise Exception("模型未初始化")

        logger.info(f"开始AI处理: {image_path}")

        # 1. 读取图片
        img = Image.open(image_path).convert('RGB')

        # 2. 转换为tensor
        import torchvision.transforms as transforms
        transform_list = [
            transforms.Resize([opt.load_size, opt.load_size], Image.BICUBIC),
            transforms.ToTensor(),
            transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))
        ]
        transform = transforms.Compose(transform_list)
        img_tensor = transform(img).unsqueeze(0)

        # 3. 模型推理
        # 注意：在推理时，我们需要为 B 提供一个占位符（与 A 相同即可）
        data = {
            'A': img_tensor,
            'B': img_tensor,  # 推理时使用 A 作为 B 的占位符
            'A_paths': [image_path],
            'B_paths': [image_path]
        }
        model.set_input(data)
        model.test()
        visuals = model.get_current_visuals()

        # 4. 获取生成的刺绣图案
        fake_img_tensor = visuals['fake_B']

        # 5. 转换为图片并保存
        from util.util import tensor2im
        result_img = tensor2im(fake_img_tensor)
        output_path = os.path.join(RESULTS_DIR, f"result_{os.path.basename(image_path)}")

        result_pil = Image.fromarray(result_img)
        result_pil.save(output_path)

        logger.info(f"✅ AI处理完成: {output_path}")
        return output_path

    except Exception as e:
        logger.error(f"❌ 模型处理失败: {str(e)}")
        logger.error(traceback.format_exc())
        raise Exception(f"AI处理失败: {str(e)}")


def image_to_base64(image_path):
    """将图片转换为base64编码"""
    try:
        with open(image_path, 'rb') as f:
            image_data = f.read()
            base64_data = base64.b64encode(image_data).decode('utf-8')
            return base64_data
    except Exception as e:
        logger.error(f"图片转base64失败: {str(e)}")
        return None


def get_font(font_name='Arial', font_size=48):
    """
    获取字体对象（支持中文）

    Args:
        font_name: 字体名称（songti=宋体, heiti=黑体, Arial等）
        font_size: 字号大小

    Returns:
        ImageFont对象
    """
    try:
        # 字体映射表
        font_mapping = {
            'songti': ['simsun.ttc', 'SimSun', 'NotoSansCJK-Regular.ttc'],
            'heiti': ['simhei.ttf', 'SimHei', 'NotoSansCJK-Bold.ttc'],
            'kaiti': ['simkai.ttf', 'KaiTi'],
            'Arial': ['arial.ttf', 'Arial.ttf'],
            'default': ['arial.ttf', 'simhei.ttf', 'simsun.ttc']
        }

        # 获取字体文件列表
        font_files = font_mapping.get(font_name.lower(), font_mapping['default'])

        # 常见字体路径
        font_paths = [
            'C:/Windows/Fonts/',  # Windows
            '/usr/share/fonts/truetype/',  # Linux
            '/System/Library/Fonts/',  # macOS
            '/usr/share/fonts/opentype/noto/',  # Linux Noto fonts
        ]

        # 尝试加载字体
        for font_file in font_files:
            for font_path in font_paths:
                try:
                    full_path = os.path.join(font_path, font_file)
                    if os.path.exists(full_path):
                        logger.info(f"使用字体: {full_path}")
                        return ImageFont.truetype(full_path, font_size)
                except:
                    continue

        # 如果都失败，使用PIL默认字体
        logger.warning(f"未找到字体 {font_name}，使用默认字体")
        return ImageFont.load_default()

    except Exception as e:
        logger.error(f"加载字体失败: {str(e)}")
        return ImageFont.load_default()


def text_to_image(text, font_name='Arial', font_size=48, text_color='black', bg_color='white'):
    """
    将文字渲染成图片

    Args:
        text: 文字内容
        font_name: 字体名称
        font_size: 字号
        text_color: 文字颜色
        bg_color: 背景颜色

    Returns:
        PIL.Image对象
    """
    try:
        # 获取字体
        font = get_font(font_name, font_size)

        # 创建临时图片来计算文字尺寸
        temp_img = Image.new('RGB', (1, 1), bg_color)
        draw = ImageDraw.Draw(temp_img)

        # 计算文字边界框
        bbox = draw.textbbox((0, 0), text, font=font)
        text_width = bbox[2] - bbox[0]
        text_height = bbox[3] - bbox[1]

        # 添加边距
        padding = 40
        img_width = text_width + padding * 2
        img_height = text_height + padding * 2

        # 创建实际图片
        image = Image.new('RGB', (img_width, img_height), bg_color)
        draw = ImageDraw.Draw(image)

        # 绘制文字（居中）
        x = padding
        y = padding - bbox[1]  # 调整y坐标以正确居中
        draw.text((x, y), text, font=font, fill=text_color)

        logger.info(f"文字渲染成功: {text}, 尺寸: {img_width}x{img_height}")
        return image

    except Exception as e:
        logger.error(f"文字渲染失败: {str(e)}")
        logger.error(traceback.format_exc())
        raise Exception(f"文字渲染失败: {str(e)}")


@app.route('/health', methods=['GET'])
def health_check():
    """健康检查接口"""
    return jsonify({
        'status': 'ok',
        'model_loaded': model is not None,
        'message': 'AI服务运行正常'
    })


@app.route('/api/image-to-embroidery', methods=['POST'])
def image_to_embroidery():
    """
    图片转刺绣接口

    请求参数:
        - image_url: 图片URL（必填）
        - style: 刺绣风格（可选，默认SU）
        - return_base64: 是否返回base64编码的图片（可选，默认false）

    返回:
        {
            "success": true,
            "result_url": "处理后图片URL",
            "result_base64": "base64编码（如果请求）",
            "stitch_count": 估算针数,
            "color_count": 估算颜色数
        }
    """
    try:
        # 获取请求参数
        data = request.get_json()

        if not data or 'image_url' not in data:
            return jsonify({
                'success': False,
                'error': '缺少image_url参数'
            }), 400

        image_url = data['image_url']
        style = data.get('style', 'SU')
        return_base64 = data.get('return_base64', False)

        logger.info(f"收到图片转刺绣请求: URL={image_url}, Style={style}")

        # 1. 从URL下载图片
        image = download_image_from_url(image_url)

        # 2. 预处理图片
        processed_image = preprocess_image(image)

        # 3. 保存临时文件
        temp_input_path = os.path.join(TEMP_DIR, f'input_{os.urandom(8).hex()}.png')
        processed_image.save(temp_input_path)

        # 4. 使用AI模型处理
        result_path = process_image_with_model(temp_input_path)

        # 5. 估算参数（这里使用简单的估算，实际应基于模型输出）
        stitch_count = int(processed_image.size[0] * processed_image.size[1] * 0.5)  # 示例值
        color_count = len(processed_image.getcolors(maxcolors=256)) if processed_image.getcolors(maxcolors=256) else 10

        # 6. 准备返回结果（总是返回 base64 供后端上传到 OSS）
        response_data = {
            'success': True,
            'result_base64': image_to_base64(result_path),  # 总是返回 base64
            'stitch_count': stitch_count,
            'color_count': min(color_count, 20),  # 限制最大颜色数
            'original_filename': os.path.basename(result_path)
        }

        # 清理临时文件（保留结果文件7天）
        try:
            os.remove(temp_input_path)
        except:
            pass

        logger.info(f"✅ 图片处理成功: 针数={stitch_count}, 颜色数={color_count}")
        return jsonify(response_data)

    except Exception as e:
        logger.error(f"处理请求失败: {str(e)}")
        logger.error(traceback.format_exc())
        return jsonify({
            'success': False,
            'error': str(e)
        }), 500


@app.route('/api/text-to-embroidery', methods=['POST'])
def text_to_embroidery():
    """
    文字转刺绣接口

    请求参数:
        - text: 文字内容（必填）
        - font: 字体名称（可选，songti=宋体, heiti=黑体, Arial等）
        - size: 字号（可选，默认48）
        - color: 文字颜色（可选，默认black）
        - style: 刺绣风格（可选，默认SU）

    返回:
        {
            "success": true,
            "result_base64": "base64编码的结果图片",
            "stitch_count": 估算针数,
            "color_count": 估算颜色数,
            "original_filename": "文件名"
        }
    """
    try:
        # 获取请求参数
        data = request.get_json()

        if not data or 'text' not in data:
            return jsonify({
                'success': False,
                'error': '缺少text参数'
            }), 400

        text = data['text']
        font = data.get('font', 'Arial')
        size = data.get('size', 48)
        color = data.get('color', 'black')
        style = data.get('style', 'SU')

        logger.info(f"收到文字转刺绣请求: Text={text}, Font={font}, Size={size}, Color={color}, Style={style}")

        # 1. 将文字渲染成图片
        text_image = text_to_image(text, font, size, color, 'white')

        # 2. 预处理图片（调整大小等）
        processed_image = preprocess_image(text_image)

        # 3. 保存临时文件
        temp_input_path = os.path.join(TEMP_DIR, f'text_input_{os.urandom(8).hex()}.png')
        processed_image.save(temp_input_path)

        # 4. 使用AI模型处理
        result_path = process_image_with_model(temp_input_path)

        # 5. 估算参数
        stitch_count = int(processed_image.size[0] * processed_image.size[1] * 0.3)  # 文字针数相对较少
        # 获取颜色数
        try:
            result_img = Image.open(result_path)
            colors = result_img.getcolors(maxcolors=256)
            color_count = len(colors) if colors else 5
        except:
            color_count = 5  # 默认值

        # 6. 转换为base64
        result_base64 = image_to_base64(result_path)

        if not result_base64:
            raise Exception("转换base64失败")

        # 7. 准备返回结果
        response_data = {
            'success': True,
            'result_base64': result_base64,
            'stitch_count': stitch_count,
            'color_count': min(color_count, 20),  # 限制最大颜色数
            'original_filename': os.path.basename(result_path)
        }

        # 8. 清理临时输入文件（保留结果文件供后续清理）
        try:
            os.remove(temp_input_path)
        except:
            pass

        logger.info(f"✅ 文字转刺绣成功: 针数={stitch_count}, 颜色数={color_count}")
        return jsonify(response_data)

    except Exception as e:
        logger.error(f"处理文字请求失败: {str(e)}")
        logger.error(traceback.format_exc())
        return jsonify({
            'success': False,
            'error': str(e)
        }), 500


@app.route('/api/optimize', methods=['POST'])
def optimize():
    """
    优化刺绣参数接口

    请求参数:
        - image_url: 图片URL
        - target_stitch_count: 目标针数（可选）
    """
    try:
        data = request.get_json()

        if not data or 'image_url' not in data:
            return jsonify({
                'success': False,
                'error': '缺少image_url参数'
            }), 400

        # TODO: 实现优化逻辑

        return jsonify({
            'success': True,
            'optimized_params': {
                'stitch_count': 5000,
                'color_count': 8,
                'recommended_size': '20x20cm'
            }
        })

    except Exception as e:
        logger.error(f"优化请求失败: {str(e)}")
        return jsonify({
            'success': False,
            'error': str(e)
        }), 500


@app.route('/results/<filename>', methods=['GET'])
def get_result_file(filename):
    """获取处理后的结果文件"""
    try:
        file_path = os.path.join(RESULTS_DIR, filename)
        if os.path.exists(file_path):
            return send_file(file_path, mimetype='image/png')
        else:
            return jsonify({'error': '文件不存在'}), 404
    except Exception as e:
        logger.error(f"获取文件失败: {str(e)}")
        return jsonify({'error': str(e)}), 500


@app.route('/api/cleanup', methods=['POST'])
def cleanup_result_file():
    """
    清理AI生成的结果文件接口

    请求参数:
        - filename: 要删除的文件名（必填）

    返回:
        {
            "success": true,
            "message": "文件已删除"
        }
    """
    try:
        data = request.get_json()

        if not data or 'filename' not in data:
            return jsonify({
                'success': False,
                'error': '缺少filename参数'
            }), 400

        filename = data['filename']
        file_path = os.path.join(RESULTS_DIR, filename)

        # 安全检查：防止路径遍历攻击
        if '..' in filename or '/' in filename or '\\' in filename:
            return jsonify({
                'success': False,
                'error': '无效的文件名'
            }), 400

        # 删除文件
        if os.path.exists(file_path):
            os.remove(file_path)
            logger.info(f"✅ 已删除结果文件: {filename}")
            return jsonify({
                'success': True,
                'message': f'文件 {filename} 已删除'
            })
        else:
            logger.warning(f"⚠️ 文件不存在，无需删除: {filename}")
            return jsonify({
                'success': True,
                'message': '文件不存在或已被删除'
            })

    except Exception as e:
        logger.error(f"清理文件失败: {str(e)}")
        return jsonify({
            'success': False,
            'error': str(e)
        }), 500


if __name__ == '__main__':
    # 启动时初始化模型
    logger.info("=" * 50)
    logger.info("刺绣AI服务启动中...")
    logger.info("=" * 50)

    # 初始化模型（可选，如果模型文件不存在则跳过）
    if os.path.exists('./checkpoints/AttnEmbGAN_st_10'):
        init_model()
    else:
        logger.warning("未找到模型文件，服务将以降级模式运行")

    # 启动Flask服务
    logger.info("启动HTTP服务，监听端口: 8001")
    app.run(host='0.0.0.0', port=8001, debug=False)
