package com.example.administrator.mymessager.MessageAPI;

/**
 * Created by Administrator on 2/19/2018.
 */
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Administrator on 1/16/2017.
 */

public class ConnectionDB {

    Connection connection;
    @SuppressLint("NewApi")
    public Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://27.106.125.26;databaseName=TallyDB;user=sa;password=optimalrx;";
            connection = DriverManager.getConnection(ConnectionURL);

        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());

        }
        return connection;
    }
}
