package jp.astra.cameramap.helper;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;

/**
 * @category Dictionary
 * @author Kelvzs
 * @updated Kat
 */
public final class Dictionary implements Serializable {

	private static final long serialVersionUID = -7088094205454415132L;
	private HashMap<String, Object> map;

    public Dictionary() {
        this.map = new HashMap<String, Object>();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    public int size() {
        return this.map.keySet().toArray().length;
    }

    public boolean isEmpty() {
        return (this.size() == 0);
    }

    public String[] getKeys() {

        Object[] data = this.map.keySet().toArray();
        String[] keys = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            keys[i] = (String)data[i];
        }

        return keys;
    }

    public void setData(String key, Object data) {
        this.map.put(key, data);
    }

    public void setData(Context context, int id, Object data) {
        this.setData(context.getString(id), data);
    }

    public void setData(Context context, int id, int dataId) {
        this.setData(context.getString(id), context.getString(dataId));
    }

    public void setDataInt(String key, int data) {
        this.setData(key, String.valueOf(data));
    }

    public void setDataInt(Context context, int id, int data) {
    	this.setDataInt(context.getString(id), data);
    }
    
    public void setDataFloat(String key, float data) {
        this.setData(key, String.valueOf(data));
    }

    public void setDataFloat(Context context, int id, float data) {
    	this.setDataFloat(context.getString(id), data);
    }
    
    public void setDataLong(String key, long data) {
        this.setData(key, String.valueOf(data));
    }

    public void setDataLong(Context context, int id, long data) {
    	this.setDataLong(context.getString(id), data);
    }

    public void setDataBoolean(String key, boolean data) {

    	String string = "false";

    	if (data) {
    		string = "true";
    	}

    	this.setData(key, string);
    }

    public void setDataBoolean(Context context, int id, boolean data) {
        this.setDataBoolean(context.getString(id), data);
    }

    public Object getObject(String key) {
        return this.map.get(key);
    }

    public Object getObject(Context context, int id) {
        return this.getObject(context.getString(id));
    }

    public String getString(String key) {

        String string = (String)this.getObject(key);

        if (StringUtil.isEmpty(string)) {
            return "";
        } else {
            return string;
        }
    }

    public String getString(Context context, int id) {
        return this.getString(context.getString(id));
    }

    public int getInt(String key) {

        String string = this.getString(key);

        if (StringUtil.isEmpty(string)) {
            return 0;
        } else {
            return Integer.parseInt(string);
        }
    }

    public int getInt(Context context, int id) {
        return this.getInt(context.getString(id));
    }

    public long getLong(String key) {

        String string = this.getString(key);

        if (StringUtil.isEmpty(string)) {
            return 0;
        } else {
            return Long.parseLong(string);
        }
    }

    public long getLong(Context context, int id) {
        return this.getLong(context.getString(id));
    }

    public boolean getBoolean(String key) {

    	String string = this.getString(key);

    	if (StringUtil.isEqual(string, "true", false) || StringUtil.isEqual(string, "yes", false)) {
			return true;
		} else {
			return false;
		}
    }

    public boolean getBoolean(Context context, int id) {
    	return this.getBoolean(context.getString(id));
    }

    public Dictionary getDictionary(String key) {
        return (Dictionary)this.map.get(key);
    }

    public Dictionary[] getDictionaryList(String key) {

        Object[] data = (Object[])this.map.get(key);

        if (data == null) {
        	return null;
        }

        Dictionary[] listData = new Dictionary[data.length];

        for (int i = 0; i < data.length; i++) {
        	listData[i] = (Dictionary)data[i];
        }

        return listData;
    }
}
