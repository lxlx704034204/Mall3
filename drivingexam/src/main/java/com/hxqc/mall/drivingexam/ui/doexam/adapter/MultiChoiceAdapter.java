package com.hxqc.mall.drivingexam.ui.doexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.drivingexam.model.Options;


/**
 * 多选adapter
 * Created by zhaofan
 */
public class MultiChoiceAdapter extends BaseAdapter<Options, BaseAdapter.BaseViewHolder> {
    private String mAnswerStr;
    boolean isShow = false;
    public int index;
    ExamRecord data;
    private ListView lv;
    String choose[], answer[];

    public MultiChoiceAdapter(Context context) {
        super(context);
    }

    public void setAnswer(String str) {
        mAnswerStr = str;
    }

    public void setIndex(int index) {
        this.index = index;
      /*  data = DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(index));
        if (data != null) {
            choose = data.getChoose().split(",");
            answer = data.getAnswer().split(",");
        }*/
    }

    public void setListView(ListView lv) {
        this.lv = lv;
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driving, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, final View convertView, final int position, final Options entity) {
        final ViewHolder viewHodler = (ViewHolder) holder;
        viewHodler.option.setText(entity.content);

        if (position == 0) {
            viewHodler.num.setText("A");
        } else if (position == 1) {
            viewHodler.num.setText("B");
        } else if (position == 2) {
            viewHodler.num.setText("C");
        } else if (position == 3) {
            viewHodler.num.setText("D");
        }

        if (isShow) {
            if (entity.choose.equals("1")) {
                viewHodler.status.setVisibility(View.VISIBLE);
                viewHodler.status.setImageResource(R.drawable.yes_3);
            } else
                viewHodler.status.setVisibility(View.GONE);
        }

        data = DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(index));
        if (data.isFinish.equals("1")) {
            convertView.setClickable(false);
            choose = data.getChoose().split(",");
            answer = data.getAnswer().split(",");
            if (choose[position].equals("1")) {
                if (answer[position].equals("1")) {
                    viewHodler.status.setVisibility(View.VISIBLE);
                    viewHodler.status.setImageResource(R.drawable.yes_3);
                } else {
                    viewHodler.status.setVisibility(View.VISIBLE);
                    viewHodler.status.setImageResource(R.drawable.no);
                }
                viewHodler.lay.setBackgroundResource(R.drawable.btn_click_circle);
                viewHodler.num.setTextColor(0xff333333);
            } else if (answer[position].equals("1") && !choose[position].equals("1")) {
                viewHodler.status.setVisibility(View.GONE);
                viewHodler.lay.setBackgroundResource(R.drawable.yes);
                viewHodler.num.setTextColor(0xffffffff);
            } else {
                viewHodler.status.setVisibility(View.GONE);
                viewHodler.lay.setBackgroundResource(R.drawable.btn_click_circle);
                viewHodler.num.setTextColor(0xff333333);
            }


        } else {
            convertView.setClickable(true);
            //点击事件
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ToastUtils.show("position: " + position);
                    lv.setItemChecked(position, !lv.isItemChecked(position));
                    notifyDataSetChanged();

                }
            });

            viewHodler.lay.setBackgroundResource(lv.isItemChecked(position) ?
                    R.drawable.bg_circle_press : R.drawable.btn_click_circle);
            viewHodler.num.setTextColor(lv.isItemChecked(position) ?
                    0xffffffff : 0xff333333);
        }

    }


    private class ViewHolder extends BaseViewHolder {
        //店铺
        private TextView option, num;
        private FrameLayout lay;
        private ImageView status;

        public ViewHolder(View itemView) {
            super(itemView);

            option = getView(R.id.option);
            num = getView(R.id.num);
            status = getView(R.id.status);
            lay = getView(R.id.lay1);
        }
    }

}
