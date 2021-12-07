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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FuFix extends AppCompatActivity {

    int newYear;
    int newMonth;
    int newDay;

    static String _id = "";
    String fixDate = "";
    String fixItem = "";
    String fixAmount = "";
    String fixMemo = "";
    static int FuDisplayMonth;
    static int FuDisplayYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_fix);
        Intent intent = getIntent();

        _id = intent.getStringExtra("listId");
        fixDate = intent.getStringExtra("fixDate");
        fixItem = intent.getStringExtra("fixItem");
        fixAmount = intent.getStringExtra("fixAmount");
        fixMemo = intent.getStringExtra("fixMemo");
        FuDisplayMonth = intent.getIntExtra("FuDisplayMonth",0);
        FuDisplayYear = intent.getIntExtra("FuDisplayYear",0);


        TextView fixDateText = findViewById(R.id.fixDate);
        TextView fixItemText = findViewById(R.id.fixItem);
        TextView fixAmountText = findViewById(R.id.fixAmount);
        TextView fixMemoText = findViewById(R.id.fixMemo);

        // メモの()を除去
        String[] strMemo = fixMemo.split("()");
        fixMemo = "";
        for (int i = 2; i < strMemo.length-1; i++) {
            fixMemo += strMemo[i];
        }

        // ラジオグループのオブジェクトを取得
        RadioGroup rg = findViewById(R.id.flgIncomeExpenditure);

        String[] strAmount = fixAmount.split(",");
        fixAmount = "";
        for(String i : strAmount ) {
            fixAmount += i;
        }

        int fixAmountInt = Integer.parseInt(fixAmount);

        if(fixAmountInt >= 0) {
            // 収入（金額が正の数の場合の処理）
            rg.check(R.id.flgIncome);

        } else {
            // 支出（金額が負の数の場合の処理）
            rg.check(R.id.flgExpenditure);
            fixAmountInt *= -1;
        }

        // テキストをxmlファイルにセット
        fixDateText.setText(fixDate);
        fixItemText.setText(fixItem);
        fixAmountText.setText(Integer.toString(fixAmountInt));
        fixMemoText.setText(fixMemo);

        // DBの日時の分割（初期値用）
        String[] strDate = fixDate.split("/");
        newYear = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]);
        newMonth = month - 1;
        newDay = Integer.parseInt(strDate[2]);

        //EditTextにリスナーをつける
        fixDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FuFix.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                fixDateText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));

                                // 初期選択日付を選択日付に更新
                                newYear = year;
                                newMonth = month;
                                newDay = dayOfMonth;
                            }
                        },
                        // 初期値セット
                        newYear,newMonth,newDay
                );

                //dialogを表示
                datePickerDialog.show();
            }

        });


        // 修正ボタンの取得
        Button fixClick = findViewById(R.id.fixClick);
        // 修正ボタンのリスナクラスのインスタンスを作成
        FuFix.FixClickListenerListener fix_listener = new FuFix.FixClickListenerListener();
        // 修正ボタンにリスナを設定
        fixClick.setOnClickListener(fix_listener);

        // 削除ボタンの取得
        Button deleteClick = findViewById(R.id.deleteClick);
        // 削除ボタンのリスナクラスのインスタンスを作成
        FuFix.DeleteClickListener delete_listener = new FuFix.DeleteClickListener();
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

        // simulation radio buttonの取得
        RadioButton HouseholdAccountBookClick = findViewById(R.id.flgHouseholdAccountBook);
        // simulation radio buttonのリスナクラスのインスタンスを作成
        FuFix.HouseholdAccountBookClickListener HouseholdAccountBook_Listener = new FuFix.HouseholdAccountBookClickListener();
        // simulation radio buttonにリスナを設定
        HouseholdAccountBookClick.setOnClickListener(HouseholdAccountBook_Listener);

    }

    // 戻るボタンを押した場合の処理
    public void onBackButtonClick(View view) {

        Intent intent = new Intent(FuFix.this, FuLook.class);
        intent.putExtra("FuDisplayMonth", FuDisplayMonth);
        intent.putExtra("FuDisplayYear", FuDisplayYear);
        startActivity(intent);
        finish();
    }

    // 修正ボタンを押した場合の処理
    private class FixClickListenerListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 金額の入力内容をString型で取得
            TextView fixAmountText = findViewById(R.id.fixAmount);
            String fixAmountString = fixAmountText.getText().toString();

            if (fixAmountString.equals("")){
                // 金額が入力されていない場合の処理
                // トーストを表示
                Toast.makeText(FuFix.this, R.string.toast_amount, Toast.LENGTH_LONG).show();

            } else if (Double.parseDouble(fixAmountString) % 1 != 0){
                // 金額が小数の場合
                // トーストを表示
                Toast.makeText(FuFix.this, R.string.toast_double, Toast.LENGTH_LONG).show();
            } else {
                // 選択されているラジオボタンの取得
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.flgIncomeExpenditure);

                int checkedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(checkedId);

                // ラジオボタンのテキストを取得
                String text = radioButton.getText().toString();

                // 入力内容を取得
                TextView fixDateText = findViewById(R.id.fixDate);
                TextView fixItemText = findViewById(R.id.fixItem);
                TextView fixMemoText = findViewById(R.id.fixMemo);

                String fixDate = fixDateText.getText().toString();
                String fixItem = fixItemText.getText().toString();
                int fixAmount = Integer.parseInt(fixAmountString);
                String fixMemo = fixMemoText.getText().toString();
                String flag = "シミュレーション";

                // 入力された月を取得
                String[] strDate = fixDate.split("/");
                int year = Integer.parseInt(strDate[0]);
                int month = Integer.parseInt(strDate[1]);


                // 金額の符号を設定
                if (text.equals("支出")) {
                    fixAmount *= -1;
                }

                // DB更新処理(UPDATE)
                int id = Integer.parseInt(_id);
                DatabaseHelper helper = new DatabaseHelper(FuFix.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    String sqlUpdate = "UPDATE tsuyu6 SET date = ?, item = ?, amount = ?, memo = ?, flag = ? WHERE _id = " + id;
                    SQLiteStatement stmt = db.compileStatement(sqlUpdate);
                    stmt.bindString(1, fixDate);
                    stmt.bindString(2, fixItem);
                    stmt.bindLong(3, fixAmount);
                    stmt.bindString(4, fixMemo);
                    stmt.bindString(5,flag);

                    stmt.executeInsert();

                }finally {
                    db.close();
                }

                Intent intent = new Intent(FuFix.this, FuLook.class);
                intent.putExtra("FuDisplayYear", year);
                intent.putExtra("FuDisplayMonth", month);
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
            FuDeleteDialog dialogFragment = new FuDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(),"FuDeleteDialog");
        }
    }

    private class HouseholdAccountBookClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            Intent intent = new Intent(FuFix.this, Fix.class);

            intent.putExtra("listId",_id);
            intent.putExtra("fixDate", fixDate);
            intent.putExtra("fixItem", fixItem);
            intent.putExtra("fixMemo", fixMemo);
            intent.putExtra("fixAmount", fixAmount);

            intent.putExtra("displayMonth", FuDisplayMonth);
            intent.putExtra("displayYear", FuDisplayYear);

            startActivity(intent);
            finish();
        }
    }
}