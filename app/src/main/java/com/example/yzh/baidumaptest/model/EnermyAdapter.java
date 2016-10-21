package com.example.yzh.baidumaptest.model;

import android.content.Context;
import android.content.DialogInterface;
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

import com.example.yzh.baidumaptest.R;
import com.example.yzh.baidumaptest.database.RadarDB;

import java.util.List;

/**
 * Created by YZH on 2016/10/16.
 */

public class EnermyAdapter extends ArrayAdapter<Person> {
    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private RadarDB db;
    private String number;
    private String name;

    public EnermyAdapter(Context context, int textViewResourceId, List<Person> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        db = RadarDB.getInstance(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvEnermyName = (TextView) view.findViewById(R.id.tvEnermyName);
            viewHolder.tvEnermyNumber_item = (TextView) view.findViewById(R.id.tvEnermyNumber_item);
            viewHolder.btnDeleteEnermy = (ImageButton) view.findViewById(R.id.btnDeleteEnermy);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        viewHolder.tvEnermyName.setText(person.getName());
        viewHolder.tvEnermyNumber_item.setText(person.getNumber());

        viewHolder.btnDeleteEnermy.setOnClickListener(new lvButtonListener(position));

        number = viewHolder.tvEnermyNumber_item.getText().toString();
        name = viewHolder.tvEnermyName.getText().toString();

        return view;
    }

    class ViewHolder{
        TextView tvEnermyName;
        TextView tvEnermyNumber_item;
        ImageButton btnDeleteEnermy;
    }

    // 自定义listView中监听类
    class lvButtonListener implements View.OnClickListener {
        private int position;

        public lvButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            if ((v.getId()) == (viewHolder.btnDeleteEnermy.getId())) {
                DeleteEnermy();
            }
        }
    }

    private void DeleteEnermy(){

        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.enermy_delete,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Button btnDeleteEnermyOK = (Button) dialogView.findViewById(R.id.btnDeleteEnermyOK);
        Button btnDeleteEnermyCancel = (Button) dialogView.findViewById(R.id.btnDeleteEnermyCancel);
        TextView tvDeleteEnermyNumber = (TextView) dialogView.findViewById(R.id.tvDeleteEnermyNumber);

        tvDeleteEnermyNumber.setText(name+" / "+number);

        builder.setView(dialogView).setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        btnDeleteEnermyOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delPerson(number);
                alertDialog.dismiss();
            }
        });

        btnDeleteEnermyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
