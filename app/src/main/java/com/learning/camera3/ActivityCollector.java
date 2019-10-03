package com.learning.camera3;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
//活动管理器
public class ActivityCollector {
    public static List<Activity> activities;

    static {
        activities = new ArrayList<>();
    }

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        if(activities.isEmpty()){
            return;
        }
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
