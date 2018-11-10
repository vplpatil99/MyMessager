package com.example.administrator.mymessager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/19/2018.
 */

public class User {
    public static  String UserName;
    public static String ReceiverName;
    public static String GorI;


    public static String getGorI() {
        return GorI;
    }

    public static void setGorI(String gorI) {
        GorI = gorI;
    }



    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public static String getUserName() {
        return UserName;
    }

    public static String getReceiverName() {
        return ReceiverName;
    }


}
