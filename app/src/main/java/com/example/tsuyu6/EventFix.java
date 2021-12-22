package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class EventFix extends AppCompatActivity {

    static int backFlg;

    int newYear;
    int newMonth;
    int newDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_fix);

        Intent intent = getIntent();
        backFlg = intent.getIntExtra("backFlg",-1);

        TextView TargetLimitText = findViewById(R.id.target_limit);

        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        newYear = date.get(Calendar.YEAR);
        newMonth = date.get(Calendar.MONTH);
        newDay = date.get(Calendar.DATE);


        //EditTextにリスナーをつける
        TargetLimitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventFix.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                TargetLimitText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                                newYear = year;
                                newMonth = month;
                                newDay = dayOfMonth;
                            }
                        },
                        newYear,newMonth,newDay
                );

                //dialogを表示
                datePickerDialog.show();
            }

        });

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


        // 背景がタップされるとキーボードを閉じる
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Whatever
                InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {
        switch (backFlg){
            case 0:
                Intent intent = new Intent(EventFix.this, Event.class);
                startActivity(intent);
                finish();
                break;
            case 1:
                Intent intent1 = new Intent(EventFix.this, EventDetail.class);
                startActivity(intent1);
                finish();
                break;
        }


    }

    // 登録ボタンを押した場合の処理
    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {


            Intent intent = new Intent(EventFix.this, EventDetail.class);
            startActivity(intent);
            finish();


        }
    }

    // 削除ボタンを押した場合の処理
    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // ダイアログを開く
            EventDeleteDialog dialogFragment = new EventDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(),"EventDeleteDialog");
        }
    }



}