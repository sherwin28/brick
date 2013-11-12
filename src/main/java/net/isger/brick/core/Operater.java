package net.isger.brick.core;

/**
 * 操作器
 * 
 * @author issing
 * 
 */
public interface Operater {

    /**
     * 初始
     * 
     */
    public void initial();

    /**
     * 操作入口
     * 
     */
    public void operate();

    /**
     * 注销
     * 
     */
    public void destroy();

}
