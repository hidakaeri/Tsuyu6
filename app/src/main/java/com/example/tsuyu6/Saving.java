package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

public class Saving extends AppCompatActivity {
    String TargetAmount;
    String TargetLimit;
    String RegisterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        // 登録データの取得
        // SQLお願いします。
        // TargetAmount TargetLimit, RegisterDate。

        TargetAmount = "100000";
        TargetLimit = "2022/12/31";
        RegisterDate = "2021/12/1";


        // すでに登録されているデータを表示
        TextView TargetAmountText = findViewById(R.id.target_amount);
        TargetAmountText.setText(TargetAmount);
        TextView TargetLimitText = findViewById(R.id.target_limit);
        TargetLimitText.setText(TargetLimit);
        TextView RedisterDateText = findViewById(R.id.register_date);
        RedisterDateText.setText(RegisterDate);

        // 総資産の取得
        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        int newYear = date.get(Calendar.YEAR);
        int newMonth = date.get(Calendar.MONTH);

        // newYear年newMonth月までの総資産を出して下さい。
        int MonthStart = 11111;
        // MonthStartに総資産を入れてください。


        String MonthStartString = Integer.toString(MonthStart);
        TextView MonthStartText = findViewById(R.id.month_start);
        MonthStartText.setText(MonthStartString);



        // newYear年(newMonth+1)月の合計を出して下さい。
        // 家計簿とシュミレーション両方
        // 支出は-でお願いします。
        int MonthAmount = 22222;
        // MonthAmountに総資産を入れてください。

        String MonthAmountString = Integer.toString(MonthAmount);
        TextView MonthAmountText = findViewById(R.id.month_amount);
        MonthAmountText.setText(MonthAmountString);


        // 月末総資産計算

        int MonthEnd = MonthStart + MonthAmount;
        String MonthEndString = Integer.toString(MonthEnd);
        TextView MonthEndText = findViewById(R.id.month_end);
        MonthEndText.setText(MonthEndString);




        // settingボタンの取得
        Button settingClick = findViewById(R.id.setting);
        // 追加ボタンのリスナクラスのインスタンスを作成
        settingClickListener setting_listener = new settingClickListener();
        // 追加ボタンにリスナを設定
        settingClick.setOnClickListener(setting_listener);

        // HouseholdAccountBook radio buttonの取得
        RadioButton HouseholdAccountBookClick = findViewById(R.id.flgHouseholdAccountBook);
        // HouseholdAccountBook radio buttonのリスナクラスのインスタンスを作成
        HouseholdAccountBookClickListener HouseholdAccountBook_Listener = new HouseholdAccountBookClickListener();
        // HouseholdAccountBook radio buttonにリスナを設定
        HouseholdAccountBookClick.setOnClickListener(HouseholdAccountBook_Listener);

        // simulation radio buttonの取得
        RadioButton simulationClick = findViewById(R.id.flgSimulation);
        // simulation radio buttonのリスナクラスのインスタンスを作成
        SimulationClickListener simulation_Listener = new SimulationClickListener();
        // simulation radio buttonにリスナを設定
        simulationClick.setOnClickListener(simulation_Listener);
    }

    // settingボタンを押した場合の処理
    private class settingClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 入力画面に遷移
            Intent intent = new Intent(Saving.this, TargetSetting.class);
            startActivity(intent);
            finish();
        }
    }



    // simulationボタンを押した場合の処理
    private class SimulationClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Saving.this, FuLook.class);
            startActivity(intent);
            finish();

        }
    }

    // HouseholdAccountBookボタンを押した場合の処理
    private class HouseholdAccountBookClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Saving.this, Look.class);
            startActivity(intent);

        }
    }
}