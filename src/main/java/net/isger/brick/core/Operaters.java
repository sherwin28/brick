package net.isger.brick.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Operaters extends Director implements Iterable<Operater> {

    private static final String KEY_OPERATERS = "brick.core.operaters";

    private static final String OPERATER_PATH = "net/isger/brick/core/operater";

    private static final Logger LOG;

    private Map<String, Operater> operaters;

    static {
        LOG = LoggerFactory.getLogger(Operaters.class);
    }

    Operaters(Object source) {
        operaters = new HashMap<String, Operater>();
        // 加载操作实例（便车引导方式）
        canonicalize(this, source);
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_OPERATERS, OPERATER_PATH);
    }

    /**
     * 新增操作
     * 
     * @param name
     * @param operater
     */
    public void add(String name, Operater operater) {
        Operater oldOperater = operaters.put(name, operater);
        if (oldOperater == null) {
            LOG.info("Binded the operater [{}] {}", name, operater);
        } else {
            LOG.info("Multiple binded the operater [{}] {}", name, operater);
        }
    }

    /**
     * 初始
     * 
     */
    public void initial() {
        for (Operater operater : operaters.values()) {
            operater.initial();
        }
    }

    /**
     * 获取指定操作
     * 
     * @param name
     * @return
     */
    public Operater get(String name) {
        return operaters.get(name);
    }

    /**
     * 迭代所有操作
     * 
     */
    public Iterator<Operater> iterator() {
        return operaters.values().iterator();
    }

    /**
     * 注销
     * 
     */
    public void destroy() {
        for (Operater operater : operaters.values()) {
            operater.destroy();
        }
        operaters.clear();
    }
}
