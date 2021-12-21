package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
}