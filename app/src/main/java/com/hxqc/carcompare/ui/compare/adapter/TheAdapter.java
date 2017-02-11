package com.hxqc.carcompare.ui.compare.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.ui.view.CHScrollView;
import com.hxqc.carcompare.ui.compare.CarCompareDetailActivity;
import com.hxqc.carcompare.ui.popuwindow.ChooseModePopuwindow;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.sticklistviewbyzf.StickyListHeadersAdapter;
import com.hxqc.mall.core.views.sticklistviewbyzf.StickyListHeadersListView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;


/**
 * 车型杜比Adapter
 * Created by zhaofan on 2016/10/9.
 */
public class TheAdapter extends BaseAdapter implements StickyListHeadersAdapter,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {
    public static final String CHOOSE_MODE = "choose_mode";
    private boolean isShowStickHeader = true;
    private StickyListHeadersListView mListView;
    private Context context;
    private List<CompareNew> data = new ArrayList<>();
    private List<List<CompareNew>> datas = new ArrayList<>();
    private List<CHScrollView> mHScrollViews = new ArrayList<>();
    private boolean isDelete = false;
    private boolean isMove = false;
    private int scrollX_1;
    private int mode = 0;
    LinearLayout.LayoutParams lp;
    private int delPos;

    public TheAdapter(Context context, List<CHScrollView> mHScrollViews, StickyListHeadersListView mListView) {
        this.context = context;
        this.mHScrollViews = mHScrollViews;
        this.mListView = mListView;
    }

   /* public void setContent(List<List<PinpaiAfter>> datas) {
        this.datas.clear();
        this.datas = datas;
    }
*/

    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }


    public void setData(List<List<CompareNew>> dataSource) {
        //    this.datas.clear();
        try {
            this.datas = new ArrayList<>(dataSource);
            this.data = new ArrayList<>(datas.get(0));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    /**
     * @param isClear true刷新 false加载
     */
    public void setData(List<CompareNew> data, boolean isClear) {
        if (isClear) {
            this.data.clear();
            this.data = data;
        } else {
            addDate(data);
        }
    }

    // 拼接list
    public void addDate(List<CompareNew> list) {
        for (int i = 0; i < list.size(); i++) {
            this.data.add(list.get(i));
        }
    }


    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHodler viewHodler = convertView == null ? new ViewHodler() : (ViewHodler) convertView.getTag();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_compare, parent, false);
            viewHodler.EmptyView = convertView.findViewById(R.id.empty);
            viewHodler.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHodler.scrollView = (CHScrollView) convertView.findViewById(R.id.item_scroll);
            addHViews(viewHodler.scrollView);

            viewHodler.layout = (LinearLayout) convertView.findViewById(R.id.lay);

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.width = ScreenUtil.dip2px(context, 120);
            //   lp.height = ScreenUtil.dip2px(context, 80);

            viewHodler.textView = new TextView[datas.size()];
            viewHodler.lay1 = new LinearLayout[datas.size()];
            viewHodler.lay2 = new RelativeLayout[datas.size()];
            viewHodler.lay3 = new RelativeLayout[datas.size()];
            viewHodler.btn = new TextView[datas.size()];
            viewHodler.pl_name = new TextView[datas.size()];
            viewHodler.pl_date = new TextView[datas.size()];
            viewHodler.all_pinglun = new TextView[datas.size()];
            viewHodler.content = new TextView[datas.size()];
            viewHodler.detail = new TextView[datas.size()];
            initColumn(viewHodler, datas.size());
            // viewHodler.scrollView.addView( viewHodler.layout);
            convertView.setTag(viewHodler);
        }

        if (isDelete) {
            viewHodler.layout.removeAllViews();
            DebugLog.i("size()1dd", delPos + "");
            initColumn(viewHodler, delPos);
        }

        //让listview的HScrollView跟着头部车型菜单的HScrollView移动
        CHScrollView scrollView = mHScrollViews.get(0);
        final int scrollX = scrollView.getScrollX();
        DebugLog.i("bbb", "  " + scrollX);
        mListView.post(new Runnable() {
            @Override
            public void run() {
                viewHodler.scrollView.scrollTo(scrollX, 0);
            }
        });

        final CompareNew entity = (CompareNew) getItem(position);
        try {
            //设置label
            String label = entity.getLabel();
            if (label.contains("(") && label.contains(")")) {
                String a = label.substring(0, label.indexOf("("));
                String b = label.substring(label.indexOf("("), label.length());
                viewHodler.title.setText(a + "\n" + b);
            } else
                viewHodler.title.setText(mode == 3 ? "" : label);

            for (int j = 0; j < datas.size(); j++) {
                if (mode != 3) {
                    viewHodler.lay1[j].setVisibility(View.VISIBLE);
                    viewHodler.lay2[j].setVisibility(View.GONE);
                    viewHodler.lay3[j].setVisibility(View.GONE);
                    viewHodler.textView[j].setGravity(Gravity.CENTER);
                    viewHodler.textView[j].setTextColor(mode == 2 ? context.getResources().getColor(R.color.text_color_orange) : Color.parseColor("#666666"));
                    viewHodler.textView[j].setGravity(mode == 1 ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.CENTER);
                    //  viewHodler.textView[j].setText(datas.get(j).get(position).getValue());

                    String value;
                    if (entity.getLabel().contains("参考价") || entity.getLabel().contains("指导价")) {
                        value = OtherUtil.formatPriceSingleOrRange(datas.get(j).get(position).getValue());
                    } else
                        value = datas.get(j).get(position).getValue();
                    viewHodler.textView[j].setText(value);
                   /* if (position == 0) {
                        viewHodler.btn[j].setVisibility(entity.getLabel().contains("参考价") ? View.VISIBLE : View.GONE);
                    } else
                        viewHodler.btn[j].setVisibility(View.GONE);*/
                }
                //评论对比 mode==3
                else {
                    viewHodler.lay1[j].setVisibility(View.GONE);
                    if (datas.get(j).get(position).getLabel().equals("0")) {
                        viewHodler.lay3[j].setVisibility(View.VISIBLE);
                        viewHodler.lay2[j].setVisibility(View.GONE);
                    } else {
                        viewHodler.lay3[j].setVisibility(View.GONE);
                        viewHodler.lay2[j].setVisibility(View.VISIBLE);
                    }
                    viewHodler.pl_name[j].setText(datas.get(j).get(position).userName);
                    String date = datas.get(j).get(position).publishDate;
                    viewHodler.pl_date[j].setText(TextUtils.isEmpty(date) ? "" : date.split(" ")[0] + "发表");
                    viewHodler.content[j].setText(datas.get(j).get(position).value);
                    viewHodler.all_pinglun[j].setText("查看评论\n(" + datas.get(j).get(datas.get(j).size() - 1).value + "人评论)");
                    viewHodler.detail[j].setVisibility(TextUtils.isEmpty(datas.get(j).get(position).value) ? View.GONE : View.VISIBLE);
                    //    viewHodler.all_pinglun[j] = (TextView) viewHodler.view.findViewById(R.id.all_pinglun);
                }

                //bindListener
                //   viewHodler.btn[j].setOnClickListener(new MyOnclickListener(j, position));
                viewHodler.lay1[j].setOnClickListener(new MyOnclickListener(j, position));
                viewHodler.detail[j].setOnClickListener(new MyOnclickListener(j, position));
                viewHodler.all_pinglun[j].setOnClickListener(new MyOnclickListener(j, position));
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }

        viewHodler.EmptyView.setVisibility(datas.size() < CarCompareDetailActivity.MAX_MODEL ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private void initColumn(ViewHodler viewHodler, int size) {
        try {
            for (int j = 0; j < size; j++) {
                viewHodler.view = (RelativeLayout) View.inflate(context, R.layout.item_compare_child, null);
                viewHodler.btn[j] = (TextView) viewHodler.view.findViewById(R.id.btn_view);
                viewHodler.textView[j] = (TextView) viewHodler.view.findViewById(R.id.txt_view);
                viewHodler.lay1[j] = (LinearLayout) viewHodler.view.findViewById(R.id.lay1);
                viewHodler.lay2[j] = (RelativeLayout) viewHodler.view.findViewById(R.id.lay2);
                viewHodler.lay3[j] = (RelativeLayout) viewHodler.view.findViewById(R.id.lay3);
                //评论
                viewHodler.pl_name[j] = (TextView) viewHodler.view.findViewById(R.id.name);
                viewHodler.pl_date[j] = (TextView) viewHodler.view.findViewById(R.id.date);
                viewHodler.all_pinglun[j] = (TextView) viewHodler.view.findViewById(R.id.all_pinglun);
                viewHodler.detail[j] = (TextView) viewHodler.view.findViewById(R.id.detail);
                viewHodler.content[j] = (TextView) viewHodler.view.findViewById(R.id.content);
                // 动态设置高度
                android.view.ViewGroup.LayoutParams pp = viewHodler.lay1[j].getLayoutParams();
                pp.height = ScreenUtil.dip2px(context, 60);
                android.view.ViewGroup.LayoutParams pp2 = viewHodler.lay2[j].getLayoutParams();
                pp2.height = ScreenUtil.dip2px(context, 148);
                viewHodler.lay1[j].setLayoutParams(pp);
                viewHodler.lay2[j].setLayoutParams(pp2);
                viewHodler.view.setLayoutParams(lp);
                viewHodler.layout.addView(viewHodler.view);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void resetPosition(boolean isMove, int scrollX) {
        this.isMove = isMove;
        this.scrollX_1 = scrollX;
        //  notifyDataSetChanged();
    }

    public void resetPosition(boolean isMove) {
        this.isMove = isMove;
    }


    public void delete(boolean isDelete, int pos) {
        this.isDelete = isDelete;
        this.delPos = pos;
    }


  /*  public void setLeftAlign(boolean b) {
        this.isLeft = b;
    }*/

    public void setMode(int i) {
        this.mode = i;
    }

    public void ShowStickHeader(boolean b) {
        this.isShowStickHeader = b;
    }

    public void setHScrollView(List<CHScrollView> mHScrollViews) {
        this.mHScrollViews = mHScrollViews;
    }


    private class MyOnclickListener implements View.OnClickListener {
        private int position, j;

        public MyOnclickListener(int j, int position) {
            this.j = j;
            this.position = position;

        }

        @Override
        public void onClick(View v) {
            if (mode == 1) {
                String newsID = datas.get(j).get(position).newsID;
                //跳转资讯
                if (!TextUtils.isEmpty(newsID)) {
                    //  ToastUtils.show(context, "资讯id：" + newsID);
                    //  EventBus.getDefault().post(new Event(new Position(j, position), CarCompareDetailActivity.TO_NEWS));
                    ActivitySwitchAutoInformation.toAutoInfoDetail(context, newsID);
                }

            } else if (mode == 3) {
                String discID = datas.get(j).get(position).label;
                //跳转评论详情   评论id： discID
                if (!discID.equals("0")) {
                    EventBus.getDefault().post(new Event(new Position(j, position), CarCompareDetailActivity.SHOW_DISC_DETAIL));
                }
                //跳转全部评论
                else {
                    EventBus.getDefault().post(new Event(new Position(j, position), CarCompareDetailActivity.ALL_DISCUSS));
                }
            }
        }
    }

    public static class Position {
        public int parantPos;
        public int childPos;

        public Position(int parantPos, int childPos) {
            this.parantPos = parantPos;
            this.childPos = childPos;
        }
    }


    public void addHViews(final CHScrollView hScrollView) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView scrollView = mHScrollViews.get(size - 1);
            int a = 0;
            a++;
            final int scrollX = isMove ? scrollX_1 : scrollView.getScrollX();
            DebugLog.i("aaa", a + "  " + scrollX);
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                        //   isDelete = false;
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }


    class ViewHodler {
        private TextView title;
        private TextView[] textView, btn, detail;
        private TextView[] pl_name, pl_date, all_pinglun, content;
        private CHScrollView scrollView;
        private RelativeLayout view;
        private LinearLayout layout;
        private View EmptyView;
        private LinearLayout[] lay1;
        private RelativeLayout[] lay2, lay3;

    }


    public View getHeaderView(final int position, View convertView, ViewGroup parent) {
        final HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.header_carcompare_group, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.main = (RelativeLayout) convertView.findViewById(R.id.main);
            holder.lay = (RelativeLayout) convertView.findViewById(R.id.lay);
            holder.selection = (RelativeLayout) convertView.findViewById(R.id.selection);
            holder.modeText = (TextView) convertView.findViewById(R.id.mode);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }


        android.view.ViewGroup.LayoutParams pp = holder.main.getLayoutParams();
        if (isShowStickHeader) {
            pp.height = ScreenUtil.dip2px(context, 38);
            holder.main.setLayoutParams(pp);
            holder.lay.setVisibility(View.VISIBLE);
        } else {
            pp.height = ScreenUtil.dip2px(context, 0);
            holder.main.setLayoutParams(pp);
            holder.lay.setVisibility(View.GONE);
        }

        if (position < data.size()) {
            holder.text.setText(data.get(position).getGroupTag());
        }

        // if (!show) {
        if (position == pos_tag) {
            holder.selection.setVisibility(View.VISIBLE);
            DebugLog.i("onStickyHeaderChanged", "POS123:" + "  " + position);
        } else
            holder.selection.setVisibility(View.GONE);

        DebugLog.i("onStickyHeaderChanged", "POS:" + "  " + position);

        holder.modeText.setText(CarCompareDetailActivity.chooseMode);

        holder.selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseModePopuwindow mPopuwindow = new ChooseModePopuwindow((Activity) context, holder.modeText.getText().toString(), holder.checkbox);
                mPopuwindow.showAsDropDown(holder.selection);
                mPopuwindow.setCallBack(new ChooseModePopuwindow.OnCallBackkListener() {
                    @Override
                    public void onCallBack(int index) {
                        if (index == 0)
                            CarCompareDetailActivity.chooseMode = CarCompareDetailActivity.DEFAULT_LABEL;
                        else if (index == 1)
                            CarCompareDetailActivity.chooseMode = CarCompareDetailActivity.DIF_LABEL;
                        else
                            CarCompareDetailActivity.chooseMode = CarCompareDetailActivity.ALL_LABEL;
                        holder.modeText.setText(CarCompareDetailActivity.chooseMode);
                        //     mListView.setSelection(0);
                        EventBus.getDefault().post(new Event(CarCompareDetailActivity.chooseMode, CHOOSE_MODE));
                    }
                });
            }
        });
        return convertView;
    }

    private int pos_tag = 0;

    public void chooseModeButtonIndex(int pos_tag) {
        if (pos_tag == 0) {
            this.pos_tag = pos_tag;
            return;
        }
        pos_tag = mListView.getFirstVisiblePosition();
        if (pos_tag > data.size()) {
            String groupTag = data.get(data.size() - 1).getGroupTag();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getGroupTag().equals(groupTag)) {
                    pos_tag = i;
                    break;
                }
            }
        }
        DebugLog.i("onStickyHeaderChanged", "showModeIndex:   " + pos_tag);
        this.pos_tag = pos_tag;
    }


    public String getHeaderId(int position) {
        if (position > data.size() - 1) {
            return "";
        }

        return data.get(position).getGroupTag();

    }

    class HeaderViewHolder {
        TextView text;
        RelativeLayout main, lay;
        RelativeLayout selection;
        TextView modeText;
        CheckBox checkbox;
    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, String headerId) {
        RelativeLayout selection = (RelativeLayout) header.findViewById(R.id.selection);
        selection.setVisibility(View.VISIBLE);
        DebugLog.i("onStickyHeaderChanged", itemPosition + "  " + headerId);

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        DebugLog.i("onStickyHeaderChanged", offset + "  ");
        //  final RelativeLayout selection = (RelativeLayout) header.findViewById(R.id.selection);
        //   selection.setVisibility(View.VISIBLE);

    }

    public void bindListener() {
        mListView.setOnStickyHeaderChangedListener(this);
        mListView.setOnStickyHeaderOffsetChangedListener(this);
    }


    public Object[] getSections() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPositionForSection(int section) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


}
