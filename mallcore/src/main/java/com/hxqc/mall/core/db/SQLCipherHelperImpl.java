package com.hxqc.mall.core.db;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.dbflow.android.sqlcipher.SQLCipherOpenHelper;

/**
 * Created 胡俊杰
 * 2017/1/16.
 * Todo:
 */

public class SQLCipherHelperImpl extends SQLCipherOpenHelper {

	public SQLCipherHelperImpl(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
		super(databaseDefinition, listener);
	}

	@Override
	protected String getCipherSecret() {
		return "hxqc-rules";
	}
}