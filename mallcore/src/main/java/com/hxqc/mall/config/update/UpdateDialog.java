package com.hxqc.mall.config.update;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Created 胡俊杰
 * 2016/9/23.
 * Todo:
 */

public class UpdateDialog extends Dialog {

	VersionPo versionPo;

	protected UpdateDialog(Context context) {
		super(context);
	}

	protected UpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 * @param versionPo
	 * @param isForceUpdate 是否强制更新
	 */
	public UpdateDialog(Context context, VersionPo versionPo, boolean isForceUpdate) {
		super(context, R.style.submitDIalog);
		initDialog(versionPo, isForceUpdate);
	}

	public UpdateDialog(Context context, VersionPo versionPo) {
		super(context, R.style.submitDIalog);
		initDialog(versionPo, false);
	}

	private void initDialog(VersionPo versionPo, boolean isForceUpdate) {
		setContentView(R.layout.umeng_update_dialog);
		setCancelable(false);
		this.versionPo = versionPo;
		Button mIgnoreView = (Button) findViewById(R.id.umeng_update_id_ignore);
		Button mCancelView = (Button) findViewById(R.id.umeng_update_id_cancel);
		Button mOKView = (Button) findViewById(R.id.umeng_update_id_ok);
		if (isForceUpdate) {
			mIgnoreView.setVisibility(View.GONE);
		}
		mIgnoreView.setOnClickListener(new IgnoreClick());
		mCancelView.setOnClickListener(new CancelClick());
		mOKView.setOnClickListener(new OKClick());
		TextView mContentView = (TextView) findViewById(R.id.umeng_update_content);
		mContentView.setText(versionPo.description);
	}

	private class IgnoreClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			new UpdateSP(getContext()).saveIgnoreVersionName(versionPo.versionNum);
			dismiss();
		}
	}

	private class CancelClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	}

	private class OKClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			dismiss();
			ActivitySwitchBase.toWebBrowser(versionPo.url, getContext());
//			if (!((Activity) getContext()).isFinishing()) {
//				((Activity) getContext()).finish();
//				System.exit(0);
//			}
		}
	}
}
