package net.isger.brick.plugin;

import net.isger.brick.plugin.persist.Persist;
import net.isger.brick.plugin.persist.Persists;
import net.isger.brick.plugin.service.Service;
import net.isger.brick.plugin.service.Services;

public class BasePlugin extends AbstractPlugin implements Plugin {

    protected Services services;

    protected Persists persists;

    public void initial() {
        super.initial();
    }

    protected Service getService(String name) {
        return services.get(name);
    }

    protected Persist getPersist(String name) {
        return persists.get(name);
    }

    public void destroy() {
        super.destroy();
    }

}
