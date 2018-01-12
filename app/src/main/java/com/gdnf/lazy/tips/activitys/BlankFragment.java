package com.gdnf.lazy.tips.activitys;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gdnf.lazy.tips.R;
import com.gdnf.lazy.tips.dao.MyBaseAdapter;
import com.gdnf.lazy.tips.dao.TipsDao;
import com.gdnf.lazy.tips.entity.Tips;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlankFragment extends Fragment{

    private ListView list_item;
    private LinearLayout toDoEmptyView;
    private MyBaseAdapter adapter;
    private List<Map<String,Object>> mapList;

    public BlankFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        list_item = (ListView) view.findViewById(R.id.list_item);

        list_item.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("操作提示");
                menu.add(1,0,1,"删除");
                menu.add(1,1,2,"分享");
                menu.add(1,2,3,"取消");
            }
        });

        loadData();

        adapter = new MyBaseAdapter(mapList,getActivity());

        int i=mapList.size();
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("lock",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("i",i);
        editor.commit();
        list_item.setAdapter(adapter);


        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int id = (int) info.id;

        if (-1 == id) {
            super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case 0:
                mapList.remove(id);
                adapter.notifyDataSetChanged();
                Log.i("delete","删除成功");
                onStart();
                break;
        }
        return super.onContextItemSelected(item);
    }


    public void loadData(){
        mapList = new ArrayList<Map<String,Object>>();

        List<Tips> tips = null;
        try {
            tips = new TipsDao(getActivity()).getTips();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i =0 ;i<tips.size();i++ ){

            Map<String,Object>map = new HashMap<String,Object>();

            map.put("title",tips.get(i).getTitle());
            map.put("image",R.drawable.biaoqian);
            map.put("date",tips.get(i).getDate());

            mapList.add(map);

        }
    }

}
