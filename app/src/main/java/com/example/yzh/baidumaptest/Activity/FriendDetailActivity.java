package com.example.yzh.baidumaptest.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzh.baidumaptest.R;
import com.example.yzh.baidumaptest.database.RadarDB;
import com.example.yzh.baidumaptest.model.Person;

public class FriendDetailActivity extends Activity implements View.OnClickListener{

    private RadarDB db;
    private Person person = new Person();

    private Button btnFriendList;
    private Button btnEditFriend;
    private Button btnHome;
    private Button btnEnermyList;
    private Button btnDeleteFriend;
    private TextView tvFriendNumber;
    private TextView tvFriendName;
    private TextView tvFriendLatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_detail);

        initViews();

        db = RadarDB.getInstance(this);

        String number = getIntent().getStringExtra("number");
        person = db.getPerson(number);

        tvFriendName.setText(person.getName());
        tvFriendNumber.setText(person.getNumber());
        tvFriendLatlng.setText(person.getLatitude()+"/"+person.getLongitude());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditFriendDetial:
                break;
            case R.id.btnDeleteFriendDetail:
                DeleteFriend();
                break;
            case R.id.btnFriendList:
                Intent intent_friendList = new Intent(FriendDetailActivity.this,FriendListActivity.class);
                startActivity(intent_friendList);
                finish();
                break;
            case R.id.btnFriendDetailToHome:
                Intent intent_home = new Intent(FriendDetailActivity.this,MainActivity.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.btnFriendDetailToEnermy:
                Intent intent_enermyList = new Intent(FriendDetailActivity.this,EnermyListActivity.class);
                startActivity(intent_enermyList);
                finish();
                break;
        }
    }

    private void initViews(){
        tvFriendName = (TextView) findViewById(R.id.tvFriendNameDetail);
        tvFriendNumber = (TextView) findViewById(R.id.tvFriendNumberDetail);
        tvFriendLatlng = (TextView) findViewById(R.id.tvFriendLatlng);

        btnEditFriend = (Button) findViewById(R.id.btnEditFriendDetial);
        btnEditFriend.setOnClickListener(this);
        btnHome = (Button) findViewById(R.id.btnFriendDetailToHome);
        btnHome.setOnClickListener(this);
        btnEnermyList = (Button) findViewById(R.id.btnFriendDetailToEnermy);
        btnEnermyList.setOnClickListener(this);
        btnFriendList = (Button) findViewById(R.id.btnFriendList);
        btnFriendList.setOnClickListener(this);
        btnDeleteFriend = (Button) findViewById(R.id.btnDeleteFriendDetail);
        btnDeleteFriend.setOnClickListener(this);
    }

    private void DeleteFriend(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("警告");
        builder.setMessage("是否确认删除朋友？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FriendDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                db.delPerson(tvFriendNumber.getText().toString());
                Intent intent = new Intent(FriendDetailActivity.this,FriendListActivity.class);
                startActivity(intent);
                finish();
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
