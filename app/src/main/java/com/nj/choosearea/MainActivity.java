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
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<String> mRecentVisitCityNameList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();
    private List<String> mHotCityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        ChooseAreaView chooseAreaView = findViewById(R.id.chooseareaview);
        TextView textView = findViewById(R.id.textview);
        chooseAreaView.setTextView(textView);
        quertRecentVisitCity();
        final ListView listView = findViewById(R.id.listview);
        ChooseAreaAdapter chooseAreaAdapter = new ChooseAreaAdapter(this, mRecentVisitCityNameList, mHotCityList, mCityList);
        listView.setAdapter(chooseAreaAdapter);
        chooseAreaView.setOnSlidingListener(new ChooseAreaView.OnSlidingListener() {
            @Override
            public void sliding(String text) {
                //获取到字母第一次出现的位置，并将显示位置设置为这里
                int firstPosition = getTheFirstLetterPosition(text);
                if (firstPosition >= 0) {
                    listView.setSelection(firstPosition + 4);
                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 26; i++) {
            Random random = new Random();
            int count = random.nextInt(10) + 1;
            char c = (char) ('A' + i);
            for (int i1 = 0; i1 < count; i1++) {
                City city = new City();
                city.setName(c + "城市" + i1);
                city.setPinyin(c + "chengshi" + i1);
                mCityList.add(city);
                if (i1 == 0 && i <= 4) {
                    mHotCityList.add(city.getName());
                }
                insertRecentVisitCity(city.getName());
            }

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

    public int getTheFirstLetterPosition(String text) {
        for (int i = 0; i < mCityList.size(); i++) {
            if (mCityList.get(i).getPinyin().startsWith(text)) {
                return i;
            }
        }
        return -1;
    }
}
