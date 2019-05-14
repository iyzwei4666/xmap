package com.github.xmap.poi.di.component;

import com.github.xmap.poi.di.module.PoiModule;
import com.github.xmap.poi.mvp.contract.PoiContract;
import com.github.xmap.poi.mvp.ui.activity.PoiActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.BindsInstance;
import dagger.Component;


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
@ActivityScope
@Component(modules = PoiModule.class, dependencies = AppComponent.class)
public interface PoiComponent {
    void inject(PoiActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PoiComponent.Builder view(PoiContract.View view);

        PoiComponent.Builder appComponent(AppComponent appComponent);

        PoiComponent build();
    }
}