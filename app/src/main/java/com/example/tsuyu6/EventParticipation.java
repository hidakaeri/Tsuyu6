package com.example.tsuyu6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventParticipation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_participation);

        // 参加ボタンの取得
        Button participationClick = findViewById(R.id.participation);
        // 参加ボタンのリスナクラスのインスタンスを作成
        ParticipationClickListener participation_listener = new ParticipationClickListener();
        // 参加ボタンにリスナを設定
        participationClick.setOnClickListener(participation_listener);
    }

    // 参加ボタンを押した場合の処理
    private class ParticipationClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {


            Intent intent = new Intent(EventParticipation.this, Event.class);
            startActivity(intent);
            finish();

        }
    }
}