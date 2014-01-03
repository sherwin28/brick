package net.isger.brick.core.preparer;

/**
 * 预处理接口
 * 
 * @author issing
 * 
 */
public interface Preparer {

    /**
     * 处理
     * 
     * @param cmd
     */
    public void prepare();

    /**
     * 清除
     * 
     * @param cmd
     */
    public void cleanup();

}
