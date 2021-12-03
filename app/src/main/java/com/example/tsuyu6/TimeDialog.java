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

public class TimeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // タイトルのデザインを作成
        TextView titleView = new TextView(getActivity());
        titleView.setText(getResources().getText(R.string.dialog_title));
        titleView.setTextSize(20);
        titleView.setBackgroundColor(getResources().getColor(R.color.future));
        titleView.setPadding(80, 20, 20, 20);

        // メッセージのデザインを作成
        TextView msgView = new TextView(getActivity());
        msgView.setText(getResources().getText(R.string.time_dialog_msg));
        msgView.setTextSize(16);
        msgView.setPadding(160, 60, 20, 20);

        // ダイアログビルダーを作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // ダイアログのタイトルを設定
        builder.setCustomTitle(titleView);
        // ダイアログのメッセージを設定
        builder.setView(msgView);
        // Positive Buttonを設定。
        builder.setPositiveButton(R.string.positive_button, new TimeDialog.DialogButtonClickListener());
        // Negative Buttonを設定。
        builder.setNegativeButton(R.string.negative_button, new TimeDialog.DialogButtonClickListener());
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
                    FuLook fulook = new FuLook();

                    String _id = fulook._id;
                    String fixDate = fulook.fixDate;
                    String fixItem = fulook.fixItem;
                    String fixMemo = fulook.fixMemo;
                    String fixAmount = fulook.fixAmount;
                    int FuDisplayYear = fulook.FuDisplayYear;
                    int FuDisplayMonth = fulook.FuDisplayMonth;


                    // 画面遷移
                    Intent intent = new Intent(getActivity(), Fix.class);

                    intent.putExtra("listId",_id);
                    intent.putExtra("fixDate", fixDate);
                    intent.putExtra("fixItem", fixItem);
                    intent.putExtra("fixMemo", fixMemo);
                    intent.putExtra("fixAmount", fixAmount);

                    intent.putExtra("FuDisplayYear", FuDisplayYear);
                    intent.putExtra("FuDisplayMonth", FuDisplayMonth);
                    startActivity(intent);
                    getActivity().finish();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
