package com.nj.choosearea.dapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nj.choosearea.R;
import com.nj.choosearea.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-03.
 */

public class ChooseAreaAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 5;
    private Context mContext;
    private List<String> mRecentVisitCityNameList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();

    public ChooseAreaAdapter(Context context, List<String> recentVisitCityNameList, List<City> cityList) {
        mContext = context;
        mRecentVisitCityNameList = recentVisitCityNameList;
        mCityList = cityList;
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        //position是ListView中的每项的位置，前4个是不一样的（0,1,2,3），后面都一样
        return position < 4 ? position : 4;
    }

    @Override
    public int getCount() {
        //由于前四个都是只有一个item，所以我们只用这个函数获取最后一个值
        return mCityList.size() + 4;
    }

    @Override
    public City getItem(int i) {
        return mCityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case 0: //定位
//                TextView view1 = (TextView) View.inflate(mContext, android.R.layout.simple_list_item_1, null);
//                view1.setText("石家庄");
                convertView = View.inflate(mContext, R.layout.listview_item, null);
                TextView textItem1 = convertView.findViewById(R.id.text_item);
                textItem1.setText("this is item1");
                break;
            case 1: //最近
                convertView = View.inflate(mContext, R.layout.recent_visit_city, null);
                GridView recentVisitCityGridview = convertView.findViewById(R.id.recent_visit_city_gridview);
                RecentVisitCityAdapter recentVisitCityAdapter = new RecentVisitCityAdapter(mContext, mRecentVisitCityNameList);
                recentVisitCityGridview.setAdapter(recentVisitCityAdapter);
                break;
            case 2: //热门
                convertView = View.inflate(mContext, R.layout.listview_item, null);
                TextView textItem3 = convertView.findViewById(R.id.text_item);
                textItem3.setText("this is item3");
                break;
            case 3: //全部
                convertView = View.inflate(mContext, R.layout.all_city_textview, null);
                break;
            case 4: //A-Z
                convertView = View.inflate(mContext, R.layout.listview_item, null);
                TextView textItem5 = convertView.findViewById(R.id.text_item);
                textItem5.setText("this is item5");
                break;
        }
        return convertView;
    }
}
