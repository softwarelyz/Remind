package com.gdnf.lazy.tips.dao;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdnf.lazy.tips.R;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/12/20.
 */

public class MyBaseAdapter extends BaseAdapter {

        private List<Map<String,Object>> mapList;
        private Context context;

        public MyBaseAdapter(List<Map<String,Object>> mapList,Context context){

            this.context = context;
            this.mapList = mapList;
        }


        @Override
        public int getCount() {
            if(mapList != null)
                return mapList.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if(mapList != null)
                return mapList.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            if(mapList != null)
                return i;
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if( mapList.size() >0 && mapList != null ){
                Intents intents;
                if(view == null){
                    view = View.inflate(context,R.layout.layout_tipslist,null);
                    intents = new Intents();
                    intents.tipsImage = view.findViewById(R.id.tipsImage);
                    intents.tiles = view.findViewById(R.id.tipsTitle);
                    intents.tipsTime = view.findViewById(R.id.tipsTime);

                    view.setTag(intents);

                }else {
                    intents = (Intents) view.getTag();
                }

                Map<String,Object> map = mapList.get(i);
                intents.tipsImage.setImageResource((Integer) map.get("image"));
                intents.tiles.setText(map.get("title").toString());
                intents.tipsTime.setText(map.get("date").toString());

                return  view;

            }

            return null;
        }
    }


final class Intents{
    ImageView tipsImage;
    TextView tiles,tipsTime;
}

