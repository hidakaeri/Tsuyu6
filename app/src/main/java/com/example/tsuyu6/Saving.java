package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Random;

public class Saving extends AppCompatActivity {
    String TargetAmount = "";
    String TargetLimit;
    static int oneMonth;
    static int targetFlg;
    static int balance;
    static int limitYear;
    static int limitMonth;
    static int limitDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        int newYear = date.get(Calendar.YEAR);
        int newMonth = date.get(Calendar.MONTH);
        int newDay = date.get(Calendar.DAY_OF_MONTH);

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(Saving.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        // TargetAmount TargetLimit, RegisterDate
        String sql = "SELECT * FROM target6";
        Cursor cur = db.rawQuery(sql, null);
        while(cur.moveToNext()){
            int tamountId = cur.getColumnIndex("targetamount");
            int tlimitId = cur.getColumnIndex("targetlimit");

            TargetAmount = cur.getString(tamountId);
            TargetLimit = cur.getString(tlimitId);

            cur.moveToFirst();
        }


        // 初期値
        int TargetAmountInt = -1;

        if(TargetAmount.equals("")) {

        } else {
            // データがあったら
            // すでに登録されているデータを表示
            TextView TargetAmountText = findViewById(R.id.target_amount);
            TargetAmountInt = Integer.parseInt(TargetAmount);
            TargetAmountText.setText(String.format("%,d", TargetAmountInt));

            // 期限が当日のとき
            TargetLimit = (String.format("%d / %02d / %02d", 2021, 12, 14));


            TextView TargetLimitText = findViewById(R.id.target_limit);
            TargetLimitText.setText(TargetLimit);

            // 貯金目標額/月　の計算
            // 期限の分割
            String[] strLimit = TargetLimit.split(" / ");
            limitYear = Integer.parseInt(strLimit[0]);
            limitMonth = Integer.parseInt(strLimit[1]);
            limitDay = Integer.parseInt(strLimit[2]);

            // 計算用　現在日時の設定
            int thisYear = newYear;
            int thisMonth = newMonth + 1;

            // 貯金月数を計算
            int month_to_saving;
            if((limitYear - thisYear) == 0) {
                month_to_saving = limitMonth - thisMonth +1;
            } else {
                month_to_saving = (12 - thisMonth + 1) + limitMonth + 12 * (limitYear - newYear - 1);
            }


            oneMonth = Integer.parseInt(TargetAmount) / month_to_saving;

            TextView oneMonthText = findViewById(R.id.one_month);
            oneMonthText.setText(String.format("%,d",oneMonth));
        }

        // 期限当日の時の処理
        if(newYear == limitYear && (newMonth + 1) == limitMonth && newDay == limitDay) {
            Intent intent = new Intent(Saving.this,Goal.class);
            startActivity(intent);
            finish();
        }


        // 先月総資産の取得
        //月を二桁表示
        Format f = new DecimalFormat("00");

        //（MonthStart）
        String msSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE flag = '家計簿' " +
                "AND date <= '" + newYear + " / " + f.format(newMonth) + " / 31'";
        cur = db.rawQuery(msSql,null);
        cur.moveToFirst();
        int MonthStart = cur.getInt(0);

        // 先月の総資産を表示
        TextView MonthStartText = findViewById(R.id.month_start);
        MonthStartText.setText(String.format("%,d", MonthStart));


        // 今月の合計額（MonthAmount）
        // 家計簿とシュミレーション両方
        String maSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE date >= '" + newYear + " / " + f.format(newMonth+1) + " / 01' " +
                "AND date <= '" + newYear + " / " + f.format(newMonth+1) + " / 31'";
        cur = db.rawQuery(maSql,null);
        cur.moveToFirst();
        int MonthAmount = cur.getInt(0);
        db.close();


        // 今月の合計額を表示
        TextView MonthAmountText = findViewById(R.id.month_amount);
        MonthAmountText.setText(String.format("%,d", MonthAmount));


        // 月末総資産の計算
        int MonthEnd = MonthStart + MonthAmount;
        TextView MonthEndText = findViewById(R.id.month_end);
        MonthEndText.setText(String.format("%,d", MonthEnd));

        // 梅雨ちゃんのImageView取得
        ImageView tsuyu_imageView = findViewById(R.id.tsuyu_chan);
        // セリフのTextView取得
        TextView dialogue = findViewById(R.id.dialogue);
        // 吹き出しの取得
        ImageView speechBubble = findViewById(R.id.speech_bubble);


        //　梅雨ちゃんの画像とセリフを設定
        if(TargetAmountInt == -1) {
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu6);
            dialogue.setText("目標を設定してね");
        } else if (MonthAmount >= oneMonth * 1.5){
            // 貯金額が目標の1.5倍以上
            targetFlg = 1;
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu1);
            dialogue.setText("すごい！！");
            balance = MonthAmount - oneMonth;

            // 吹き出しのリスナクラスのインスタンスを作成
            DialogueClick1 speech_bubble_listener = new DialogueClick1();
            // 吹き出しにリスナを設定
            speechBubble.setOnClickListener(speech_bubble_listener);

        } else if (MonthAmount >= oneMonth) {
            // 貯金額が目標以上
            targetFlg = 2;
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu2);
            dialogue.setText("頑張ったね！");
            balance = MonthAmount - oneMonth;

            // 吹き出しのリスナクラスのインスタンスを作成
            DialogueClick2 speech_bubble_listener = new DialogueClick2();
            // 吹き出しにリスナを設定
            speechBubble.setOnClickListener(speech_bubble_listener);

        } else if ((MonthAmount >= (oneMonth * 0.5))) {
            // 貯金額が目標の半分以上
            targetFlg = 3;
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu3);
            dialogue.setText("おしい！");
            balance = oneMonth - MonthAmount;

            // 吹き出しのリスナクラスのインスタンスを作成
            DialogueClick3 speech_bubble_listener = new DialogueClick3();
            // 吹き出しにリスナを設定
            speechBubble.setOnClickListener(speech_bubble_listener);

        } else if (MonthAmount >= 0) {
            // 貯金額がマイナスでない
            targetFlg = 4;
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu4);
            dialogue.setText("もっと頑張って！");
            balance = oneMonth - MonthAmount;

            // 吹き出しのリスナクラスのインスタンスを作成
            DialogueClick4 speech_bubble_listener = new DialogueClick4();
            // 吹き出しにリスナを設定
            speechBubble.setOnClickListener(speech_bubble_listener);

        } else {
            // 貯金額がマイナス
            targetFlg = 5;
            tsuyu_imageView.setBackgroundResource(R.drawable.tsuyu5);
            dialogue.setText("来月頑張ろう！！");

            // 吹き出しのリスナクラスのインスタンスを作成
            DialogueClick5 speech_bubble_listener = new DialogueClick5();
            // 吹き出しにリスナを設定
            speechBubble.setOnClickListener(speech_bubble_listener);
        }




        // settingボタンの取得
        ImageButton settingClick = findViewById(R.id.setting);
        // settingボタンのリスナクラスのインスタンスを作成
        settingClickListener setting_listener = new settingClickListener();
        // settingボタンにリスナを設定
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

    private class DialogueClick1 implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // セリフのTextView取得
            TextView dialogue = findViewById(R.id.dialogue);
            String dialogueText = dialogue.getText().toString();

            String first = "すごい！！";
            String second = "目標を" + balance + "円\n超えたよ";
            String third = "素晴らしい‼\nやったね‼";

            if(dialogueText.equals(first)) {
                dialogue.setText(second);
            } else if (dialogueText.equals(second)) {
                dialogue.setText(third);
            } else {
                dialogue.setText(first);
            }
        }
    }

    private class DialogueClick2 implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // セリフのTextView取得
            TextView dialogue = findViewById(R.id.dialogue);
            String dialogueText = dialogue.getText().toString();

            String first = "頑張ったね！";
            String second = "目標を" + balance + "円\n超えたよ";
            String third = "その調子！\n目標達成！";

            if(dialogueText.equals(first)) {
                dialogue.setText(second);
            } else if (dialogueText.equals(second)) {
                dialogue.setText(third);
            } else {
                dialogue.setText(first);
            }
        }
    }

    private class DialogueClick3 implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // セリフのTextView取得
            TextView dialogue = findViewById(R.id.dialogue);
            String dialogueText = dialogue.getText().toString();

            String first = "おしい！";
            String second = "目標まで\n" + balance + "円だよ";
            String third = "あと少しがんばれ‼\nもう少し‼";

            if(dialogueText.equals(first)) {
                dialogue.setText(second);
            } else if (dialogueText.equals(second)) {
                dialogue.setText(third);
            } else {
                dialogue.setText(first);
            }
        }
    }

    private class DialogueClick4 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // セリフのTextView取得
            TextView dialogue = findViewById(R.id.dialogue);
            String dialogueText = dialogue.getText().toString();

            String first = "もっと頑張って！";
            String second = "目標まで\n" + balance + "円だよ";
            String third = "まだまだ…\n頑張って…";

            if(dialogueText.equals(first)) {
                dialogue.setText(second);
            } else if (dialogueText.equals(second)) {
                dialogue.setText(third);
            } else {
                dialogue.setText(first);
            }
        }
    }

    private class DialogueClick5 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // セリフのTextView取得
            TextView dialogue = findViewById(R.id.dialogue);
            String dialogueText = dialogue.getText().toString();

            String first = "来月頑張ろう！！";
            String second = "貯金がマイナスだよ‼";
            String third = "使いすぎ‼";

            if(dialogueText.equals(first)) {
                dialogue.setText(second);
            } else if (dialogueText.equals(second)) {
                dialogue.setText(third);
            } else {
                dialogue.setText(first);
            }
        }
    }
}