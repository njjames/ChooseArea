package com.nj.choosearea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nj.choosearea.dapter.ChooseAreaAdapter;
import com.nj.choosearea.db.ChooseAreaOpenHelper;
import com.nj.choosearea.model.City;
import com.nj.choosearea.view.ChooseAreaView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mRecentVisitCityNameList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        ChooseAreaView chooseAreaView = findViewById(R.id.chooseareaview);
        TextView textView = findViewById(R.id.textview);
        chooseAreaView.setTextView(textView);
        quertRecentVisitCity();
        ListView listView = findViewById(R.id.listview);
        ChooseAreaAdapter chooseAreaAdapter = new ChooseAreaAdapter(this, mRecentVisitCityNameList, mCityList);
        listView.setAdapter(chooseAreaAdapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            City city = new City();
            city.setName("城市" + i);
            city.setPinyin("chengshi" + i);
            mCityList.add(city);
            insertRecentVisitCity(city.getName());
        }
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

    private void insertRecentVisitCity(String name) {
        ChooseAreaOpenHelper chooseAreaOpenHelper = new ChooseAreaOpenHelper(this, "dbchoosearea", null, 1);
        SQLiteDatabase database = chooseAreaOpenHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from recentcity where name = '" + name + "'", null);
        if (cursor.getCount() > 0) {
            database.delete("recentcity", "name = ?", new String[]{name});
        }
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", System.currentTimeMillis());
        database.insert("recentcity", null, values);
        database.close();
    }
}
