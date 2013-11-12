package net.isger.brick.core;

import java.util.Vector;

import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 预处理器
 * 
 * @author issing
 * 
 */
public class Prepares extends Director {

    private static final String KEY_PREPARES = "brick.core.prepares";

    private static final String PREPARE_PATH = "net/isger/brick/core/prepare";

    private static final Logger LOG;

    private static final Prepares PREPARES;

    private Vector<Prepare> prepares;

    static {
        LOG = LoggerFactory.getLogger(Prepares.class);
        PREPARES = new Prepares();
    }

    private Prepares() {
        prepares = new Vector<Prepare>();
        prepares.add(new ContextPrepare());
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_PREPARES, PREPARE_PATH);
    }

    public static Prepares getPrepares() {
        return canonicalize(PREPARES);
    }

    public void add(Prepare prepare) {
        LOG.info("Binding the prepare {}", prepare);
        prepares.add(prepare);
    }

    public static void prepare(Command cmd) {
        Prepares prepares = getPrepares();
        for (Prepare prepare : prepares.prepares) {
            prepare.prepare(cmd);
        }
    }

    public static void cleanup(Command cmd) {
        Prepares prepares = getPrepares();
        for (Prepare prepare : prepares.prepares) {
            prepare.cleanup(cmd);
        }
    }

}
