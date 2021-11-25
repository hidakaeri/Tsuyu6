package com.example.tsuyu6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class FuLook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_look);

        // simulation radio buttonの取得
        RadioButton HouseholdAccountBookClick = findViewById(R.id.flgHouseholdAccountBook);
        // simulation radio buttonのリスナクラスのインスタンスを作成
        HouseholdAccountBookClickListener HouseholdAccountBook_Listener = new HouseholdAccountBookClickListener();
        // simulation radio buttonにリスナを設定
        HouseholdAccountBookClick.setOnClickListener(HouseholdAccountBook_Listener);
    }

    // HouseholdAccountBookボタンを押した場合の処理
    private class HouseholdAccountBookClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(FuLook.this, Look.class);
            startActivity(intent);

        }
    }
}