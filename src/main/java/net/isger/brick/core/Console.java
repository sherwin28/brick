package net.isger.brick.core;

import net.isger.brick.core.inject.Container;

/**
 * 控制台
 * 
 * @author issing
 * 
 */
public interface Console {

    /**
     * 初始
     * 
     */
    public void initial();

    /**
     * 容器
     * 
     * @return
     */
    public Container getContainer();

    /**
     * 执行
     * 
     */
    public void execute();

    /**
     * 执行
     * 
     * @param cmd
     */
    public void execute(Command cmd);

    /**
     * 注销
     * 
     */
    public void destroy();

}
