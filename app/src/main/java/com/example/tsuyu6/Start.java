package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 家計簿 buttonの取得
        Button householdAccountBookClick = findViewById(R.id.householdAccountBook);
        // 家計簿 buttonのリスナクラスのインスタンスを作成
        HouseholdAccountBookClickListener householdAccountBook_Listener = new HouseholdAccountBookClickListener();
        // 家計簿 buttonにリスナを設定
        householdAccountBookClick.setOnClickListener(householdAccountBook_Listener);

        // 共有 buttonの取得
        Button shareClick = findViewById(R.id.share);
        // 共有 buttonのリスナクラスのインスタンスを作成
        ShareClickListener share_Listener = new ShareClickListener();
        // 共有 buttonにリスナを設定
        shareClick.setOnClickListener(share_Listener);
    }

    // 家計簿ボタンを押した場合の処理
    private class HouseholdAccountBookClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 画面遷移
            Intent intent = new Intent(Start.this, Look.class);
            startActivity(intent);
        }
    }

    // 共有ボタンを押した場合の処理
    private class ShareClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 画面遷移
            Intent intent = new Intent(Start.this, Event.class);
            startActivity(intent);
        }
    }
}