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

import java.util.Calendar;

public class FuInput extends AppCompatActivity {

    int newYear;
    int newMonth;
    int newDay;
    static int FuDisplayYear;
    static int FuDisplayMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_input);
        Intent intent = getIntent();

        FuDisplayYear = intent.getIntExtra("FuDisplayYear",0);
        FuDisplayMonth = intent.getIntExtra("FuDisplayMonth",0);

        // 保存ボタンの取得
        Button inputClick = findViewById(R.id.inputClick);
        // 保存ボタンのリスナクラスのインスタンスを作成
        FuInput.InputClickListener input_listener = new FuInput.InputClickListener();
        // 保存ボタンにリスナを設定
        inputClick.setOnClickListener(input_listener);


        //部品の取得
        TextView inputDateText =  findViewById(R.id.inputDate);

        // 現在日時の取得
        Calendar date = Calendar.getInstance();
        newYear = date.get(Calendar.YEAR);
        newMonth = date.get(Calendar.MONTH);
        newDay = date.get(Calendar.DATE);
        inputDateText.setText(String.format("%d / %02d / %02d", newYear, newMonth+1, newDay));

        //EditTextにリスナーをつける
        inputDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FuInput.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                inputDateText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
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

        Intent intent = new Intent(FuInput.this, FuLook.class);
        intent.putExtra("FuDisplayYear", FuDisplayYear);
        intent.putExtra("FuDisplayMonth", FuDisplayMonth);
        startActivity(intent);

        finish();
    }


    // 保存ボタンを押した場合の処理
    private class InputClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 金額の入力内容をString型で取得
            TextView inputAmountText = findViewById(R.id.inputAmount);
            String inputAmountString = inputAmountText.getText().toString();

            // ラジオボタンの内容で金額場合分け
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.flgIncomeExpenditure);

            int checkedId = radioGroup.getCheckedRadioButtonId();

            if (checkedId == -1) {
                // ラジオボタンが選択されていない場合の処理
                // トーストを表示
                Toast.makeText(FuInput.this, R.string.toast_radio, Toast.LENGTH_LONG).show();
            } else if (inputAmountString.equals("")){
                // 金額が入力されていない場合の処理
                // トーストを表示
                Toast.makeText(FuInput.this, R.string.toast_amount, Toast.LENGTH_LONG).show();

            } else if (Double.parseDouble(inputAmountString) % 1 != 0){
                // 金額が小数の場合
                // トーストを表示
                Toast.makeText(FuInput.this, R.string.toast_double, Toast.LENGTH_LONG).show();
            } else {

                // 入力された内容を取得
                TextView inputDateText = findViewById(R.id.inputDate);
                TextView inputItemText = findViewById(R.id.inputItem);
                TextView inputMemoText = findViewById(R.id.inputMemo);


                String inputDate = inputDateText.getText().toString();
                String inputItem = inputItemText.getText().toString();
                String inputMemo = inputMemoText.getText().toString();

                // 入力された月を取得
                String[] strDate = inputDate.split(" / ");
                int year = Integer.parseInt(strDate[0]);
                int month = Integer.parseInt(strDate[1]);

                // 選択されているラジオボタンの取得
                RadioButton radioButton = (RadioButton) findViewById(checkedId);

                // ラジオボタンのテキストを取得
                String text = radioButton.getText().toString();

                // 金額をint型に変換
                int inputAmount = Integer.parseInt(inputAmountString);


                // 金額の符号を設定
                if(text.equals("支出")) {
                    inputAmount *= -1;
                }

                // DBの更新処理(INSERT)
                DatabaseHelper helper = new DatabaseHelper(FuInput.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                if(helper == null){
                    helper = new DatabaseHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getReadableDatabase();
                }
                try {
                    String sqlInsert = "INSERT INTO tsuyu6 (_id, date, item, amount, memo) VALUES (?,?,?,?,?)";
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);
                    stmt.bindString(2, inputDate);
                    stmt.bindString(3, inputItem);
                    stmt.bindLong(4, inputAmount);
                    stmt.bindString(5, inputMemo);

                    stmt.executeInsert();
                }finally {
                    db.close();
                }
                Intent intent = new Intent(FuInput.this, FuLook.class);
                intent.putExtra("FuDisplayYear", year);
                intent.putExtra("FuDisplayMonth", month);
                startActivity(intent);
                finish();

            }
        }
    }
}