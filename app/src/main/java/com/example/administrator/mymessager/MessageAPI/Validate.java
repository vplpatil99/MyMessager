package com.example.administrator.mymessager.MessageAPI;

/**
 * Created by Administrator on 2/19/2018.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Validate {

    public String validateUser(String Username)
    {
        String result="False";
        Connection con=null;
        Statement st=null;
        ResultSet rs = null;

        ConnectionDB co=new ConnectionDB();
        con=co.getConnection();
        String SQL = "SELECT * FROM Account where Username='"+Username+"'";



        try {
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next()) {
                result="True";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return result;
    }

    public String IsGroup(String Username)
    {
        String result="False";
        Connection con=null;
        Statement st=null;
        ResultSet rs = null;

        ConnectionDB co=new ConnectionDB();
        con=co.getConnection();
        String SQL = "SELECT * FROM Account where Username='"+Username+"' and GorI='G'";



        try {
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            if(rs.next()) {
                result="True";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return result;
    }


}
