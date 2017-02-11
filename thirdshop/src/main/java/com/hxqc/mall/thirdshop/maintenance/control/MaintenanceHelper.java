//package com.hxqc.mall.thirdshop.maintenance.control;
//
//import android.content.Context;
//import android.widget.LinearLayout;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.CheckPackage;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReturnPackage;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadItem;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadObject;
//import com.hxqc.mall.thirdshop.maintenance.views.MaintainAndRepairParent;
//import com.hxqc.util.JSONUtils;
//
//import cz.msebera.android.httpclient.Header;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-04-11
// * FIXME
// * Todo 保养方案，维修保养，确认订单 帮助类
// */
//public class MaintenanceHelper  {
//    private static MaintenanceHelper instance;
//    private static MaintenanceClient client;
//
//    //店铺ID
//    private String shopID;
//    //车辆ID
//    private String autoModelID;
//
//    //保养方案中的所有项目
//    private ArrayList<MaintenanceItem> maintenanceItems;
//    //用于准备订单上传的参数
//    private UploadObject uploadObject = new UploadObject();
//    //用于存放套餐中的item
//    private ArrayList<UploadItem> packageItmes = new ArrayList<>();
//    //维修保养中的所有项目
//    private ArrayList<MaintenanceItem> repairMaintenanceItems;
//    //从维修保养返回给保养的项目
//    private ArrayList<MaintenanceItem> retrunMaintenanceItems = new ArrayList<>();
//
//    private MaintenanceHelper(){
//        if (client == null)
//            client = new MaintenanceClient();
//    }
//
//    public static  MaintenanceHelper getInstance(){
//        if(instance == null)
//            synchronized (MaintenanceHelper.class){
//            if(instance == null){
//                instance = new MaintenanceHelper();
//            }
//        }
//        return instance;
//    }
//
//    /**
//     * 销毁
//     */
//    public void destroy() {
//        if (instance != null)
//            instance = null;
//    }
//
//    /**
//     * 获取套餐保养项目
//     * @param context
//     * @param shopID
//     * @param packageID
//     */
//    public void getRecommendProgram(Context context,String shopID, final String packageID, final boolean isPackage,String autoModelID ,final recommendProgramHandle recommendProgramHandle){
//        this.shopID = shopID;
//        this.autoModelID = autoModelID;
//        client.recommendProgram(shopID, packageID, autoModelID, new LoadingAnimResponseHandler(context) {
//            @Override
//            public void onSuccess(String response) {
//                maintenanceItems = JSONUtils.fromJson(response, new TypeToken<ArrayList<MaintenanceItem>>() {
//                });
//                if (isPackage) {
//                    uploadObject.packages = new ArrayList<>();
//                    uploadObject.packages.add(packageID);
//
//                    for (int i = 0; i < maintenanceItems.size(); i++) {
//                        UploadItem item = new UploadItem();
//                        MaintenanceItem maintenanceItem = maintenanceItems.get(i);
//                        item.itemID = maintenanceItem.itemId;
//                        item.goodsID = new ArrayList<String>();
//                        for (int j = 0; j < maintenanceItem.goods.size(); j++) {
//                            item.goodsID.add(maintenanceItem.goods.get(j).goodsID);
//                        }
//                        packageItmes.add(item);
//                    }
//                } else {
//                    uploadObject.item = new ArrayList<UploadItem>();
//
//                    for (int i = 0; i < maintenanceItems.size(); i++) {
//                        UploadItem item = new UploadItem();
//                        MaintenanceItem maintenanceItem = maintenanceItems.get(i);
//                        item.itemID = maintenanceItem.itemId;
//                        item.goodsID = new ArrayList<String>();
//                        for (int j = 0; j < maintenanceItem.goods.size(); j++) {
//                            item.goodsID.add(maintenanceItem.goods.get(j).goodsID);
//                        }
//                        uploadObject.item.add(item);
//                    }
//                }
//                recommendProgramHandle.onSuccess(maintenanceItems, uploadObject, packageItmes);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                recommendProgramHandle.onFailure();
//            }
//        });
//    }
//
//
//    /**
//     * 检查套餐
//     * @param context
//     * @param checkPackageHandle
//     */
//    public void checkPackage(Context context, final checkPackageHandle checkPackageHandle){
//        String items = JSONUtils.toJson(uploadObject);
//        client.checkPackage(shopID, items, new BaseMallJsonHttpResponseHandler(context) {
//            @Override
//            public void onStart() {
//                super.onStart();
//                checkPackageHandle.onStart();
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                CheckPackage checkPackage = JSONUtils.fromJson(response, new TypeToken<CheckPackage>() {
//                });
//
//                checkPackageHandle.onSuccess(checkPackage);
//
//                if (checkPackage.packages != null && checkPackage.packages.size() != 0) {
//                    //清空原有的数据
//                    if (uploadObject.packages == null) {
//                        uploadObject.packages = new ArrayList<String>();
//                    } else {
//                        uploadObject.packages.clear();
//                    }
//                    //用于返回的套餐中的项目对比
//                    ArrayList<UploadItem> tempItem = new ArrayList<UploadItem>();
//                    //清空原有的数据
//                    if (uploadObject.item == null) {
//                        uploadObject.item = new ArrayList<UploadItem>();
//                        tempItem.addAll(uploadObject.item);
//                    } else {
//                        tempItem.addAll(uploadObject.item);
//                        uploadObject.item.clear();
//                    }
//
//                    //清空原有的数据
//                    packageItmes.clear();
//
//                    for (int i = 0; i < checkPackage.packages.size(); i++) {
//
//                        uploadObject.packages.add(checkPackage.packages.get(i).packageID);
//                        ArrayList<ReturnPackage.ReturnItem> returnItems = checkPackage.packages.get(i).items;
//
//                        for (int m = 0; m < returnItems.size(); m++) {
//                            ReturnPackage.ReturnItem returnItem = returnItems.get(m);
//                            UploadItem tempPackageItem = new UploadItem();
//                            tempPackageItem.itemID = returnItem.itemID;
//                            //把返回的item从原来的数据中清除
//                            for (int n = 0; n < tempItem.size(); n++) {
//                                if (tempItem.get(n).itemID.equals(returnItem.itemID)) {
//                                    tempItem.get(n).isInPackage = true;
//                                }
//                            }
//                            //将所有套餐中包含的item加入packageItems中
//                            packageItmes.add(tempPackageItem);
//                        }
//                        for (int n = 0; n < tempItem.size(); n++) {
//                            if (!tempItem.get(n).isInPackage) {
//                                uploadObject.item.add(tempItem.get(n));
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                checkPackageHandle.onFailure();
//            }
//        });
//    }
//
//
//    /**
//     * 确认上传参数
//     * @param maintenanceItems
//     * @param confirmUploadObjectHandle
//     */
//    public void ConfirmUploadObject(ArrayList<MaintenanceItem> maintenanceItems,ConfirmUploadObjectHandle confirmUploadObjectHandle){
//        this.maintenanceItems = maintenanceItems;
//        ArrayList<MaintenanceItem> tempMaintenanceItems = new ArrayList<>();
//        tempMaintenanceItems.addAll(maintenanceItems);
//        if (uploadObject.item != null) {
//            uploadObject.item.clear();
//        }else {
//            uploadObject.item = new ArrayList<>();
//        }
//
//        //去掉返回中包含套餐的item
//        for (int i = 0; i < maintenanceItems.size(); i++) {
//            for (int j = 0; j < packageItmes.size(); j++) {
//                if (packageItmes.get(j).itemID.equals(maintenanceItems.get(i).itemId)) {
//                    tempMaintenanceItems.get(i).isInPackage = true ;
//                }
//            }
//        }
//
//        if(tempMaintenanceItems.size() != 0){
//            for(int i = 0 ; i < tempMaintenanceItems.size() ; i++){
//                if(!tempMaintenanceItems.get(i).isInPackage){
//                    UploadItem item = new UploadItem();
//                    item.goodsID = new ArrayList<>();
//                    MaintenanceItem maintenanceItem = tempMaintenanceItems.get(i);
//                    item.itemID = maintenanceItem.itemId;
//                    for (int m = 0; m < maintenanceItem.goods.size(); m++) {
//                        item.goodsID.add(maintenanceItem.goods.get(m).goodsID);
//                    }
//                    uploadObject.item.add(item);
//                }
//            }
//        }
//        confirmUploadObjectHandle.onSuccess();
//        tempMaintenanceItems.clear();
//
//    }
//
//
//    /**
//     * 获取维修保养项目
//     * @param context
//     * @param getMaintenanceItemsHandle
//     */
//    public void getMaintenanceItems(Context context, final getMaintenanceItemsHandle getMaintenanceItemsHandle){
//
////        if(repairMaintenanceItems == null){
//            client.maintenanceItems(shopID, autoModelID, new LoadingAnimResponseHandler(context) {
//                @Override
//                public void onSuccess(String response) {
//                    repairMaintenanceItems = JSONUtils.fromJson(response,new TypeToken<ArrayList<MaintenanceItem>>(){});
//                    //对比推荐保养 如果包含 则替换成推荐保养中的项目
//                    for( int i = 0 ;i < repairMaintenanceItems.size() ; i++ ){
//                        for( int j = 0 ; j < maintenanceItems.size() ; j++){
//                            if(repairMaintenanceItems.get(i).itemId.equals(maintenanceItems.get(j).itemId)){
//                                repairMaintenanceItems.set(i,maintenanceItems.get(j));
//                                repairMaintenanceItems.get(i).isCheck = true;
//                            }
//                        }
//                    }
//                    getMaintenanceItemsHandle.onSuccess(repairMaintenanceItems);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
//                    getMaintenanceItemsHandle.onFailure();
//                }
//            });
////        } else {
////            for( int i = 0 ;i < repairMaintenanceItems.size() ; i++ ){
////                repairMaintenanceItems.get(i).isCheck = false;
////                for( int j = 0 ; j < maintenanceItems.size() ; j++){
////                    if(repairMaintenanceItems.get(i).itemId.equals(maintenanceItems.get(j).itemId)){
////                        repairMaintenanceItems.set(i,maintenanceItems.get(j));
////                        repairMaintenanceItems.get(i).isCheck = true;
////                    }
////                }
////            }
////            getMaintenanceItemsHandle.onSuccess(repairMaintenanceItems);
////        }
//    }
//
//    /**
//     * 从维修保养返回到 保养的项目
//     * @param ll_parent_layer
//     */
//    public void returnItem(LinearLayout ll_parent_layer) {
//        for(int i = 0 ; i< repairMaintenanceItems.size() ; i++){
//            MaintainAndRepairParent childView = (MaintainAndRepairParent) ll_parent_layer.getChildAt(i);
//            if(childView.returnItem().isCheck){
//                retrunMaintenanceItems.add(childView.returnItem());
//            }
//        }
//    }
//
//
//
//    /**
//     * 从维修保养返回到保养,修改保养中项目的值
//     */
//    public void ConfirmMaintenanceItems(){
//        ArrayList<MaintenanceItem> tempItems = new ArrayList<>();
//        tempItems.addAll(maintenanceItems);
//
//        for(MaintenanceItem tempItem : tempItems){
//            tempItem.isInReturn = false;
//        }
//
//        for(int j =0 ; j < tempItems.size() ; j++){
//            for(int i = 0 ; i< retrunMaintenanceItems.size(); i++){
//                if(tempItems.get(j).itemId.equals(retrunMaintenanceItems.get(i).itemId)){
//                    tempItems.get(j).isInReturn =true;
//                    retrunMaintenanceItems.get(i).isInReturn = true ;
//                }
//            }
//        }
//
//        ArrayList<MaintenanceItem> tempItems2 = new ArrayList<>();
//        for(int m = 0 ; m< tempItems.size() ; m++){
//            if(tempItems.get(m).isInReturn){
//                tempItems2.add(tempItems.get(m));
//            }
//        }
//
//        for(MaintenanceItem retrunMaintenanceItem: retrunMaintenanceItems){
//            if (!retrunMaintenanceItem.isInReturn){
//                tempItems2.add(retrunMaintenanceItem);
//            }
//        }
//        maintenanceItems.clear();
//        maintenanceItems.addAll(tempItems2);
//        tempItems.clear();
//        tempItems2.clear();
//        retrunMaintenanceItems.clear();
//    }
//
//    // 返回maintenanceItems
//    public ArrayList<MaintenanceItem> getMaintainItems() {
//        return maintenanceItems;
//    }
//
//    //返回shopId
//    public String getShopID(){
//        return shopID;
//    }
//
//    public void setAutoModelID(String autoModelID){
//        this.autoModelID =autoModelID;
//    }
//
//    //返回autoModelID
//    public String getAutoModelID(){
//        return autoModelID;
//    }
//
//    //返回 uploadObject
//    public UploadObject getUploadObject(){
//        return uploadObject;
//    }
//
//
//    /*********套餐保养项目接口**********/
//    public interface recommendProgramHandle{
//        void onSuccess(ArrayList<MaintenanceItem> maintenanceItems,UploadObject uploadObject,ArrayList<UploadItem> packageItmes);
//        void onFailure();
//    }
//
//    /*********检查套餐接口**********/
//    public interface checkPackageHandle{
//        void onStart();
//        void onSuccess(CheckPackage checkPackage);
//        void onFailure();
//    }
//
//    /*********确定上传参数接口**********/
//    public interface ConfirmUploadObjectHandle{
//        void onSuccess();
//    }
//
//    /*********获取维修保养项目接口**********/
//    public interface getMaintenanceItemsHandle{
//        void onSuccess(ArrayList<MaintenanceItem> repairMaintenanceItems);
//        void onFailure();
//    }
//
//}
