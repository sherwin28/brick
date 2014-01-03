package net.isger.brick.stub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.stub.dialect.Dialect;
import net.isger.brick.stub.dialect.Page;
import net.isger.brick.stub.dialect.PageSQL;
import net.isger.brick.stub.dialect.Sort;
import net.isger.brick.util.Sqls;
import net.isger.brick.util.Sqls.SQLEntry;

public class SqlStub extends AbstractStub {

    public static final String PARAM_DRIVERNAME = "driverName";

    public static final String PARAM_URL = "url";

    public static final String PARAM_USER = "user";

    public static final String PARAM_PASSWORD = "password";

    private Dialect dialect;

    public void initial() {
        super.initial();
        String driverName = null;
        try {
            Class.forName(driverName = getDriverName());
        } catch (ClassNotFoundException e) {
            throw new BrickException("Invalid driver " + driverName);
        }
        dialect = getDialect(driverName);
        if (dialect == null) {
            throw new BrickException("Unsupport driver " + driverName);
        }
    }

    protected Dialect getDialect(String driverName) {
        return Dialects.getDialect(driverName);
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
        Object table = cmd.getTable();
        SearchCondition condition = getSearchCondition(cmd.getCondition());
        Connection conn = getConnection();
        try {
            String sql;
            Object[] values;
            if (table instanceof String) {
                SQLEntry entry = Sqls.getQueryEntry((String) table,
                        (Map<String, Object>) condition.getValues());
                sql = entry.getSQL();
                values = entry.getValues();
            } else if (table instanceof Class) {
                sql = Sqls.getSQL((Class<?>) table, condition.getTarget(),
                        condition.getArgments());
                values = (Object[]) condition.getValues();
            } else {
                SQLEntry entry = Sqls.getQueryEntry(table);
                sql = entry.getSQL();
                values = entry.getValues();
            }
            cmd.setResult(search(conn, sql, values, condition.getPage(),
                    condition.getSort()));
        } finally {
            close(conn);
        }
    }

    private SearchCondition getSearchCondition(Object config) {
        if (config instanceof SearchCondition) {
            return (SearchCondition) config;
        }
        SearchCondition condition = new SearchCondition();
        if (config != null) {
            Object[] searchConfig = new Object[5];
            System.arraycopy(config, 0, searchConfig, 0, 5);
            condition.setPage((Page) searchConfig[0]);
            condition.setSort((Sort) searchConfig[1]);
            condition.setValues(searchConfig[2]);
            condition.setTarget((String) searchConfig[3]);
            condition.setArgments((Object[]) searchConfig[4]);
        }
        return condition;
    }

    private Object search(Connection conn, String sql, Object[] values,
            Page page, Sort sort) {
        Object[] result = null;
        if (sort != null && sort.getName() != null && sort.getOrder() != null) {
            sql = dialect.makeSortSQL(sql, sort);
        }
        if (page == null || page.getStart() == null || page.getLimit() == null) {
            result = Sqls.query(sql, values, conn);
        } else {
            PageSQL pageSQL = dialect.makePageSQL(sql, values, page);
            Object[] datas = Sqls.query(pageSQL.getWrapSQL(),
                    pageSQL.getWrapValues(), conn);
            if (page.getTotal() == null) {
                // 查询总数
                result = new Object[3];
                result[0] = datas[0];
                result[1] = datas[1];
                result[2] = ((Object[][]) Sqls.query(pageSQL.getCountSQL(),
                        values, conn)[1])[0][0];
            } else {
                result = datas;
            }
        }
        return result;
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
