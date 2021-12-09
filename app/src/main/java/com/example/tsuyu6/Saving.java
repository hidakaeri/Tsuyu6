package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

public class Saving extends AppCompatActivity {
    String TargetAmount = "";
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
        TargetLimit = "2021  /  12  /  21";
        RegisterDate = "2021  /  12  /  1";
        oneMonth = 2000;

        // 初期値
        int TargetAmountInt = -1;

        if(TargetAmount.equals("")) {

        } else {
            // データがあったら
            // すでに登録されているデータを表示
            TextView TargetAmountText = findViewById(R.id.target_amount);
            TargetAmountInt = Integer.parseInt(TargetAmount);
            TargetAmountText.setText(String.format("%,d", TargetAmountInt));

            TextView TargetLimitText = findViewById(R.id.target_limit);
            TargetLimitText.setText(TargetLimit);

            TextView RegisterDateText = findViewById(R.id.register_date);
            RegisterDateText.setText(RegisterDate);

            TextView oneMonthText = findViewById(R.id.one_month);
            oneMonthText.setText(String.format("%,d",oneMonth));
        }


        // 総資産の取得
        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        int newYear = date.get(Calendar.YEAR);
        int newMonth = date.get(Calendar.MONTH);

        // newYear年newMonth月までの総資産を出して下さい。
        // MonthStartに総資産を入れてください。

        // DB出来たら消す
        int MonthStart = 11111;


        TextView MonthStartText = findViewById(R.id.month_start);
        MonthStartText.setText(String.format("%,d", MonthStart));


        // newYear年(newMonth+1)月の合計を出して下さい。
        // 家計簿とシュミレーション両方
        // 支出は-でお願いします。

        // MonthAmountに合計を入れてください。

        // DB出来たら消す
        int MonthAmount = 3500;

        // 月合計を表示
        TextView MonthAmountText = findViewById(R.id.month_amount);
        MonthAmountText.setText(String.format("%,d", MonthAmount));


        // 月末総資産計算
        int MonthEnd = MonthStart + MonthAmount;
        TextView MonthEndText = findViewById(R.id.month_end);
        MonthEndText.setText(String.format("%,d", MonthEnd));

        // 梅雨ちゃんのImageView取得
        ImageView tsuyu_imageView = findViewById(R.id.tsuyu_chan);
        // セリフのTextView取得
        TextView dialogue = findViewById(R.id.dialogue);


        //　梅雨ちゃんの画像とセリフを設定
        if(TargetAmountInt == -1) {
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu6);
            dialogue.setText("目標を設定してね");
        } else if (MonthAmount >= (TargetAmountInt * 1.5)){
            // 貯金額が目標の1.5倍以上
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu1);
            dialogue.setText("すごい！！");
        } else if (MonthAmount >= TargetAmountInt) {
            // 貯金額が目標以上
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu2);
            dialogue.setText("頑張ったね！");
        } else if ((MonthAmount >= (TargetAmountInt * 0.5))) {
            // 貯金額が目標の半分以上
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu3);
            dialogue.setText("おしい！");
        } else if (MonthAmount >= 0) {
            // 貯金額がマイナスでない
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu4);
            dialogue.setText("もっと頑張って！");
        } else {
            // 貯金額がマイナス
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu5);
            dialogue.setText("来月頑張ろう！！");
        }




        // settingボタンの取得
        ImageButton settingClick = findViewById(R.id.setting);
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
            finish();
        }
    }
}