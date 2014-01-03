package net.isger.brick.stub.dialect;

public abstract class AbstractDialect implements Dialect {

    public boolean isSupport(String driverName) {
        return false;
    }

    public String makeSortSQL(String sql, Sort sort) {
        // TODO 存在SQL注入问题
        return "select * from (" + sql + ") t order by " + sort.getName() + " "
                + sort.getOrder();
    }

}
