package net.isger.brick.plugin.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.SimpleLoader;
import net.isger.brick.util.reflect.Conversion;
import net.isger.brick.util.scanner.Scanner;
import net.isger.brick.util.scanner.scan.ScanFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistsConversion implements Conversion {

    private static final String KEY_PATH = "path";

    private static final String PERSIST_SUFFIX = "Persist.class";

    private static final Logger LOG;

    public static final PersistsConversion CONVERSION;

    private static final ScanFilter FILTER;

    static {
        LOG = LoggerFactory.getLogger(PersistsConversion.class);
        CONVERSION = new PersistsConversion();
        FILTER = new ScanFilter() {
            public boolean isDeep() {
                return true;
            }

            public boolean accept(String name) {
                return name.endsWith(PERSIST_SUFFIX);
            }
        };
    }

    private PersistsConversion() {
    }

    public boolean isSupport(Class<?> clazz) {
        return Persists.class.isAssignableFrom(clazz);
    }

    public Object convert(Object res) {
        if (res instanceof Collection) {
            final List<Persist> persists = new ArrayList<Persist>();
            new SimpleLoader(Persist.class) {
                protected Object load(Map<String, Object> res) {
                    Class<?> targetClass = getTargetClass(res);
                    if (Persist.class != targetClass) {
                        if (!Persist.class.isAssignableFrom(targetClass)) {
                            throw new BrickException("Invalid persist "
                                    + targetClass);
                        }
                        return (Persist) super.load(res);
                    }
                    List<Object> result = new ArrayList<Object>();
                    Object config = res.get(KEY_PATH);
                    if (config != null) {
                        res.remove(KEY_PATH);
                        if (config instanceof String) {
                            result.addAll(scan((String) config, res));
                        } else if (config instanceof Collection) {
                            for (Object path : (Collection<?>) config) {
                                if (path instanceof String) {
                                    result.addAll(scan((String) config, res));
                                } else {
                                    LOG.warn(
                                            "Skipped the persist invalid config {}",
                                            path);
                                }
                            }
                        } else {
                            LOG.warn("Skipped the persist invalid config {}",
                                    config);
                        }
                    }
                    return result;
                }

                protected List<Object> scan(String path, Map<String, Object> res) {
                    List<Object> result = new ArrayList<Object>();
                    // 扫描路径下的所有服务
                    String className;
                    for (String name : Scanner.scan(
                            path.replaceAll("[.]", "/"), FILTER)) {
                        className = (path + name.replaceFirst("[.]class$", ""))
                                .replaceAll("[\\\\/]", ".");
                        result.add(create(Reflects.getClass(className), res));
                    }
                    return result;
                }

                protected Object create(Class<?> clazz, Map<String, Object> res) {
                    if (!Persist.class.isAssignableFrom(clazz)) {
                        throw new BrickException("Invalid persist class name "
                                + clazz.getName());
                    }
                    Persist persist = (Persist) Reflects
                            .newInstance(clazz, res);
                    persists.add(persist);
                    return persist;
                }

                protected Object create(Object res) {
                    throw new BrickException(
                            "Unexpected load the persist of plugin by " + res);
                }
            }.load(res);
            return new Persists(persists);
        }
        throw new IllegalStateException("Unexpected persists conversion for "
                + res);
    }
}
