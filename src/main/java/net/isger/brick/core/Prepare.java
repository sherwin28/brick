package net.isger.brick.core;

/**
 * 预处理接口
 * 
 * @author issing
 * 
 */
public interface Prepare {

    /**
     * 处理
     * 
     * @param cmd
     */
    public void prepare(Command cmd);

    /**
     * 清除
     * 
     * @param cmd
     */
    public void cleanup(Command cmd);

}
