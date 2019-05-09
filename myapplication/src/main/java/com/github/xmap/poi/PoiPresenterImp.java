package com.github.xmap.poi;

import com.amap.api.maps.model.Poi;

public class PoiPresenterImp implements PoiPresenter{
    POIView poiView;

    public PoiPresenterImp(POIView poiView) {
        this.poiView = poiView;
    }

    @Override
    public void showPOIInfo(Poi poi) {

    }

    @Override
    public void onPOIClick(Poi poi) {
        poiView.showPOIInfo(poi);
    }
}
