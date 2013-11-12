package net.isger.brick.plugin.service;

import net.isger.brick.core.Context;
import net.isger.brick.plugin.DynamicTarget;
import net.isger.brick.plugin.PluginCommand;
import net.isger.brick.plugin.PluginModule;
import net.isger.brick.util.anno.Ignore;

@Ignore
public class BaseService extends DynamicTarget implements Service {

    protected String bus;

    protected BaseService() {
    }

    public void service() {
        invoke();
    }

    protected void toService() {
        Context.getActionPlugin().service();
    }

    protected void toPersist() {
        Context.getActionPlugin().persist();
    }

    protected void toService(PluginCommand cmd) {
        cmd.setDirect(PluginCommand.OPERATE_SERVICE);
        ((PluginModule) Context.getActionModule()).call(cmd);
    }

    protected void toPersist(PluginCommand cmd) {
        cmd.setDirect(PluginCommand.OPERATE_PERSIST);
        ((PluginModule) Context.getActionModule()).call(cmd);
    }

}
