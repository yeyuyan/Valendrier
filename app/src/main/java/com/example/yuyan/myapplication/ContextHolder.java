package com.example.yuyan.myapplication;

import android.content.Context;

/**
 * Created by yuyan on 4/26/18.
 */

public class ContextHolder {
    static Context ApplicationContext;
    public static void initial(Context context) {
        ApplicationContext = context;
    }
    public static Context getContext() {
        return ApplicationContext;
    }
}