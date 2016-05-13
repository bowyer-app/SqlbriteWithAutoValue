package com.bowyer.app.playermanage.database;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import com.bowyer.app.playermanage.BuildConfig;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module public final class DbModule {
  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(Application application) {
    return new DbOpenHelper(application);
  }

  @Provides @Singleton SqlBrite provideSqlBrite() {
    return SqlBrite.create(message -> Timber.tag("Database").v(message));
  }

  @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
    BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    db.setLoggingEnabled(BuildConfig.DEBUG);
    return db;
  }
}
