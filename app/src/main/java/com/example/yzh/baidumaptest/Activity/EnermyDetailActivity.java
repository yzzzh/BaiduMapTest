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

public class EnermyDetailActivity extends Activity implements View.OnClickListener{

    private RadarDB db;
    private Person person;

    private Button btnEnermyList;
    private Button btnEditEnermy;
    private Button btnHome;
    private Button btnFriendList;
    private Button btnDeleteEnermy;
    private TextView tvEnermyNumber;
    private TextView tvEnermyName;
    private TextView tvEnermyLatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enermy_detail);

        initViews();

        db = RadarDB.getInstance(this);

        String number = getIntent().getStringExtra("number");
        person = db.getPerson(number);

        tvEnermyName.setText(person.getName());
        tvEnermyNumber.setText(person.getNumber());
        tvEnermyLatlng.setText(person.getLatitude()+"/"+person.getLongitude());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditEnermyDetial:
                break;
            case R.id.btnDeleteEnermyDetail:
                DeleteEnermy();
                break;
            case R.id.btnEnermyList:
                Intent intent_enermyList = new Intent(EnermyDetailActivity.this,EnermyListActivity.class);
                startActivity(intent_enermyList);
                finish();
                break;
            case R.id.btnEnermyDetailToHome:
                Intent intent_home = new Intent(EnermyDetailActivity.this,MainActivity.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.btnEnermyDetailToFriend:
                Intent intent_frendList = new Intent(EnermyDetailActivity.this,FriendListActivity.class);
                startActivity(intent_frendList);
                finish();
                break;
        }
    }

    private void initViews(){
        tvEnermyName = (TextView) findViewById(R.id.tvEnermyNameDetail);
        tvEnermyNumber = (TextView) findViewById(R.id.tvEnermyNumberDetail);
        tvEnermyLatlng = (TextView) findViewById(R.id.tvEnermyLatlng);

        btnEditEnermy = (Button) findViewById(R.id.btnEditEnermyDetial);
        btnEditEnermy.setOnClickListener(this);
        btnHome = (Button) findViewById(R.id.btnEnermyDetailToHome);
        btnHome.setOnClickListener(this);
        btnEnermyList = (Button) findViewById(R.id.btnEnermyDetailToFriend);
        btnEnermyList.setOnClickListener(this);
        btnFriendList = (Button) findViewById(R.id.btnEnermyList);
        btnFriendList.setOnClickListener(this);
        btnDeleteEnermy = (Button) findViewById(R.id.btnDeleteEnermyDetail);
        btnDeleteEnermy.setOnClickListener(this);
    }

    private void DeleteEnermy(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("警告");
        builder.setMessage("是否确认删除敌人？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EnermyDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                db.delPerson(tvEnermyNumber.getText().toString());
                Intent intent = new Intent(EnermyDetailActivity.this,EnermyListActivity.class);
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
