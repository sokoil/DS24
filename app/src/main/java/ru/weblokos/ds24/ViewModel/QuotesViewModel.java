package ru.weblokos.ds24.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import ru.weblokos.ds24.App;
import ru.weblokos.ds24.DB.Entity.QuoteEntity;

public class QuotesViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<QuoteEntity>> mObservableQuotes;

    public QuotesViewModel(Application application) {
        super(application);
        mObservableQuotes = new MediatorLiveData<>();
        mObservableQuotes.setValue(null);
        LiveData<List<QuoteEntity>> quotes = ((App) application).getRepository().getAllQuotes();
        mObservableQuotes.addSource(quotes, mObservableQuotes::setValue);
    }

    public LiveData<List<QuoteEntity>> getQuotes() {
        return mObservableQuotes;
    }

    public int getQuotesOffset() {
        return mObservableQuotes.getValue() != null ? mObservableQuotes.getValue().size()+1 : 1;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new QuotesViewModel(mApplication);
        }
    }
}
