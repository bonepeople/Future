package com.bonepeople.android.future.future.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bonepeople.android.future.future.R;
import com.bonepeople.android.future.future.model.ConstructorInfo;

import java.util.ArrayList;

/**
 * 施工队列表
 * <p>
 * Created by bonepeople on 2018/1/23.
 */

public class ConstructorAdapter extends RecyclerView.Adapter<ConstructorAdapter.ViewHolder> {
    private ArrayList<ConstructorInfo> data;

    public ConstructorAdapter(ArrayList<ConstructorInfo> data) {
        this.data = data;
    }

    @Override
    public ConstructorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_constructor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConstructorAdapter.ViewHolder holder, int position) {
        ConstructorInfo constructor = data.get(position);
        holder.name.setText(constructor.getConsName());
        if (constructor.isAuthorise())
            holder.auth.setImageResource(R.drawable.icon_authorise_true);
        else
            holder.auth.setImageResource(R.drawable.icon_authorise_false);
        if (constructor.isWarranty())
            holder.warranty.setImageResource(R.drawable.icon_warranty_true);
        else
            holder.warranty.setImageResource(0);
        holder.rating.setRating(getLevel(constructor.getGradePraise()));
        String info = holder.name.getContext().getResources()
                .getString(R.string.caption_text_constructor
                        , constructor.getJobNum()
                        , constructor.getWorkYear()
                        , constructor.getNativePalceName());
        holder.info.setText(info);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private int getLevel(double gradePraise) {
        int star = 0;
        if (gradePraise >= 4 && gradePraise < 51) {
            star = 1;
        } else if (gradePraise >= 51 && gradePraise < 151) {
            star = 2;
        } else if (gradePraise >= 151 && gradePraise < 301) {
            star = 3;
        } else if (gradePraise >= 301 && gradePraise < 601) {
            star = 4;
        } else if (gradePraise >= 601) {
            star = 5;
        }
        return star;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView header, auth, warranty;
        TextView name, info;
        RatingBar rating;

        ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.imageView_header);
            name = view.findViewById(R.id.textView_name);
            auth = view.findViewById(R.id.imageView_auth);
            warranty = view.findViewById(R.id.imageView_warranty);
            info = view.findViewById(R.id.textView_info);
            rating = view.findViewById(R.id.ratingBar);
        }
    }
}
