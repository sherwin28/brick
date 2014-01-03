package net.isger.brick.core.inject;


public interface ContainerProvider {

    public boolean isReload();

    public void register(ContainerBuilder builder);

}
