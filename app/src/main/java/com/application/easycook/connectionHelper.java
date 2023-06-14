package com.application.easycook;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.Connection;

public class connectionHelper {
    Connection con;
    @SuppressLint("NewApi")
    String uname, pass, ip, port, database;

    public Connection connectionclass(){
        ip="10.100.102.12";
        port="1433";
        uname="localiron";
        pass="evil123";
        database="eadb";
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;
        String ConnectionURL= null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ database+";user="+uname+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex){
            Log.e("Error ",ex.getMessage());
        }
        return connection;
    } // End ConnectionClass


}