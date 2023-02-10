package cn.whpu.webserver.Database.connectionPool;

import cn.whpu.webserver.GlobalVar.Lock;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接池对象，单例
 */
public class ConnectionPool {
    private static volatile ConnectionPool dbConnection;
    private ComboPooledDataSource cpds1;
    private ComboPooledDataSource cpds2;

    /**
     * 在构造函数初始化的时候获取数据库连接
     */
    private ConnectionPool() {
        try {
            /*通过属性文件获取数据库连接的参数值**/
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src/main/java/cn/whpu/webserver/Database/connectionPool/jdbc-sqlite1.properties");
            properties.load(fileInputStream);
            /*获取属性文件中的值**/
            String driverClassName = properties.getProperty("jdbc.driverClassName");
            String url = properties.getProperty("jdbc.url");

            /*数据库连接池对象**/
            cpds1 = new ComboPooledDataSource();

            /*设置数据库连接驱动**/
            cpds1.setDriverClass(driverClassName);
            /*设置数据库连接地址**/
            cpds1.setJdbcUrl(url);
            /*初始化时创建的连接数,应在minPoolSize与maxPoolSize之间取值.默认为3**/
            cpds1.setInitialPoolSize(3);
            /*连接池中保留的最大连接数据.默认为15**/
            cpds1.setMaxPoolSize(10);
            /*当连接池中的连接用完时，C3PO一次性创建新的连接数目;**/
            cpds1.setAcquireIncrement(1);
            /*隔多少秒检查所有连接池中的空闲连接,默认为0表示不检查;**/
            cpds1.setIdleConnectionTestPeriod(60);
            /*最大空闲时间,超过空闲时间的连接将被丢弃.为0或负数据则永不丢弃.默认为0;**/
            cpds1.setMaxIdleTime(3000);

            /*因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
             时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
             等方法来提升连接测试的性能。Default: false**/
            cpds1.setTestConnectionOnCheckout(true);

            /*如果设为true那么在取得连接的同时将校验连接的有效性。Default: false **/
            cpds1.setTestConnectionOnCheckin(true);
            /*定义在从数据库获取新的连接失败后重复尝试获取的次数，默认为30;**/
            cpds1.setAcquireRetryAttempts(30);
            /*两次连接中间隔时间默认为1000毫秒**/
            cpds1.setAcquireRetryDelay(1000);
            /* 获取连接失败将会引起所有等待获取连接的线程异常,
             但是数据源仍有效的保留,并在下次调用getConnection()的时候继续尝试获取连接.如果设为true,
             那么尝试获取连接失败后该数据源将申明已经断开并永久关闭.默认为false**/
            cpds1.setBreakAfterAcquireFailure(true);



            /*通过属性文件获取数据库连接的参数值**/
            properties = new Properties();
            fileInputStream = new FileInputStream("src/main/java/cn/whpu/webserver/Database/connectionPool/jdbc-sqlite2.properties");
            properties.load(fileInputStream);
            /*获取属性文件中的值**/
            driverClassName = properties.getProperty("jdbc.driverClassName");
            url = properties.getProperty("jdbc.url");


            cpds2 = new ComboPooledDataSource();
            cpds2.setDriverClass(driverClassName);
            cpds2.setJdbcUrl(url);

            // 初始化时创建的连接数
            cpds2.setInitialPoolSize(1);
            // 连接池中保留的最大连接数据.默认为15
            cpds2.setMaxPoolSize(5);
            // 当连接池中的连接用完时，C3PO一次性创建新的连接数目
            cpds2.setAcquireIncrement(1);
            // 隔多少秒检查所有连接池中的空闲连接,默认为0表示不检查
            cpds2.setIdleConnectionTestPeriod(60);
            // 最大空闲时间,超过空闲时间的连接将被丢弃.为0或负数据则永不丢弃.默认为0
            cpds2.setMaxIdleTime(3000);

            cpds2.setTestConnectionOnCheckout(true);
            cpds2.setTestConnectionOnCheckin(true);
            cpds2.setAcquireRetryAttempts(30);
            cpds2.setAcquireRetryDelay(1000);
            cpds2.setBreakAfterAcquireFailure(true);
        } catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据库连接对象，单例
     *
     */
    public static ConnectionPool getInstance() {
        if (dbConnection == null) {
            synchronized (ConnectionPool.class) {
                if (dbConnection == null) {
                    dbConnection = new ConnectionPool();
                }
            }
        }
        return dbConnection;
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     */
    public final synchronized Connection getConnection() throws SQLException {
        // 当plane1在更新的时候返回plane2的连接
        if(Lock.lock)
            return cpds2.getConnection();
        else
            return cpds1.getConnection();
    }
}
