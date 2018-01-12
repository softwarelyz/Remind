package com.gdnf.lazy.tips.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gdnf.lazy.tips.R;
import com.gdnf.lazy.tips.dao.TipsDao;
import com.gdnf.lazy.tips.entity.Tips;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private LinearLayout tipslistView;
    private LinearLayout toDoEmptyView;
    //private List<Map<String,Object>> mapList;
    private FragmentManager fm;
    private FragmentTransaction tras;

    public MainActivity() throws SQLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoEmptyView = (LinearLayout)findViewById(R.id.toDoEmptyView);
        tipslistView = (LinearLayout)findViewById(R.id.tipslistView);


    }



    @Override
    protected void onStart(){
        super.onStart();

        fm = getFragmentManager();

        tras = fm.beginTransaction();

        tras.replace(R.id.tipslistView,new BlankFragment());
        Therets();

        tras.commit();

        try {
            List<Tips> t = new TipsDao(this).getTips();
            Therets();
            if (t.size()>0) {
                toDoEmptyView.setVisibility(View.INVISIBLE);
            } else {
                toDoEmptyView.setVisibility(View.VISIBLE);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*@Override
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
                Toast.makeText(this,"删除成功！", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,"您已取消删除！",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }*/


    //================通过代码创建选项菜单=================//
    //1:创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("message","Hello");
        menu.add(0,0,0,"设置");
        menu.add(0,2,1,"删除");
        menu.add(0,1,2,"关于");
        return super.onCreateOptionsMenu(menu);
    }
    //2:选项菜单打开之前执行操作
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //菜单打开前进行一些设置
        menu.getItem(0).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }*/
    //3:选项菜单中菜单项被选中
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Intent intent1 = new Intent(this,ReminderActivity.class);
                startActivity(intent1);
                Toast.makeText(this, "你选中了设置", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                //this.finish();//窗体关闭
                //System.exit(0);//系统安全退出
                break;
            case 2:
                try {
                    new TipsDao(this).deleteTips();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                List<Tips> t = null;
                try {
                    t = new TipsDao(this).getTips();
                    onStart();
                    tipslistView.setVisibility(View.VISIBLE);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //点击添加提醒
    public void addTips(View view){
        Intent intent = new Intent(this,AddTipsActivity.class);
        startActivity(intent);
    }


    public void Therets(){

        //SystemClock.sleep((60-times())*1000);

                new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<Tips> tipses = null;
                            try {
                               tipses = new TipsDao(MainActivity.this).getTips();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            for(int i=0;i<tipses.size();i++){

                                if(tipses.get(i).getDate().toString().equals(time().toString())){
                                    show();
                                    return ;
                                }
                            }
                            //show();
                           // Log.i("dsf",tipses.get(0).getDate());
                            Log.i("fdg",time().toString());
                        }
                    });
                    SystemClock.sleep((60-times())*1000);

                }
            }
        }).start();
    }

    //






    //时间格式化
    public String time(){

        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm yyyy-MM-dd");

        return sdf.format(new Date());

    }

    //时间格式化
    public int times() {

        SimpleDateFormat sdf = new SimpleDateFormat("ss");

        return Integer.parseInt(sdf.format(new Date()));

    }

    public void show(){
        SharedPreferences preferences = getSharedPreferences("lock",MODE_PRIVATE);
        String title = preferences.getString("title", null);
        NotificationManager manager;
        //获取NotifactionManager对象
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //构建一个Notifaction的build的对象
        Notification.Builder builder = new Notification.Builder(this);
        //设置通知相关信息
        builder.setTicker("新的通知");//设置信息提示
        builder.setSmallIcon(R.drawable.biaoqian);//设置通知提示图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.biaoqian));
        builder.setContentTitle("小提醒通知");
        builder.setContentText("提醒内容："+title+"  "+"提醒时间："+time());
        builder.setAutoCancel(true);//查看后自动取消
        //builder.setWhen(SystemClock.currentThreadTimeMillis());//什么时候发出的通知
        builder.setDefaults(Notification.DEFAULT_ALL);//消息提示模式

        //设置震动规律
        builder.setVibrate(new long[]{1000,2000,1000,3000});//停止1秒震动2秒
        //设置声音
        //builder.setSound();
        //设置灯
        builder.setLights(Color.GREEN,1000,1000);

        //设置点击通知后执行的操作
        Intent intent = new Intent(this,ReminderActivity.class);
        intent.putExtra("message","今天中午12点在教室开会，请大家准时到场,通知发布时间为："+time());
        int requested = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,requested,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //发布通知
        manager.notify(requested,builder.build());
    }

}
