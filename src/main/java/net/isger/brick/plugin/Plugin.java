package net.isger.brick.plugin;

/**
 * 插件接口
 * 
 * @author issing
 * 
 */
public interface Plugin {

    /**
     * 初始
     * 
     */
    public void initial();

    /**
     * 服务
     * 
     */
    public void service();

    /**
     * 持久
     * 
     */
    public void persist();

    /**
     * 注销
     * 
     */
    public void destroy();

}
