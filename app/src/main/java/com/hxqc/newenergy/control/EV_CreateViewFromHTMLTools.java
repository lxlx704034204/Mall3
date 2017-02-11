package com.hxqc.newenergy.control;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.hxqc.newenergy.view.HTMLParseLayoutPieceView;
import com.hxqc.util.DebugLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Author:  wh
 * Date:  2016/4/19
 * FIXME  通过html string   生成需要的    view
 * Todo
 */
public class EV_CreateViewFromHTMLTools {

    private String HTMLString = "";
    private LinearLayout parentView;
    private int index = 1;

    public EV_CreateViewFromHTMLTools(String HTMLString, LinearLayout pv) {
        this.HTMLString = HTMLString;
        this.parentView = pv;
        DebugLog.i("html_ev", "EV_CreateViewFromHTMLTools");
        if (!TextUtils.isEmpty(HTMLString) && parentView != null) {
            startParseHTML();
        }
    }

    private void startParseHTML() {
        DebugLog.i("html_ev", "EV_CreateViewFromHTMLTools: " + HTMLString);
        Document parse = Jsoup.parse(HTMLString);
        Elements es = parse.getElementsByTag("td");
        for (int i = 0; i < es.size(); i++) {
            if (i%2==1){
                Element e = es.get(i);
                String td = e.text();
                DebugLog.i("html_ev", "td str: " + td);
                addSubViews(td);
            }
        }
    }

    private void addSubViews(String content){
        HTMLParseLayoutPieceView subView = new HTMLParseLayoutPieceView(parentView.getContext());
        subView.setValues(index+"",content);
        parentView.addView(subView);
        index++;
    }

}
