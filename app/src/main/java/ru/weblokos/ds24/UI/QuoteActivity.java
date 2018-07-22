package ru.weblokos.ds24.UI;

import android.arch.lifecycle.Lifecycle;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.weblokos.ds24.DB.Entity.TagEntity;
import ru.weblokos.ds24.R;
import ru.weblokos.ds24.Service.QuoteService;
import ru.weblokos.ds24.Service.QuotesListResponse;
import ru.weblokos.ds24.databinding.ActivityQuoteBinding;

public class QuoteActivity extends AppCompatActivity {

    ActivityQuoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadQuote();
    }

    private Call<QuotesListResponse> call;

    private void loadQuote() {
        call = (new QuoteService())
                .getAPI()
                .getQuote(getIntent().getExtras().getInt("quote"));;
        call.enqueue(new retrofit2.Callback<QuotesListResponse>() {
            @Override
            public void onResponse(Call<QuotesListResponse> call, Response<QuotesListResponse> response) {
                if(response.body().data != null && response.body().data.size() > 0) {
                    binding.setQuote(response.body().data.get(0));
                    binding.setIsLoading(true);
                    setUpTags(response.body().data.get(0).getTagList());
                }
            }

            @Override
            public void onFailure(Call<QuotesListResponse> call, Throwable t) {
                if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    Toast.makeText(QuoteActivity.this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
    }

    private void setUpTags(List<TagEntity> tagEntities) {
        // вью для отображения тэгов не особо сложно сделать
        // поэтому я решил взять готовую с гитхаба ;),
        // унаследовав свой обьект тэга от обьекта тэга из класса вьюхи.
        binding.tagsView.setTags(tagEntities
                .subList(0, tagEntities.size() > 20 ? 20 : tagEntities.size()), " ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
