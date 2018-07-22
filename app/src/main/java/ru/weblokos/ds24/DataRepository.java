package ru.weblokos.ds24;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import ru.weblokos.ds24.DB.Database;
import ru.weblokos.ds24.DB.Entity.QuoteEntity;


public class DataRepository {
    private static DataRepository sInstance;

    private final Database mDatabase;
    private MediatorLiveData<List<QuoteEntity>> mObservableQuotes;

    private DataRepository(final Database database) {
        mDatabase = database;
        mObservableQuotes = new MediatorLiveData<>();
        mObservableQuotes.addSource(mDatabase.quoteDao().loadAllQuotes(),
                quoteEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableQuotes.postValue(quoteEntities);
                    }
                });
    }

    public static DataRepository getInstance(final Database database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<QuoteEntity>> getAllQuotes() {
        return mObservableQuotes;
    }

    public void addQuotes(List<QuoteEntity> quoteEntities) {
        mDatabase.quoteDao().insertAll(quoteEntities);
    }

    public void deleteQuotes() {
        mDatabase.quoteDao().deleteQuotes();
    }
}
