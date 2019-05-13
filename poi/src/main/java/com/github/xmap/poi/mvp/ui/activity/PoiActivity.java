package com.github.xmap.poi.mvp.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.common.entity.PublicEvent;
import com.github.xmap.poi.R;
import com.github.xmap.poi.di.component.DaggerPoiComponent;
import com.github.xmap.poi.mvp.contract.PoiContract;
import com.github.xmap.poi.mvp.model.entity.Event;
import com.github.xmap.poi.mvp.presenter.PoiPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayout;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayoutBehavior;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class PoiActivity extends BaseActivity<PoiPresenter> implements PoiContract.View, LocationSource, AMapLocationListener, AMap.OnPOIClickListener, PoiSearch.OnPoiSearchListener {




    private MapView mapView;
    private AMap aMap;
    private LinearLayout.LayoutParams mParams;
    private CoordinatorLayout mContainerLayout;

    private WifiManager mWifiManager;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    int[] mDrawables = {
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3
    };

    TextView bottomSheetTextView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPoiComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_poi; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mContainerLayout = findViewById(R.id.root);

        mapView = findViewById(R.id.amap);
        mapView.onCreate(savedInstanceState);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        aMap = mapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setScaleControlsEnabled(true);
//        aMap.setMyLocationEnabled(true);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.167029352515243, 107.58567626007701), 14.5f));
        uiSettings.setRotateGesturesEnabled(false);//禁止地图旋转手势.
        uiSettings.setTiltGesturesEnabled(false);//禁止倾斜手势.
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        aMap.setOnPOIClickListener(this);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                EventBus.getDefault().post(new Event.MapClick(latLng));
            }
        });
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）

        mPresenter.init(this);



        viewPager = findViewById(R.id.pager);
        NestedScrollView bottomSheet = findViewById(R.id.bottom_sheet);
        FloatingActionButton fabBtn = findViewById(R.id.fab);
        MergedAppBarLayout mergedAppBarLayout = findViewById(R.id.mergedappbarlayout);


        mapui.add(viewPager);
        mapui.add(bottomSheet);
        mapui.add(fabBtn);
        mapui.add(mergedAppBarLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" ");
        }


        CoordinatorLayout coordinatorLayout = findViewById(R.id.root);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        mergedAppBarLayout = findViewById(R.id.mergedappbarlayout);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle("Title Dummy");
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
            }
        });

        bottomSheetTextView = (TextView) bottomSheet.findViewById(R.id.bottom_sheet_title);


        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
    }
    ViewPager viewPager;
    ItemPagerAdapter adapter;
    BottomSheetBehaviorGoogleMapsLike behavior;
    List<View> mapui = new ArrayList<>();
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    private void checkWifiSetting() {
        if (mWifiManager.isWifiEnabled()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("开启WIFI模块会提升定位准确性"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent); // 打开系统设置界面
            }
        });
        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Timber.e(errText);

            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        checkWifiSetting();
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();

            mLocationOption.setSensorEnable(false);
        }
    }

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mListener = null;
        mlocationClient = null;
    }

    @Override
    public void onPOIClick(Poi poi) {
        mPresenter.handlePoi(poi);
        aMap.animateCamera(CameraUpdateFactory.newLatLng(poi.getCoordinate()));

    }

    private Marker marker;

    /**
     * 添加带生长效果marker
     */
    @Override
    public void addPoiMarker(LatLng latLng) {
        if (marker != null)
            marker.remove();
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_openmap_mark)));
        marker = aMap.addMarker(options);
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
        marker.setAnimation(markerAnimation);
        marker.startAnimation();
    }

    @Override
    public void delPoiMarker() {
        if (marker != null)
            marker.remove();
        marker = null;
    }

    @Override
    public void showPoiUI() {
        for (View v : mapui){
            v.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hidePoiUI() {
        for (View v : mapui){
            v.setVisibility(View.GONE);
        }
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

    }

    @Override
    public void showPoiInfo(PoiItem poiItem) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        mPresenter.handlePoiItem(poiItem);


        adapter = new ItemPagerAdapter(this);
        viewPager.setAdapter(adapter);
        adapter.setImgUrls(poiItem.getPhotos());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PublicEvent.onBackPressed());
    }


}
