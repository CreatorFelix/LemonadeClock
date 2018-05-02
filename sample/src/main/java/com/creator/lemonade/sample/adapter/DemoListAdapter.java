package com.creator.lemonade.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creator.lemonade.sample.R;
import com.creator.lemonade.sample.activity.ClockActivity;
import com.creator.lemonade.sample.bean.Demo;

import java.util.ArrayList;

/**
 * @author Felix.Liang
 */
public class DemoListAdapter extends RecyclerView.Adapter<DemoListAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private final ArrayList<Demo> mDemos = new ArrayList<>();
    private final Context mContext;

    public DemoListAdapter(@NonNull Context context) {
        mContext = context;
        final String[] labels = mContext.getResources().getStringArray(R.array.demo_label_list);
        final Class[] classes = {ClockActivity.class};
        for (int i = 0; i < labels.length; i++) {
            final Demo demo = new Demo(labels[i], R.mipmap.ic_launcher, classes[i]);
            mDemos.add(demo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_demo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Demo demo = getItem(position);
        holder.tvLabel.setText(demo.getLabel());
        holder.ivIcon.setImageResource(demo.getIconResId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(v,
                        (int) v.getTranslationX(), (int) v.getTranslationY(),
                        v.getWidth(), v.getHeight());
                mContext.startActivity(new Intent(mContext, getItem(holder.getAdapterPosition()).getClazz()),
                        options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDemos.size();
    }

    private Demo getItem(int position) {
        return mDemos.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcon;
        private final TextView tvLabel;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_demo_icon);
            tvLabel = itemView.findViewById(R.id.tv_demo_label);
        }
    }
}
