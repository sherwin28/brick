package net.isger.brick.plugin.persist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.util.Formats;
import net.isger.brick.util.anno.Alias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Persists {

    private static final Logger LOG;

    private Map<String, Persist> persists;

    static {
        LOG = LoggerFactory.getLogger(Persists.class);
    }

    public Persists(List<Persist> persists) {
        this.persists = new HashMap<String, Persist>();
        for (Persist persist : persists) {
            add(persist);
        }
    }

    public void add(Persist persist) {
        Class<?> clazz = persist.getClass();
        Alias alias = clazz.getAnnotation(Alias.class);
        String name = alias == null ? null : alias.value();
        if (name == null || (name = name.trim()).length() == 0) {
            name = Formats.toCap(clazz.getSimpleName());
        } else if (name.indexOf((int) '.') != -1) {
            throw new BrickException("Invalid persist alias " + name);
        } else {
            name += "Persist";
        }
        Persist oldPersist = persists.put(name, persist);
        if (oldPersist == null) {
            LOG.info("Binded the persist [{}] with {}", name, persist);
        } else {
            LOG.info("Multiple binded the persist [{}] with {}", name, persist);
        }
    }

    public Persist get(String name) {
        return persists.get(name);
    }
}
