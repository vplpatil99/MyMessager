package com.example.administrator.mymessager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mymessager.DataBase.Contact;

import java.util.List;

/**
 * Created by Administrator on 2/23/2018.
 */

public class ContactListAdapter extends BaseAdapter {
    private List<Contact> contactList;
    TextView phonenoText,nameText;
    ImageView profileImage;
    Contact contact;
    private Activity activity;
    private LayoutInflater inflater;


    public ContactListAdapter(Activity activity,List<Contact> contacts) {

        this.activity = activity;
        contactList = contacts;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int location) {
        return contactList.get(location);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_contact, null);

        nameText = (TextView) convertView.findViewById(R.id.text_contact_name);
        phonenoText = (TextView) convertView.findViewById(R.id.text_contact_number);
        profileImage = (ImageView) convertView.findViewById(R.id.image_contact_profile);

        contact=(Contact) contactList.get(position);

        nameText.setText(contact.getName());
        phonenoText.setText(contact.getPhoneNumber());
        profileImage.setImageResource(R.drawable.circle);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
