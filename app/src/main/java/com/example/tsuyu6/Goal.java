package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;

public class Goal extends AppCompatActivity {

    String TargetAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(Goal.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        // TargetAmount取得してください
        String sql = "SELECT * FROM target6";
        Cursor cur = db.rawQuery(sql, null);
        while(cur.moveToNext()){
            int tamountId = cur.getColumnIndex("targetamount");
            TargetAmount = cur.getString(tamountId);

            cur.moveToFirst();
        }


        // TargetAmountをint型に変更
        int TargetAmountInt = Integer.parseInt(TargetAmount);

        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        int newYear = date.get(Calendar.YEAR);
        int newMonth = date.get(Calendar.MONTH);
        int newDay = date.get(Calendar.DAY_OF_MONTH);

        //月を二桁表示
        Format f = new DecimalFormat("00");

        int nowSaving;

        // newYear年newMonth月newDay日時点の総資産
        String nsSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE flag = '家計簿' " +
                "AND date <= '" + newYear + " / " + f.format(newMonth) + " / " + f.format(newDay) + " '";
        cur = db.rawQuery(nsSql,null);
        cur.moveToFirst();
        nowSaving = cur.getInt(0);


        // messageのImageView取得
        ImageView message_imageView = findViewById(R.id.message);

        // 梅雨ちゃんのImageView取得
        ImageView tsuyu_imageView = findViewById(R.id.tsuyu_chan);


        if(nowSaving >= TargetAmountInt) {
            // 貯金目標達成

            message_imageView.setBackgroundResource(R.drawable.tsuyu1);
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu1);

        } else {
            // 目標達成できず...

            message_imageView.setBackgroundResource(R.drawable.tsuyu4);
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu4);

        }

        // target_clearボタンの取得
        Button targetClearClick = findViewById(R.id.target_clear);
        // target_clearボタンのリスナクラスのインスタンスを作成
        targetClearClickListener target_clear_listener  = new targetClearClickListener();
        // target_clearにリスナを設定
        targetClearClick.setOnClickListener(target_clear_listener);
    }

    // settingボタンを押した場合の処理
    private class targetClearClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // DB削除
            DatabaseHelper helper = new DatabaseHelper(Goal.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String sqlDelete = "DELETE FROM target6";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.executeUpdateDelete();

            // 入力画面に遷移
            Intent intent = new Intent(Goal.this, Saving.class);
            startActivity(intent);
            finish();
        }
    }
}