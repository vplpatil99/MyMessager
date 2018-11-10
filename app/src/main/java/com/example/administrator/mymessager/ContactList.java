package com.example.administrator.mymessager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.administrator.mymessager.DataBase.Contact;
import com.example.administrator.mymessager.DataBase.DatabaseHandler;
import com.example.administrator.mymessager.MessageAPI.ConnectionDB;
import com.example.administrator.mymessager.MessageAPI.Validate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {
    private ListView mMessageListView;
    private ContactListAdapter mMessageAdapter;
    private List<Contact> mMessageList=new ArrayList<>();
    SharedPreferences sharedpreferences;
    ProgressBar contactpb;
    public static final String MyPREFERENCES = "MyPrefs" ;

    Button refreshBtn,logout;

    DatabaseHandler db;
    Validate val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        db=new DatabaseHandler(this);
        val=new Validate();




        contactpb=(ProgressBar) findViewById(R.id.contact_progress);
        refreshBtn=(Button) findViewById(R.id.contact_Refresh_button);
        logout=(Button) findViewById(R.id.logout_button);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mMessageListView = (ListView) findViewById(R.id.listview_contact_list);
        mMessageAdapter = new ContactListAdapter(this,mMessageList);
        mMessageListView.setAdapter(mMessageAdapter);

        for (Contact var : db.getAllContacts())
        {
            mMessageList.add(var);
            mMessageAdapter.notifyDataSetChanged();
        }
        if(getContactFlag()=="NotUpdated")
        {
            Refreshcontact();
            setContactFlag("Updated");
        }
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Refreshcontact();

            }
        });
        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Sender;
                TextView listText = (TextView) view.findViewById(R.id.text_contact_number);
                Sender=listText.getText().toString();
                User.setReceiverName(Sender);

                if(val.IsGroup(User.ReceiverName)=="True")
                {
                    User.setGorI("G");
                }else{
                    User.setGorI("I");
                }
                Intent intent = new Intent(ContactList.this, MessageListActivity.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSnderName("logout");
                Intent intent = new Intent(ContactList.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }

    public void setSnderName(String Sender)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("SenderName",Sender);
        editor.commit();
    }

    public String getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);

            }
            cursor.close();
        }

        return contactName;
    }


    public void Refreshcontact() {
        // do something long
        Runnable runnable = new Runnable() {
            String name;
            Statement stisUser=null;
            ResultSet rs = null;
            String phoneNumber;
            @Override
            public void run() {
                db.deleteAllContact();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactpb.setVisibility(View.VISIBLE);
                        mMessageList.clear();
                        mMessageAdapter.notifyDataSetChanged();
                    }
                });
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (phones.moveToNext()) {
                    name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(phoneNumber.length()>=10) {
                        phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
                    }
                    if (val.validateUser(phoneNumber) == "True" && !phoneNumber.equals(User.getUserName())&&!phoneNumber.equals("+91"+User.getUserName())) {

                        db.addContact(new Contact(0, name, phoneNumber));

                    }
                }
                phones.close();

                //get groups to the local list
                try {
                    ConnectionDB co = new ConnectionDB();
                    Connection con = co.getConnection();
                    String query = "select Username from account where username in (select groupname from groups where user_name='" + User.getUserName() + "')";
                    stisUser = con.createStatement();
                    rs = stisUser.executeQuery(query);
                    if (rs.next())
                    {
                        db.addContact(new Contact(0, rs.getString(1), rs.getString(1)));
                    }
                    con.close();
                }catch(Exception e){

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessageList.clear();
                        mMessageAdapter.notifyDataSetChanged();
                        for (Contact var : db.getAllContacts())
                        {
                            mMessageList.add(var);
                            mMessageAdapter.notifyDataSetChanged();
                        }
                        mMessageAdapter.notifyDataSetChanged();
                        contactpb.setVisibility(View.GONE);

                    }
                });

            }
        };
        new Thread(runnable).start();
    }

    public void setContactFlag(String ContactFlag)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("ContactFlag",ContactFlag);
        editor.commit();
    }

    public String getContactFlag() {
        String ContactFlag=sharedpreferences.getString("ContactFlag", "NotUpdated");
        return  ContactFlag;
    }

}

