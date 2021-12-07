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
    int oneMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        // 登録データの取得
        // SQLお願いします。
        // TargetAmount TargetLimit, RegisterDate, oneMonth

        // Db出来たら消す
        TargetAmount = "120000";
        TargetLimit = "2021 / 12 / 21";
        RegisterDate = "2021 / 12 / 1";
        oneMonth = 2000;


        // すでに登録されているデータを表示
        TextView TargetAmountText = findViewById(R.id.target_amount);
        TargetAmountText.setText(TargetAmount);
        TextView TargetLimitText = findViewById(R.id.target_limit);
        TargetLimitText.setText(TargetLimit);
        TextView RegisterDateText = findViewById(R.id.register_date);
        RegisterDateText.setText(RegisterDate);
        TextView oneMonthText = findViewById(R.id.one_month);
        oneMonthText.setText(Integer.toString(oneMonth));


        // 総資産の取得
        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        int newYear = date.get(Calendar.YEAR);
        int newMonth = date.get(Calendar.MONTH);

        // newYear年newMonth月までの総資産を出して下さい。
        // MonthStartに総資産を入れてください。

        // DB出来たら消す
        int MonthStart = 11111;


        String MonthStartString = Integer.toString(MonthStart);
        TextView MonthStartText = findViewById(R.id.month_start);
        MonthStartText.setText(MonthStartString);



        // newYear年(newMonth+1)月の合計を出して下さい。
        // 家計簿とシュミレーション両方
        // 支出は-でお願いします。

        // MonthAmountに合計を入れてください。

        // DB出来たら消す
        int MonthAmount = 22222;

        // 月合計を表示
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