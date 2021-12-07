package com.example.tsuyu6;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuLook extends AppCompatActivity {

    static int month_count;
    static int FuDisplayMonth;
    static int FuDisplayYear;

    static String _id;
    static String fixDate;
    static String fixItem;
    static String fixMemo;
    static String fixAmount;
    static String fixFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_look);

        // intentを受け取る
        Intent intent = getIntent();
        month_count = intent.getIntExtra("month_count",0);
        int FuDisplayReturnMonth = intent.getIntExtra("FuDisplayMonth",-1);
        int FuDisplayReturnYear = intent.getIntExtra("FuDisplayYear",-1);

        // 現在の年月を取得
        Calendar date = Calendar.getInstance();
        int nowMonth = date.get(Calendar.MONTH)+1;
        int nowYear = date.get(Calendar.YEAR);



        // 追加、編集画面から戻ってきた場合
        if(FuDisplayReturnMonth != -1) {
            FuDisplayMonth = FuDisplayReturnMonth;
            FuDisplayYear = FuDisplayReturnYear;
        } else {
            // 表示したい月
            FuDisplayMonth = nowMonth+month_count;
            int year_count = 0;
            if(FuDisplayMonth > 12) {
                while(FuDisplayMonth > 12){
                    FuDisplayMonth -= 12;
                    year_count++;
                }
            } else if (FuDisplayMonth < 1){
                while(FuDisplayMonth < 1){
                    FuDisplayMonth += 12;
                    year_count--;
                }
            }
            FuDisplayYear = nowYear+year_count;
        }


        TextView year_month = findViewById(R.id.year_month);
        year_month.setText(FuDisplayYear + "年" + FuDisplayMonth + "月");

        // ListView の背景設定
        ImageView imageView = findViewById(R.id.flower);
        switch (FuDisplayMonth) {
            case 1:
                imageView.setBackgroundResource(R.drawable.jan);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.feb);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.mar);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.apr);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.may);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.jun);
                break;
            case 7:
                imageView.setBackgroundResource(R.drawable.jul);
                break;
            case 8:
                imageView.setBackgroundResource(R.drawable.aug);
                break;
            case 9:
                imageView.setBackgroundResource(R.drawable.sep);
                break;
            case 10:
                imageView.setBackgroundResource(R.drawable.out);
                break;
            case 11:
                imageView.setBackgroundResource(R.drawable.nov);
                break;
            case 12:
                imageView.setBackgroundResource(R.drawable.dec);
                break;
        }



        // 合計、収入、支出の変数定義、初期値設定
        int total = 0;
        int income = 0;
        int expenditure = 0;

        //DB接続準備
        DatabaseHelper helper = new DatabaseHelper(FuLook.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //月を二桁表示
        Format f = new DecimalFormat("00");

        //年、月ごとにDB検索(合計)
        String totalSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE date >= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth) + " / 01'" +
                "AND date <= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth)  + " / 31'" +
                "ORDER BY date";

        Cursor cur = db.rawQuery(totalSql,null);
        cur.moveToFirst();
        total = cur.getInt(0);

        //年、月ごとにDB検索(収入)
        String incomeSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE amount > 0 " +
                "AND date >= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth) + " / 01'" +
                "AND date <= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth)  + " / 31'";

        cur = db.rawQuery(incomeSql,null);
        cur.moveToFirst();
        income = cur.getInt(0);

        //年、月ごとにDB検索(支出)
        String expenditureSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE amount < 0 " +
                "AND date >= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth) + " / 01'" +
                "AND date <= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth)  + " / 31'";

        cur = db.rawQuery(expenditureSql,null);
        cur.moveToFirst();
        expenditure = cur.getInt(0);

        // 合計、収入、支出のTextView取得
        TextView totalText = findViewById(R.id.look_total);
        TextView incomeText = findViewById(R.id.look_income);
        TextView expenditureText = findViewById(R.id.look_expenditure);

        // 合計、収入、支出の内容をTextViewにセット
        totalText.setText(String.format("%,d", total));
        incomeText.setText(String.format("%,d", income));
        expenditureText.setText(String.format("%,d", expenditure));


        ListView lvMenu = findViewById(R.id.look_list);
        List<Map<String,Object>> menuList = new ArrayList<>();

        Map<String,Object> menu = new HashMap<>();

        //DB操作(SELECT)
        if(helper == null){
            helper = new DatabaseHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        try {

            String sql = "SELECT * FROM tsuyu6";
            cur = db.rawQuery(sql,null);

            //DBの_idをリストに渡す
            String[] from = {"_id","date","item","memo","amount","flag"};
            int[] to = {R.id.display_id, R.id.display_date, R.id.display_item, R.id.display_memo, R.id.display_amount, R.id.flg};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(FuLook.this, R.layout.row, cur, from, to,0);
            lvMenu.setAdapter(adapter);

            //1行ずつDBからリストへ
            long Count = DatabaseUtils.queryNumEntries(db,"tsuyu6", null,null);
            for (int i = 0; i <= Count; i++){

                String selectSql = "SELECT * FROM tsuyu6 " +
                        "WHERE date >= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth) + " / 01'" +
                        "AND date <= '" + FuDisplayYear + " / " + f.format(FuDisplayMonth)  + " / 31'" +
                        "ORDER BY date " +
                        "LIMIT " + i + "," + 1;
                cur = db.rawQuery(selectSql, null);
                String _id = "";
                String sdate = "";
                String item = "";
                String amount = "";
                String memo = "";
                String flg = "";

                while(cur.moveToNext()){
                    //DBの列番号(index)を取得
                    int idxId = cur.getColumnIndex("_id");
                    int idxDate = cur.getColumnIndex("date");
                    int idxItem = cur.getColumnIndex("item");
                    int idxAmount = cur.getColumnIndex("amount");
                    int idxMemo = cur.getColumnIndex("memo");
                    int idxFlg = cur.getColumnIndex("flag");

                    //列番号(index)にあるデータを取得
                    _id = cur.getString(idxId);
                    sdate = cur.getString(idxDate);
                    item = cur.getString(idxItem);
                    amount = cur.getString(idxAmount);
                    memo = cur.getString(idxMemo);
                    flg = cur.getString(idxFlg);

                    if(memo.equals("")) {

                    } else {
                        memo = "(" + memo + ")";
                    }


                    //取得したデータをリストのmapに入れる
                    menu = new HashMap<>();
                    menu.put("_id", _id);
                    menu.put("date", sdate);
                    menu.put("item", item);
                    menu.put("amount",amount);
                    menu.put("memo", memo);
                    menu.put("flag",flg);
                    menuList.add(menu);

                    cur.moveToFirst();
                }
            }


        }finally {
            db.close();
        }


        String[] from = {"_id","date","item","memo","amount","flag"};
        int[] to = {R.id.display_id, R.id.display_date, R.id.display_item, R.id.display_memo, R.id.display_amount, R.id.flg};
        SimpleAdapter adapter = new SimpleAdapter(FuLook.this,menuList,R.layout.row,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new FuLook.ListItemClickListener());




        // 追加ボタンの取得
        Button addClick = findViewById(R.id.addClick);
        // 追加ボタンのリスナクラスのインスタンスを作成
        FuLook.AddClickListener add_listener = new FuLook.AddClickListener();
        // 追加ボタンにリスナを設定
        addClick.setOnClickListener(add_listener);

        // 来月ボタンの取得
        Button nextClick = findViewById(R.id.next_month);
        // 追加ボタンのリスナクラスのインスタンスを作成
        FuLook.nextClickListener next_listener = new FuLook.nextClickListener();
        // 追加ボタンにリスナを設定
        nextClick.setOnClickListener(next_listener);

        // 先月ボタンの取得
        Button lastClick = findViewById(R.id.last_month);
        // 追加ボタンのリスナクラスのインスタンスを作成
        FuLook.lastClickListener last_listener = new FuLook.lastClickListener();
        // 追加ボタンにリスナを設定
        lastClick.setOnClickListener(last_listener);



        // HouseholdAccountBook radio buttonの取得
        RadioButton HouseholdAccountBookClick = findViewById(R.id.flgHouseholdAccountBook);
        // HouseholdAccountBook radio buttonのリスナクラスのインスタンスを作成
        HouseholdAccountBookClickListener HouseholdAccountBook_Listener = new HouseholdAccountBookClickListener();
        // HouseholdAccountBook radio buttonにリスナを設定
        HouseholdAccountBookClick.setOnClickListener(HouseholdAccountBook_Listener);

        // saving radio buttonの取得
        RadioButton savingClick = findViewById(R.id.flgSaving);
        // simulation radio buttonのリスナクラスのインスタンスを作成
        SavingClickListener saving_Listener = new SavingClickListener();
        // simulation radio buttonにリスナを設定
        savingClick.setOnClickListener(saving_Listener);
    }

    //　リストがクリックされた時の処理
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(position);

            _id = item.get("_id").toString();
            fixDate = item.get("date").toString();
            fixItem = item.get("item").toString();
            fixMemo = item.get("memo").toString();
            fixAmount = item.get("amount").toString();
            fixFlag = item.get("flag").toString();


            if(fixFlag.equals("家計簿")) {
                // 家計簿の時の処理
                // ダイアログを開く
                TimeDialog dialogFragment = new TimeDialog();
                dialogFragment.show(getSupportFragmentManager(),"TimeDialog");

            } else {
               // シミュレーションの時の処理
                // fix画面に送るデータの格納
                Intent intent = new Intent(FuLook.this, FuFix.class);

                intent.putExtra("listId",_id);
                intent.putExtra("fixDate", fixDate);
                intent.putExtra("fixItem", fixItem);
                intent.putExtra("fixMemo", fixMemo);
                intent.putExtra("fixAmount", fixAmount);

                intent.putExtra("FuDisplayMonth", FuDisplayMonth);
                intent.putExtra("FuDisplayYear", FuDisplayYear);

                startActivity(intent);

                finish();
            }
        }
    }

    // 追加ボタンを押した場合の処理
    private class AddClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            // 入力画面に遷移
            Intent intent = new Intent(FuLook.this, FuInput.class);
            intent.putExtra("FuDisplayMonth", FuDisplayMonth);
            intent.putExtra("FuDisplayYear", FuDisplayYear);
            startActivity(intent);
            finish();
        }
    }

    // nextボタンを押した場合の処理
    private class nextClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            month_count += 1;

            Intent intent = new Intent(FuLook.this, FuLook.class);
            intent.putExtra("month_count", month_count);
            startActivity(intent);
            finish();
        }
    }

    // lastボタンを押した場合の処理
    private class lastClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            month_count -= 1;

            Intent intent = new Intent(FuLook.this, FuLook.class);
            intent.putExtra("month_count", month_count);
            startActivity(intent);
            finish();
        }
    }

    // HouseholdAccountBookボタンを押した場合の処理
    private class HouseholdAccountBookClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(FuLook.this, Look.class);
            startActivity(intent);
            finish();
        }
    }

    // savingボタンを押した場合の処理
    private class SavingClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(FuLook.this, Saving.class);
            startActivity(intent);
            finish();

        }
    }
}