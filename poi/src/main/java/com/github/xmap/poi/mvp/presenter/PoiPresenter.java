package com.github.xmap.poi.mvp.presenter;

import android.app.Application;

import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.github.xmap.poi.mvp.contract.PoiContract;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/10/2019 09:35
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PoiPresenter extends BasePresenter<PoiContract.Model, PoiContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public PoiPresenter(PoiContract.Model model, PoiContract.View rootView) {
        super(model, rootView);

        query = new PoiSearch.Query( "都匀东站" ,"火车站" ,"0854");
        poiSearch = new PoiSearch(mApplication, query);
        poiSearch.setOnPoiSearchListener(poiSearchListener);

    }
    PoiSearch.OnPoiSearchListener  poiSearchListener = new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult poiResult, int retCode) {

        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int retCode) {
            mRootView.showPoiInfo(poiItem);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    public void handlePoi(Poi poi) {
        poiSearch.searchPOIIdAsyn(poi.getPoiId());
    }
}
