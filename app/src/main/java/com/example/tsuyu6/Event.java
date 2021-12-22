package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ListView lvMenu = findViewById(R.id.event_list);
        List<Map<String,Object>> menuList = new ArrayList<>();
        Map<String,Object> menu = new HashMap<>();

        menu = new HashMap<>();
        menu.put("_id", "12345");
        menu.put("event", "旅行");
        menu.put("amount","100000");
        menu.put("member", "はなまる、だいきち");
        menuList.add(menu);

        String[] from = {"_id","event","amount","member"};
        int[] to = {R.id._id,R.id.event, R.id.amount, R.id.member};
        SimpleAdapter adapter = new SimpleAdapter(Event.this,menuList,R.layout.row2,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new ListItemClickListener());

        // 作成ボタンの取得
        Button createClick = findViewById(R.id.create);
        // 作成ボタンのリスナクラスのインスタンスを作成
        CreateClickListener create_listener = new CreateClickListener();
        // 作成ボタンにリスナを設定
        createClick.setOnClickListener(create_listener);


        // 参加ボタンの取得
        Button participationClick = findViewById(R.id.participation);
        // 参加ボタンのリスナクラスのインスタンスを作成
        ParticipationClickListenerListener participation_listener = new ParticipationClickListenerListener();
        // 参加ボタンにリスナを設定
        participationClick.setOnClickListener(participation_listener);

    }

    //　リストがクリックされた時の処理
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(position);

            String _id = item.get("_id").toString();


            Intent intent = new Intent(Event.this, EventDetail.class);

            intent.putExtra("listId",_id);

            startActivity(intent);
            finish();
        }
    }

    // 作成ボタンを押した場合の処理
    private class CreateClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Event.this, EventFix.class);
            intent.putExtra("backFlg",0);
            startActivity(intent);

            finish();

        }
    }

    // 参加ボタンを押した場合の処理
    private class ParticipationClickListenerListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Event.this, EventParticipation.class);
            startActivity(intent);

            finish();

        }
    }
}