package com.rede.msgforward;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 * 可扩展的SharedPreferences, configKey值可用枚举方式自定义防止名字重名
 */
public class SharedPrefUtil {
	private static SharedPrefUtil instance;
	/**
	 * 配置文件名
	 */
	public static String CONFIG_NAME = "common_share";

	public void init(Context context){
		share = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
	}

	private SharedPreferences share;

	
	public static SharedPrefUtil get() {
		if(instance == null){
			synchronized (SharedPrefUtil.class) {
				if(instance == null){
					instance = new SharedPrefUtil();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 设置值
	 * @param configKey 设置配置文件的key
	 * @param configValue 设置配置文件的value, 只能是这几种类型:{@link Integer},{@link String},{@link Boolean},{@link Float}或{@link Long}
	 */
	public void setConfig(String configKey, Object configValue) {
		SharedPreferences.Editor shareEdit = share.edit();
		if (configValue instanceof Integer) {
			shareEdit.putInt(configKey, (Integer) configValue);
        } else if (configValue instanceof String) {
        	shareEdit.putString(configKey, (String) configValue);
        } else if (configValue instanceof Boolean) {
        	shareEdit.putBoolean(configKey, (Boolean) configValue);
        } else if (configValue instanceof Float) {
        	shareEdit.putFloat(configKey, (Float) configValue);
        } else if (configValue instanceof Long) {
        	shareEdit.putLong(configKey, (Long) configValue);
        }
		if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD){
			shareEdit.commit();
		} else{
			shareEdit.apply();
		}
    }

	/**
	 * 移除配置文件中对应的key
	 * @param configKey
	 */
	public void removeConfig(String configKey) {
    	try {
    		share.edit().remove(configKey);
    		share.edit().commit();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	/**
	 * 获取int值的配置信息
	 * @param configKey
	 * @param defValue 如果未获取到返回该默认值
	 * @return
	 */
    public int getIntConfig(String configKey, int defValue) {
        return share.getInt(configKey, defValue);
    }
    /**
	 * 获取boolean值的配置信息
	 * @param configKey
	 * @param defValue 如果未获取到返回该默认值
	 * @return
	 */
    public boolean getBooleanConfig(String configKey, boolean defValue) {
        return share.getBoolean(configKey, defValue);
    }
    /**
	 * 获取long值的配置信息
	 * @param configKey 
	 * @param defValue 如果未获取到返回该默认值
	 * @return
	 */
    public long getLongConfig(String configKey, Long defValue) {
        return share.getLong(configKey, defValue);
    }
    /**
	 * 获取Float值的配置信息
	 * @param configKey key
	 * @param defValue 如果未获取到返回该默认值
	 * @return
	 */
    public Float getFloatConfig(String configKey, Float defValue) {
    	return share.getFloat(configKey, defValue);
    }
    /**
	 * 获取String值的配置信息
	 * @param configKey key
	 * @param defValue 如果未获取到返回该默认值
	 * @return
	 */
    public String getStringConfig(String configKey, String defValue) {
        return share.getString(configKey, defValue);
    }
    
}
