package me.SamV522.dBUB;

import java.sql.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * Author: SamV522
 * Date: 9/02/12
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class Database {
    private static Connection dbCon;
    private static boolean dbConnected;
    dBUBLogger pluginLogger = Main.pluginLogger;

    public Boolean connect(String dbHost, String databaseName, String username, String password, String port){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            if(dbConnected){
                if(closeConnection()){
                    connect(dbHost, databaseName, username, password, port);   
                }
            }else{
                    String url = "jdbc:mysql://%s:%s/%s";
                    url = String.format(url, dbHost, port, databaseName);
                    pluginLogger.info("Connecting to database:");
                    pluginLogger.info(url);
                    pluginLogger.info("Username: "+username);
                    pluginLogger.info("Password: "+password);
                    Connection dbCon = DriverManager.getConnection(url, username, password);
                    dbConnected = true;
            }
        }catch(SQLException e){
            pluginLogger.warning("Database Error: " + e.getMessage());
        }catch(ClassNotFoundException e){
            pluginLogger.warning("Driver Error: "+ e.getMessage());
        }

        return dbConnected;
    }

    public Connection getConnection()
    {
        return dbCon;
    }

    public Boolean isConnected()
    {
        return dbConnected;
    }

    public ResultSet sendQuery(String dbQuery)
    {
        ResultSet rs = null;
        Statement stmt = null;
        try{
            stmt = dbCon.createStatement();
            rs = stmt.executeQuery(dbQuery);
        }catch(SQLException e){
            pluginLogger.info("Database Error: "+ e.getMessage());
        }
        return rs;
    }

    public Boolean closeConnection()
    {
        Boolean retBool = false;
        if(dbConnected){
            try{
                dbCon.close();
                dbCon = null;
            }catch(SQLException e){
                pluginLogger.warning("Could not close active Database connection!");
            }finally {
                if(dbCon == null){
                    retBool = true;
                }
            }
        }
        return retBool;
    }

}