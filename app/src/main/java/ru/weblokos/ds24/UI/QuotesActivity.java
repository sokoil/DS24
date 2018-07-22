package ru.weblokos.ds24.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.weblokos.ds24.App;
import ru.weblokos.ds24.Model.Quote;
import ru.weblokos.ds24.R;
import ru.weblokos.ds24.Service.QuoteService;
import ru.weblokos.ds24.Service.QuotesListResponse;
import ru.weblokos.ds24.ViewModel.QuotesViewModel;
import ru.weblokos.ds24.databinding.ActivityQuotesBinding;

import static ru.weblokos.ds24.Service.QuoteService.DEFAULT_LIMIT;

public class QuotesActivity extends AppCompatActivity {

    ActivityQuotesBinding binding;
    private QuotesViewModel model;
    private QuoteAdapter mQuotesAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quotes);
        setUpUi();
        QuotesViewModel.Factory factory = new QuotesViewModel.Factory(
                getApplication());
        model = ViewModelProviders.of(this, factory)
                .get(QuotesViewModel.class);
        binding.setModel(model);
        subscribeToModel(model);
        loadQuotes();
    }

    private void setUpUi() {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.quoteList.setLayoutManager(mLayoutManager);
        mQuotesAdapter = new QuoteAdapter(mQuoteClickCallback);
        binding.quoteList.setAdapter(mQuotesAdapter);
        binding.quoteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            loadQuotes();
                        }
                    }
                }
            }
        });
    }

    private Call<QuotesListResponse> call;

    private void subscribeToModel(final QuotesViewModel model) {
        model.getQuotes().observe(this,
                quoteEntities -> {
                    if (quoteEntities != null) {
                        binding.setIsLoading(false);
                        mQuotesAdapter.setQuoteList(quoteEntities);
                    } else {
                        binding.setIsLoading(true);
                    }
                }
        );
    }

    private void loadQuotes() {
        call = (new QuoteService())
                .getAPI()
                .getQuotes(model.getQuotesOffset(), DEFAULT_LIMIT);
        call.enqueue(new retrofit2.Callback<QuotesListResponse>() {
            @Override
            public void onResponse(Call<QuotesListResponse> call, Response<QuotesListResponse> response) {
                loading = true;
                if(response.body().data != null) {
                    ((App) getApplicationContext()).getExecutors().diskIO().execute(() -> { // заполнение базы данными
                        ((App) getApplicationContext()).getRepository().addQuotes(response.body().data);
                    });
                }
            }

            @Override
            public void onFailure(Call<QuotesListResponse> call, Throwable t) {
                loading = true;
                if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    Toast.makeText(QuotesActivity.this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
    }

    private final QuoteClickCallback mQuoteClickCallback = new QuoteClickCallback() {
        @Override
        public void onClick(Quote quote) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                startActivity(new Intent(QuotesActivity.this, QuoteActivity.class).putExtra("quote", quote.getId()));
            }
        }
    };
}
