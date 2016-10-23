package com.example.yzh.baidumaptest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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
import com.example.yzh.baidumaptest.model.FriendAdapter;
import com.example.yzh.baidumaptest.model.Person;

import java.util.List;
import java.util.regex.Pattern;

public class FriendListActivity extends Activity implements View.OnClickListener{

    public static List<Person> friendList;

    private RadarDB db;
    private FriendAdapter adapter;

    private ListView lvFriend;
    private Button btnAddFriend;
    private Button btnEditFriend;
    private Button btnHome;
    private Button btnEnermyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_list);

        initViews();

        db = RadarDB.getInstance(this);

        loadFriends();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddFriend:
                AddFriend();
                break;
            case R.id.btnEditFriend:
                loadFriends();
                break;
            case R.id.btnFriendToHome:
                Intent intent_home = new Intent(FriendListActivity.this,MainActivity.class);
                startActivity(intent_home);
                finish();
                break;
            case R.id.btnFriendToEnermy:
                Intent intent_enermy = new Intent(FriendListActivity.this,EnermyListActivity.class);
                startActivity(intent_enermy);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFriends();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFriends();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFriends();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FriendListActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews(){
        lvFriend = (ListView) findViewById(R.id.lvFriend);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        btnEditFriend = (Button) findViewById(R.id.btnEditFriend);
        btnHome = (Button) findViewById(R.id.btnFriendToHome);
        btnEnermyList = (Button) findViewById(R.id.btnFriendToEnermy);
        btnAddFriend.setOnClickListener(this);
        btnEditFriend.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnEnermyList.setOnClickListener(this);
    }

    private void AddFriend(){
        /*
        总体思路：
        1.获取布局
        2.生成并设置builder
        3.由builder生成altertdialog控件
        4.展示
         */

        //生成自定义的布局
        final View myDialog = LayoutInflater.from(this).inflate(R.layout.friend_add,null);
        //生成builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etAddFriendName = (EditText) myDialog.findViewById(R.id.etAddFriendName);
        final EditText etAddFriendNumber = (EditText) myDialog.findViewById(R.id.etAddFriendNumber);
        final Button btnAddFriendOK = (Button) myDialog.findViewById(R.id.btnAddFriendOK);
        final Button btnAddFriendCancel = (Button) myDialog.findViewById(R.id.btnAddFriendCancel);

        builder.setView(myDialog).setCancelable(false);

        //生成弹出框
        final AlertDialog alertDialog = builder.create();
        //展示弹出框

        btnAddFriendOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etAddFriendName.getText().toString();
                String number = etAddFriendNumber.getText().toString();
                if (!name.equals("") && !number.equals("")) {
                    if (!isInteger(number)){
                        Toast.makeText(FriendListActivity.this,"电话号码格式有误",Toast.LENGTH_SHORT).show();
                    }else {
                        Person person = new Person();
                        person.setName(etAddFriendName.getText().toString());
                        person.setNumber(etAddFriendNumber.getText().toString());
                        person.setType("friend");
                        db.addPerson(person);
                        loadFriends();
                        alertDialog.dismiss();
                    }
                }else {
                    Toast.makeText(FriendListActivity.this,"姓名/号码 不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddFriendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    private void loadFriends(){
        friendList = db.loadPeople("friend");
        adapter = new FriendAdapter(this, R.layout.friend_item, friendList);
        lvFriend.setAdapter(adapter);

        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = friendList.get(position);
                Intent intent = new Intent(FriendListActivity.this,FriendDetailActivity.class);
                intent.putExtra("number",person.getNumber());
                startActivity(intent);
                finish();
            }
        });
    }

    //判断是否为字符串
    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
