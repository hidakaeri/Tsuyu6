package com.example.tsuyu6;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Look extends AppCompatActivity {

    static int month_count;
    static int displayMonth;
    static int displayYear;

    static int displayReturnMonth;
    static int displayReturnYear;

    static int lastNextMonth;
    static int lastNextYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.currently);
        setContentView(R.layout.activity_look);

        ListView lvMenu = findViewById(R.id.look_list);
        List<Map<String,Object>> menuList = new ArrayList<>();
        Map<String,Object> menu = new HashMap<>();

        // intentを受け取る
        Intent intent = getIntent();
        month_count = intent.getIntExtra("month_count",0);
        displayReturnMonth = intent.getIntExtra("displayMonth",-1);
        displayReturnYear = intent.getIntExtra("displayYear",-1);
        lastNextMonth = intent.getIntExtra("lastNextMonth",-1);
        lastNextYear = intent.getIntExtra("lastNextYear",-1);

        // 現在の年月を取得
        Calendar date = Calendar.getInstance();
        int nowMonth = date.get(Calendar.MONTH)+1;
        int nowYear = date.get(Calendar.YEAR);




        if(displayReturnMonth != -1 ) {
            // 追加、編集画面から戻ってきた場合
            displayMonth = displayReturnMonth;
            displayYear = displayReturnYear;

        } else if (lastNextMonth != -1) {
            // 先月、来月から戻ってきた場合
            displayMonth = lastNextMonth;
            displayYear = lastNextYear;
        } else {
            // 表示したい月
            displayMonth = nowMonth+month_count;
            int year_count = 0;
            if(displayMonth > 12) {
                while(displayMonth > 12){
                    displayMonth -= 12;
                    year_count++;
                }
            } else if (displayMonth < 1){
                while(displayMonth < 1){
                    displayMonth += 12;
                    year_count--;
                }
            }
            displayYear = nowYear+year_count;
        }


        TextView year_month = findViewById(R.id.year_month);
        year_month.setText(displayYear + "年" + displayMonth + "月");

        // ListView の背景設定
        ImageView imageView = findViewById(R.id.flower);
        switch (displayMonth) {
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
        DatabaseHelper helper = new DatabaseHelper(Look.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //月を二桁表示
        Format f = new DecimalFormat("00");

        //年、月ごとにDB検索(合計)
        String totalSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE flag = '家計簿' " +
                "AND date >= '" + displayYear + " / " + f.format(displayMonth) + " / 01'" +
                "AND date <= '" + displayYear + " / " + f.format(displayMonth)  + " / 31'" +
                "ORDER BY date";

        Cursor cur = db.rawQuery(totalSql,null);
        cur.moveToFirst();
        total = cur.getInt(0);

        //年、月ごとにDB検索(収入)
        String incomeSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE flag = '家計簿' AND amount > 0 " +
                "AND date >= '" + displayYear + " / " + f.format(displayMonth) + " / 01'" +
                "AND date <= '" + displayYear + " / " + f.format(displayMonth)  + " / 31'";

        cur = db.rawQuery(incomeSql,null);
        cur.moveToFirst();
        income = cur.getInt(0);

        //年、月ごとにDB検索(支出)
        String expenditureSql = "SELECT TOTAL(amount) FROM tsuyu6 " +
                "WHERE flag = '家計簿' AND amount < 0 " +
                "AND date >= '" + displayYear + " / " + f.format(displayMonth) + " / 01'" +
                "AND date <= '" + displayYear + " / " + f.format(displayMonth)  + " / 31'";

        cur = db.rawQuery(expenditureSql,null);
        cur.moveToFirst();
        expenditure = cur.getInt(0);

        // 合計、収入、支出のTextView取得
        TextView totalText = findViewById(R.id.look_total);
        TextView incomeText = findViewById(R.id.look_income);
        TextView expenditureText = findViewById(R.id.look_expenditure);

        // 合計、収入、支出の内容をTextViewにセット
        // totalText.setText(String.format("%,d", total));
        incomeText.setText(String.format("%,d", income));
        expenditureText.setText(String.format("%,d", expenditure));




        //DB操作(SELECT)
        if(helper == null){
            helper = new DatabaseHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        try {

            String sql = "SELECT * FROM tsuyu6 WHERE flag = '家計簿' ";
            cur = db.rawQuery(sql,null);

            //DBの_idをリストに渡す
            String[] from = {"_id","date","item","memo","amount","flag"};
            int[] to = {R.id.display_id, R.id.display_date, R.id.display_item, R.id.display_memo, R.id.display_amount, R.id.flg};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(Look.this, R.layout.row, cur, from, to,0);
            lvMenu.setAdapter(adapter);

            //1行ずつDBからリストへ
            long Count = DatabaseUtils.queryNumEntries(db,"tsuyu6", null,null);
            for (int i = 0; i <= Count; i++){

                String selectSql = "SELECT * FROM tsuyu6 " +
                        "WHERE flag = '家計簿' " +
                        "AND date >= '" + displayYear + " / " + f.format(displayMonth) + " / 01'" +
                        "AND date <= '" + displayYear + " / " + f.format(displayMonth)  + " / 31'" +
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
                    menu.put("flag", flg);
                    menuList.add(menu);

                    cur.moveToFirst();
                }
            }


        }finally {
            db.close();
        }

        String[] from = {"_id","date","item","memo","amount","flag"};
        int[] to = {R.id.display_id, R.id.display_date, R.id.display_item, R.id.display_memo, R.id.display_amount, R.id.flg};
        SimpleAdapter adapter = new SimpleAdapter(Look.this,menuList,R.layout.row,from,to);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new ListItemClickListener());

        // 追加ボタンの取得
        ImageButton addClick = findViewById(R.id.addClick);
        // 追加ボタンのリスナクラスのインスタンスを作成
        AddClickListener add_listener = new AddClickListener();
        // 追加ボタンにリスナを設定
        addClick.setOnClickListener(add_listener);

        // 来月ボタンの取得
        Button nextClick = findViewById(R.id.next_month);
        // 追加ボタンのリスナクラスのインスタンスを作成
        nextClickListener next_listener = new nextClickListener();
        // 追加ボタンにリスナを設定
        nextClick.setOnClickListener(next_listener);

        // 先月ボタンの取得
        Button lastClick = findViewById(R.id.last_month);
        // 追加ボタンのリスナクラスのインスタンスを作成
        lastClickListener last_listener = new lastClickListener();
        // 追加ボタンにリスナを設定
        lastClick.setOnClickListener(last_listener);

        // simulation radio buttonの取得
        RadioButton simulationClick = findViewById(R.id.flgSimulation);
        // simulation radio buttonのリスナクラスのインスタンスを作成
        SimulationClickListener simulation_Listener = new SimulationClickListener();
        // simulation radio buttonにリスナを設定
        simulationClick.setOnClickListener(simulation_Listener);

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

            String _id = item.get("_id").toString();
            String fixDate = item.get("date").toString();
            String fixItem = item.get("item").toString();
            String fixMemo = item.get("memo").toString();
            String fixAmount = item.get("amount").toString();
            String fixFlg = item.get("flag").toString();

            // fix画面に送るデータの格納
            Intent intent = new Intent(Look.this, Fix.class);

            intent.putExtra("listId",_id);
            intent.putExtra("fixDate", fixDate);
            intent.putExtra("fixItem", fixItem);
            intent.putExtra("fixMemo", fixMemo);
            intent.putExtra("fixAmount", fixAmount);
            intent.putExtra("fixFlg", fixFlg);

            intent.putExtra("displayMonth", displayMonth);
            intent.putExtra("displayYear", displayYear);

            startActivity(intent);
            finish();
        }
    }

    // 追加ボタンを押した場合の処理
    private class AddClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            // 入力画面に遷移
            Intent intent = new Intent(Look.this, Input.class);
            intent.putExtra("displayMonth", displayMonth);
            intent.putExtra("displayYear", displayYear);
            startActivity(intent);
            finish();
        }
    }

    // nextボタンを押した場合の処理
    private class nextClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            month_count += 1;

            if(displayReturnMonth != -1 ) {
                // 追加、編集画面から戻ってきた場合
                lastNextMonth = displayReturnMonth + 1;
                lastNextYear = displayReturnYear;
                if (lastNextMonth == 13 ) {
                    lastNextMonth = 1;
                    lastNextYear+= 1;
                }

                Intent intent = new Intent(Look.this, Look.class);

                intent.putExtra("lastNextMonth", lastNextMonth);
                intent.putExtra("lastNextYear", lastNextYear);
                startActivity(intent);

            } else if (lastNextMonth != -1) {
                // 先月、来月から戻ってきた場合
                lastNextMonth = lastNextMonth + 1;
                if (lastNextMonth == 13 ) {
                    lastNextMonth = 1;
                    lastNextYear+= 1;
                }

                Intent intent = new Intent(Look.this, Look.class);

                intent.putExtra("lastNextMonth", lastNextMonth);
                intent.putExtra("lastNextYear", lastNextYear);
                startActivity(intent);
            } else {
                // lookが最初に開かれたとき
                Intent intent = new Intent(Look.this, Look.class);
                intent.putExtra("month_count", month_count);

                startActivity(intent);
            }
            finish();
        }
    }

    // lastボタンを押した場合の処理
    private class lastClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            month_count -= 1;
            if (displayReturnMonth != -1) {
                // 追加、編集画面から戻ってきた場合
                lastNextMonth = displayReturnMonth - 1;
                lastNextYear = displayReturnYear;
                if (lastNextMonth == 0) {
                    lastNextMonth = 12;
                    lastNextYear -= 1;
                }

                Intent intent = new Intent(Look.this, Look.class);

                intent.putExtra("lastNextMonth", lastNextMonth);
                intent.putExtra("lastNextYear", lastNextYear);
                startActivity(intent);

            } else if (lastNextMonth != -1) {
                // 先月、来月から戻ってきた場合
                lastNextMonth = lastNextMonth - 1;
                if (lastNextMonth == 0) {
                    lastNextMonth = 12;
                    lastNextYear -= 1;
                }

                Intent intent = new Intent(Look.this, Look.class);

                intent.putExtra("lastNextMonth", lastNextMonth);
                intent.putExtra("lastNextYear", lastNextYear);
                startActivity(intent);
            } else {
                // lookが最初に開かれたとき
                Intent intent = new Intent(Look.this, Look.class);
                intent.putExtra("month_count", month_count);

                startActivity(intent);
            }
            finish();
        }
    }

    // simulationボタンを押した場合の処理
    private class SimulationClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Look.this, FuLook.class);
            intent.putExtra("FuDisplayMonth", displayMonth);
            intent.putExtra("FuDisplayYear", displayYear);
            startActivity(intent);
            finish();

        }
    }

    // savingボタンを押した場合の処理
    private class SavingClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {

            Intent intent = new Intent(Look.this, Saving.class);
            startActivity(intent);
            finish();

        }
    }
}