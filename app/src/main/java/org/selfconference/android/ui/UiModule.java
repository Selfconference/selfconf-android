package org.selfconference.android.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class,
    },
    complete = false,
    library = true
)
public final class UiModule {

  @Provides @Singleton ViewContainer viewContainer() {
    return ViewContainer.DEFAULT;
  }
}
