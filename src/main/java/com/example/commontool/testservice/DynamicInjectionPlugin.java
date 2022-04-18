package com.example.commontool.testservice;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: DynamicInjectionPlugin
 * @Description: TODO 类描述
 * @Author: th_legend
 * @Date: 2022/2/25
 **/
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        , @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
@Slf4j
public class DynamicInjectionPlugin implements Interceptor {
    private static final String UPDATED_TIME_CAMELCASE = "updated_time";

    public DynamicInjectionPlugin() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String updatedColumnName = UPDATED_TIME_CAMELCASE;

        try {
            // 获取sql
            String sql = getSqlByInvocation(invocation);
            System.out.println("sql===>" + sql);
            String sql2Reset = sql;
            String[] argKeys = getDynaticArgKeys(invocation);
            if (argKeys != null && argKeys.length > 0) {
                for (String argKey : argKeys) {
                    System.out.println("argKey = " + argKey);
                }
            }

            //忽略sql中包含on conflict的情况
//            if (!StringUtils.isBlank(sql2Reset) && !sql2Reset.toUpperCase().contains("ON CONFLICT")) {
//                try {
//                    Statement statement = CCJSqlParserUtil.parse(sql);
//
//                    if (statement instanceof Update) {
//                        Update updateStatement = (Update) statement;
//                        Table table = updateStatement.getTable();
//
//                        if (table != null) {
//                            List<Column> columns = updateStatement.getColumns();
//                            List<Expression> expressions = updateStatement.getExpressions();
//                            if (!isUpdatedTimeExists(columns, updatedColumnName)) {
//                                columns.add(new Column(updatedColumnName));
//                                expressions.add(CCJSqlParserUtil.parseExpression("now()"));
//                                updateStatement.setColumns(columns);
//                                updateStatement.setExpressions(expressions);
//                                sql2Reset = updateStatement.toString();
//                            }
//                        }
//
//                    }
//                } catch (Exception e) {
//                    log.info("sql_parse error " + sql, e);
//                }
//            }

            // 包装sql后，重置到invocation中
            resetSql2Invocation(invocation, sql2Reset);

            // 返回，继续执行
            return invocation.proceed();
        } catch (Throwable e) {
            log.error("sql_update_interceptor error", e);
            throw e;
        }
    }

    private String[] getDynaticArgKeys(Invocation invocation) throws ClassNotFoundException {
        final Object[] args = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        String namespace = mappedStatement.getId();
        String className = namespace.substring(0, namespace.lastIndexOf("."));
        String methedName = namespace.substring(namespace.lastIndexOf(".") + 1, namespace.length());
        Method[] ms = Class.forName(className).getMethods();
        for (Method m : ms) {
            if (m.getName().equals(methedName)) {
                DynamicInjectArgs annotation = m.getAnnotation(DynamicInjectArgs.class);
                if (annotation != null) {
                    String[] args2 = annotation.value();
                    return args2;
                }
            }
        }
        return null;
    }

    private boolean isUpdatedTimeExists(List<Column> columns, String updatedColumnName) {
        if (columns == null || columns.size() <= 0) {
            return false;
        }
        for (Column column : columns) {
            if (String.valueOf(column.getColumnName()).equalsIgnoreCase(updatedColumnName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 获取sql语句
     *
     * @param invocation
     * @return
     */
    private String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    /**
     * 包装sql后，重置到invocation中
     *
     * @param invocation
     * @param sql
     * @throws SQLException
     */
    private void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String getOperateType(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType commondType = ms.getSqlCommandType();
        if (commondType.compareTo(SqlCommandType.SELECT) == 0) {
            return "select";
        }
        if (commondType.compareTo(SqlCommandType.INSERT) == 0) {
            return "insert";
        }
        if (commondType.compareTo(SqlCommandType.UPDATE) == 0) {
            return "update";
        }
        if (commondType.compareTo(SqlCommandType.DELETE) == 0) {
            return "delete";
        }
        return null;
    }

    // 定义一个内部辅助类，作用是包装sql
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
