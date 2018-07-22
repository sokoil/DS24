package ru.weblokos.ds24.DB;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import ru.weblokos.ds24.AppExecutors;
import ru.weblokos.ds24.DB.DAO.QuoteDao;
import ru.weblokos.ds24.DB.Entity.QuoteEntity;

@android.arch.persistence.room.Database(entities = {QuoteEntity.class}, version = 1)
public abstract class Database extends RoomDatabase { // база с цитатами

    private static Database sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    @VisibleForTesting
    public static final String DATABASE_NAME = "ds24-quotes-db";

    public abstract QuoteDao quoteDao();

    public static Database getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (Database.class) {
                if (sInstance == null) {
                    sInstance = create(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context, executors);
                }
            }
        }
        return sInstance;
    }

    private static Database create(final Context context, final AppExecutors executors) {
        return Room.databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                getInstance(context, executors).updateDatabaseCreated(context, executors);
            }
        }).build();
    }

    private void updateDatabaseCreated(final Context context, final AppExecutors executors) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
            executors.diskIO().execute(() -> {
                quoteDao().deleteQuotes(); // очистка базы при каждом запуске, по условиям тз. так бы я хранил цитаты.
            });
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(Boolean.valueOf(true));
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
