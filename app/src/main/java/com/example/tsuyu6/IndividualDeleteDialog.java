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

public class IndividualDeleteDialog extends DialogFragment {

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
        builder.setPositiveButton(R.string.positive_button, new IndividualDeleteDialog.DialogButtonClickListener());
        // Negative Buttonを設定。
        builder.setNegativeButton(R.string.negative_button, new IndividualDeleteDialog.DialogButtonClickListener());
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

                    // DELETE文実行
                    IndividualFix fix = new IndividualFix();

                    String listId = fix._id;
                    String _id1 = fix._id1;
                    String event1 = fix.event1;
                    String amount1 = fix.amount1;
                    String limit1 = fix.limit1;
                    String member1 = fix.member1;


                    // DBの更新処理(DELETE)
                    int _id = Integer.parseInt(listId);
                    DatabaseHelper helper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    try {
                        String sqlDelete = "DELETE FROM individual6 WHERE _id = " + _id;
                        SQLiteStatement stmt = db.compileStatement(sqlDelete);
                        stmt.executeUpdateDelete();
                    }finally {
                        db.close();
                    }

                    // 画面遷移
                    Intent intent = new Intent(getActivity(), EventDetail.class);

                    intent.putExtra("_id",_id1);
                    intent.putExtra("event",event1);
                    intent.putExtra("amount",amount1);
                    intent.putExtra("limit",limit1);
                    intent.putExtra("member",member1);

                    startActivity(intent);
                    getActivity().finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}

