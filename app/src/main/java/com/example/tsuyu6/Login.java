package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    String loginId;
    String loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(Login.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "SELECT * FROM login6";
        Cursor cur = db.rawQuery(sql, null);
        while(cur.moveToNext()){
            int loginIdId = cur.getColumnIndex("loginid");
            int loginPassId = cur.getColumnIndex("loginpass");

            loginId = cur.getString(loginIdId);
            loginPass = cur.getString(loginPassId);

            cur.moveToFirst();
        }


        EditText loginIdText = findViewById(R.id.login_id);
        EditText loginPassText = findViewById(R.id.login_pass);

        if(loginId != null) {
            loginIdText.setText(loginId);
            // loginPassText.setText(loginPass);
        }

        // ログインボタンの取得
        Button loginClick = findViewById(R.id.loginClick);
        // ログインボタンのリスナクラスのインスタンスを作成
        LoginClickListener login_listener = new LoginClickListener();
        // ログインボタンにリスナを設定
        loginClick.setOnClickListener(login_listener);

        // 登録ボタンの取得
        Button registerClick = findViewById(R.id.registerClick);
        // 登録ボタンのリスナクラスのインスタンスを作成
        RegisterClickListener register_listener = new RegisterClickListener();
        // 登録ボタンにリスナを設定
        registerClick.setOnClickListener(register_listener);


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
        finish();
    }

    // ログインボタンを押した場合の処理
    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // ログインID EditTextの取得
            EditText loginIdText = findViewById(R.id.login_id);
            String loginIdInput = loginIdText.getText().toString();


            // ログインpassword EditTextの取得
            EditText loginPassText = findViewById(R.id.login_pass);
            String loginPassInput = loginPassText.getText().toString();

            // ログイン可能判別処理

            //DB接続準備
            DatabaseHelper helper = new DatabaseHelper(Login.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            String sql = "SELECT * FROM login6";
            Cursor cur = db.rawQuery(sql, null);
            while(cur.moveToNext()){
                int loginIdId = cur.getColumnIndex("loginid");
                int loginPassId = cur.getColumnIndex("loginpass");

                loginId = cur.getString(loginIdId);
                loginPass = cur.getString(loginPassId);

                cur.moveToFirst();
            }


            if (loginIdInput.equals(loginId) && loginPassInput.equals(loginPass) ) {
                // ログイン成功
                Intent intent = new Intent(Login.this,Event.class);
                intent.putExtra("loginId", loginId);
                startActivity(intent);
                finish();
            } else {
                loginIdText.setText(loginId);
                // loginPassText.setText(loginPass);

                // ログイン失敗
                Toast.makeText(Login.this, R.string.toast_login_false, Toast.LENGTH_LONG).show();
            }



        }
    }

    // 登録ボタンを押した場合の処理
    private class RegisterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DatabaseHelper helper = new DatabaseHelper(Login.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            // ログインID EditTextの取得
            EditText loginIdText = findViewById(R.id.login_id);
            String loginIdInput = loginIdText.getText().toString();


            // ログインpassword EditTextの取得
            EditText loginPassText = findViewById(R.id.login_pass);
            String loginPassInput = loginPassText.getText().toString();


            if (helper == null) {
                helper = new DatabaseHelper(getApplicationContext());
            }

            if (db == null) {
                db = helper.getReadableDatabase();
            }
            try {
                String sqlDelete = "DELETE FROM login6";
                SQLiteStatement Dstmt = db.compileStatement(sqlDelete);
                Dstmt.executeUpdateDelete();
                String sqlInsert = "INSERT INTO login6 (loginid, loginpass) VALUES (?,?)";
                SQLiteStatement stmt = db.compileStatement(sqlInsert);
                stmt.bindString(1, loginIdInput);
                stmt.bindString(2, loginPassInput);
                stmt.executeInsert();

                Intent intent = new Intent(Login.this,Event.class);
                intent.putExtra("loginId", loginId);
                startActivity(intent);
                finish();
            } finally {
                db.close();
            }

        }
    }
}