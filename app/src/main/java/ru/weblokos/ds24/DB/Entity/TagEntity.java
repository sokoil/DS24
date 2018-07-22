package ru.weblokos.ds24.DB.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;

import ru.weblokos.ds24.UI.TagView;

public class TagEntity extends TagView.Tag { // Тэг, унаследованный от объекта тэга из класса вьюхи тэгов

    private String label;

    private String color = null;


    public TagEntity(String tagText, int tagColor) {
        super(tagText, tagColor);
    }

    public String getTag() {
        return label;
    }

    public void setTag(String label) {
        this.label = label;
    }

    public int getColor() {
        return Color.parseColor(color == null ? "#dddddd" : color);
    }

    public void setColor(String color) {
        this.color = color;
    }
}
