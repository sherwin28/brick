package net.isger.brick.stub.dialect;

import net.isger.brick.util.Sqls;

public abstract class AbstractDialect implements Dialect {

    public boolean isSupport(String driverName) {
        return false;
    }

    public String makeSortSQL(String sql, Sort sort) {
        // TODO 容易产生SQL注入问题
        return "select * from (" + sql + ") t order by "
                + Sqls.toColumnName(sort.getName()) + " " + sort.getOrder();
    }

}
