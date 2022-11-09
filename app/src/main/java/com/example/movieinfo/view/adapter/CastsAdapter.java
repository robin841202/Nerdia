package com.example.movieinfo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieinfo.R;
import com.example.movieinfo.model.CreditsResponse.CastData;
import com.example.movieinfo.model.StaticParameter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.CastsViewHolder> {
    private final String LOG_TAG = "CastsAdapter";

    private ArrayList<CastData> cast_list;
    private final ICastListener listener;

    public interface ICastListener {
        /**
         * Cast item onClick Event
         */
        void onCastClick(CastData video);
    }


    public CastsAdapter(ICastListener listener) {
        this.cast_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public CastsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);
        return new CastsViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CastsViewHolder holder, int position) {
        holder.bind(cast_list.get(position));
    }

    @Override
    public int getItemCount() {
        return cast_list.size();
    }


    public void setCasts(ArrayList<CastData> casts) {
        cast_list = casts;
        notifyDataSetChanged();
    }

    public void removeAll() {
        cast_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class CastsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile;
        private final TextView castName;
        private final TextView characterName;
        private final ICastListener listener;

        public CastsViewHolder(@NonNull View itemView, ICastListener listener) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.img_profile);
            this.castName = itemView.findViewById(R.id.text_main_name);
            this.characterName = itemView.findViewById(R.id.text_sub_name);

            this.listener = listener;

        }

        public void bind(CastData castData) {
            if (!Strings.isNullOrEmpty(castData.getProfile_path())) {
                String imgUrl = StaticParameter.getImageUrl(StaticParameter.ProfileSize.W185, castData.getProfile_path());
                // set profile image and use circleCrop
                Glide.with(itemView)
                        .load(imgUrl)
                        .placeholder(R.drawable.ic_profile)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_profile)
                        .circleCrop()
                        .into(profile);
            }

            // set cast name
            castName.setText(castData.getName());

            // set character name
            characterName.setText(castData.getCharacterName());

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onCastClick method and pass castData
                    listener.onCastClick(castData);
                }
            });

        }

    }


}
