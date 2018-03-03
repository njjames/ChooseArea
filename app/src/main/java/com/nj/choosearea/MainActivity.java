package com.nj.choosearea;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nj.choosearea.db.ChooseAreaOpenHelper;
import com.nj.choosearea.view.ChooseAreaView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mRecentVisitCityNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChooseAreaView chooseAreaView = findViewById(R.id.chooseareaview);
        TextView textView = findViewById(R.id.textview);
        chooseAreaView.setTextView(textView);
        quertRecentVisitCity();
    }

    /**
     * 查询最近访问的前三个城市
     * @return
     */
    private void quertRecentVisitCity() {
        ChooseAreaOpenHelper chooseAreaOpenHelper = new ChooseAreaOpenHelper(this, "dbchoosearea", null, 1);
        SQLiteDatabase database = chooseAreaOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from recentcity order by date desc limit 0,3", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            mRecentVisitCityNameList.add(name);
        }
        cursor.close();
        database.close();
    }
}
