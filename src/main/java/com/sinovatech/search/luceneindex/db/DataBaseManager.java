package com.sinovatech.search.luceneindex.db;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

    private String driver = "oracle.jdbc.driver.OracleDriver";
    private String driver_mysql = "com.mysql.jdbc.Driver";
    private String driver_oracle = "oracle.jdbc.driver.OracleDriver";
    private String driver_sqlserver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private DbType dbType = DbType.ORACLE;

    public static enum DbType {
        SQLSERVER {
            @Override
            public String toString() {
                return "SQLSERVER";
            }
        }, //sqlserver
        ORACLE {
            @Override
            public String toString() {
                return "ORACLE";
            }
        }, //ORACLE
        MYSQL {
            @Override
            public String toString() {
                return "MYSQL";
            }
        }//MYSQL
    }

    private Connection conn = null;
    private Statement statement = null;

    private String path = "";
    // jdbc:oracle:thin:@11.1.1:1521:mydb    
    // jdbc:mysql://11.1.1:3306/
    // mydb?useUnicode=true&characterEncoding=utf-8&autoReconnect=true    
    private String user = "";
    private String pwd = "";

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    /**   
     * Constructor   
     *    
     */
    public DataBaseManager() {
        init();
    }

    public DataBaseManager(String driver, String url, String user, String pwd) {
        if (url.indexOf("oracle") != -1) {
            this.driver = driver_oracle;
            this.dbType = DbType.ORACLE;
        } else if (url.indexOf("mysql") != -1) {
            this.driver = driver_mysql;
            this.dbType = DbType.MYSQL;
        } else if (url.indexOf("sqlserver") != -1) {
            this.driver = driver_sqlserver;
            this.dbType = DbType.SQLSERVER;
        } else {
            this.driver = driver;
            this.dbType = DbType.ORACLE;
        }

        this.path = url;
        this.user = user;
        this.pwd = pwd;

        init();
    }

    private void init() {
        //    
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block    
            e.printStackTrace();
        }

        try {
            conn = java.sql.DriverManager.getConnection(path, user, pwd);
            statement = conn.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block    
            System.out.println("can not connect db!");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**   
     * close Connection   
     *    
     * @param sql   
     * @return   
     */
    public ResultSet getResultSet(String sql) throws Exception {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block    
            e.printStackTrace();
            throw new Exception("查询出错", e);
        }
        return rs;
    }

    /**   
     *    
     * @param hql   
     * @return   
     */
    public boolean exe(String hql) {
        try {
            return statement.execute(hql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block    
            e.printStackTrace();
        }

        return false;
    }

    /**   
     * close Connection   
     *    
     */
    public void closeConnection() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block    
                e.printStackTrace();
            }
        if (statement != null)
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block    
                e.printStackTrace();
            }
    }
    
    @Test
    public static void ss() {
        String url = "jdbc:sqlserver://" + "192.168.1.67" + ":" + "1433" + ";databaseName=searchH";
        DataBaseManager dataBaseManager = new DataBaseManager("",
                url, "sa", "Yhxx123");
        Connection conn = dataBaseManager.getConn();
        System.out.println(conn);
    }
}