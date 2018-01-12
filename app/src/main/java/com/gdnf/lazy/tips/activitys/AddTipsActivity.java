package com.gdnf.lazy.tips.activitys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gdnf.lazy.tips.R;
import com.gdnf.lazy.tips.dao.TipsDao;
import com.gdnf.lazy.tips.entity.Tips;

import java.sql.SQLException;
import java.util.Calendar;

public class AddTipsActivity extends Activity {

    //开关
    private Switch switchs;
    //标题内容
    private EditText userToDoEditText;
    //日期
    private EditText newTodoDateEditText;
    //时间
    private EditText newTodoTimeEditText;
    //选择日期时间的
    private LinearLayout toDoEnterDateLinearLayout;
    //显示日期时间的
    private TextView newToDoDateTimeReminderTextView;

    //定义全局日期时间变量
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tips);
        switchs = (Switch)findViewById(R.id.switchs);
        newTodoDateEditText = (EditText)findViewById(R.id.newTodoDateEditText);
        newTodoTimeEditText = (EditText)findViewById(R.id.newTodoTimeEditText);
        newToDoDateTimeReminderTextView = (TextView)findViewById(R.id.newToDoDateTimeReminderTextView);
        userToDoEditText = (EditText)findViewById(R.id.userToDoEditText);

        //获取日期模块的id
        toDoEnterDateLinearLayout = (LinearLayout)findViewById(R.id.toDoEnterDateLinearLayout);

        //获取选定的日期模块，默认设置为隐藏
        toDoEnterDateLinearLayout.setVisibility(View.INVISIBLE);

        switchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //判断是否打开开关，如果没打开就隐藏
                if (!b){
                    toDoEnterDateLinearLayout.setVisibility(View.INVISIBLE);
                }else{
                    toDoEnterDateLinearLayout.setVisibility(View.VISIBLE);
                    defaultDate();
                  //  getDate();
                }
            }
        });

    }

    //默认日期
    final public void defaultDate(){
        //创建一个日期对象
        Calendar cal = Calendar.getInstance();//当前日期
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        newTodoDateEditText.setText(year+"-"+(month+1)+"-"+date);
        newTodoTimeEditText.setText(hour+":"+minute);

        String temp = this.getString(R.string.remind_date_and_time);
        String subString = String.format(temp,(year+"-"+(month+1)+"-"+date),(hour+":"+minute));
        newToDoDateTimeReminderTextView.setText(subString);
    }

    //占位符获取
    public void getDate(){
        String subString=null;
        String temp = this.getString(R.string.remind_date_and_time);
        boolean is1=date!=null?true:false;
        boolean is2=time!=null?true:false;
        if(is1 && is2){
            subString = String.format(temp,date,time);
        }else if(is1){
            subString = String.format(temp,date,newTodoTimeEditText.getText().toString());
        }else if(is2){
            subString = String.format(temp,newTodoDateEditText.getText().toString(),time);
        }
        newToDoDateTimeReminderTextView.setText(subString);
    }

    //添加日期
    public void addDate(View view){
        //创建一个日期对象
        Calendar cal = Calendar.getInstance();//当前日期
        //2个月后今天的日期
        //cal.add(Calendar.MONTH,2);
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i+"-"+(i1+1)+"-"+i2;
                newTodoDateEditText.setText(date);
                newTodoDateEditText.setSelection(newTodoDateEditText.getText().length());
                getDate();
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        dialog.show();
    }

    //添加时间
    public void addTime(View view){
        //构建一个日历对象
        Calendar cal=Calendar.getInstance();//当前日期
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time = i+":"+i1;
                newTodoTimeEditText.setText(time);
                getDate();
            }
        },cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),true);
        dialog.show();
    }

    //点击确认获取值
    public void Determine(View view) throws SQLException {

        String title = userToDoEditText.getText().toString();
        SharedPreferences preferences = getSharedPreferences("lock",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("title",title);
        editor.commit();
        String date = newTodoTimeEditText.getText().toString();
        String time = newTodoDateEditText.getText().toString();

        Toast.makeText(this,title +","+date+" "+time, Toast.LENGTH_SHORT).show();
        //SharedPreferences shp=getSharedPreferences("lock",MODE_PRIVATE);
        new TipsDao(this).addTips(new Tips(0,title,date+" "+time));

        this.finish();
    }


    //关闭
    public void  close(View view){
        this.finish();
    }
}
