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
import java.util.Calendar;
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


        int id = Integer.parseInt(_id1);

        // 貯金額計算
        String totalSql = "SELECT TOTAL(amount) FROM individual6 " +
                "WHERE event_id = " + id;

        Cursor cur = db.rawQuery(totalSql,null);
        cur.moveToFirst();
        int total = cur.getInt(0);

        if(total >= Integer.parseInt(amount1)) {
            // 目標達成時の処理
            Intent intent3 = new Intent(EventDetail.this, ShareGoal.class);
            intent3.putExtra("moveFlg","eventDetail");
            intent3.putExtra("_id",_id1);
            intent3.putExtra("event",event1);
            intent3.putExtra("amount",amount1);
            intent3.putExtra("limit",limit1);
            intent3.putExtra("member",member1);
            intent3.putExtra("flg","success");
            intent3.putExtra("total",total);

            startActivity(intent3);
            finish();
        }

        // 現在日時の取得
        Calendar date1 = Calendar.getInstance();
        int newYear = date1.get(Calendar.YEAR);
        int newMonth = date1.get(Calendar.MONTH);
        int newDay = date1.get(Calendar.DAY_OF_MONTH);

        // 期限の分割
        String[] strLimit = limit1.split(" / ");
        int limitYear = Integer.parseInt(strLimit[0]);
        int limitMonth = Integer.parseInt(strLimit[1]);
        int limitDay = Integer.parseInt(strLimit[2]);

        // 期限当日の時の処理
        if(newYear > limitYear ||
                (newYear == limitYear) && ((newMonth + 1) > limitMonth) ||
                (newYear == limitYear) && ((newMonth + 1) == limitMonth) && (newDay >= limitDay)) {
            Intent intent4 = new Intent(EventDetail.this, ShareGoal.class);
            intent4.putExtra("moveFlg","eventDetail");
            intent4.putExtra("_id",_id1);
            intent4.putExtra("event",event1);
            intent4.putExtra("amount",amount1);
            intent4.putExtra("limit",limit1);
            intent4.putExtra("member",member1);
            intent4.putExtra("flg","time_over");
            intent4.putExtra("total",total);

            startActivity(intent4);
            finish();
        }



        TextView eventText = findViewById(R.id.event);
        eventText.setText(event1);

        TextView amountText = findViewById(R.id.amount);
        amountText.setText(amount1);

        TextView limitText = findViewById(R.id.limit);
        limitText.setText(limit1);

        TextView SavingText = findViewById(R.id.now_saving);
        SavingText.setText(Integer.toString(total));



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

                String sql = "SELECT * FROM individual6 WHERE event_id = " + id + " LIMIT " + i + "," +1;
                cur = db.rawQuery(sql, null);

                String _id = "";
                String event_id = "";
                String date = "";
                String member = "";
                String amount = "";
                String kakeibo_id = "";

                while(cur.moveToNext()){
                    //DBの列番号(index)を取得
                    int idxId = cur.getColumnIndex("_id");
                    int idxEventId = cur.getColumnIndex("event_id");
                    int idxDate = cur.getColumnIndex("date");
                    int idxMember = cur.getColumnIndex("member");
                    int idxAmount = cur.getColumnIndex("amount");
                    int idxKakeiboId = cur.getColumnIndex("kakeibo_id");

                    //列番号(index)にあるデータを取得
                    _id = cur.getString(idxId);
                    event_id = cur.getString(idxEventId);
                    date = cur.getString(idxDate);
                    member = cur.getString(idxMember);
                    amount = cur.getString(idxAmount);
                    kakeibo_id = cur.getString(idxKakeiboId);


                    //取得したデータをリストのmapに入れる
                    menu = new HashMap<>();
                    menu.put("_id", _id);
                    menu.put("event_id", event_id);
                    menu.put("date", date);
                    menu.put("member",member);
                    menu.put("amount", amount);
                    menu.put("kakeibo_id", kakeibo_id);
                    menuList.add(menu);

                    cur.moveToFirst();
                }
            }


        }finally {
            db.close();
        }

        String[] from = {"_id","event_id","date","member","amount","kakeibo_id"};
        int[] to = {R.id._id,R.id.event_id, R.id.date, R.id.member,R.id.amount,R.id.kakeibo_id};
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
            intent.putExtra("event",event1);
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
            String kakeibo_id = item.get("kakeibo_id").toString();



            Intent intent = new Intent(EventDetail.this, IndividualFix.class);

            intent.putExtra("_id",_id);
            intent.putExtra("event_id",event_id);
            intent.putExtra("date",date);
            intent.putExtra("member",member);
            intent.putExtra("amount",amount);
            intent.putExtra("kakeibo_id",kakeibo_id);

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