package com.github.xmap.box.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.github.xmap.box.mvp.contract.BoxContract;
import com.github.xmap.box.mvp.model.BoxModel;


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
@Module
public abstract class BoxModule {

    @Binds
    abstract BoxContract.Model bindBoxModel(BoxModel model);
}