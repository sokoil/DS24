package ru.weblokos.ds24.UI;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.weblokos.ds24.Model.Quote;
import ru.weblokos.ds24.R;
import ru.weblokos.ds24.databinding.QuoteItemBinding;


public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {

    List<? extends Quote> mQuoteList;
    @Nullable
    private final QuoteClickCallback mQuoteClickCallback;


    public QuoteAdapter(@Nullable QuoteClickCallback productClickCallback) {
        this.mQuoteClickCallback = productClickCallback;
    }

    public void setQuoteList(final List<? extends Quote> QuoteList) {
        if (mQuoteList == null) {
            mQuoteList = QuoteList;
            notifyItemRangeInserted(0, QuoteList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mQuoteList.size();
                }

                @Override
                public int getNewListSize() {
                    return QuoteList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mQuoteList.get(oldItemPosition).getId() ==
                            QuoteList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Quote newQuote = QuoteList.get(newItemPosition);
                    Quote oldQuote = mQuoteList.get(oldItemPosition);
                    return newQuote.getText().equalsIgnoreCase(oldQuote.getText());
                }
            });
            mQuoteList = QuoteList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuoteItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.quote_item, parent,
                        false);
        binding.setCallback(mQuoteClickCallback);
        return new QuoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {
        holder.binding.setQuote(mQuoteList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mQuoteList == null ? 0 : mQuoteList.size();
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {

        final QuoteItemBinding binding;

        public QuoteViewHolder(QuoteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}