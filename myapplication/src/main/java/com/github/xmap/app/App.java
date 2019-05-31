package com.github.xmap.app;

import android.os.Environment;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.xmap.app.logx.FileTree;
import com.github.xmap.app.logx.MTagTree;
import com.jess.arms.base.BaseApplication;

import timber.log.Timber;

/**
 * Created by Whyn on 2017/8/28.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        setLog();
    }

    private void setLog() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
        String date = simpleDateFormat.format(new Date());
        Timber.plant(new FileTree().name("XMap"+date + ".log").storeAt(path+"/log"));
        Timber.plant(new MTagTree().addTag("XMap"));
    }
}
