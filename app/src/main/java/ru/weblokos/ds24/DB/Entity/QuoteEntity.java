package ru.weblokos.ds24.DB.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ru.weblokos.ds24.Model.Quote;

@Entity(tableName = "quotes")
public class QuoteEntity extends BaseObservable implements Quote { // цитата

    @PrimaryKey
    private int id;
    private int createdBy;
    private String text;

    @Ignore
    private String createdAt = null;
    @Ignore
    private List<TagEntity> tagList = null;

    public QuoteEntity() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<TagEntity> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagEntity> tagList) {
        this.tagList = tagList;
    }

    public boolean isLeft() {
        return getCreatedBy() == 0;
    }


}
