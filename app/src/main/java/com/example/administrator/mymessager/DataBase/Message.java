package com.example.administrator.mymessager.DataBase;

/**
 * Created by Administrator on 2/23/2018.
 */

public class Message {
    int id;
    String Type;
    String To;
    String From;
    String Message;
    String report;
    String GNameorI;
    String Datetime;

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setTo(String to) {
        To = to;
    }

    public void setFrom(String from) {
        From = from;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setGNameorI(String GNameorI) {
        this.GNameorI = GNameorI;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public String getTo() {
        return To;
    }

    public String getFrom() {
        return From;
    }

    public String getMessage() {
        return Message;
    }

    public String getReport() {
        return report;
    }

    public String getGNameorI() {
        return GNameorI;
    }

    public String getDatetime() {
        return Datetime;
    }

    public Message(){

    }
    public Message(int id, String type, String to, String from, String message, String report, String GNameorI, String datetime) {
        this.id = id;
        Type = type;
        To = to;
        From = from;
        Message = message;
        this.report = report;
        this.GNameorI = GNameorI;
        Datetime = datetime;
    }
}
