package net.isger.brick.stub.dialect;

public class OracleDialect extends AbstractDialect {

    public PageSQL makePageSQL(final String sql, final Object[] values,
            final Page page) {
        return new PageSQL() {
            public String getWrapSQL() {
                return "select * from (select t1.*, rownum rn from (" + sql
                        + ") t1 where rownum <= ?) t2 where rn > ?";
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
                wrapValues[valCount - 1] = page.getStart();
                wrapValues[valCount - 2] = page.getStart() + page.getLimit();
                return wrapValues;
            }

            public String getCountSQL() {
                return "select count(*) from (" + sql + ") t";
            }

        };
    }

}
