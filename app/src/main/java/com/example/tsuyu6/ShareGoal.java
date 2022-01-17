package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareGoal extends AppCompatActivity {

    static String _id1 = "";
    static String event1 = "";
    static String amount1 = "";
    static String limit1 = "";
    static String member1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_goal);

        Intent intent = getIntent();
        _id1 = intent.getStringExtra("_id");
        event1 = intent.getStringExtra("event");
        amount1 = intent.getStringExtra("amount");
        limit1 = intent.getStringExtra("limit");
        member1 = intent.getStringExtra("member");
        String flg = intent.getStringExtra("flg");
        String total = intent.getStringExtra("total");

        // 目標貯金額のTextView取得
        TextView target_amount_textView = findViewById(R.id.target_amount);
        target_amount_textView.setText(amount1);

        // 現在の貯金額のTextView取得
        TextView now_saving_textView = findViewById(R.id.now_saving);
        now_saving_textView.setText(total);

        // messageのImageView取得
        ImageView message_imageView1 = findViewById(R.id.message1);

        // messageのImageView取得
        ImageView message_imageView2 = findViewById(R.id.message2);

        // 梅雨ちゃんのImageView取得
        ImageView tsuyu_imageView = findViewById(R.id.tsuyu_chan);


        switch (flg) {
            // 貯金目標達成
            case "success":
                message_imageView1.setBackgroundResource(R.drawable.success1);
                message_imageView2.setBackgroundResource(R.drawable.success2);
                tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu1);
                break;


            // 目標達成できず...
            case "time_over":
                message_imageView1.setBackgroundResource(R.drawable.failture1);
                message_imageView2.setBackgroundResource(R.drawable.failture2);
                tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu4);
                break;

        }

        // event_clearボタンの取得
        Button eventClearClick = findViewById(R.id.event_clear);
        // event_clearボタンのリスナクラスのインスタンスを作成
        eventClearClickListener event_clear_listener  = new eventClearClickListener();
        // event_clearにリスナを設定
        eventClearClick.setOnClickListener(event_clear_listener);

        // all_clearボタンの取得
        Button allClearClick = findViewById(R.id.all_clear);
        // target_clearボタンのリスナクラスのインスタンスを作成
        allClearClickListener all_clear_listener  = new allClearClickListener();
        // target_clearにリスナを設定
        allClearClick.setOnClickListener(all_clear_listener);


    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(ShareGoal.this, EventDetail.class);

        intent.putExtra("_id",_id1);
        intent.putExtra("event",event1);
        intent.putExtra("amount",amount1);
        intent.putExtra("limit",limit1);
        intent.putExtra("member",member1);

        startActivity(intent);
        finish();
    }

    // event削除ボタンを押した場合の処理
    private class eventClearClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // 消す
            DatabaseHelper helper = new DatabaseHelper(ShareGoal.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            try {

                int id = Integer.parseInt(_id1);

                // event
                String sqlDelete = "DELETE FROM event6 WHERE _id = " + id;
                SQLiteStatement stmt = db.compileStatement(sqlDelete);
                stmt.executeUpdateDelete();

                // individual
                String sqlDelete3 = "DELETE FROM individual6  WHERE event_id =" + id;
                SQLiteStatement stmt3= db.compileStatement(sqlDelete3);
                stmt3.executeUpdateDelete();

            }finally {
                db.close();
            }

            // 画面遷移
            Intent intent = new Intent(ShareGoal.this, Event.class);
            startActivity(intent);
            finish();
        }
    }

    // all削除ボタンを押した場合の処理
    private class allClearClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // 消す
            DatabaseHelper helper = new DatabaseHelper(ShareGoal.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            try {

                int id = Integer.parseInt(_id1);

                // event
                String sqlDelete = "DELETE FROM event6 WHERE _id = " + id;
                SQLiteStatement stmt = db.compileStatement(sqlDelete);
                stmt.executeUpdateDelete();

                // 家計簿
                String sqlDelete2 = "DELETE FROM tsuyu6  WHERE _id IN (SELECT kakeibo_id FROM individual6 WHERE event_id = " + id + ")";
                SQLiteStatement stmt2 = db.compileStatement(sqlDelete2);
                stmt2.executeUpdateDelete();

                // individual
                String sqlDelete3 = "DELETE FROM individual6  WHERE event_id =" + id;
                SQLiteStatement stmt3= db.compileStatement(sqlDelete3);
                stmt3.executeUpdateDelete();

            }finally {
                db.close();
            }

            // 画面遷移
            Intent intent = new Intent(ShareGoal.this, Event.class);
            startActivity(intent);
            finish();
        }
    }
}