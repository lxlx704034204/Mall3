package com.hxqc.mall.core.views;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Created by CPR113 on 2016/12/21.
 * liaoguilong
 * 2016年12月21日 11:13:11
 */

public class AgreementView extends TextView {

    public void setAgreementClickListener(AgreementClickListener agreementClickListener) {
        this.agreementClickListener = agreementClickListener;
    }

    private AgreementClickListener agreementClickListener;

    public AgreementView(Context context) {
        super(context);
    }

    public AgreementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(LinkMovementMethod.getInstance());
        String xieyi_text=getResources().getString(R.string.me_codelogin_xieyi_text);
        String xieyi=getResources().getString(R.string.me_codelogin_xieyi);
        String agreementText=xieyi_text+xieyi;
        setText(getText(agreementText, xieyi_text.length(), agreementText.length(), "#2196f3"));
    }

    public SpannableStringBuilder getText(String text, int start, int end, final String color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if(agreementClickListener !=null)
                  agreementClickListener.onClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor(color));
            }
        }, start, end, 0);
        return style;
    }

    public interface AgreementClickListener{
         void onClick();
    }

}
