package com.nj.choosearea.dapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nj.choosearea.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nj on 2018/3/3.
 */

public class RecentVisitCityAdapter extends BaseAdapter {
    private List<String> mRecentVisitCityNameList = new ArrayList<>();
    private Context mContext;

    public RecentVisitCityAdapter(Context context, List<String> recentVisitCityNameList) {
        mContext = context;
        mRecentVisitCityNameList = recentVisitCityNameList;
    }

    @Override
    public int getCount() {
        return mRecentVisitCityNameList.size();
    }

    @Override
    public String getItem(int i) {
        return mRecentVisitCityNameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView= View.inflate(mContext, R.layout.recent_visit_city_item, null);
        }
        TextView recentVisitCityName = convertView.findViewById(R.id.recent_visit_city_name);
        recentVisitCityName.setText(mRecentVisitCityNameList.get(i));
        return convertView;
    }
}
