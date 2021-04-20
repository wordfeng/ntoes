package org.feng.persistent.config;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.feng.persistent.holder.UserContextHolder;

import java.sql.Connection;
import java.util.Properties;

@Slf4j
@Intercepts({
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class}),
//        @Signature(
//        type = Executor.class,
//        method = "query", //query无法拦截
//        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class SqlInterceptor implements Interceptor {

    private Properties properties = new Properties();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (invocation.getTarget() instanceof StatementHandler) {
            RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
            StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
            BoundSql boundSql = delegate.getBoundSql();
            String sql = boundSql.getSql().replaceAll("\\s+", " ");
            if (sql.trim().startsWith("select")) {
                log.debug("raw sql: " + sql);
                String tenant = UserContextHolder.getTenant();

                //实际使用需要分析sql判断构建正确的新sql 此处使用一条会出问题的sql警示
                String newSQL = String.format(
                        "select * from ( %s ) tenant_random where tenant='%s'",
                        sql, tenant);
                log.debug("context tenant key: " + tenant);
                ReflectUtil.setFieldValue(boundSql, "sql", newSQL);
                log.debug("new sql: " + boundSql.getSql());
                log.debug("properties: " + properties.toString());
            }
        }
        long startTimeMillis = System.currentTimeMillis();
        Object returnObject = invocation.proceed();
        long endTimeMillis = System.currentTimeMillis();
        log.debug(String.format("SQL执行时间： %s ms", (endTimeMillis - startTimeMillis)));
        return returnObject;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


}
