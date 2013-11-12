package net.isger.brick;

/**
 * 键信息
 * 
 * @author issing
 * 
 */
public interface BrickConstants {

    public static final String MODULE_AUTH = "auth";

    public static final String MODULE_BUS = "bus";

    public static final String MODULE_CACHE = "cache";

    public static final String MODULE_PLUGIN = "plugin";

    public static final String MODULE_STUB = "stub";

    /** 操作失败 */
    public static final int CODE_FAILURE = -1;

    /** 操作成功 */
    public static final int CODE_SUCCESS = 0;

    /** 未建立会话 */
    public static final int CODE_UNSHAKE = 1;

    /** 未分配权限 */
    public static final int CODE_UNAUTH = 2;

}
