package com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.model.LightQuestion;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.drivingexam.utils.ResourceUtils;

import java.util.List;

/**
 * 灯光问题列表adapter
 * Created by zhaofan on 2016/8/23.
 */
public class LightQuestionAdapter extends BaseAdapter<LightQuestion, BaseAdapter.BaseViewHolder> {
    private GridView gv;


    public LightQuestionAdapter(Context context) {
        super(context);
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_light_question, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, LightQuestion data) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.title.setText((position + 1) + "、" + data.getQuestion());
        vh.answer.setText(data.getAnswer());
        vh.answer_lay.setVisibility(TextUtils.isEmpty(data.getAnswer()) ? View.GONE : View.VISIBLE);
        vh.detail.setText(data.getDetail());

        List<String> images = data.getImage();
        if (images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                vh.img[i].setVisibility(View.VISIBLE);
                int resId = ResourceUtils.getResourceId(context, images.get(i), "raw");
                String url = "android.resource://" + context.getPackageName() + "/" + resId;
                Uri uri = Uri.parse(url);
//                ImageUtil.build(context, uri).into(vh.img[i]);
                ImageUtil.setImage(context, vh.img[i], url);
                vh.img[i].setOnClickListener(new MyOnClickListener(url));

                //   ImageUtil.setImageNormalSize(context, vh.img[i], uri);
            }
        } else {
            vh.img[0].setVisibility(View.GONE);
            vh.img[1].setVisibility(View.GONE);
        }
    }


    class MyOnClickListener implements View.OnClickListener {
        String url;

        public MyOnClickListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            ActivitySwitcherExam.toPhotoView(context, new String[]{url}, 0);

        }
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private class ViewHolder extends BaseViewHolder {
        private TextView title, answer, detail;
        private ImageView[] img = new ImageView[2];
        private RelativeLayout answer_lay;

        public ViewHolder(View itemView) {
            super(itemView);
            img[0] = getView(R.id.img1);
            img[1] = getView(R.id.img2);
            title = getView(R.id.title);
            answer = getView(R.id.answer);
            detail = getView(R.id.detail);
            answer_lay = getView(R.id.answer_lay);
        }
    }
}