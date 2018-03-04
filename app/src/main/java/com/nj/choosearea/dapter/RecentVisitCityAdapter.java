package com.nj.choosearea.dapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nj.choosearea.MainActivity;
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
            convertView= View.inflate(mContext, R.layout.recent_visitorhot_city_item, null);
        }
        TextView recentVisitCityName = convertView.findViewById(R.id.recent_visit_city_name);
        final String cityName = mRecentVisitCityNameList.get(i);
        recentVisitCityName.setText(cityName);
        recentVisitCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, cityName, Toast.LENGTH_SHORT).show();
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.insertRecentVisitCity(cityName);
            }
        });
        return convertView;
    }
}
