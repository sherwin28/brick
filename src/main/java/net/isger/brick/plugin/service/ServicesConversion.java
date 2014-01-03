package net.isger.brick.plugin.service;

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

public class ServicesConversion implements Conversion {

    private static final String KEY_PATH = "path";

    private static final String SERVICE_SUFFIX = "Service.class";

    private static final Logger LOG;

    public static final ServicesConversion CONVERSION;

    private static final ScanFilter FILTER;

    static {
        LOG = LoggerFactory.getLogger(ServicesConversion.class);
        CONVERSION = new ServicesConversion();
        FILTER = new ScanFilter() {
            public boolean isDeep() {
                return true;
            }

            public boolean accept(String name) {
                return name.endsWith(SERVICE_SUFFIX);
            }
        };
    }

    private ServicesConversion() {
    }

    public boolean isSupport(Class<?> clazz) {
        return Services.class.isAssignableFrom(clazz);
    }

    public Object convert(Object res) {
        if (res instanceof Collection) {
            final List<Service> services = new ArrayList<Service>();
            new SimpleLoader(Service.class) {
                protected Object load(Map<String, Object> res) {
                    Class<?> targetClass = getTargetClass(res);
                    if (Service.class != targetClass) {
                        if (!Service.class.isAssignableFrom(targetClass)) {
                            throw new BrickException("Invalid service "
                                    + targetClass);
                        }
                        return (Service) super.load(res);
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
                                            "Skipped the service invalid config {}",
                                            path);
                                }
                            }
                        } else {
                            LOG.warn("Skipped the service invalid config {}",
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
                    if (!Service.class.isAssignableFrom(clazz)) {
                        throw new BrickException("Invalid service class name "
                                + clazz.getName());
                    }
                    Service service = (Service) Reflects
                            .newInstance(clazz, res);
                    services.add(service);
                    return service;
                }

                protected Object create(Object res) {
                    throw new BrickException(
                            "Unexpected load the service of plugin by " + res);
                }
            }.load(res);
            return new Services(services);
        }
        throw new IllegalStateException("Unexpected services conversion for "
                + res);
    }
}
