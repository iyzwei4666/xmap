package com.github.xmap.box.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.github.xmap.box.di.module.BoxModule;
import com.github.xmap.box.mvp.contract.BoxContract;

import com.jess.arms.di.scope.ActivityScope;
import com.github.xmap.box.mvp.ui.activity.BoxActivity;


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
@ActivityScope
@Component(modules = BoxModule.class, dependencies = AppComponent.class)
public interface BoxComponent {
    void inject(BoxActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BoxComponent.Builder view(BoxContract.View view);

        BoxComponent.Builder appComponent(AppComponent appComponent);

        BoxComponent build();
    }
}