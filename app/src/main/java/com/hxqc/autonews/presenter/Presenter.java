package com.hxqc.autonews.presenter;

import com.hxqc.autonews.model.AutoCalendarModel;
import com.hxqc.autonews.model.AutoInfoCommentModel;
import com.hxqc.autonews.model.AutoInfoDetailModel;
import com.hxqc.autonews.model.AutoInfoTypeModel;
import com.hxqc.autonews.model.AutoInformationModel;
import com.hxqc.autonews.model.pojos.AutoCalendar;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.model.pojos.InfoType;
import com.hxqc.autonews.view.AutoInfoHomeDataHandler;
import com.hxqc.autonews.view.IMoreDataWithCacheHandler;
import com.hxqc.autonews.view.IView;
import com.hxqc.autonews.view.RefrashableView;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.mall.core.interfaces.CacheDataListener;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.Error;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯部分执行器
 */
public class Presenter implements IPresenter {
    private static final int PAGE_INIT = 1;
    public int page = PAGE_INIT;

    private boolean hasMore = true;

    /**
     * 汽车资讯首页列表
     *
     * @param view
     * @param model
     */
    public void getAutoInfoListData(final IView<AutoInfoHomeData> view, AutoInformationModel model) {
        DebugLog.d(getClass().getSimpleName(), "getAutoInfoListData");
        initPage();
        model.getAutoInfoHomeData(page, new LoadDataCallBack<AutoInfoHomeData>() {
            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoHomeData obj) {
                view.onDataBack(obj);
            }
        });

    }

    /**
     * 加载更多汽车资讯列表数据
     *
     * @param view
     * @param model
     */
    public void loadMoreRecommentAutoInfoListData(final IMoreDataWithCacheHandler<ArrayList<AutoInformation>> view, AutoInformationModel model) {
        DebugLog.d(getClass().getSimpleName(), "loadMoreRecommentAutoInfoListData");
        if (hasMore) {
            page++;
            model.loadMoreRecommentAutoListData(page, new CacheDataListener<ArrayList<AutoInformation>>() {
                @Override
                public void onCacheDataBack(ArrayList<AutoInformation> obj) {
                    view.onMoreCacheDataBack(obj);
                }

                @Override
                public void onCacheDataNull() {
                    view.onMoreCacheDataNull("");
                }

                @Override
                public void onDataNull(String message) {
                    view.onMoreDataNull(message);
                    if (message.equals("")) {
                        hasMore = false;
                    }
                }

                @Override
                public void onDataGot(ArrayList<AutoInformation> obj) {
                    view.onMoreDataBack(obj);
                }
            });
        }
    }

    /**
     * 加载更多汽车资讯列表数据
     *
     * @param view
     * @param model
     */
    @Deprecated
    public void loadMoreRecommentAutoInfoListData(final RefrashableView<AutoInfoHomeData> view, AutoInformationModel model) {
        DebugLog.d(getClass().getSimpleName(), "loadMoreRecommentAutoInfoListData");
        if (hasMore) {
            page++;
            model.getAutoInfoHomeData(page, new LoadDataCallBack<AutoInfoHomeData>() {
                @Override
                public void onDataNull(String message) {
                    view.onDataNull(message);
                }

                @Override
                public void onDataGot(AutoInfoHomeData obj) {
                    if (obj == null)
                        view.onDataNull("");
                    else {
                        if (obj.infoList == null || obj.infoList.isEmpty())
                            hasMore = false;
                        else
                            view.onMoreDataResponse(obj);
                    }
                }
            });
        }
    }

    /**
     * 分类加载更多汽车资讯列表数据
     *
     * @param view
     * @param model
     */
    public void loadMoreAutoInfoListDataByType(String type, final IMoreDataWithCacheHandler<ArrayList<AutoInformation>> view, AutoInformationModel model) {
        DebugLog.d(getClass().getSimpleName(), "loadMoreRecommentAutoInfoListData");
        if (hasMore) {
            page++;

            model.loadMoreAutoListDataByType(page, type, new CacheDataListener<ArrayList<AutoInformation>>() {
                @Override
                public void onCacheDataBack(ArrayList<AutoInformation> obj) {
                    view.onMoreCacheDataBack(obj);
                }

                @Override
                public void onCacheDataNull() {
                    view.onMoreCacheDataNull("");
                }

                @Override
                public void onDataNull(String message) {
                    view.onMoreDataNull(message);
                    if (message.equals("")) {
                        hasMore = false;
                    }
                }

                @Override
                public void onDataGot(ArrayList<AutoInformation> obj) {
                    view.onMoreDataBack(obj);
                }
            });
        }
    }

//    /**
//     * 分类加载更多汽车资讯列表数据
//     *
//     * @param view
//     * @param model
//     */
//    public void loadMoreAutoInfoListDataByType(String type, final AutoInfoFragment_2 view, AutoInformationModel model) {
//        DebugLog.d(getClass().getSimpleName(), "loadMoreRecommentAutoInfoListData");
//        if (hasMore) {
//            page++;
//            model.getDataByType(page, type, new LoadDataCallBack<ArrayList<AutoInformation>>() {
//                @Override
//                public void onDataNull(String message) {
//                    view.onTypeListDataFail(message);
//                }
//
//                @Override
//                public void onDataGot(ArrayList<AutoInformation> obj) {
//                    if (obj == null) {
//                        view.onTypeListDataFail("");
//                    } else {
//                        view.onMoreTypeListDataBack(obj);
//                        if (obj == null || obj.isEmpty()) {
//                            hasMore = false;
//                        }
//                    }
//                }
//            });
//        }
//    }

//    /**
//     * 按照类型获取
//     *
//     * @param guideCode
//     * @param fragment
//     * @param model
//     */
//    public void getAutoInfoByType(String guideCode, final AutoInfoFragment_2 fragment, AutoInformationModel model) {
//        initPage();
//        model.getDataByType(page, guideCode, new LoadDataCallBack<ArrayList<AutoInformation>>() {
//            @Override
//            public void onDataNull(String message) {
//                fragment.onTypeListDataFail(message);
//            }
//
//            @Override
//            public void onDataGot(ArrayList<AutoInformation> obj) {
//                fragment.onTypeListDataBack(obj);
//            }
//        });
//    }

    private void initPage() {
        page = PAGE_INIT;
        hasMore = true;
    }

    /**
     * 按照类型获取第一页
     *
     * @param guideCode
     * @param fragment
     * @param model
     */
    public void getAutoInfoByType(String guideCode, final RequestDataWithCacheHandler<ArrayList<AutoInformation>> fragment, AutoInformationModel model) {
        initPage();
        model.getDataByType(page, guideCode, new CacheDataListener<ArrayList<AutoInformation>>() {
            @Override
            public void onCacheDataBack(ArrayList<AutoInformation> obj) {
                fragment.onCacheDataBack(obj);
            }

            @Override
            public void onCacheDataNull() {
                fragment.onCacheDataNull();
            }

            @Override
            public void onDataNull(String message) {
                fragment.onDataNull(message);
            }

            @Override
            public void onDataGot(ArrayList<AutoInformation> obj) {
                fragment.onDataResponse(obj);
            }
        });
    }

    /**
     * 获取banner的数据
     *
     * @param handler
     * @param model
     */
    public void getAutoInfoHomeData(final AutoInfoHomeDataHandler handler, AutoInformationModel model) {
        model.getData(new CacheDataListener<AutoInfoHomeData>() {
            @Override
            public void onCacheDataBack(AutoInfoHomeData obj) {
                handler.onHomeDataResponse(obj);
            }

            @Override
            public void onCacheDataNull() {
                handler.onHomeDataNull("cache");
            }

            @Override
            public void onDataNull(String message) {
                handler.onHomeDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoHomeData obj) {
                handler.onHomeDataResponse(obj);
            }
        });
    }

    /**
     * 获取推荐资讯列表数据
     *
     * @param fragment
     * @param model
     */
    public void getRecommentAutoInfo(final RequestDataWithCacheHandler<ArrayList<AutoInformation>> fragment,
                                     AutoInformationModel model) {
        initPage();
        model.getAutoInfoHomeData(page, new CacheDataListener<AutoInfoHomeData>() {
            @Override
            public void onCacheDataBack(AutoInfoHomeData obj) {
                if (obj != null) {
                    if (obj.infoList != null) {
                        fragment.onCacheDataBack(obj.infoList);
                    } else fragment.onCacheDataNull();
                } else fragment.onCacheDataNull();
            }

            @Override
            public void onCacheDataNull() {
                fragment.onCacheDataNull();
            }

            @Override
            public void onDataNull(String message) {
                fragment.onDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoHomeData obj) {
                if (obj != null) {
                    if (obj.infoList != null) {
                        fragment.onDataResponse(obj.infoList);
                    } else fragment.onDataNull("");
                } else fragment.onDataNull("");
            }
        });
    }

    /**
     * 获取推荐资讯列表数据
     *
     * @param fragment
     * @param model
     */
    @Deprecated
    public void getRecommentAutoInfo(final IView<ArrayList<AutoInformation>> fragment,
                                     AutoInformationModel model) {
        initPage();
        model.getAutoInfoHomeData(page, new LoadDataCallBack<AutoInfoHomeData>() {
            @Override
            public void onDataNull(String message) {
                fragment.onDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoHomeData obj) {
                if (obj != null) {
                    if (obj.infoList != null) {
                        fragment.onDataBack(obj.infoList);
                    } else fragment.onDataNull("");
                } else fragment.onDataNull("");
            }
        });
    }

    /**
     * 汽车资讯详情
     *
     * @param infoID
     * @param view
     * @param model
     */
    @Deprecated
    public void getAutoInfoDetail(String infoID, final IView<AutoInfoDetail> view, AutoInfoDetailModel model) {
        model.getAutoDetail(infoID, new LoadDataCallBack<AutoInfoDetail>() {
            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoDetail obj) {
                view.onDataBack(obj);
            }
        });
    }

    /**
     * 汽车资讯详情
     *
     * @param infoID
     * @param view
     * @param model
     */
    public void getAutoInfoDetail(String infoID, final RequestDataWithCacheHandler<AutoInfoDetail> view, AutoInfoDetailModel model) {
        model.getAutoDetail(infoID, new CacheDataListener<AutoInfoDetail>() {
            @Override
            public void onCacheDataBack(AutoInfoDetail obj) {
                view.onCacheDataBack(obj);
            }

            @Override
            public void onCacheDataNull() {
                view.onCacheDataNull();
            }

            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(AutoInfoDetail obj) {
                view.onDataResponse(obj);
            }
        });
    }

//    /**
//     * 汽车资讯类型
//     *
//     * @param view
//     * @param model
//     */
//    public void getAutoInfoTypes(final AutoInfoFragment_2 view, AutoInfoTypeModel model) {
//        model.getData(new CacheDataListener<ArrayList<InfoType>>() {
//            @Override
//            public void onCacheDataBack(ArrayList<InfoType> obj) {
//                view.onCacheTypeDataBack(obj);
//            }
//
//            @Override
//            public void onCacheDataNull() {
//                view.onCacheTypeNull();
//            }
//
//            @Override
//            public void onDataNull(String message) {
//                view.onTypeLoadFail(message);
//            }
//
//            @Override
//            public void onDataGot(ArrayList<InfoType> obj) {
//                if (obj.size() > 0)
//                    view.onTypeLoad(obj);
//                else view.onTypeLoadFail("");
//            }
//        });
//    }

    /**
     * 汽车资讯类型
     *
     * @param view
     * @param model
     */
    public void getAutoInfoTypes(final RequestDataWithCacheHandler<ArrayList<InfoType>> view, AutoInfoTypeModel model) {
        model.getData(new CacheDataListener<ArrayList<InfoType>>() {
            @Override
            public void onCacheDataBack(ArrayList<InfoType> obj) {
                view.onCacheDataBack(obj);
            }

            @Override
            public void onCacheDataNull() {
                view.onCacheDataNull();
            }

            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(ArrayList<InfoType> obj) {
                if (obj.size() > 0)
                    view.onDataResponse(obj);
                else view.onDataNull("");
            }
        });
    }

    /**
     * 发送评价
     *
     * @param infoID
     * @param content
     * @param view
     * @param model
     */
    public void sendAutoInfoComment(String infoID, String content, final IView<Error> view, AutoInfoCommentModel model) {
        model.sendComment(infoID, content, new LoadDataCallBack<Error>() {
            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(Error obj) {
                view.onDataBack(obj);
            }
        });
    }


    public void getAutoCalendarYears(final IView<ArrayList<String>> view, AutoCalendarModel model) {
        model.autoCalendarYears(new LoadDataCallBack<ArrayList<String>>() {
            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(ArrayList<String> obj) {
                view.onDataBack(obj);
            }
        });
    }

    public void getAutoCalendar(String year, final IView<ArrayList<AutoCalendar>> view, AutoCalendarModel model) {

        model.autoCalendar(year, new LoadDataCallBack<ArrayList<AutoCalendar>>() {
            @Override
            public void onDataNull(String message) {
                view.onDataNull(message);
            }

            @Override
            public void onDataGot(ArrayList<AutoCalendar> obj) {
                view.onDataBack(obj);
            }
        });
    }
}
