package net.isger.brick.stub;

import java.util.Vector;

import net.isger.brick.stub.dialect.Dialect;
import net.isger.brick.stub.dialect.MySQLDialect;
import net.isger.brick.stub.dialect.OracleDialect;
import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dialects extends Director {

    private static final String KEY_DIALECTS = "brick.core.dialects";

    private static final String DIALECT_PATH = "net/isger/brick/stub/dialect";

    private static final Logger LOG;

    private static final Dialects DIALECTS;

    private Vector<Dialect> dialects;

    static {
        LOG = LoggerFactory.getLogger(Dialects.class);
        DIALECTS = new Dialects();
    }

    private Dialects() {
        dialects = new Vector<Dialect>();
        add(new MySQLDialect());
        add(new OracleDialect());
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_DIALECTS, DIALECT_PATH);
    }

    public static Dialects getDialects() {
        return canonicalize(DIALECTS);
    }

    public void add(Dialect dialect) {
        LOG.info("Binding the dialect with {}", dialect);
        dialects.add(dialect);
    }

    public static Dialect getDialect(String driverName) {
        Vector<Dialect> dialects = getDialects().dialects;
        for (Dialect dialect : dialects) {
            if (dialect.isSupport(driverName)) {
                return dialect;
            }
        }
        return null;
    }

}
