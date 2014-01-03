package net.isger.brick.core;

/**
 * 模块接口
 * 
 * @author issing
 * 
 */
public interface Module {

    /**
     * 加载
     * 
     * @param res
     * @return
     */
    public void load(Object res);

    /**
     * 初始
     * 
     */
    public void initial();

    /**
     * 执行
     * 
     */
    public void execute();

    /**
     * 注销
     * 
     */
    public void destroy();

}
