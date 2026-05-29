package com.jinyue.cixiu.common.constant;

/**
 * Redis Key常量
 */
public class RedisKeyConstant {

    /**
     * 验证码前缀
     */
    public static final String VERIFY_CODE_PREFIX = "verify:code:";

    /**
     * 验证码前缀（简称）
     */
    public static final String CODE_PREFIX = "verify:code:";

    /**
     * 验证码锁前缀
     */
    public static final String CODE_LOCK_PREFIX = "verify:code:lock:";

    /**
     * 用户Token前缀
     */
    public static final String USER_TOKEN_PREFIX = "user:token:";

    /**
     * 用户信息前缀
     */
    public static final String USER_INFO_PREFIX = "user:info:";

    /**
     * 用户前缀（简称）
     */
    public static final String USER_PREFIX = "user:info:";

    /**
     * 订单锁前缀
     */
    public static final String ORDER_LOCK_PREFIX = "order:lock:";

    /**
     * 模板列表缓存前缀
     */
    public static final String TEMPLATE_LIST_PREFIX = "template:list:";

    /**
     * 材料价格缓存前缀
     */
    public static final String MATERIAL_PRICE_PREFIX = "material:price:";

    /**
     * 设备状态缓存前缀
     */
    public static final String DEVICE_STATUS_PREFIX = "device:status:";

    /**
     * 生产进度缓存前缀
     */
    public static final String PRODUCTION_PROGRESS_PREFIX = "production:progress:";

    private RedisKeyConstant() {
    }
}
