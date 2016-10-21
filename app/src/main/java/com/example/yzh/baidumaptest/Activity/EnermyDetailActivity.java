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

public class EnermyDetailActivity extends Activity implements View.OnClickListener{

    private RadarDB db;
    private Person person;

    private Button btnEnermyList;
    private Button btnEditEnermy;
    private Button btnHome;
    private Button btnFriendList;
    private ImageButton btnDeleteEnermy;
    private TextView tvEnermyNumber;
    private TextView tvEnermyName;
    private TextView tvEnermyLatlng;

    private String name;
    private String mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enermy_detail);

        initViews();

        db = RadarDB.getInstance(this);

        String number = getIntent().getStringExtra("number");
        person = db.getPerson(number);

        name = person.getName();
        mNumber = person.getNumber();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnermyDetailActivity.this,EnermyListActivity.class);
        startActivity(intent);
        finish();
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
        btnDeleteEnermy = (ImageButton) findViewById(R.id.btnDeleteEnermyDetail);
        btnDeleteEnermy.setOnClickListener(this);
    }

    private void DeleteEnermy(){
        final View dialogView = LayoutInflater.from(EnermyDetailActivity.this).inflate(R.layout.enermy_delete,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(EnermyDetailActivity.this);

        Button btnDeleteEnermyOK = (Button) dialogView.findViewById(R.id.btnDeleteEnermyOK);
        Button btnDeleteEnermyCancel = (Button) dialogView.findViewById(R.id.btnDeleteEnermyCancel);
        TextView tvDeleteEnermyNumber = (TextView) dialogView.findViewById(R.id.tvDeleteEnermyNumber);

        tvDeleteEnermyNumber.setText(name+" / "+mNumber);

        builder.setView(dialogView).setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        btnDeleteEnermyOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delPerson(mNumber);
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
