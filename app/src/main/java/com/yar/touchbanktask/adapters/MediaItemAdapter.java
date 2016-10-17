package com.yar.touchbanktask.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yar.touchbanktask.R;
import com.yar.touchbanktask.entity.json.media.Caption;
import com.yar.touchbanktask.entity.json.media.MediaItem;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemHolder> {

    private List<MediaItem> mMediaItems;

    private Context mContext;

    public MediaItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MediaItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new MediaItemHolder(v);
    }

    @Override
    public void onBindViewHolder(MediaItemHolder holder, int position) {
        MediaItem mediaItem = mMediaItems.get(position);

        Caption caption = mediaItem.getCaption();

        holder.title.setText(caption != null ? caption.getText() : "");


        String date = DateFormat.format("dd MMM yyyy, EEE\nkk:mm", mediaItem.getCreatedTime() * 1000).toString();

        holder.created.setText(date);

        String url = mediaItem.getImages().getStandardResolution().getUrl();

        if (url != null) {
            Glide.with(mContext).load(url).centerCrop().into(holder.photo);
        }

    }

    @Override
    public int getItemCount() {
        return mMediaItems != null ? mMediaItems.size() : 0;
    }

    public void setMediaItems(List<MediaItem> freshItems) {
        if (mMediaItems == null) { // if empty list simply add everything
            mMediaItems = freshItems;
            notifyDataSetChanged();
        } else { // find changes and sort from new to old

            int initialSize = mMediaItems.size();

            mMediaItems.retainAll(freshItems);

            for (MediaItem mediaItem : freshItems) {
                if (!mMediaItems.contains(mediaItem)) {
                    mMediaItems.add(mediaItem);
                }
            }

            boolean changed = mMediaItems.size() != initialSize;
            if (changed) {
                Collections.sort(mMediaItems);
                notifyDataSetChanged();
            }
        }
    }

    public class MediaItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.media_item_title)
        protected TextView title;

        @BindView(R.id.media_item_created)
        protected TextView created;

        @BindView(R.id.media_item_image)
        protected ImageView photo;

        public MediaItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
