package com.rede.msgforward;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

/**
 * @author zhengxh
 * @version 1.0, 2017/9/15 09:33
 */

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = "SmsReceiver";
    private String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";
    private String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, ">>>>>onReceive>>>>action:"+action);
        if(SMS_DELIVER_ACTION.equals(action)||SMS_RECEIVED_ACTION.equals(action)){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                StringBuilder sb = new StringBuilder();
                String receivePhone = null;
                for (Object p : pdusObj) {
                    SmsMessage message= SmsMessage.createFromPdu((byte[]) p);
                    String content = message.getMessageBody();// 得到短信内容
                    String sender = message.getOriginatingAddress();// 得到发信息的号码

                    Log.e(TAG, ">>>>>onReceive>>>>sender:"+sender+"; content:"+content);
                    if(sender!=null) {
                        if (sender.startsWith("\\+86")) {
                            sender = sender.replaceAll("\\+86", "");
                        }
                        String phone = SharedPrefUtil.get().getStringConfig("phone", "");
                        if (sender.equals(phone)) {
                            String[] split = content.split("#:>");
                            if(split!=null&&split.length>0){
                                int len = split.length;
                                for (int i=0;i<len;i++){
                                    if(i==len-1){
                                        receivePhone = split[i];
                                    }else{
                                        sb.append(split[i]);
                                    }
                                }
                            }else{
                                sb.append(content);
                            }
                        } else {
                            if (receivePhone == null)receivePhone = SharedPrefUtil.get().getStringConfig("phone", "");
                            sb.append(content).append("[from:").append(sender).append("]");
                        }
                    }

                }
                forwardSms(receivePhone, sb.toString());
            }

        }else if(BOOT_COMPLETED_ACTION.equals(action)){
            context.startService(new Intent(context, SmsService.class));
        }
    }

    public void forwardSms(String receivePhone, String message){//转发短信
        if(receivePhone==null||receivePhone.length()<3||message==null)return;
        SmsManager manager = SmsManager.getDefault();
        /** 切分短信，每七十个汉字切一个，短信长度限制不足七十就只有一个：返回的是字符串的List集合*/
        List<String> texts =manager.divideMessage(message);//这个必须有
        for(String text:texts){
            manager.sendTextMessage(receivePhone, null, text, null, null);
        }
    }
}
