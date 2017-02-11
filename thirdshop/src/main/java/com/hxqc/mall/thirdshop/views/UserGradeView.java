package com.hxqc.mall.thirdshop.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.InfoApiClient;
import com.hxqc.mall.thirdshop.model.newcar.GradeScore;
import com.hxqc.mall.thirdshop.model.newcar.PublicComment;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.adpter.UserGradeAdapter2;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车系车型 口碑评价
 * Created by zhaofan on 2016/11/1.
 */
public class UserGradeView extends LinearLayout {
	private Context context;
	private GridView userGradeGv;  //口碑评分
	private ListView userDiscLv;
	private String brand, series, extID;

	public UserGradeView(Context context) {
		this(context, null);
	}

	public UserGradeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.view_serieslist_usergrade, this);
		init();
	}


	public UserGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	private void init() {
		userGradeGv = (GridView) findViewById(R.id.user_grade_gv);
		userDiscLv = (ListView) findViewById(R.id.user_grade_lv);
		toTotal();
	}


	public void getData(String extID, String brand, String series, final UserGradeCallBack callBack) {
		this.brand = brand;
		this.series = series;
		this.extID = extID;

		new InfoApiClient().getAutoGrade(extID, brand, series, 1, 2, new LoadingAnimResponseHandler(context, true, false) {
			@Override
			public void onSuccess(String response) {
				PublicComment mUserGrade = JSONUtils.fromJson(response, PublicComment.class);
				setUserGrade(mUserGrade);
				if (callBack != null)
					callBack.getUserGrade(mUserGrade);
			}
		});


	}

	/**
	 * 用户评价
	 *
	 * @param mUserGrade
	 */
	private void setUserGrade(PublicComment mUserGrade) {
		if (mUserGrade == null) return;
		((TextView) findViewById(R.id.user_grade_total)).setText(String.format("%d人评价", mUserGrade.count));

		//评分GridView
		GradeScore gradeBean = mUserGrade.grade;
		String[] labels = new String[]{"空间", "动力", "油耗", "舒适性", "外观", "内饰"};
		String[] grades = new String[]{gradeBean.space, gradeBean.power, gradeBean.fuelConsumption,
				gradeBean.comfort, gradeBean.appearance, gradeBean.interiorTrimming};

		List<Map<String, Object>> mList = new ArrayList<>();
		for (int i = 0; i < labels.length; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("label", labels[i]);
			map.put("grade", grades[i] + "分");
			mList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(context, mList,
				R.layout.item_user_grade, new String[]{"label", "grade"},
				new int[]{R.id.label, R.id.grade});
		userGradeGv.setAdapter(adapter);


		//评论列表
		UserGradeAdapter2 mUserGradeAdapter = new UserGradeAdapter2(context, false);
		userDiscLv.setAdapter(mUserGradeAdapter);
		mUserGradeAdapter.setData(mUserGrade.userGradeComment, false);
	}

	public void showPublishButton(final String itemPic) {
		findViewById(R.id.publish_lay).setVisibility(VISIBLE);
		findViewById(R.id.publish_lay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivitySwitcherThirdPartShop.toSendPublicComment((Activity) context, extID, brand, "", itemPic);
			}
		});

	}

	//查看全部
	public void toTotal() {
		findViewById(R.id.to_total).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivitySwitcherThirdPartShop.toPublicCommentList2(context, extID, brand, series);
			}
		});

	}

	public interface UserGradeCallBack {
		void getUserGrade(PublicComment data);
	}

}
