package com.example.tsuyu6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;


public class DeleteDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // タイトルのデザインを作成
        TextView titleView = new TextView(getActivity());
        titleView.setText(getResources().getText(R.string.dialog_title));
        titleView.setTextSize(20);
        titleView.setBackgroundColor(getResources().getColor(R.color.currently));
        titleView.setPadding(80, 20, 20, 20);

        // メッセージのデザインを作成
        TextView msgView = new TextView(getActivity());
        msgView.setText(getResources().getText(R.string.dialog_msg));
        msgView.setTextSize(16);
        msgView.setPadding(160, 60, 20, 20);

        // ダイアログビルダーを作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // ダイアログのタイトルを設定
        builder.setCustomTitle(titleView);
        // ダイアログのメッセージを設定
        builder.setView(msgView);
        // Positive Buttonを設定。
        builder.setPositiveButton(R.string.positive_button, new DialogButtonClickListener());
        // Negative Buttonを設定。
        builder.setNegativeButton(R.string.negative_button, new DialogButtonClickListener());
        // ダイアログオブジェクトを生成し、リターン
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // DELETE文実行
                    Fix fix = new Fix();

                    String listid = fix._id;
                    int displayMonth = fix.displayMonth;
                    int displayYear = fix.displayYear;

                    // DBの更新処理(DELETE)
                    int _id = Integer.parseInt(listid);
                    DatabaseHelper helper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        String sqlDelete = "DELETE FROM tsuyu6 WHERE _id = " + _id;
                        SQLiteStatement stmt = db.compileStatement(sqlDelete);
                        stmt.executeUpdateDelete();
                    }finally {
                        db.close();
                    }

                    // 画面遷移
                    Intent intent = new Intent(getActivity(), Look.class);
                    intent.putExtra("displayYear", displayYear);
                    intent.putExtra("displayMonth", displayMonth);
                    startActivity(intent);
                    getActivity().finish();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
