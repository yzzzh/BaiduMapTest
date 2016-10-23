package com.example.yzh.baidumaptest.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzh.baidumaptest.Activity.FriendListActivity;
import com.example.yzh.baidumaptest.R;
import com.example.yzh.baidumaptest.database.RadarDB;

import java.util.List;

/**
 * Created by YZH on 2016/10/16.
 */

public class FriendAdapter extends ArrayAdapter<Person> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private RadarDB db;
    private List<Person> friendList = FriendListActivity.friendList;

    public FriendAdapter(Context context, int textViewResourceId, List<Person> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        db = RadarDB.getInstance(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Person person = getItem(position);

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvFriendName = (TextView) view.findViewById(R.id.tvFriendName);
            viewHolder.tvFriendNumber_item = (TextView) view.findViewById(R.id.tvFriendNumber_item);
            viewHolder.btnDeleteFriend = (ImageButton) view.findViewById(R.id.btnDeleteFriend);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tvFriendName.setText(person.getName());
        viewHolder.tvFriendNumber_item.setText(person.getNumber());

        viewHolder.btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFriend(person,position);
            }
        });

        return view;
    }

    class ViewHolder{
        TextView tvFriendName;
        TextView tvFriendNumber_item;
        ImageButton btnDeleteFriend;
    }

    private void DeleteFriend(final Person person, final int position){

        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.friend_delete,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Button btnDeleteFriendOK = (Button) dialogView.findViewById(R.id.btnDeleteFriendOK);
        Button btnDeleteFriendCancel = (Button) dialogView.findViewById(R.id.btnDeleteFriendCancel);
        TextView tvDeleteFriendNumber = (TextView) dialogView.findViewById(R.id.tvDeleteFriendNumber);

        tvDeleteFriendNumber.setText(person.getName()+" / "+person.getNumber());

        builder.setView(dialogView).setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        btnDeleteFriendOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delPerson(person.getNumber());
                refresh(position);
                alertDialog.dismiss();
            }
        });

        btnDeleteFriendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void refresh(int position){
        friendList.remove(position);
        this.notifyDataSetChanged();
    }
}

