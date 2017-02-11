package com.hxqc.autonews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.widget.InfoImagesItem;
import com.hxqc.autonews.widget.InfoTextItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯列表适配器
 */
public class AutoInfoListAdapter_2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<AutoInformation> mInfoList;
    private static final int TYPE_LIST_TEXT = 20;//普通资讯
    private static final int TYPE_LIST_IMAGES = 30;//图集item

    public AutoInfoListAdapter_2(ArrayList<AutoInformation> mInfoList, Context context) {
        mContext = context;
        this.mInfoList = mInfoList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mInfoList.get(position).getType() == AutoInformation.Type.Images)
            return TYPE_LIST_IMAGES;
        else return TYPE_LIST_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ViewHolderTxt holderTxt = new ViewHolderTxt(LayoutInflater.from(mContext)
//                .inflate(R.layout.item_auto_information_text, null));
//        ViewHolderImages holderImages = new ViewHolderImages(LayoutInflater.from(mContext)
//                .inflate(R.layout.item_auto_information_images, null));

        ViewHolderTxt holderTxt = new ViewHolderTxt(new InfoTextItem(mContext));
        ViewHolderImages holderImages = new ViewHolderImages(new InfoImagesItem(mContext));

        switch (viewType) {
            case TYPE_LIST_IMAGES:
                return holderImages;
            case TYPE_LIST_TEXT:
                return holderTxt;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_LIST_IMAGES:
//                setImagesItem((ViewHolderImages) holder, position);
                ((ViewHolderImages) holder).rootView.addData(mInfoList.get(position));
                break;
            case TYPE_LIST_TEXT:
//                setTextItem((ViewHolderTxt) holder, position);
                ((ViewHolderTxt) holder).rootView.addData(mInfoList.get(position));
                break;
        }
    }

//    private void setTextItem(ViewHolderTxt holder, int index) {
//        ViewHolderTxt holderTxt = holder;
//        final AutoInformation information = mInfoList.get(index);
//        ArrayList<String> thumbImages = information.thumbImage;
//        holderTxt.infoTitle.setText(information.title);
//        holderTxt.infoDate.setText(DateUtil.fullDate2Day(information.date));
////        holderTxt.infoType.setText(information.kind);
//        if (information.tags != null) {
//            setTypeTags(holderTxt.infoType, information.tags);
//        }
//        if (thumbImages != null && thumbImages.size() > 0) {
//            ImageUtil.setImage(mContext, holderTxt.autoIcon, thumbImages.get(0));
//        } else ImageUtil.setImage(mContext, holderTxt.autoIcon, "");
//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                toAutoInfoDetail(information.infoID + "");
//                ToAutoInfoDetailUtil
//                        .onToNextPage(information.infoID,  information.getType(),mContext);
//            }
//        });
//    }

//    private void setImagesItem(ViewHolderImages holder, final int index) {
//        ViewHolderImages holderImages = holder;
//        final AutoInformation information = mInfoList.get(index);
//        ArrayList<String> thumbImages = information.thumbImage;
//
//        if (information.tags != null) {
//            setTypeTags(holderImages.type, information.tags);
//        }
//        holderImages.date.setText(DateUtil.fullDate2Day(information.date));
//        holderImages.title.setText(information.title);
//        if (thumbImages != null && thumbImages.size() > 2) {
//            ImageUtil.setImage(mContext, holderImages.img1, thumbImages.get(0));
//            ImageUtil.setImage(mContext, holderImages.img2, thumbImages.get(1));
//            ImageUtil.setImage(mContext, holderImages.img3, thumbImages.get(2));
//        } else {
//            ImageUtil.setImage(mContext, holderImages.img1, "");
//            ImageUtil.setImage(mContext, holderImages.img2, "");
//            ImageUtil.setImage(mContext, holderImages.img3, "");
//        }
//
//        holderImages.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                toAutoImagesDetail(information.infoID + "", information.title);
//                ToAutoInfoDetailUtil
//                        .onToNextPage(information.infoID,  information.getType(),mContext);
//            }
//        });
//    }


//    /**
//     * 设置标签
//     *
//     * @param textView
//     * @param tags
//     */
//    private void setTypeTags(TextView textView, List<InfoTag> tags) {
//        StringBuilder stringBuilder = new StringBuilder();
//        for (InfoTag tag : tags) {
//            stringBuilder.append(tag.tagName).append(",");
//        }
//        String s = stringBuilder.toString();
//        if (!TextUtils.isEmpty(s)) {
//            s = s.substring(0, s.length() - 1);
//            textView.setText(s);
//        }
//    }


//    public void notifyData(AutoInfoHomeData autoInfoHomeData) {
//        mInfoList.clear();
//        mInfoList.addAll(autoInfoHomeData.infoList);
//        notifyDataSetChanged();
//    }

//    /**
//     * 更新列表数据
//     *
//     * @param autoInformation
//     */
//    public void notifyListData(ArrayList<AutoInformation> autoInformation) {
//        mInfoList.addAll(autoInformation);
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }


//    /**
//     * 去图文详情界面
//     */
//    private void toAutoInfoDetail(String infoID) {
//        ActivitySwitchAutoInformation.toAutoInfoDetail(mContext, infoID);
//    }
//
//    /**
//     * 去图集界面
//     */
//    private void toAutoImagesDetail(String infoID, String title) {
//        ActivitySwitchAutoInformation.toAutoGallery(mContext, infoID);
//    }

    class ViewHolderTxt extends RecyclerView.ViewHolder {
        //        private final TextView gTitle;
        //        private ImageView autoIcon;
        //        private TextView infoTitle;
        //        private TextView infoType;
        //        private TextView infoDate;
        private InfoTextItem rootView;

        public ViewHolderTxt(View itemView) {
            super(itemView);
            rootView = (InfoTextItem) itemView;
//            gTitle = (TextView) itemView.findViewById(R.id.group_title);
//            autoIcon = (ImageView) itemView.findViewById(R.id.auto_icon);
//            infoDate = (TextView) itemView.findViewById(R.id.info_date);
//            infoType = (TextView) itemView.findViewById(R.id.info_type);
//            infoTitle = (TextView) itemView.findViewById(R.id.info_title);
        }
    }

    class ViewHolderImages extends RecyclerView.ViewHolder {
        //        private TextView title;
//        private TextView date;
//        private TextView type;
//        private ImageView img1, img2, img3;
//        private TextView gTitle;
        private InfoImagesItem rootView;

        public ViewHolderImages(View itemView) {
            super(itemView);
            rootView = (InfoImagesItem) itemView;
//            title = (TextView) itemView.findViewById(R.id.item_title);
//            gTitle = (TextView) itemView.findViewById(R.id.group_title);
//            date = (TextView) itemView.findViewById(R.id.item_date);
//            type = (TextView) itemView.findViewById(R.id.item_type);
//            img1 = (ImageView) itemView.findViewById(R.id.img1);
//            img2 = (ImageView) itemView.findViewById(R.id.img2);
//            img3 = (ImageView) itemView.findViewById(R.id.img3);
        }
    }
}
