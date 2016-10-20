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
            viewHolder.btnDeleteEnermy = (Button) view.findViewById(R.id.btnDeleteEnermy);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        viewHolder.tvEnermyName.setText(person.getName());
        viewHolder.tvEnermyNumber_item.setText(person.getNumber());

        viewHolder.btnDeleteEnermy.setOnClickListener(new lvButtonListener(position));

        number = viewHolder.tvEnermyNumber_item.getText().toString();

        return view;
    }

    class ViewHolder{
        TextView tvEnermyName;
        TextView tvEnermyNumber_item;
        Button btnDeleteEnermy;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("警告");
        builder.setMessage("是否确认删除敌人？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                db.delPerson(number);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
