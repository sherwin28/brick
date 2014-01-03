package net.isger.brick.core.preparer;

import java.util.Vector;

import net.isger.brick.util.anno.Ignore;
import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 预处理器
 * 
 * @author issing
 * 
 */
@Ignore
public class Preparers extends Director {

    private static final String KEY_PREPARES = "brick.core.prepares";

    private static final String PREPARE_PATH = "net/isger/brick/core/prepare";

    private static final Logger LOG;

    private Vector<Preparer> preparers;

    static {
        LOG = LoggerFactory.getLogger(Preparers.class);
    }

    public Preparers() {
        preparers = new Vector<Preparer>();
        canonicalize(this);
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_PREPARES, PREPARE_PATH);
    }

    public void add(Preparer preparer) {
        LOG.info("Binding the prepare {}", preparer);
        preparers.add(preparer);
    }

    public void prepare() {
        for (Preparer preparer : preparers) {
            preparer.prepare();
        }
    }

    public void cleanup() {
        for (Preparer preparer : preparers) {
            preparer.cleanup();
        }
    }

}
