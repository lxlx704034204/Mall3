package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.advisory.Answer;
import com.hxqc.mall.thirdshop.R;

/**
 * Author:李烽
 * Date:2016-05-11
 * FIXME
 * Todo  答复
 */
public class AdvisoryAnswerView extends LinearLayout {
    private final TextView nameAndContent;
    private TextView name;
    private TextView content;
    private TextView time;


    public AdvisoryAnswerView(Context context) {
        this(context, null);
    }

    public AdvisoryAnswerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvisoryAnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_advisory_answer, this);
        name = (TextView) findViewById(R.id.name);
        content = (TextView) findViewById(R.id.content);
        time = (TextView) findViewById(R.id.time);


        nameAndContent = (TextView) findViewById(R.id.name_and_content);
    }

    public void addData(Answer answer) {
        if (answer != null) {
            name.setText(answer.AUser);
            content.setText(answer.AContent);
            time.setText(answer.ATime);

            nameAndContent.setText(answer.AUser + "：" + answer.AContent);
        }

    }
}
