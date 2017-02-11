package com.hxqc.mall.auto.view.swipemenulistview;


import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuItem {

	private int id;
	private Context mContext;
	private String title;
	private Drawable icon;
	private Drawable background;
	private int titleColor;
	private int titleSize;
	private int width;

	public SwipeMenuItem(Context context) {
		mContext = context;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public int getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(int resId) {
		setTitle(mContext.getString(resId));
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public void setIcon(int resId) {
		this.icon = mContext.getResources().getDrawable(resId);
	}

	public Drawable getBackground() {
		return background;
	}

	public void setBackground(Drawable background) {
		this.background = background;
	}

	public void setBackground(int resId) {
		this.background = mContext.getResources().getDrawable(resId);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	public static void createSwipeMenuItem(SwipeMenu menu, Context context, int backgroundId, int width, String title, int titleSize, int titleColor, int resId) {
		// create "open" item
		SwipeMenuItem defaultItem = new SwipeMenuItem(
				context);
		// set item background
		defaultItem.setBackground(backgroundId);
		// set item width
		defaultItem.setWidth(width);
		// set item title
		defaultItem.setTitle(title);
		// set item title fontsize
		defaultItem.setTitleSize(titleSize);
		// set item title font color
		defaultItem.setTitleColor(titleColor);
		//set icon
		if (resId != 0)
			defaultItem.setIcon(resId);
		// add to menu
		menu.addMenuItem(defaultItem);
	}
}
