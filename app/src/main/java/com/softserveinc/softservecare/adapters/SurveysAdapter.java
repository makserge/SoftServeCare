package com.softserveinc.softservecare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.api.FirebaseApi;
import com.softserveinc.softservecare.interfaces.SurveysItemClickListener;
import com.softserveinc.softservecare.model.Survey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smakoh on 16.03.2016.
 */
public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.ViewHolder> {
    private final SurveysItemClickListener mItemClickListener;
    private final RecyclerView mRecyclerView;
    private int mSelectedItem = 0;
    private List<Survey> mItems = new ArrayList<Survey>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public ImageView mDoneImageView;

        public ViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
            mDoneImageView = (ImageView) view.findViewById(R.id.doneImageView);
        }
    }

    public SurveysAdapter(SurveysItemClickListener itemClickListener, RecyclerView recyclerView) {
        mItemClickListener = itemClickListener;
        mRecyclerView = recyclerView;
    }

    public void updateItems(List<Survey> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.surveys_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Survey item = mItems.get(position);

        holder.mTitleTextView.setText(item.getTitle());

        if (FirebaseApi.getInstance().isSurveyAnswered(item.getId())) {
            holder.mDoneImageView.setVisibility(View.VISIBLE);
        }
        else {
            holder.mDoneImageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyItemChanged(mSelectedItem);
                mSelectedItem = mRecyclerView.getChildAdapterPosition(v);
                notifyItemChanged(mSelectedItem);

                mItemClickListener.openItem(mItems.get(mSelectedItem));
            }
        });

        holder.itemView.setSelected(mSelectedItem == position);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }
}