package com.nj.choosearea.dapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nj.choosearea.MainActivity;
import com.nj.choosearea.R;
import com.nj.choosearea.model.City;
import com.nj.choosearea.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-03.
 */

public class ChooseAreaAdapter extends BaseAdapter {
    private static final String TAG = "ChooseAreaAdapter";
    private static final int VIEW_TYPE_COUNT = 5;
    private Context mContext;
    private List<String> mRecentVisitCityNameList = new ArrayList<>();
    private List<String> mHotCityNameList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();

    public ChooseAreaAdapter(Context context, List<String> recentVisitCityNameList, List<String> hotCityNameList, List<City> cityList) {
        mContext = context;
        mRecentVisitCityNameList = recentVisitCityNameList;
        mHotCityNameList = hotCityNameList;
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
        return mCityList.size() + 4;
    }

    @Override
    public City getItem(int i) {
        if(i < 4) {
            return null;
        }else {
            return mCityList.get(i - 4);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case 0: //定位
                convertView = View.inflate(mContext, R.layout.current_city, null);
                break;
            case 1: //最近
                convertView = View.inflate(mContext, R.layout.recent_visitorhot_city, null);
                MyGridView recentVisitCityGridview = convertView.findViewById(R.id.recent_visit_city_gridview);
                TextView recentTitle = convertView.findViewById(R.id.recentorhot_title);
                recentTitle.setText("最近访问的城市");
                RecentVisitCityAdapter recentVisitCityAdapter = new RecentVisitCityAdapter(mContext, mRecentVisitCityNameList);
                recentVisitCityGridview.setAdapter(recentVisitCityAdapter);
                break;
            case 2: //热门（其实和最近一样，只是数据不同）
                convertView = View.inflate(mContext, R.layout.recent_visitorhot_city, null);
                MyGridView hotCityGridview = convertView.findViewById(R.id.recent_visit_city_gridview);
                TextView hotTitle = convertView.findViewById(R.id.recentorhot_title);
                hotTitle.setText("热门城市");
                RecentVisitCityAdapter hotCityAdapter = new RecentVisitCityAdapter(mContext, mHotCityNameList);
                hotCityGridview.setAdapter(hotCityAdapter);
                break;
            case 3: //全部
                convertView = View.inflate(mContext, R.layout.all_city_textview, null);
                break;
            case 4: //A-Z
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.city_list_item, null);
                    TextView cityFirstLetter = convertView.findViewById(R.id.city_firt_letter);
                    TextView cityName = convertView.findViewById(R.id.city_name);
                    LinearLayout llMain = convertView.findViewById(R.id.llmain);
                    viewHolder.cityFirstLetter = cityFirstLetter;
                    viewHolder.cityName = cityName;
                    viewHolder.llMain = llMain;
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                final City item = getItem(i);
                viewHolder.cityName.setText(item.getName());
                viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, item.getName(), Toast.LENGTH_SHORT).show();
                        MainActivity mainActivity = (MainActivity) mContext;
                        mainActivity.insertRecentVisitCity(item.getName());
                    }
                });
                //如果不是List的第一条，就先查出上一条的拼音
                if (i == 4) {
                    viewHolder.cityFirstLetter.setText(getItem(i).getPinyin().substring(0, 1));
                    viewHolder.cityFirstLetter.setVisibility(View.VISIBLE);
                }
                else if (i > 4) {
                    String prevPinyin = getItem(i - 1).getPinyin();
                    boolean isEqual = isEqualOfTheFirstLetter(getItem(i).getPinyin(), prevPinyin);
                    if (isEqual) {
                        viewHolder.cityFirstLetter.setVisibility(View.GONE);
                    }else {
                        viewHolder.cityFirstLetter.setText(getItem(i).getPinyin().substring(0, 1));
                        viewHolder.cityFirstLetter.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
        return convertView;
    }

    /**
     * 判断当前拼音的首字母是否和上一个拼音的首字母相等
     * @param pinyin
     * @param prevPinyin
     * @return
     */
    private boolean isEqualOfTheFirstLetter(String pinyin, String prevPinyin) {
        if (!TextUtils.isEmpty(pinyin) && !TextUtils.isEmpty(prevPinyin)) {
            return pinyin.substring(0, 1).equals(prevPinyin.substring(0, 1));
        }
        return false;
    }

    public static class ViewHolder {
        public TextView cityFirstLetter;
        public TextView cityName;
        public LinearLayout llMain;
    }
}
