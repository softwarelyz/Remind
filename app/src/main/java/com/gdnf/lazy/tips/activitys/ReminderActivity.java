package com.gdnf.lazy.tips.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.gdnf.lazy.tips.R;

public class ReminderActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        text = (TextView)findViewById(R.id.text);

        text.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("操作提示");
                menu.add(0,0,3,"删除");
                menu.add(0,1,4,"分享");
                menu.add(0,1,5,"取消");
            }
        });
    }
}
