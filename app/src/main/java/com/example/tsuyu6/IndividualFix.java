package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import java.util.HashMap;

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

    static String kakeibo_id_input = "";
    static String kakeibo_id = "";

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
                Calendar Date = Calendar.getInstance();

                newYear = Date.get(Calendar.YEAR);
                newMonth = Date.get(Calendar.MONTH);
                newDay = Date.get(Calendar.DATE);
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

        intent.putExtra("_id",_id1);
        intent.putExtra("event",event1);
        intent.putExtra("amount",amount1);
        intent.putExtra("limit",limit1);
        intent.putExtra("member",member1);

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

            // 現在日時の取得
            Calendar Date = Calendar.getInstance();

            newYear = Date.get(Calendar.YEAR);
            newMonth = Date.get(Calendar.MONTH);
            newDay = Date.get(Calendar.DATE);

            String inputDate = String.format("%d / %02d / %02d", newYear, newMonth + 1, newDay);


            // 更新内容を変数に代入
            String inputItem = event1;
            String inputMemo = "シェア";
            String inputFlg = "家計簿";

            // DB更新準備
            DatabaseHelper helper = new DatabaseHelper(IndividualFix.this);
            SQLiteDatabase db = helper.getWritableDatabase();



            // メンバー(自分のID)入力
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
                int inputAmount = (Integer.parseInt(amount)) * -1;


                switch (moveFlg) {
                    case "input":

                        // 登録


                        if(helper == null){
                            helper = new DatabaseHelper(getApplicationContext());
                        }

                        if(db == null){
                            db = helper.getReadableDatabase();
                        }
                        try {
                            // DBの更新処理(INSERT)

                            // 家計簿
                            String sqlInsert = "INSERT INTO tsuyu6 (_id, date, item, amount, memo, flag) VALUES (?,?,?,?,?,?)";
                            SQLiteStatement stmt = db.compileStatement(sqlInsert);
                            stmt.bindString(2, inputDate);
                            stmt.bindString(3, inputItem);
                            stmt.bindLong(4, inputAmount);
                            stmt.bindString(5, inputMemo);
                            stmt.bindString(6,inputFlg);

                            stmt.executeInsert();



                            // tsuyu6 DB のid取得
                            // 一番最後のデータ取得
                            String sql1 = "SELECT _id FROM tsuyu6 WHERE _id = (SELECT max(_id) FROM tsuyu6)";
                            cur = db.rawQuery(sql1, null);
                            while(cur.moveToNext()){
                                int idId = cur.getColumnIndex("_id");

                                kakeibo_id_input = cur.getString(idId);

                                cur.moveToFirst();
                            }

                            // individual6

                            String sqlInsert2 = "INSERT INTO individual6 (_id, event_id, date, member, amount, kakeibo_id) VALUES (?,?,?,?,?,?)";
                            SQLiteStatement stmt2 = db.compileStatement(sqlInsert2);

                            stmt2.bindString(2, event_id);
                            stmt2.bindString(3, date);
                            stmt2.bindString(4, member);
                            stmt2.bindString(5, amount);
                            stmt2.bindString(6, kakeibo_id_input);

                            stmt2.executeInsert();
                        }finally {
                            db.close();
                        }


                        break;

                    case "fix":



                        int id = Integer.parseInt(_id);

                        // kakeibo_id取得
                        String sql3 = "SELECT kakeibo_id FROM individual6 WHERE _id = " + id;
                        cur = db.rawQuery(sql3, null);
                        while(cur.moveToNext()){
                            int IdxKakeibo_id = cur.getColumnIndex("kakeibo_id");

                            kakeibo_id = cur.getString(IdxKakeibo_id);

                            cur.moveToFirst();
                        }


                        int kakeibo_id_int  = Integer.parseInt(kakeibo_id);

                        try {
                            String sqlUpdate = "UPDATE tsuyu6 SET date = ?, amount = ? WHERE _id = " + kakeibo_id_int;
                            SQLiteStatement stmt = db.compileStatement(sqlUpdate);
                            stmt.bindString(1, inputDate);
                            stmt.bindLong(2, inputAmount);

                            stmt.executeInsert();

                            // 修正
                            // DB更新処理(UPDATE)
                            // int id = Integer.parseInt(_id);

                            String sqlUpdate2 = "UPDATE individual6 SET event_id = ?, date = ?, member = ?, amount = ? WHERE _id =" + id;
                            SQLiteStatement stmt2 = db.compileStatement(sqlUpdate2);

                            stmt2.bindString(1, event_id);
                            stmt2.bindString(2, date);
                            stmt2.bindString(3, member);
                            stmt2.bindString(4, amount);

                            stmt2.executeInsert();

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


            // DB更新準備
            DatabaseHelper helper = new DatabaseHelper(IndividualFix.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            int id = Integer.parseInt(_id);

            // kakeibo_id取得
            String sql3 = "SELECT kakeibo_id FROM individual6 WHERE _id = " + id;
            Cursor cur = db.rawQuery(sql3, null);
            while(cur.moveToNext()){
                int IdxKakeibo_id = cur.getColumnIndex("kakeibo_id");

                kakeibo_id = cur.getString(IdxKakeibo_id);

                cur.moveToFirst();
            }

            // ダイアログを開く
            IndividualDeleteDialog dialogFragment = new IndividualDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(),"IndividualDeleteDialog");
        }
    }
}