package com.example.yzh.baidumaptest.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yzh.baidumaptest.R;
import com.example.yzh.baidumaptest.database.RadarDB;
import com.example.yzh.baidumaptest.model.EnermyAdapter;
import com.example.yzh.baidumaptest.model.FriendAdapter;
import com.example.yzh.baidumaptest.model.Person;

import java.util.List;

public class EnermyListActivity extends Activity implements View.OnClickListener{

    private List<Person> enermyList;
    private RadarDB db;
    private EnermyAdapter adapter;

    private ListView lvEnermy;
    private Button btnHome;
    private Button btnAddEnermy;
    private Button btnEditEnermy;
    private Button btnFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enermy_list);

        initViews();

        db = RadarDB.getInstance(this);

        loadEnermies();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddEnermy:
                addEnermy();
                break;
            case R.id.btnEditEnermy:
                break;
            case R.id.btnEnermyToHome:
                Intent intent_home = new Intent(EnermyListActivity.this,MainActivity.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.btnEnermyToFriend:
                Intent intent_friend = new Intent(EnermyListActivity.this,FriendListActivity.class);
                startActivity(intent_friend);
                finish();
                break;
        }
    }

    private void initViews(){
        lvEnermy = (ListView) findViewById(R.id.lvEnermy);
        btnAddEnermy = (Button) findViewById(R.id.btnAddEnermy);
        btnEditEnermy = (Button) findViewById(R.id.btnEditEnermy);
        btnHome = (Button) findViewById(R.id.btnEnermyToHome);
        btnFriendList = (Button) findViewById(R.id.btnEnermyToFriend);

        btnAddEnermy.setOnClickListener(this);
        btnEditEnermy.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnFriendList.setOnClickListener(this);
    }

    private void addEnermy(){
        //生成自定义的布局
        View myDialog = LayoutInflater.from(this).inflate(R.layout.enermy_add,null);
        //生成builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etAddEnermyName = (EditText) myDialog.findViewById(R.id.etAddEnermyName);
        final EditText etAddEnermyNumber = (EditText) myDialog.findViewById(R.id.etAddEnermyNumber);

        builder.setView(myDialog)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Person person = new Person();
                        person.setName(etAddEnermyName.getText().toString());
                        person.setNumber(etAddEnermyNumber.getText().toString());
                        person.setType("enermy");
                        db.addPerson(person);
                        loadEnermies();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        //生成弹出框
        AlertDialog alertDialog = builder.create();
        //展示弹出框
        alertDialog.show();
    }

    private void loadEnermies(){
        enermyList = db.loadPeople("enermy");
        adapter = new EnermyAdapter(this,R.layout.enermy_item,enermyList);
        lvEnermy.setAdapter(adapter);

        lvEnermy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EnermyListActivity.this,"hhhhhh",Toast.LENGTH_SHORT).show();
                Person person = enermyList.get(position);
                Intent intent = new Intent(EnermyListActivity.this,EnermyDetailActivity.class);
                intent.putExtra("number",person.getNumber());
                startActivity(intent);;
                finish();
            }
        });
    }
}
