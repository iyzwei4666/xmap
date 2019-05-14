package com.github.xmap.poi.mvp.model.entity;

import com.amap.api.maps.model.LatLng;

public class Event {
    public static class MapClick{
        public LatLng latLng;
        public MapClick(LatLng latLng) {
            this.latLng = latLng;
        }
    }
}
