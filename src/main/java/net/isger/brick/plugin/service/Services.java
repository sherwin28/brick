package net.isger.brick.plugin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.util.Formats;
import net.isger.brick.util.anno.Alias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Services {

    private static final Logger LOG;

    private Map<String, Service> services;

    static {
        LOG = LoggerFactory.getLogger(Services.class);
    }

    public Services(List<Service> services) {
        this.services = new HashMap<String, Service>();
        for (Service service : services) {
            add(service);
        }
    }

    public void add(Service service) {
        Class<?> clazz = service.getClass();
        Alias alias = clazz.getAnnotation(Alias.class);
        String name = alias == null ? null : alias.value();
        if (name == null || (name = name.trim()).length() == 0) {
            name = Formats.toCap(clazz.getSimpleName());
        } else if (name.indexOf((int) '.') != -1) {
            throw new BrickException("Invalid service alias " + name);
        } else {
            name += "Service";
        }
        Service oldService = services.put(name, service);
        if (oldService == null) {
            LOG.info("Binded the service [{}] with {}", name, service);
        } else {
            LOG.info("Multiple binded the service [{}] with {}", name, service);
        }
    }

    public Service get(String name) {
        return services.get(name);
    }
}
