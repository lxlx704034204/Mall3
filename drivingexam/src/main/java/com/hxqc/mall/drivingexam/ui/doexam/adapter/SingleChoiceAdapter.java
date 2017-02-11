package com.hxqc.mall.drivingexam.ui.doexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.drivingexam.model.Options;
import com.hxqc.util.DebugLog;


/**
 * 单选adapter
 * Created by zhaofan
 */
public class SingleChoiceAdapter extends BaseAdapter<Options, BaseAdapter.BaseViewHolder> {
    private String answer;
    boolean isShow = false;
    public int index;
    ExamRecord data;
    private String id;
    private View.OnClickListener listner = null;

    public SingleChoiceAdapter(Context context) {
        super(context);
    }

    public void setAnswer(String str) {
        answer = str;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void isFinish(int index) {
        this.index = index;
    }


    public void setQuestionId(String id) {
        this.id = id;
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driving, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, final View convertView, final int position, final Options entity) {
        final ViewHolder viewHodler = (ViewHolder) holder;
       /* if (!TextUtils.isEmpty(entity.content)) {
            viewHodler.option.setText(entity.content);
            convertView.setVisibility(View.VISIBLE);
        } else
            convertView.setVisibility(View.GONE);*/
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
            int choose = Integer.parseInt(data.getChoose());
            int answer = Integer.parseInt(data.getAnswer());
            //选择正确
            if (choose == answer) {
                if (position == choose) {
                    setGougou(viewHodler.status, viewHodler.lay, viewHodler.num);
                } else {
                    clear(viewHodler.status, viewHodler.lay, viewHodler.num);
                }
            }
            //选择错误
            else {
                if (position == choose) {
                    setChacha(viewHodler.status, viewHodler.lay, viewHodler.num);
                } else if (position == answer) {
                    setRight(viewHodler.status, viewHodler.lay, viewHodler.num);
                } else
                    clear(viewHodler.status, viewHodler.lay, viewHodler.num);
            }
        } else {
            convertView.setClickable(true);
            //点击事件
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    convertView.setClickable(false);
                    //    ToastUtils.show(entity.choose);
                    int boolRight;
                    if (entity.choose.equals("1")) {
                        boolRight = 1;
                        setGougou(viewHodler.status, viewHodler.lay, viewHodler.num);
                    } else {
                        boolRight = 0;
                        setChacha(viewHodler.status, viewHodler.lay, viewHodler.num);
                        isShow = true;
                    }
                    DebugLog.i("index", index + "");
                    if (DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(index)).isFinish.equals("0")) {
                        upDataRecord(position + "", boolRight + "");
                    }
                    notifyDataSetChanged();

                    if (listner != null) {
                        listner.onClick(v);
                    }


                }
            });
        }

    }

    public void upDataRecord(String choose, String boolRight) {
        DbHelper.delete(ExamRecord.class, ExamRecord_Table.num.eq(index));
        new ExamRecord(index, "1", choose, answer, boolRight).save();
    }


    private void setRight(View status, View lay, TextView num) {
        status.setVisibility(View.GONE);
        lay.setBackgroundResource(R.drawable.yes);
        num.setTextColor(0xffffffff);
    }

    private void setGougou(ImageView status, View lay, TextView num) {
        status.setVisibility(View.VISIBLE);
        status.setImageResource(R.drawable.yes_3);
        lay.setBackgroundResource(R.drawable.btn_click_circle);
        num.setTextColor(0xff333333);
    }

    private void setChacha(ImageView status, View lay, TextView num) {
        status.setVisibility(View.VISIBLE);
        status.setImageResource(R.drawable.no);
        lay.setBackgroundResource(R.drawable.btn_click_circle);
        num.setTextColor(0xff333333);
    }


    private void clear(ImageView status, View lay, TextView num) {
        status.setVisibility(View.GONE);
        lay.setBackgroundResource(R.drawable.btn_click_circle);
        num.setTextColor(0xff333333);
    }

    public void setOnClickListener(View.OnClickListener listner) {
        this.listner = listner;
    }


    private class ViewHolder extends BaseViewHolder {
        //店铺
        private TextView option, num;
        private RelativeLayout main;
        private ImageView status;
        private FrameLayout lay;

        public ViewHolder(View itemView) {
            super(itemView);

            option = getView(R.id.option);
            num = getView(R.id.num);
            status = getView(R.id.status);
            lay = getView(R.id.lay1);
        }
    }

}
