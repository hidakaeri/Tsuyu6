package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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

    static String _id1 = "";
    static String event1 = "";
    static String amount1 = "";
    static String limit1 = "";
    static String member1 = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ListView lvMenu = findViewById(R.id.eventDetail_list);
        List<Map<String,Object>> menuList = new ArrayList<>();

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(EventDetail.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        Map<String,Object> menu = new HashMap<>();

        Intent intent = getIntent();
        _id1 = intent.getStringExtra("_id");
        event1 = intent.getStringExtra("event");
        amount1 = intent.getStringExtra("amount");
        limit1 = intent.getStringExtra("limit");
        member1 = intent.getStringExtra("member");


        TextView eventText = findViewById(R.id.event);
        eventText.setText(event1);

        TextView amountText = findViewById(R.id.amount);
        amountText.setText(amount1);

        TextView limitText = findViewById(R.id.limit);
        limitText.setText(limit1);

        //DB操作(SELECT)
        if(helper == null){
            helper = new DatabaseHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        try {

            //1行ずつDBからリストへ
            long Count = DatabaseUtils.queryNumEntries(db,"individual6", null,null);
            for (int i = 0; i <= Count; i++){

                String sql = "SELECT * FROM individual6 LIMIT " + i + "," +1;
                Cursor cur = db.rawQuery(sql, null);

                String _id = "";
                String event_id = "";
                String date = "";
                String member = "";
                String amount = "";

                while(cur.moveToNext()){
                    //DBの列番号(index)を取得
                    int idxId = cur.getColumnIndex("_id");
                    int idxEventId = cur.getColumnIndex("event_id");
                    int idxDate = cur.getColumnIndex("date");
                    int idxMember = cur.getColumnIndex("member");
                    int idxAmount = cur.getColumnIndex("amount");

                    //列番号(index)にあるデータを取得
                    _id = cur.getString(idxId);
                    event_id = cur.getString(idxEventId);
                    date = cur.getString(idxDate);
                    member = cur.getString(idxMember);
                    amount = cur.getString(idxAmount);


                    //取得したデータをリストのmapに入れる
                    menu = new HashMap<>();
                    menu.put("_id", _id);
                    menu.put("event_id", event_id);
                    menu.put("date", date);
                    menu.put("member",member);
                    menu.put("amount", amount);
                    menuList.add(menu);

                    cur.moveToFirst();
                }
            }


        }finally {
            db.close();
        }

        String[] from = {"_id","event_id","date","member","amount"};
        int[] to = {R.id._id,R.id.event_id, R.id.date, R.id.member,R.id.amount};
        SimpleAdapter adapter = new SimpleAdapter(EventDetail.this,menuList,R.layout.row3,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new ListItemClickListener());


        // 修正ボタンの取得
        Button fixClick = findViewById(R.id.fixClick);
        // 修正ボタンのリスナクラスのインスタンスを作成
        FixClickListener fix_listener = new FixClickListener();
        // 修正ボタンにリスナを設定
        fixClick.setOnClickListener(fix_listener);

        // 追加ボタンの取得
        ImageButton addClick = findViewById(R.id.addClick);
        // 追加ボタンのリスナクラスのインスタンスを作成
        AddClickListener add_listener = new AddClickListener();
        // 追加ボタンにリスナを設定
        addClick.setOnClickListener(add_listener);

    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(EventDetail.this, Event.class);;
        startActivity(intent);
        finish();
    }

    // 修正ボタンを押した場合の処理
    private class FixClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(EventDetail.this, EventFix.class);
            intent.putExtra("moveFlg","eventDetail");
            intent.putExtra("_id",_id1);
            intent.putExtra("amount",amount1);
            intent.putExtra("limit",limit1);
            intent.putExtra("member",member1);

            startActivity(intent);
            finish();
        }
    }

    //　リストがクリックされた時の処理
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(position);

            String _id = item.get("_id").toString();
            String event_id = item.get("event_id").toString();
            String date = item.get("date").toString();
            String member = item.get("member").toString();
            String amount = item.get("amount").toString();


            Intent intent = new Intent(EventDetail.this, IndividualFix.class);

            intent.putExtra("_id",_id);
            intent.putExtra("event_id",event_id);
            intent.putExtra("date",date);
            intent.putExtra("member",member);
            intent.putExtra("amount",amount);

            intent.putExtra("moveFlg","fix");

            intent.putExtra("_id1",_id1);
            intent.putExtra("event1",event1);
            intent.putExtra("amount1",amount1);
            intent.putExtra("limit1",limit1);
            intent.putExtra("member1",member1);

            startActivity(intent);
            finish();
        }
    }

    // 追加ボタンを押した場合の処理
    private class AddClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 入力画面に遷移
            Intent intent = new Intent(EventDetail.this, IndividualFix.class);
            intent.putExtra("event_id",_id1);
            intent.putExtra("moveFlg","input");

            intent.putExtra("_id1",_id1);
            intent.putExtra("event1",event1);
            intent.putExtra("amount1",amount1);
            intent.putExtra("limit1",limit1);
            intent.putExtra("member1",member1);

            startActivity(intent);
            finish();
        }
    }
}