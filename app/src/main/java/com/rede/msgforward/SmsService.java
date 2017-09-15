package com.rede.msgforward;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author zhengxh
 * @version 1.0, 2017/9/15 10:52
 */

public class SmsService extends Service {
    private String TAG = "SmsService";
    private String action = "smsservice.alarm";
    private final static int TIMER_TYPE = 22339;
    private final static long offsetTimer = 5000;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "======onCreate====");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.e(TAG, "======onStartCommand====action:"+action);
        if(this.action.equals(action)){
            cleanAlarmTask(TIMER_TYPE);
        }
        if(!SharedPrefUtil.get().getBooleanConfig("isOpen", false)){
            stopSelf();
        }
        return START_STICKY;
    }

    /**
     * 清除定时任务
     * @param typeTag 类型
     */
    private void cleanAlarmTask(int typeTag){
        Intent intent= new Intent(getApplication(), SmsService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplication(),typeTag,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }

    private void startAlarm(){
        Intent intent = new Intent(getApplication(), SmsService.class);
        intent.setAction("action");
        PendingIntent pendingIntent = PendingIntent.getService(getApplication(),TIMER_TYPE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+offsetTimer, pendingIntent);
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "======onDestroy====");
        boolean isOpne = SharedPrefUtil.get().getBooleanConfig("isOpen", false);
        if(isOpne)startAlarm();
        super.onDestroy();
    }
}
