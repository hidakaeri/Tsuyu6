package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

public class IndividualFix extends AppCompatActivity {

    int newYear;
    int newMonth;
    int newDay;

    static String moveFlg = "";
    static String _id = "";
    static String event_id = "";
    static String date = "";
    static String member = "";
    static String amount = "";

    static String _id1 = "";
    static String event1 = "";
    static String amount1 = "";
    static String limit1 = "";
    static String member1 = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_fix);

        Intent intent = getIntent();
        moveFlg = intent.getStringExtra("moveFlg");
        _id = intent.getStringExtra("_id");
        event_id = intent.getStringExtra("event_id");
        date = intent.getStringExtra("date");
        member = intent.getStringExtra("member");
        amount = intent.getStringExtra("amount");

        _id1 = intent.getStringExtra("_id1");
        event1 = intent.getStringExtra("event1");
        amount1 = intent.getStringExtra("amount1");
        limit1 = intent.getStringExtra("limit1");
        member1 = intent.getStringExtra("member1");


        // 修正の場合の初期値表示
        TextView DateText = findViewById(R.id.date);
        TextView AmountText = findViewById(R.id.amount);
        AmountText.setText(amount);


        switch (moveFlg){
            case "input":
                // 登録の時
                // 現在日時の取得
                Calendar inputDate = Calendar.getInstance();
                newYear = inputDate.get(Calendar.YEAR);
                newMonth = inputDate.get(Calendar.MONTH);
                newDay = inputDate.get(Calendar.DATE);
                DateText.setText(String.format("%d / %02d / %02d", newYear, newMonth + 1, newDay));
                break;
            case "fix":
                // 修正の時
                DateText.setText(date);
                // DBの日時の分割（初期値用）
                String[] strDate = date.split(" / ");
                newYear = Integer.parseInt(strDate[0]);
                int month = Integer.parseInt(strDate[1]);
                newMonth = month - 1;
                newDay = Integer.parseInt(strDate[2]);
                break;


        }


        //EditTextにリスナーをつける
        DateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        IndividualFix.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                DateText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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

        Intent intent = new Intent(IndividualFix.this, EventDetail.class);
        startActivity(intent);
        finish();
    }

    // 登録ボタンを押した場合の処理
    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            TextView DateText = findViewById(R.id.date);
            TextView AmountText = findViewById(R.id.amount);

            date = DateText.getText().toString();
            amount = AmountText.getText().toString();

            // メンバー(自分のID)入力
            DatabaseHelper helper = new DatabaseHelper(IndividualFix.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            String sql = "SELECT * FROM login6";
            Cursor cur = db.rawQuery(sql, null);
            while(cur.moveToNext()){
                int loginIdId = cur.getColumnIndex("loginid");

                String loginId;
                loginId = cur.getString(loginIdId);
                member = loginId;
                cur.moveToFirst();
            }



            if (amount.equals("")) {
                // 金額未入力
                // トースト表示
                Toast.makeText(IndividualFix.this, R.string.toast_amount, Toast.LENGTH_LONG).show();

            } else {



                switch (moveFlg) {
                    case "input":


                        // 登録
                        // DBの更新処理(INSERT)


                        if (helper == null) {
                            helper = new DatabaseHelper(getApplicationContext());
                        }

                        if (db == null) {
                            db = helper.getReadableDatabase();
                        }
                        try {
                            String sqlInsert = "INSERT INTO individual6 (_id, event_id, date, member, amount) VALUES (?,?,?,?,?)";
                            SQLiteStatement stmt = db.compileStatement(sqlInsert);

                            stmt.bindString(2, event_id);
                            stmt.bindString(3, date);
                            stmt.bindString(4, member);
                            stmt.bindString(5, amount);

                            stmt.executeInsert();

                        } finally {
                            db.close();
                        }

                        break;
                    case "fix":

                        // 修正
                        // DB更新処理(UPDATE)
                        int id = Integer.parseInt(_id);

                        try {
                            String sqlUpdate = "UPDATE individual6 SET event_id = ?, date = ?, member = ?, amount = ? WHERE _id =" + id;
                            SQLiteStatement stmt = db.compileStatement(sqlUpdate);

                            stmt.bindString(1, event_id);
                            stmt.bindString(2, date);
                            stmt.bindString(3, member);
                            stmt.bindString(4, amount);

                            stmt.executeInsert();

                        } finally {
                            db.close();
                        }
                        break;

                }
            }



            Intent intent = new Intent(IndividualFix.this, EventDetail.class);

            intent.putExtra("_id",_id1);
            intent.putExtra("event",event1);
            intent.putExtra("amount",amount1);
            intent.putExtra("limit",limit1);
            intent.putExtra("member",member1);

            startActivity(intent);
            finish();


        }
    }

    // 削除ボタンを押した場合の処理
    private class DeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // ダイアログを開く
            IndividualDeleteDialog dialogFragment = new IndividualDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(),"IndividualDeleteDialog");
        }
    }
}