package net.isger.brick.stub.dialect;

public class MySQLDialect extends AbstractDialect {

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    public boolean isSupport(String driverName) {
        return DRIVER_NAME.equals(driverName);
    }

    public PageSQL makePageSQL(final String sql, final Object[] values,
            final Page page) {
        return new PageSQL() {
            public String getWrapSQL() {
                return "select * from (" + sql + ") t limit ?, ?";
            }

            public Object[] getWrapValues() {
                Object[] wrapValues = null;
                int valCount = 2;
                if (values != null) {
                    valCount += values.length;
                    wrapValues = new Object[valCount];
                    System.arraycopy(values, 0, wrapValues, 0, values.length);
                } else {
                    wrapValues = new Object[valCount];
                }
                wrapValues[valCount - 1] = page.getStart() + page.getLimit();
                wrapValues[valCount - 2] = page.getStart();
                return wrapValues;
            }

            public String getCountSQL() {
                return "select count(*) from (" + sql + ") t";
            }
        };
    }

}
