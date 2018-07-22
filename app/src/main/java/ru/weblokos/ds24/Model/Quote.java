package ru.weblokos.ds24.Model;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public interface Quote {

    public int getId();
    public int getCreatedBy();
    public String getText();
    public boolean isLeft();
    public String getCreatedAt();

}
