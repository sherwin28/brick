package net.isger.brick.core.inject;

/**
 * 容器实例供应
 * 
 * @author issing
 * 
 */
public interface ContainerProvider {

    public boolean isReload();

    public void register(ContainerBuilder builder);

}
