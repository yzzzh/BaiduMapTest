package com.example.yzh.baidumaptest.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ImageButton btnDeleteFriend;
    private TextView tvFriendNumber;
    private TextView tvFriendName;
    private TextView tvFriendLatlng;

    private String mNumber;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_detail);

        initViews();

        db = RadarDB.getInstance(this);

        String number = getIntent().getStringExtra("number");
        person = db.getPerson(number);

        name = person.getName();
        mNumber = person.getNumber();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FriendDetailActivity.this,FriendListActivity.class);
        startActivity(intent);
        finish();
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
        btnDeleteFriend = (ImageButton) findViewById(R.id.btnDeleteFriendDetail);
        btnDeleteFriend.setOnClickListener(this);
    }

    private void DeleteFriend(){
        final View dialogView = LayoutInflater.from(FriendDetailActivity.this).inflate(R.layout.friend_delete,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendDetailActivity.this);

        Button btnDeleteFriendOK = (Button) dialogView.findViewById(R.id.btnDeleteFriendOK);
        Button btnDeleteFriendCancel = (Button) dialogView.findViewById(R.id.btnDeleteFriendCancel);
        TextView tvDeleteFriendNumber = (TextView) dialogView.findViewById(R.id.tvDeleteFriendNumber);

        tvDeleteFriendNumber.setText(name+" / "+mNumber);

        builder.setView(dialogView).setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        btnDeleteFriendOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delPerson(mNumber);
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
}
