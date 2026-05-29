package com.jinyue.cixiu.common.constant;

/**
 * 通用常量
 */
public class CommonConstant {

    /**
     * 验证码长度
     */
    public static final int VERIFY_CODE_LENGTH = 6;

    /**
     * 验证码有效期（分钟）
     */
    public static final int VERIFY_CODE_EXPIRE_MINUTES = 5;

    /**
     * Token过期时间（毫秒）- 24小时
     */
    public static final long TOKEN_EXPIRE_TIME = 86400000L;

    /**
     * Refresh Token过期时间（毫秒）- 7天
     */
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 604800000L;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 文件上传最大大小（10MB）
     */
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    /**
     * 允许上传的图片格式
     */
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};

    /**
     * 订单号前缀
     */
    public static final String ORDER_NO_PREFIX = "CX";

    /**
     * 成功标识
     */
    public static final int SUCCESS = 1;

    /**
     * 失败标识
     */
    public static final int FAIL = 0;

    private CommonConstant() {
    }
}
