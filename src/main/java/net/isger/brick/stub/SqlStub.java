package net.isger.brick.stub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.util.Sqls;
import net.isger.brick.util.Sqls.SQLEntry;

public class SqlStub extends AbstractStub {

    public static final String PARAM_DRIVERNAME = "driverName";

    public static final String PARAM_URL = "url";

    public static final String PARAM_USER = "user";

    public static final String PARAM_PASSWORD = "password";

    public void initial() {
        super.initial();
        String driverName = null;
        try {
            Class.forName(driverName = getDriverName());
        } catch (ClassNotFoundException e) {
            throw new BrickException("Invalid driver " + driverName);
        }
    }

    protected String getDriverName() {
        return (String) getParameter(PARAM_DRIVERNAME);
    }

    protected String getUrl() {
        return (String) getParameter(PARAM_URL);
    }

    protected String getUser() {
        return (String) getParameter(PARAM_USER);
    }

    protected String getPassword() {
        return (String) getParameter(PARAM_PASSWORD);
    }

    protected Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getUrl(), getUser(),
                    getPassword());
        } catch (SQLException e) {
            throw new BrickException("Failure to connect database", e);
        }
        return conn;
    }

    private Object modify(Class<?> table, String defTarget, Object[] condition,
            Connection conn) {
        String target = (String) condition[0];
        if (target == null) {
            target = defTarget;
        }
        return condition[1] instanceof Object[][] ? Sqls.modify(table, target,
                (Object[][]) condition[1], conn, condition[2]) : Sqls.modify(
                table, target, (Object[]) condition[1], conn, condition[2]);
    }

    @SuppressWarnings("unchecked")
    public void insert() {
        StubCommand cmd = StubCommand.getAction();
        Object result = null;
        Object table = cmd.getTable();
        Object config = cmd.getCondition();
        Connection conn = getConnection();
        try {
            if (table instanceof String) {
                result = Sqls.modify(Sqls.getInsertEntry((String) table,
                        (Map<String, Object>) config), conn);
            } else if (table instanceof Class) {
                result = modify((Class<?>) table, StubCommand.OPERATE_INSERT,
                        (Object[]) config, conn);
            } else {
                result = Sqls.modify(Sqls.getInsertEntry(table), conn);
            }
        } finally {
            close(conn);
        }
        cmd.setResult(result);
    }

    @SuppressWarnings("unchecked")
    public void delete() {
        StubCommand cmd = StubCommand.getAction();
        Object result = null;
        Object table = cmd.getTable();
        Object config = cmd.getCondition();
        Connection conn = getConnection();
        try {
            if (table instanceof String) {
                result = Sqls.modify(Sqls.getDeleteEntry((String) table,
                        (Map<String, Object>) config), conn);
            } else if (table instanceof Class) {
                result = modify((Class<?>) table, StubCommand.OPERATE_DELETE,
                        (Object[]) config, conn);
            } else {
                result = Sqls.modify(Sqls.getDeleteEntry(table), conn);
            }
        } finally {
            close(conn);
        }
        cmd.setResult(result);
    }

    @SuppressWarnings("unchecked")
    public void update() {
        StubCommand cmd = StubCommand.getAction();
        Object result = null;
        Object table = cmd.getTable();
        Object config = cmd.getCondition();
        Connection conn = getConnection();
        try {
            if (table instanceof String) {
                Object[] condition = (Object[]) config;
                result = Sqls.modify(Sqls.getUpdateEntry((String) table,
                        (Map<String, Object>) condition[0],
                        (Map<String, Object>) condition[1]), conn);
            } else if (table instanceof Class) {
                result = modify((Class<?>) table, StubCommand.OPERATE_UPDATE,
                        (Object[]) config, conn);
            } else {
                result = Sqls.modify(Sqls.getUpdateEntry(table, config), conn);
            }
        } finally {
            close(conn);
        }
        cmd.setResult(result);
    }

    @SuppressWarnings("unchecked")
    public void search() {
        StubCommand cmd = StubCommand.getAction();
        Object result = null;
        Object table = cmd.getTable();
        Object config = cmd.getCondition();
        Connection conn = getConnection();
        SQLEntry sql = null;
        try {
            if (table instanceof String) {
                Object[] condition = (Object[]) config;
                sql = Sqls.getQueryEntry((String) table,
                        (Map<String, Object>) condition[3]);
                result = search(conn, sql.getSQL(), sql.getValues(),
                        getStart(condition), getLimit(condition),
                        getTotal(condition));
            } else if (table instanceof Class) {
                Object[] condition = (Object[]) config;
                String target = (String) condition[4];
                if (target == null) {
                    target = StubCommand.OPERATE_SEARCH;
                }
                result = search(conn,
                        Sqls.getSQL((Class<?>) table, target, condition[5]),
                        (Object[]) condition[3], getStart(condition),
                        getLimit(condition), getTotal(condition));

            } else {
                Object[] condition = (Object[]) config;
                sql = Sqls.getQueryEntry(table);
                result = search(conn, sql.getSQL(), sql.getValues(),
                        getStart(condition), getLimit(condition),
                        getTotal(condition));
            }
        } finally {
            close(conn);
        }
        cmd.setResult(result);
    }

    private Object search(Connection conn, String sql, Object[] values,
            Integer start, Integer limit, Integer total) {
        Object[] result = null;
        if (start == null || limit == null) {
            result = Sqls.query(sql, values, conn);
        } else {
            // TODO 缺省ORACLE本地化分页
            String pageSQL = "select * from (select t1.*, rownum rn from ("
                    + sql + ") t1 where rownum <= ?) t2 where rn > ?";
            Object[] pageValues = null;
            int valCount = 2;
            if (values != null) {
                valCount += values.length;
                pageValues = new Object[valCount];
                System.arraycopy(values, 0, pageValues, 0, values.length);
            } else {
                pageValues = new Object[valCount];
            }
            pageValues[valCount - 1] = start;
            pageValues[valCount - 2] = start + limit;
            Object[] datas = Sqls.query(pageSQL, pageValues, conn);
            if (total == null) {
                // 查询总数
                result = new Object[3];
                result[0] = datas[0];
                result[1] = datas[1];
                result[2] = ((Object[][]) Sqls.query("select count(*) from ("
                        + sql + ") t", values, conn)[1])[0][0];
            } else {
                result = datas;
            }
        }
        return result;
    }

    private Integer getStart(Object[] condition) {
        return (Integer) (condition != null && condition.length > 0 ? condition[0]
                : null);
    }

    private Integer getLimit(Object[] condition) {
        return (Integer) (condition != null && condition.length > 0 ? condition[0]
                : null);
    }

    private Integer getTotal(Object[] condition) {
        return (Integer) (condition != null && condition.length > 0 ? condition[0]
                : null);
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void destroy() {
        super.destroy();
    }

}
