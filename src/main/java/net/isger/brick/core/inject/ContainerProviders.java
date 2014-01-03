package net.isger.brick.core.inject;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.isger.brick.core.ConsoleManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerProviders implements Iterable<ContainerProvider> {

    private static final Logger LOG;

    private List<ContainerProvider> providers;

    static {
        LOG = LoggerFactory.getLogger(ConsoleManager.class);
    }

    public ContainerProviders() {
        this.providers = new CopyOnWriteArrayList<ContainerProvider>();
    }

    public boolean isReload() {
        for (ContainerProvider provider : providers) {
            if (provider.isReload()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(
                            "Detected module provider [#0] needs to be reloaded. Reloading all providers.",
                            provider.toString());
                }
                return true;
            }
        }
        return false;
    }

    public void add(ContainerProvider provider) {
        this.providers.add(provider);
    }

    public int size() {
        return providers.size();
    }

    public Iterator<ContainerProvider> iterator() {
        return providers.iterator();
    }

}
