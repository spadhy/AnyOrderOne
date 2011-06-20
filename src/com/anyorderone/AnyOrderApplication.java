package com.anyorderone;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.anyorderone.entities.BusinessInfo;
import com.anyorderone.entities.ItemInfo;
import com.anyorderone.entities.OrderItem;
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
    private ItemInfo currentItemInfo;    
	private ArrayList<OrderItem> orderItems;
	private Drawable image;
	@SuppressWarnings("rawtypes")
	private HashMap staticHMap;    
    
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
    public ItemInfo getCurrentItemInfo() {
        return this.currentItemInfo;
    }

    public void setCurrentItemInfo(ItemInfo currentItemInfo) {
        this.currentItemInfo = currentItemInfo;
    }

	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(ArrayList<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * @return the orderItems
	 */
	public ArrayList<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * @param staticHMap the staticHMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setStaticHMap(HashMap staticHMap) {
		this.staticHMap = staticHMap;
	}

	/**
	 * @return the staticHMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getStaticHMap() {
		return staticHMap;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Drawable image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	public Drawable getImage() {
		return image;
	}

}
