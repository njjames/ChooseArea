package com.nj.choosearea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nj.choosearea.view.ChooseAreaView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChooseAreaView chooseAreaView = findViewById(R.id.chooseareaview);
        TextView textView = findViewById(R.id.textview);
        chooseAreaView.setTextView(textView);
    }
}
