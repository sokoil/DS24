package ru.weblokos.ds24;

import android.app.Application;

import ru.weblokos.ds24.DB.Database;


public class App extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();

    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }

    public Database getDatabase() {
        return Database.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
