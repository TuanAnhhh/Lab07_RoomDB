package com.example.dataroomdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ListView lvUsers;
    private List<User> listUser;
    private ArrayAdapter mAdapter;
    private Button btnAdd;
    private Button btnRemove;
    private Button btnCancel;
    UserDao userDao;
    private EditText tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        handle();
    }
    public void init(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "UserManager")
                .allowMainThreadQueries()
                .build();
        userDao = db.userDao();
        User user1 = new User(2,"Diem","");

        listUser = userDao.getAll();
        User[] users = new  User[listUser.size()];
        listUser.toArray(users);

        mAdapter = new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1, users);
        lvUsers = findViewById(R.id.lvUsers);

        lvUsers.setAdapter(mAdapter);
    }
    public void handle (){
        btnAdd = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnRemove);
        btnCancel = findViewById(R.id.btnCancel);
        tvName = findViewById(R.id.tvName);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvName.getText().toString().trim();
                Random rand = new Random();
                userDao.insertAll(new User(rand.nextInt(), name,""));
                listUser = userDao.getAll();
                User[] users = new  User[listUser.size()];
                listUser.toArray(users);

                mAdapter = new ArrayAdapter<User>(view.getContext(),android.R.layout.simple_list_item_1, users);

                lvUsers.setAdapter(mAdapter);


            }
        });
       lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               User user = listUser.get(i);
               btnRemove.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       userDao.delete(user);
                       listUser = userDao.getAll();
                       User[] users = new  User[listUser.size()];
                       listUser.toArray(users);

                       mAdapter = new ArrayAdapter<User>(view.getContext(),android.R.layout.simple_list_item_1, users);

                       lvUsers.setAdapter(mAdapter);

                   }
               });
           }
       });
       btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               tvName.setText("");
           }
       });
    }
}