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

public class EventDeleteDialog extends DialogFragment  {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // タイトルのデザインを作成
        TextView titleView = new TextView(getActivity());
        titleView.setText(getResources().getText(R.string.dialog_title));
        titleView.setTextSize(20);
        titleView.setBackgroundColor(getResources().getColor(R.color.share));
        titleView.setPadding(80, 20, 20, 20);

        // メッセージのデザインを作成
        TextView msgView = new TextView(getActivity());
        msgView.setText(getResources().getText(R.string.delete_dialog_msg));
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
                    // DBの更新処理(DELETE)

                    EventFix fix = new EventFix();

                    String listId = fix._id;

                    int _id = Integer.parseInt(listId);
                    DatabaseHelper helper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {

                        // event
                        String sqlDelete = "DELETE FROM event6 WHERE _id = " + _id;
                        SQLiteStatement stmt = db.compileStatement(sqlDelete);
                        stmt.executeUpdateDelete();

                        // 家計簿
                        String sqlDelete2 = "DELETE FROM tsuyu6  WHERE _id IN (SELECT kakeibo_id FROM individual6 WHERE event_id = " + _id + ")";
                        SQLiteStatement stmt2 = db.compileStatement(sqlDelete2);
                        stmt2.executeUpdateDelete();

                        // individual
                        String sqlDelete3 = "DELETE FROM individual6  WHERE event_id =" + _id;
                        SQLiteStatement stmt3= db.compileStatement(sqlDelete3);
                        stmt3.executeUpdateDelete();

                    }finally {
                        db.close();
                    }


                    // 画面遷移
                    Intent intent = new Intent(getActivity(), Event.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}


