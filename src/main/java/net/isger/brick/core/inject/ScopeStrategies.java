package net.isger.brick.core.inject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 作用域集合
 * 
 * @author issing
 * 
 */
public class ScopeStrategies extends Director {

    private static final String KEY_STRATEGIES = "brick.core.inject.strategies";

    private static final String STRATEGY_PATH = "net/isger/brick/core/inject/strategy";

    private static final Logger LOG;

    private Map<Key<?>, Strategy> stgs;

    static {
        LOG = LoggerFactory.getLogger(ScopeStrategies.class);
    }

    public ScopeStrategies() {
        stgs = new HashMap<Key<?>, Strategy>();
        canonicalize(this);
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_STRATEGIES, STRATEGY_PATH);
    }

    /**
     * 新增策略
     * 
     * @param name
     * @param strategy
     */
    public void add(Class<?> type, String name, Strategy strategy) {
        Key<?> key = Key.newInstance(type, name);
        Strategy oldStrategy = stgs.put(key, strategy);
        if (oldStrategy == null) {
            LOG.info("Binded the strategy [{}] with {}", name, strategy);
        } else {
            LOG.info("Multiple binded the strategy [{}] with {}", name,
                    strategy);
        }
    }

    /**
     * 获取指定策略
     * 
     * @param type
     * @return
     */
    public Strategy get(Class<?> type) {
        return stgs.get(Key.newInstance(type, Container.DEFAULT_NAME));
    }

    /**
     * 获取指定策略
     * 
     * @param name
     * @return
     */
    public Strategy get(Class<?> type, String name) {
        return stgs.get(Key.newInstance(type, name));
    }

    /**
     * 获取策略数
     * 
     * @return
     */
    public int size() {
        return stgs.size();
    }

    /**
     * 迭代所有策略
     * 
     */
    public Iterator<Strategy> iterator() {
        return stgs.values().iterator();
    }

}
