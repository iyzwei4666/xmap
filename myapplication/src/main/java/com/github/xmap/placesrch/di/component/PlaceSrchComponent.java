package com.github.xmap.placesrch.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.github.xmap.placesrch.di.module.PlaceSrchModule;
import com.github.xmap.placesrch.mvp.contract.PlaceSrchContract;

import com.jess.arms.di.scope.ActivityScope;
import com.github.xmap.placesrch.mvp.ui.activity.PlaceSrchActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 16:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PlaceSrchModule.class, dependencies = AppComponent.class)
public interface PlaceSrchComponent {
    void inject(PlaceSrchActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PlaceSrchComponent.Builder view(PlaceSrchContract.View view);

        PlaceSrchComponent.Builder appComponent(AppComponent appComponent);

        PlaceSrchComponent build();
    }
}