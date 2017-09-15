package com.rede.msgforward;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private TextView defPhoneText;
    private EditText editPhoneEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_set_permiss).setOnClickListener(this);
        findViewById(R.id.btn_set_phone).setOnClickListener(this);
        defPhoneText = (TextView) findViewById(R.id.def_phone);
        editPhoneEdit = (EditText) findViewById(R.id.edit_phone);


        String phone = SharedPrefUtil.get().getStringConfig("phone", "");
        defPhoneText.setText(phone);
        boolean isOpne = SharedPrefUtil.get().getBooleanConfig("isOpen", false);
        Switch openStatus = (Switch) findViewById(R.id.open_status);
        openStatus.setChecked(isOpne);
        if(isOpne){
            startService(new Intent(getApplication(), SmsService.class));
        }
        openStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPrefUtil.get().setConfig("isOpen", isChecked);
                Intent intent = new Intent(getApplication(), SmsService.class);
                if (isChecked){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });
    }

    private void setPermission(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set_permiss:
                setPermission();
                break;
            case R.id.btn_set_phone:
                String newPhone = editPhoneEdit.getText().toString();
                SharedPrefUtil.get().setConfig("phone", newPhone);
                defPhoneText.setText(newPhone==null?"":newPhone);
                break;
        }
    }
}
