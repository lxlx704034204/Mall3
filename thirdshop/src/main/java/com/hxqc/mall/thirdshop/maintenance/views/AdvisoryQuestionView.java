package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.advisory.Question;
import com.hxqc.mall.thirdshop.R;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo 提问
 */
public class AdvisoryQuestionView extends LinearLayout implements View.OnClickListener {
    private final TextView nameAndContent;
//    private TextView name;
//    private TextView autoModel;
//    private TextView content;
    private TextView time;


    public AdvisoryQuestionView(Context context) {
        this(context, null);
    }

    public AdvisoryQuestionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvisoryQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_advisory_question, this);
//        name = (TextView) findViewById(R.id.name);
//        autoModel = (TextView) findViewById(R.id.auto_model);
//        content = (TextView) findViewById(R.id.content);
        time = (TextView) findViewById(R.id.time);


        nameAndContent = (TextView) findViewById(R.id.name_and_content);

//        autoModel.setOnClickListener(this);
    }

    public void addData(Question question) {
        if (question != null) {
//            name.setText(question.QUser);
//            autoModel.setText(question.QAutoName);
//            content.setText(question.QContent);
            time.setText(question.QTime);

            nameAndContent.setText(question.QUser + "：" + question.QContent);
        }
    }

    public void toAutoDetail(View view) {
        //点击车辆的名字
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto_model) {
            toAutoDetail(v);
        }
    }
}
