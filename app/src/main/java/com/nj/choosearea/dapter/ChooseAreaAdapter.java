package com.nj.choosearea.dapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nj.choosearea.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-03.
 */

public class ChooseAreaAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 5;
    private List<City> mCityList = new ArrayList<>();
    private Context mContext;

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
        return mCityList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case 0: //定位
                TextView view1 = (TextView) View.inflate(mContext, android.R.layout.simple_list_item_1, null);
                view1.setText("石家庄");
                break;
            case 1: //最近

                break;
            case 2: //热门
                break;
            case 3: //全部
                break;
            case 4: //A-Z
                break;
        }
        return null;
    }
}
