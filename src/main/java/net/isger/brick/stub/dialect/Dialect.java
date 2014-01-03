package net.isger.brick.stub.dialect;

public interface Dialect {

    public boolean isSupport(String driverName);

    public PageSQL makePageSQL(String sql, Object[] values, Page page);

    public String makeSortSQL(String sql, Sort sort);

}
