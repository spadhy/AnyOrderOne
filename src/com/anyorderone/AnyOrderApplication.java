package com.anyorderone;

import android.app.Application;

import com.anyorderone.entities.BusinessInfo;
/**
 * Extend Application for global state information for an application. Access the application via
 * Activity.getApplication().
 * 
 * There are several ways to store global state information, this is one of them. Another is to
 * create a class with static members and just access it from Activities.
 * 
 * Either approach works, and there is debate about which is better. Either way, make sure to clean
 * up in life-cycle pause or destroy methods if you use resources that need cleaning up (static
 * maps, etc).
 * 
 */
public class AnyOrderApplication extends Application {

    private BusinessInfo currentBusinessInfo;
    public AnyOrderApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public BusinessInfo getCurrentBusinessInfo() {
        return this.currentBusinessInfo;
    }

    public void setCurrentBusinessInfo(BusinessInfo currentBusinessInfo) {
        this.currentBusinessInfo = currentBusinessInfo;
    }


}
