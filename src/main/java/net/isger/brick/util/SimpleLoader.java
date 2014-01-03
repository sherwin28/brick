package net.isger.brick.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.raw.Artifact;
import net.isger.brick.raw.Depository;
import net.isger.brick.raw.Depot;
import net.isger.brick.util.reflect.Converter;

/**
 * 简单加载器
 * 
 * @author issing
 * 
 */
public class SimpleLoader implements Loader {

    private static final String CLASS = "class";

    protected Class<?> targetClass;

    public SimpleLoader(Class<?> clazz) {
        this.targetClass = clazz;
    }

    /**
     * 加载资源
     * 
     * @param res
     * @param className
     * @return
     */
    public static Object load(Object res, String className) {
        return new SimpleLoader(Reflects.getClass(className)).load(res);
    }

    /**
     * 加载资源
     * 
     * @param res
     * @param className
     * @return
     */
    public static Object load(Object res, Class<?> clazz) {
        return new SimpleLoader(clazz).load(res);
    }

    /**
     * 目标类型
     * 
     * @param res
     * @return
     */
    public Class<?> getTargetClass(Map<String, Object> res) {
        Class<?> clazz;
        String type = (String) res.get(CLASS);
        if (type != null) {
            clazz = Reflects.getClass(type);
            if (clazz != null) {
                return clazz;
            }
        }
        if (this.targetClass == null) {
            throw new BrickException("Invalid target class name " + type);
        }
        return this.targetClass;
    }

    @SuppressWarnings("unchecked")
    public Object load(Object res) {
        Object result;
        if (res instanceof String) {
            result = load((String) res);
        } else if (res instanceof Collection) {
            result = load((Collection<?>) res);
        } else if (res instanceof Map) {
            result = load((Map<String, Object>) res);
        } else {
            result = create(res);
        }
        return result;
    }

    protected Object load(String res) {
        Artifact artifact = Depository.wrap("json", Depot.LAB_FILE,
                (String) res);
        if (artifact == null) {
            throw new BrickException("Not found the resource by " + res);
        }
        Object config = artifact.use("transform");
        if (config instanceof String) {
            // 阻止字符串路径无限嵌套
            return create(config);
        }
        return load(config);
    }

    protected Object load(Collection<?> res) {
        List<Object> result = new ArrayList<Object>();
        for (Object config : res) {
            // 阻止列表集合无限嵌套
            result.add(config instanceof Collection ? create(config)
                    : load(config));
        }
        return result;
    }

    protected Object load(Map<String, Object> res) {
        return create(getTargetClass(res), res);
    }

    /**
     * 创建实例
     * 
     * @param clazz
     * @param res
     * @return
     */
    protected Object create(Class<?> clazz, Map<String, Object> res) {
        return Reflects.newInstance(clazz, res);
    }

    /**
     * 创建实例
     * 
     * @param res
     * @return
     */
    protected Object create(Object res) {
        if (targetClass == null) {
            throw new BrickException("Unexpected load the resource by " + res);
        }
        return Converter.convert(targetClass, res);
    }

}
