package com.github.xmap.box.mvp.presenter;

import android.app.Application;

import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;
import com.common.entity.PublicEvent;
import com.github.xmap.poi.mvp.model.entity.Event;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

import javax.inject.Inject;

import com.github.xmap.box.mvp.contract.BoxContract;

import org.simple.eventbus.Subscriber;

import static com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT;
import static com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED;
import static com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/03/2019 17:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class BoxPresenter extends BasePresenter<BoxContract.Model, BoxContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public BoxPresenter(BoxContract.Model model, BoxContract.View rootView) {
        super(model, rootView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        this.query = null;
        this.poiSearch = null;

        Timber.i("PoiPresenter---onDestroy");
    }

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    public void handlePoi(Poi poi) {
        Timber.i("onPOIClick");
        Timber.i("searchPOIIdAsyn开始");
        poiSearch.searchPOIIdAsyn(poi.getPoiId());
        Timber.i("addPoiMarker");
        mRootView.addPoiMarker(poi.getCoordinate());



    }


    public void handlePoiItem(PoiItem poiItem) {
        Timber.i("searchPOIIdAsyn成功");
        mRootView.showPoiInfo(poiItem);
        mRootView.showPoiUI();
    }

    public void init(PoiSearch.OnPoiSearchListener listener) {
        query = new PoiSearch.Query( "都匀东站" ,"火车站" ,"0854");
        poiSearch = new PoiSearch(mApplication , query);
        poiSearch.setOnPoiSearchListener(listener);
    }

    @Subscriber
    public void onMapClickEvent(Event.MapClick event) {
        mRootView.delPoiMarker();
        mRootView.hidePoiUI();
    }

    @Subscriber
    public void onBackPressed(PublicEvent.BackPressed event) {
        if (event.state == STATE_COLLAPSED){
            mRootView.delPoiMarker();
            mRootView.hidePoiUI();
        }else if (event.state == STATE_ANCHOR_POINT){
            mRootView.backSheetStateCollapsed();
        }else if (event.state == STATE_EXPANDED){
            mRootView.backSheetStateAnchorPoint();
        }

    }
}