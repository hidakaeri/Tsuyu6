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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EventFix extends AppCompatActivity {

    static String moveFlg;

    int newYear;
    int newMonth;
    int newDay;

    static String _id = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_fix);

        Intent intent = getIntent();
        _id = intent.getStringExtra("listId");
        moveFlg = intent.getStringExtra("moveFlg");

        TextView TargetLimitText = findViewById(R.id.limit);

        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        newYear = date.get(Calendar.YEAR);
        newMonth = date.get(Calendar.MONTH);
        newDay = date.get(Calendar.DATE);

        TextView limitText = findViewById(R.id.limit);
        limitText.setText(String.format("%d / %02d / %02d", newYear, newMonth + 1, newDay));

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
        switch (moveFlg){
            case "event":
                Intent intent = new Intent(EventFix.this, Event.class);
                startActivity(intent);
                finish();
                break;
            case "eventDetail":
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

            TextView eventText = findViewById(R.id.event);
            TextView amountText = findViewById(R.id.amount);
            TextView limitText = findViewById(R.id.limit);
            TextView memberText = findViewById(R.id.member);

            String event = eventText.getText().toString();
            String amount = amountText.getText().toString();
            String limit = limitText.getText().toString();
            String member = memberText.getText().toString();

            if (event.equals("") || amount.equals("") || member.equals("")) {
                // 未入力の項目があるとき
                // トースト表示
                Toast.makeText(EventFix.this, R.string.toast_setting_null, Toast.LENGTH_LONG).show();

            } else {

                DatabaseHelper helper = new DatabaseHelper(EventFix.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                switch (moveFlg) {
                    case "event":

                        // 入力制限toast


                        // 登録

                        // DBの更新処理(INSERT)


                        if (helper == null) {
                            helper = new DatabaseHelper(getApplicationContext());
                        }

                        if (db == null) {
                            db = helper.getReadableDatabase();
                        }
                        try {
                            String sqlInsert = "INSERT INTO event6 (_id, eventname, eventamount, eventlimit, eventmember) VALUES (?,?,?,?,?)";
                            SQLiteStatement stmt = db.compileStatement(sqlInsert);

                            stmt.bindString(2, event);
                            stmt.bindString(3, amount);
                            stmt.bindString(4, limit);
                            stmt.bindString(5, member);

                            stmt.executeInsert();
                        } finally {
                            db.close();
                        }

                        // Intent intent = new Intent(EventFix.this, Event.class);

                        Intent intent = new Intent(EventFix.this, Look.class);
                        startActivity(intent);
                        finish();

                        break;
                    case "eventDetail":

                        // 入力制限toast

                        // 修正
                        // DB更新処理(UPDATE)
                        int id = Integer.parseInt(_id);

                        try {
                            String sqlUpdate = "UPDATE event6 SET eventname = ?, eventamount = ?, eventlimit = ?, eventmember = ?, WHERE eventid = " + id;
                            SQLiteStatement stmt = db.compileStatement(sqlUpdate);
                            stmt.bindString(1, event);
                            stmt.bindString(2, amount);
                            stmt.bindString(3, limit);
                            stmt.bindString(4, member);

                            stmt.executeInsert();

                        } finally {
                            db.close();
                        }

                        // Intent intent1 = new Intent(EventFix.this, EventDetail.class);

                        Intent intent1 = new Intent(EventFix.this, FuLook.class);
                        intent1.putExtra("listId", _id);

                        startActivity(intent1);
                        finish();

                        break;
                }
            }
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