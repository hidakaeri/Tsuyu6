package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class TargetSetting extends AppCompatActivity {

    String TargetAmount;
    String TargetLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_setting);

        // 登録データの取得
        // SQLお願いします。
        // TargetAmount TargetLimitに入れてください。

        // すでに登録されているデータを表示
        EditText TargetAmountText = findViewById(R.id.target_amount);
        TargetAmountText.setText(TargetAmount);
        EditText TargetLimitText = findViewById(R.id.target_amount);
        TargetLimitText.setText(TargetLimit);

        // 登録ボタンの取得
        Button registerClick = findViewById(R.id.registerClick);
        // 登録ボタンのリスナクラスのインスタンスを作成
        RegisterClickListener register_listener = new RegisterClickListener();
        // 登録ボタンにリスナを設定
        registerClick.setOnClickListener(register_listener);

        // 削除ボタンの取得
        Button deleteClick = findViewById(R.id.deleteClick);
        // 削除ボタンのリスナクラスのインスタンスを作成
        DeleteClickListener delete_listener = new DeleteClickListener();
        // 削除ボタンにリスナを設定
        deleteClick.setOnClickListener(delete_listener);
    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(TargetSetting.this, Saving.class);
        startActivity(intent);
        finish();
    }

    // 登録ボタンを押した場合の処理
    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {


            // 目標貯金額EditTextの取得
            EditText TargetAmountText = findViewById(R.id.target_amount);
            TargetAmount = TargetAmountText.getText().toString();


            // 期限EditTextの取得
            EditText TargetLimitText = findViewById(R.id.target_limit);
            TargetLimit = TargetLimitText.getText().toString();

            // 現在日時の取得
            Calendar date = Calendar.getInstance();
            int newYear = date.get(Calendar.YEAR);
            int newMonth = date.get(Calendar.MONTH);
            int newDay = date.get(Calendar.DATE);
            String register_date = String.format("%d / %02d / %02d", newYear, newMonth+1, newDay);

            // SQL
            // 目標金額　TargetAmount
            // 期限　TargetLimit
            // 登録日　register_date
            // 該当のデータがあれば上書き、なければ新規登録お願いします。

            Intent intent = new Intent(TargetSetting.this, Saving.class);
            startActivity(intent);
            finish();

        }
    }

    // 削除ボタンを押した場合の処理
    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 削除SQL
            // DB削除してください。

            Intent intent = new Intent(TargetSetting.this, Saving.class);
            startActivity(intent);
            finish();

        }
    }
}