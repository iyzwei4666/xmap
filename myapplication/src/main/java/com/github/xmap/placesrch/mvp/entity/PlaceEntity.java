package com.github.xmap.placesrch.mvp.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class PlaceEntity extends LitePalSupport {
   private String key;
    private Date date;

    public PlaceEntity(String key, Date date) {
        this.key = key;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
