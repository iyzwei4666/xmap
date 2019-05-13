package com.github.xmap.poi.mvp.ui.activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.services.poisearch.Photo;
import com.bumptech.glide.Glide;
import com.github.xmap.poi.R;

import java.util.ArrayList;
import java.util.List;


public class ItemPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    public List<Photo> imgUrls = new ArrayList<>();

    public ItemPagerAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.bottom_sheet_photo_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(mContext).load(imgUrls.get(position).getUrl()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


    public void setImgUrls(List<Photo> imgUrls) {
        if (imgUrls.size() == 0) {
            Photo photo = new Photo();
            photo.setTitle("默认图片");
            photo.setUrl("https://images.liqucn.com/img/h99/h04/img_localize_e4c7d93cf6fca79dbf3115ec3b977012_560x350.jpg");
            imgUrls.add(photo);
        }
        this.imgUrls = imgUrls;
    }
}
