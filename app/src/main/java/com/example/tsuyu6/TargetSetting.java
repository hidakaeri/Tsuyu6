package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TargetSetting extends AppCompatActivity {

    int newYear;
    int newMonth;
    int newDay;
    static int inputYear;
    static int inputMonth;
    static int inputDay;
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
        TextView TargetLimitText = findViewById(R.id.target_limit);
        TargetLimitText.setText(TargetLimit);

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
                        TargetSetting.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                TargetLimitText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                                newYear = year;
                                newMonth = month;
                                newDay = dayOfMonth;
                                inputYear = year;
                                inputMonth = month;
                                inputDay = dayOfMonth;
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
            TextView TargetLimitText = findViewById(R.id.target_limit);
            TargetLimit = TargetLimitText.getText().toString();

            // 現在日時の取得
            Calendar date = Calendar.getInstance();
            newYear = date.get(Calendar.YEAR);
            newMonth = date.get(Calendar.MONTH);
            newDay = date.get(Calendar.DATE);




            if(TargetAmount.equals("") || TargetLimit.equals("")) {
                // 内容が入力されていない場合の処理
                // トースト表示
                Toast.makeText(TargetSetting.this, R.string.toast_setting_null, Toast.LENGTH_LONG).show();
            } else if (inputYear < newYear || (inputYear == newYear && inputMonth < newMonth) ||
                    (inputYear == newYear && inputMonth == newMonth && inputDay < newDay)) {
                // 期限に過去の日付が入力されたときの処理
                Toast.makeText(TargetSetting.this, R.string.toast_setting_past, Toast.LENGTH_LONG).show();
            } else {


                // SQL
                // 目標金額　TargetAmount
                // 期限　TargetLimit
                // 登録日　register_date
                // 貯金目標額（int型) oneMonth　
                DatabaseHelper helper = new DatabaseHelper(TargetSetting.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                if (helper == null) {
                    helper = new DatabaseHelper(getApplicationContext());
                }

                if (db == null) {
                    db = helper.getReadableDatabase();
                }
                try {
                    String sqlDelete = "DELETE FROM target6";
                    SQLiteStatement Dstmt = db.compileStatement(sqlDelete);
                    Dstmt.executeUpdateDelete();
                    String sqlInsert = "INSERT INTO target6 (targetamount, targetlimit) VALUES (?,?)";
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);
                    stmt.bindString(1, TargetAmount);
                    stmt.bindString(2, TargetLimit);
                    stmt.executeInsert();
                } finally {
                    db.close();
                }

            Intent intent = new Intent(TargetSetting.this, Saving.class);
            startActivity(intent);
            finish();

            }

        }
    }

    // 削除ボタンを押した場合の処理
    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // ダイアログを開く
            SavingDeleteDialog dialogFragment = new SavingDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(),"SavingDeleteDialog");

        }
    }
}