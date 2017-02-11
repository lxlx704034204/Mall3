package com.hxqc.mall.core.db.area;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.core.db.CoreDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created 胡俊杰
 * 2017/1/17.
 * Todo:
 */

@Table(database = CoreDatabase.class)
public class TDistrict extends BaseModel implements AreaModel {

	@PrimaryKey(autoincrement = true)
	@Expose
	public int id;
	@Column
	@Expose
	public String title;
	@Column
	@Expose
	public String cid;
	@Column
	@Expose
	public String did = "-1";

	@Override
	public String getTitle() {
		return title;
	}
}