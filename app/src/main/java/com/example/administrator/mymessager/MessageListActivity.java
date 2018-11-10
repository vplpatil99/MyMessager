package com.example.administrator.mymessager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mymessager.DataBase.DatabaseHandler;
import com.example.administrator.mymessager.MessageAPI.ConnectionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2/16/2018.
 */

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<UserMessage> mMessageList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Button Send;
    DatabaseHandler db;
    EditText ChatBox;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Send=(Button) findViewById(R.id.button_chatbox_send);
        ChatBox=(EditText) findViewById(R.id.edittext_chatbox);
        db=new DatabaseHandler(this);


        mMessageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, mMessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setItemAnimator(new DefaultItemAnimator());
        mMessageRecycler.setAdapter(mMessageAdapter);
        ReadMessages();
        ReadGroupMessages();

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChatboxMessages=ChatBox.getText().toString().trim();
                if(!ChatboxMessages.equals("")) {
                    Date TimeNow = Calendar.getInstance().getTime();
                    UserMessage Message = new UserMessage(ChatBox.getText().toString(), User.getUserName(), TimeNow);
                    sendmessage(ChatBox.getText().toString(),dateFormat.format(TimeNow).toString());
                    mMessageList.add(Message);
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                    ChatBox.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),"Empty Message Not Allowed",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void setMesssage(String message,String sender,Date createAt)
    {
        UserMessage Message = new UserMessage(message, sender, createAt);
        mMessageList.add(Message);
        mMessageAdapter.notifyDataSetChanged();
    }

    public void ReadMessages() {//for individual


        // do something long
        Runnable runnable = new Runnable() {
            Statement stmt = null,stisUser=null;
            ResultSet rs = null;
            String User_name=User.getUserName();
            String Receiver=User.getReceiverName();
            String msg,sdr,dt;
            Date createdat;
            @Override
            public void run(){
                ConnectionDB co=new ConnectionDB();
                Connection con=co.getConnection();
                String id;

                String query = "select * from Account where Username in ('"+Receiver+"','+91"+Receiver+"')";
                try {
                    stisUser = con.createStatement();
                    rs = stisUser.executeQuery(query);
                    if(rs.next()){
                        while(1!=2){
                            if(isDbConnected(con))
                            {
                                //SELECT `id`, `Type`, `To`, `from`, `Message`, `report`, `status` FROM `Message` WHERE `To`='vipul'
                                String SQL = "SELECT * FROM Message where [To]='"+User_name+"' and report='S' and [from]='"+Receiver+"' and status='I'";
                                try {


                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(SQL);


                                    while (rs.next()) {
                                        id=rs.getString(1);
                                        msg=rs.getString(5);
                                        sdr=rs.getString(4);
                                        sdr=db.getContactName(sdr);

                                        dt=rs.getString(8);
                                        createdat=rs.getTimestamp(8);

                                            //add message to the main activitys
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            //stuff that updates u
                                                setMesssage(msg,sdr,createdat);
                                                mMessageAdapter.notifyDataSetChanged();
                                                mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                                            }
                                        });

                                        Statement st = con.createStatement();
                                        st.executeUpdate("delete from Message where id="+id+"");
                                        //st.executeUpdate("update Message set `report`='R' where `id`="+id+"");

                                    }

                                } catch (SQLException ex) {
                                    //Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                                }


                                // Iterate through the data in the result set and display it.
                            }//end of if to check db connection
                            else{
                                //System.out.println("You are offline connect again");
                            }
                        }//while
                    }//if
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            public boolean isDbConnected(Connection con) {
                //final String CHECK_SQL_QUERY = "SELECT 1";
                try {
                    if(!con.isClosed() || con!=null){
                        return true;
                    }
                } catch (SQLException e) {
                    return false;
                }
                return false;
            }
        };
        new Thread(runnable).start();
    }


    public void ReadGroupMessages()
    {
        Runnable runnable = new Runnable() {
            Statement stmt = null,stisUser=null;
            ResultSet rs = null;
            String User_name=User.getUserName();
            String Receiver=User.getReceiverName();
            String msg,sdr;
            Date createdat;
            @Override
            public void run(){
                ConnectionDB co=new ConnectionDB();
                Connection con=co.getConnection();
                String id;
                String query = "select * from Groups where groupname in ('"+Receiver+"')";
                try {
                    stisUser = con.createStatement();
                    rs = stisUser.executeQuery(query);
                    if(rs.next()){
                        while(1!=2){
                            //SELECT `id`, `Type`, `To`, `from`, `Message`, `report`, `status` FROM `Message` WHERE `To`='vipul'
                            if(isDbConnected(con))
                            {
                                String SQL = "SELECT * FROM Message where [To]='"+User_name+"' and report='S' and status='"+Receiver+"'";
                                try {


                                    stmt = con.createStatement();
                                    rs = stmt.executeQuery(SQL);


                                    while (rs.next()) {
                                        id=rs.getString(1);
                                        sdr=rs.getString(4);
                                        msg=rs.getString(5);
                                        createdat=rs.getDate(8);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                //stuff that updates u
                                                setMesssage(msg,sdr,createdat);
                                                mMessageAdapter.notifyDataSetChanged();
                                                mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                                            }
                                        });

                                        //System.out.println(rs.getString(4) + " Say: " + rs.getString(5));
                                        Statement st = con.createStatement();
                                        st.executeUpdate("delete from Message where id="+id+"");
                                        //st.executeUpdate("update Message set `report`='R' where `id`="+id+"");
                                        //}else if(status=="G"&&To==User_name){
                                        //}

                                    }

                                } catch (SQLException ex) {
                                    //Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
                                }


                            }//check connection end if
                            else{
                                System.out.println("You are offline connect again");
                            }
                            // Iterate through the data in the result set and display it.
                        }//while
                    }//if

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            public boolean isDbConnected(Connection con) {
                //final String CHECK_SQL_QUERY = "SELECT 1";
                try {
                    if(!con.isClosed() || con!=null){
                        return true;
                    }
                } catch (SQLException e) {
                    return false;
                }
                return false;
            }
        };
        new Thread(runnable).start();
    }
    public void sendmessage(String Message,String CurrnetDate)
    {
        Connection con=null;
        String msg=Message;
        String Username=User.getUserName(),receiver=User.getReceiverName();
        Statement st=null;
        Statement stmt = null;
        ResultSet rs = null;
        String UN=null;

        try{
        ConnectionDB conDB=new ConnectionDB();
            con=conDB.getConnection();
        st = con.createStatement();
        if(User.getGorI()=="I")
        {

	          /*From A To B*/
            st.executeUpdate("insert into Message Values('text','"+receiver+"','"+Username+"','"+msg+"','S','I','"+CurrnetDate+"')");
        }else if(User.getGorI()=="G"){
            String SQL = "SELECT User_name FROM Groups where User_name<>'"+Username+"' and groupname='"+receiver+"'";
            try {

                stmt = con.createStatement();
                rs = stmt.executeQuery(SQL);

                while (rs.next()) {
                    UN=rs.getString(1);
                    st.executeUpdate("insert into Message Values('text','"+UN+"','"+Username+"','"+msg+"','S','"+receiver+"','"+CurrnetDate+"')");
                }

            } catch (SQLException ex) {
                Toast.makeText(getApplicationContext(),"Sender or Receiver Name is incorrect",Toast.LENGTH_SHORT).show();
                //Logger.getLogger(ReadMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }catch(Exception e)
    {
        Toast.makeText(getApplicationContext(),"Sender or Receiver Name is incorrect",Toast.LENGTH_SHORT).show();
        //Logger.getLogger(DeleteMessages.class.getName()).log(Level.SEVERE, null, e);
    }
}

}
