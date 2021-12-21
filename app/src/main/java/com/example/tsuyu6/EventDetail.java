package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ListView lvMenu = findViewById(R.id.eventDetail_list);
        List<Map<String,Object>> menuList = new ArrayList<>();
        Map<String,Object> menu = new HashMap<>();

        menu = new HashMap<>();
        menu.put("_id", "12345");
        menu.put("date", "2021/12/21");
        menu.put("member","はなまる");
        menu.put("amount", "3000");
        menuList.add(menu);


        String[] from = {"_id","date","member","amount"};
        int[] to = {R.id._id,R.id.date, R.id.member, R.id.amount};
        SimpleAdapter adapter = new SimpleAdapter(EventDetail.this,menuList,R.layout.row3,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new ListItemClickListener());

        // 修正ボタンの取得
        Button fixClick = findViewById(R.id.fixClick);
        // 修正ボタンのリスナクラスのインスタンスを作成
        FixClickListenerListener fix_listener = new FixClickListenerListener();
        // 修正ボタンにリスナを設定
        fixClick.setOnClickListener(fix_listener);

        // 削除ボタンの取得
        Button deleteClick = findViewById(R.id.deleteClick);
        // 削除ボタンのリスナクラスのインスタンスを作成
        DeleteClickListener delete_listener = new DeleteClickListener();
        // 削除ボタンにリスナを設定
        deleteClick.setOnClickListener(delete_listener);

    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(EventDetail.this, Event.class);;
        startActivity(intent);
        finish();
    }

    // 修正ボタンを押した場合の処理
    private class FixClickListenerListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

        }
    }

    // 削除ボタンを押した場合の処理
    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {


        }
    }

    //　リストがクリックされた時の処理
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(position);

            String _id = item.get("_id").toString();


            Intent intent = new Intent(EventDetail.this, Look.class);

            intent.putExtra("listId",_id);

            startActivity(intent);
            finish();
        }
    }
}