package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event extends AppCompatActivity {

    static String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ListView lvMenu = findViewById(R.id.event_list);
        List<Map<String,Object>> menuList = new ArrayList<>();

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(Event.this);
        SQLiteDatabase db = helper.getWritableDatabase();



        Map<String,Object> menu = new HashMap<>();


        //DB操作(SELECT)
        if(helper == null){
            helper = new DatabaseHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        try {

            String sql = "SELECT * FROM event6";
            Cursor cur = db.rawQuery(sql, null);

            //DBの_idをリストに渡す
            /*String[] from = {"_id","eventname","eventamount","eventlimit","eventmember"};
            int[] to = {R.id.eventid,R.id.event, R.id.amount, R.id.limit,R.id.member};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(Event.this, R.layout.row2, cur, from, to,0);
            lvMenu.setAdapter(adapter);*/

            //1行ずつDBからリストへ
            long Count = DatabaseUtils.queryNumEntries(db,"event6", null,null);
            for (int i = 0; i <= Count; i++){

                String _id = "";
                String event = "";
                String amount = "";
                String limit = "";
                String member = "";

                while(cur.moveToNext()){
                    //DBの列番号(index)を取得
                    int idxId = cur.getColumnIndex("_id");
                    int idxEvent = cur.getColumnIndex("eventname");
                    int idxAmount = cur.getColumnIndex("eventamount");
                    int idxLimit = cur.getColumnIndex("eventlimit");
                    int idxMember = cur.getColumnIndex("eventmember");

                    //列番号(index)にあるデータを取得
                    _id = cur.getString(idxId);
                    event = cur.getString(idxEvent);
                    amount = cur.getString(idxAmount);
                    limit = cur.getString(idxLimit);
                    member = cur.getString(idxMember);


                    //取得したデータをリストのmapに入れる
                    menu = new HashMap<>();
                    menu.put("_id", _id);
                    menu.put("event", event);
                    menu.put("amount", amount);
                    menu.put("limit",limit);
                    menu.put("member", member);
                    menuList.add(menu);

                    cur.moveToFirst();
                }
            }


        }finally {
            db.close();
        }

        String[] from = {"_id","event","amount","limit","member"};
        int[] to = {R.id.eventid,R.id.event, R.id.amount, R.id.limit,R.id.member};
        SimpleAdapter adapter = new SimpleAdapter(Event.this,menuList,R.layout.row2,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new ListItemClickListener());


        /*
        menu = new HashMap<>();
        menu.put("_id", "_id");
        menu.put("event", "event");
        menu.put("amount", "amount");
        menu.put("limit","limit");
        menu.put("member", "member");
        menuList.add(menu);

        menu = new HashMap<>();
        menu.put("_id", "_id");
        menu.put("event", "event");
        menu.put("amount", "amount");
        menu.put("limit","limit");
        menu.put("member", "member");
        menuList.add(menu);

        String[] from = {"_id","event","amount","limit","member"};
        int[] to = {R.id.eventid,R.id.event, R.id.amount, R.id.limit,R.id.member};
        SimpleAdapter adapter = new SimpleAdapter(Event.this,menuList,R.layout.row2,from,to);
        lvMenu.setAdapter(adapter);

         */



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

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(Event.this, Start.class);
        startActivity(intent);
        finish();
    }

    // 作成ボタンを押した場合の処理
    private class CreateClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Event.this, EventFix.class);

            intent.putExtra("moveFlg", "event");
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