package net.isger.brick.plugin;

import net.isger.brick.BrickException;
import net.isger.brick.plugin.persist.Persist;
import net.isger.brick.plugin.service.Service;
import net.isger.brick.util.Formats;

public abstract class AbstractPlugin implements Plugin {

    public void initial() {
    }

    public void service() {
        String name = getDirect();
        Service service = getService(name.concat("Service"));
        if (service == null) {
            throw new BrickException("Not found the service by " + name);
        }
        service.service();
    }

    public void persist() {
        String name = getDirect();
        Persist persist = getPersist(name.concat("Persist"));
        if (persist == null) {
            throw new BrickException("Not found the persist by " + name);
        }
        persist.persist();
    }

    private String getDirect() {
        PluginCommand cmd = PluginCommand.getAction();
        String direct = cmd.getDirect();
        if (direct == null) {
            throw new BrickException(
                    "The plugin direct can not be a null value");
        }
        return Formats.toCap(direct);
    }

    protected abstract Service getService(String name);

    protected abstract Persist getPersist(String name);

    public void destroy() {
    }

}
