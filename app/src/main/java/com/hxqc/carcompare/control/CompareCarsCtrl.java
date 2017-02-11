package com.hxqc.carcompare.control;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.carcompare.model.comparebasic.CompareGroupParm;
import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.model.comparebasic.CompareParm;
import com.hxqc.carcompare.model.comparedisc.Discuss;
import com.hxqc.carcompare.model.comparenews.AutoNews;
import com.hxqc.carcompare.model.comparenews.NewsEntity;
import com.hxqc.carcompare.ui.compare.CarCompareDetailActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.thirdshop.model.newcar.UserDiscussDetail;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;


/**
 * 车型对比ctrl
 * Created by zhaofan on 2016/10/19.
 */
public class CompareCarsCtrl {
    private static WeakReference<onDeleteListener> WrDListener;
    private static List<String> groupTag_list;
    public static List<CompareNew> label_list; //label_list 总共的label
    private final static String BLANK = "-/-";

    /**
     * 将接口返回的的嵌套list数据转化为datas
     */
    public static List<List<CompareNew>> toNewDatas(List<List<CompareNew>>[] datas, List<CompareParm> original_list) {
        datas[0] = new ArrayList<>();
        groupTag_list = new ArrayList<>();
        for (int i = 0; i < original_list.size(); i++) {
            List<CompareNew> data = new ArrayList<>();
            List<CompareGroupParm> parametersList = original_list.get(i).parameters;
            for (int j = 0; j < parametersList.size(); j++) {
                if (i == 0)
                    groupTag_list.add(parametersList.get(j).getGroupLabel());
                for (int k = 0; k < parametersList.get(j).getChileParameter().size(); k++) {
                    data.add(new CompareNew(parametersList.get(j).getGroupLabel(),
                            parametersList.get(j).getChileParameter().get(k).getLabel(),
                            parametersList.get(j).getChileParameter().get(k).getValue(),
                            parametersList.get(j).getChileParameter().get(k).def
                    ));
                }
            }
            datas[0].add(data);
        }

        return datas[0];
    }

    /**
     * 使各款车型的label数量保持一致，并组成新的new_datas
     */
    public static List<List<CompareNew>> scheduleNewDatas(List<List<CompareNew>>[] datas) {
        label_list = new ArrayList<>();
        for (int k = 0; k < groupTag_list.size(); k++) {
            for (int i = 0; i < datas[0].size(); i++) {
                for (int j = 0; j < datas[0].get(i).size(); j++)
                    if (datas[0].get(i).get(j).getGroupTag().equals(groupTag_list.get(k)))
                        label_list.add(new CompareNew(datas[0].get(i).get(j).getGroupTag(), datas[0].get(i).get(j).getLabel(), "", datas[0].get(i).get(j).def));
            }
        }
        removeDuplicate(label_list);

        List<List<CompareNew>> new_datas = new ArrayList<>();
        for (int i = 0; i < datas[0].size(); i++) {
            List<CompareNew> new_data = new ArrayList<>();
            for (int j = 0; j < label_list.size(); j++) {
                if (j < datas[0].get(i).size()) {
                    if (datas[0].get(i).get(j).getLabel().equals(label_list.get(j).getLabel())) {
                        new_data.add(new CompareNew(label_list.get(j).getGroupTag(), label_list.get(j).getLabel(), datas[0].get(i).get(j).getValue(),
                                label_list.get(j).def));
                    } else {
                        boolean haveSame = false;
                        for (int k = 0; k < datas[0].get(i).size(); k++) {
                            if (datas[0].get(i).get(k).getLabel().equals(label_list.get(j).getLabel())) {
                                haveSame = true;
                                new_data.add(new CompareNew(label_list.get(j).getGroupTag(), label_list.get(j).getLabel(), datas[0].get(i).get(k).getValue(),
                                        label_list.get(j).def));
                                break;
                            }
                        }
                        if (!haveSame)
                            new_data.add(new CompareNew(label_list.get(j).getGroupTag(), label_list.get(j).getLabel(), BLANK, label_list.get(j).def));

                        datas[0].get(i).add(j, new CompareNew(label_list.get(j).getGroupTag(), label_list.get(j).getLabel(), BLANK, label_list.get(j).def));
                    }
                } else
                    new_data.add(new CompareNew(label_list.get(j).getGroupTag(), label_list.get(j).getLabel(), BLANK, label_list.get(j).def));
            }

            new_datas.add(new_data);
            DebugLog.i("", new_data.toString());
        }

        return new_datas;
    }


    /**
     * 去重
     *
     * @param list
     */
    public static void removeDuplicate(List<CompareNew> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getLabel().equals(list.get(i).getLabel())) {
                    list.remove(j);
                }
            }
        }
    }


    /**
     * 隐藏重复的value
     *
     * @param datas0
     * @param original_datas
     * @return
     */
    public static List<List<CompareNew>> hideCommonParam(List<List<CompareNew>> datas0, List<List<CompareNew>> original_datas) {
        int a = 0;
        for (int j = 0; j < datas0.get(0).size(); j++) {
            int count = 0;
            for (int i = 0; i < datas0.size(); i++)
                if (i < datas0.size() - 1)
                    if (datas0.get(0).get(j).getValue().equals(datas0.get(i + 1).get(j).getValue())) {
                        count++;
                        if (count == datas0.size() - 1) {
                            DebugLog.i("equals", j + "");
                            for (List<CompareNew> list : original_datas) {
                                list.remove(j - a);
                            }
                            a++;
                        }
                    }
        }

        return original_datas;
    }


    /**
     * 资讯保留4格
     */
    public static void adjustNewsDatas(List<List<CompareNew>> datas, List<AutoNews> reponse_news) {
        final int REMAIN = 4;
        for (AutoNews i : reponse_news) {
            List<CompareNew> data = new ArrayList<>();
            List<NewsEntity> list = i.getAutoNews();
            for (int j = 0; j < REMAIN; j++) {
                String title = j < list.size() ? list.get(j).title : "";
                String infoID = j < list.size() ? list.get(j).infoID : "";
                data.add(new CompareNew("", "", title, infoID));
            }
            datas.add(data);
        }
    }


    /**
     * 用户评价  保留3条评论
     */
    public static List<List<CompareNew>> adjustDiscussDatas(boolean isAdd, List<Discuss> reponse_dics) {
        final int MAX_CONTENT = 3;
        int max = 0;
        for (Discuss i : reponse_dics) {
            if (max < i.getUserGrade().size())
                max = i.getUserGrade().size();
        }
        int remain = max > MAX_CONTENT ? MAX_CONTENT : max;
        List<List<CompareNew>> datas = new ArrayList<>();
        for (Discuss i : reponse_dics) {
            List<CompareNew> data = new ArrayList<>();
            List<UserDiscussDetail> list = i.getUserGrade();
            for (int j = 0; j < remain; j++) {
                String infoID = j < list.size() ? list.get(j).gradeID : "";
                String content = j < list.size() ? list.get(j).content : "";
                String time = j < list.size() ? list.get(j).time : "";
                String name = j < list.size() ? list.get(j).userInfo.nickName : "";
                data.add(new CompareNew("", infoID, content, name, time));
            }
            //增加一格子 （查看全部评论）
            data.add(new CompareNew("", "0", i.getUserGradeCount(), "", ""));
            datas.add(data);
        }
        return datas;
    }


    /**
     * 显示默认
     */
    public static void showDefault(List<List<CompareNew>> original_datas) {
        for (List<CompareNew> mList : original_datas) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).def == 0) {
                    mList.remove(i);
                    i--;
                }
            }
        }
    }


    /**
     * 已选车型的label
     *
     * @param context
     * @param array
     * @param dListener
     * @param addListener
     * @return
     */

    public static LinearLayout initModelTab(Context context, List<ChooseCarModel> array, onDeleteListener dListener, View.OnClickListener addListener) {
        WrDListener = new WeakReference<>(dListener);
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        // Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        // LayoutAnimationController controller = new LayoutAnimationController(animation);
        // controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        // controller.setDelay(0.3f);
        // layout.setLayoutAnimation(controller);

        int headerCount = array.size();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = ScreenUtil.dip2px(context, 120);
        for (int j = 0; j < headerCount; j++) {
            RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.item_compare_header, null);
            TextView textView = (TextView) view.findViewById(R.id.txt_view);
            textView.setGravity(Gravity.CENTER);
            //textView.setTextColor(getResources().getColor(R.color.text_content_black));
            textView.setText(array.get(j).getModelName());//original_list.get(j).getModel()
            //删除按钮
            ImageView[] delete = new ImageView[headerCount];
            delete[j] = (ImageView) view.findViewById(R.id.delete);
            delete[j].setOnClickListener(new onDeleteClick(j));
            view.setLayoutParams(lp);
            layout.addView(view);
        }
        //添加按钮
        if (array.size() < CarCompareDetailActivity.MAX_MODEL) {
            RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.item_add, null);
            view.setLayoutParams(lp);
            view.setOnClickListener(addListener);
            layout.addView(view);
        }

        return layout;
    }


    private static class onDeleteClick implements View.OnClickListener {
        private int pos;

        public onDeleteClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (WrDListener.get() != null)
                WrDListener.get().onClick(v, pos);
        }
    }

    public interface onDeleteListener {
        void onClick(View v, int j);
    }

}
