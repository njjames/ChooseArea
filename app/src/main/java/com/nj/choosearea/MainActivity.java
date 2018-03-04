package com.nj.choosearea;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<String> mSearchCityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        ChooseAreaView chooseAreaView = findViewById(R.id.chooseareaview);
        TextView textView = findViewById(R.id.textview);
        chooseAreaView.setTextView(textView);
        quertRecentVisitCity();
        final ListView searchListView = findViewById(R.id.search_listview);
        final LinearLayout llContent = findViewById(R.id.ll_content);
        final ListView listView = findViewById(R.id.listview);
        ChooseAreaAdapter chooseAreaAdapter = new ChooseAreaAdapter(this, mRecentVisitCityNameList, mHotCityList, mCityList);
        listView.setAdapter(chooseAreaAdapter);
        chooseAreaView.setOnSlidingListener(new ChooseAreaView.OnSlidingListener() {
            @Override
            public void sliding(String text) {
                if ("定位".equals(text)) {
                    listView.setSelection(0);
                }else if("最近".equals(text)) {
                    listView.setSelection(1);
                }else if("热门".equals(text)) {
                    listView.setSelection(2);
                }else if("全部".equals(text)) {
                    listView.setSelection(3);
                }else {
                    //获取到字母第一次出现的位置，并将显示位置设置为这里
                    int firstPosition = getTheFirstLetterPosition(text);
                    if (firstPosition >= 0) {
                        listView.setSelection(firstPosition + 4);
                    }
                }
            }
        });

        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditText editQueryCity = findViewById(R.id.edit_query_city);
        editQueryCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if (TextUtils.isEmpty(s)) {
                    llContent.setVisibility(View.VISIBLE);
                    searchListView.setVisibility(View.GONE);
                }else {
                    queryCity(s);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.search_city_item, mSearchCityList);
                    searchListView.setAdapter(arrayAdapter);
                    llContent.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);
                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String searchCityName = mSearchCityList.get(i);
                            Toast.makeText(MainActivity.this, searchCityName, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void queryCity(String s) {
        String upperS = s.toUpperCase();
        String lowerS = s.toLowerCase();
        mSearchCityList.clear();
        for (City city : mCityList) {
            if (city.getName().contains(upperS) || city.getPinyin().contains(upperS) || city.getName().contains(lowerS) || city.getPinyin().contains(lowerS)) {
                mSearchCityList.add(city.getName());
            }
        }
    }

    /**
     * 在这里是初始化数据，实际的数据肯定不是这样来的
     */
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
//                insertRecentVisitCity(city.getName());
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

    public void insertRecentVisitCity(String name) {
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
