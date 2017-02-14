package com.onightperson.hearken.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.WeakHashMap;


/**
 * Created by liubaozhu on 17/1/28.
 */

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    private static WeakHashMap<String, WeakReference<BaseActivity>> mInstanceList
            = new WeakHashMap<>();
    private String mActivityName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前Activity的名字

        BaseActivity instance = getInstance();
        if (instance != null) {
            this.finish();
        }

        //创建一个实例的弱引用
        synchronized (mInstanceList) {
            mInstanceList.put(mActivityName, new WeakReference<>(this));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseActivity instance = getInstance();
        if (instance == this) {
            synchronized (mInstanceList) {
                mInstanceList.remove(mActivityName);
            }
        }
    }

    private BaseActivity getInstance() {
        //get the name of current activity
        if (mActivityName == null) {
            mActivityName = this.getClass().getName();
        }

        WeakReference<BaseActivity> weakInstance = mInstanceList.get(mActivityName);
        if (weakInstance != null) {
            return weakInstance.get();
        }

        return null;
    }

    public static void finishAll() {
        Collection<WeakReference<BaseActivity>> weakInstanceList = mInstanceList.values();
        Log.d(TAG, "finishAll--mInstanceList's size: " + mInstanceList.size());
        for (WeakReference<BaseActivity> weakInstance : weakInstanceList) {
            if (weakInstance != null) {
                BaseActivity instance = weakInstance.get();
                instance.finish();
            }
        }
    }

}
