package com.github.xmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.github.xmap.base.CheckPermissionsActivity;
import com.github.xmap.poi.POIView;
import com.github.xmap.poi.PoiPresenter;
import com.github.xmap.poi.PoiPresenterImp;

public class MainActivity extends CheckPermissionsActivity implements LocationSource, AMapLocationListener ,AMap.OnPOIClickListener ,POIView ,PoiSearch.OnPoiSearchListener{
    private MapView mapView;
    private AMap aMap;
    private LinearLayout.LayoutParams mParams;
    private RelativeLayout mContainerLayout;

    private WifiManager mWifiManager;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    public  static  final  String CITI_KEY="city";
    public  static  final int SHANGHAI=0;
    public  static  final int BEIJING=1;
    public  static  final int GUANGZHOU=2;

    PoiPresenter poiPresenter;
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
        setContentView(R.layout.activity_main);
        mContainerLayout = (RelativeLayout) findViewById(R.id.root);

        mapView = new MapView(this);
        mapView.onCreate(savedInstanceState);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.addView(mapView, mParams);
        aMap = mapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);//禁止地图旋转手势.
        uiSettings.setTiltGesturesEnabled(false);//禁止倾斜手势.
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        aMap.setOnPOIClickListener(this);

        poiPresenter = new PoiPresenterImp(this);

        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query( "都匀东站" ,"火车站" ,"0854");
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);


        handler.sendEmptyMessageDelayed(1,2000);

    }
   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.167029352515243 ,107.58567626007701) , 14.5f));
       }
   };
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
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
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
                Log.e("AmapErr", errText);

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
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    @Override
    public void onPOIClick(Poi poi) {
        poiPresenter.onPOIClick(poi);
        aMap.moveCamera(CameraUpdateFactory.newLatLng(poi.getCoordinate() ));


    }

    @Override
    public void showPOIInfo(Poi poi) {
        poiSearch.searchPOIIdAsyn(poi.getPoiId());
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int retCode) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int retCode) {
        Toast.makeText(this , poiItem.getTitle() ,Toast.LENGTH_LONG).show();
    }
}
