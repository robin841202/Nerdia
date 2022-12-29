package com.robinhsueh.nerdia.view.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.robinhsueh.nerdia.R;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.view.PersonDetailsActivity;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.common.base.Strings;
import com.robinhsueh.nerdia.model.CreditsResponse;

import java.util.ArrayList;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    private final String LOG_TAG = "CrewAdapter";

    private ArrayList<CreditsResponse.CrewData> crew_list;
    private final AppCompatActivity context;

    public CrewAdapter(AppCompatActivity context) {
        this.crew_list = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new CrewViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        holder.bind(crew_list.get(position));
    }

    @Override
    public int getItemCount() {
        return crew_list.size();
    }


    public void setItems(ArrayList<CreditsResponse.CrewData> crew) {
        crew_list = crew;
        notifyDataSetChanged();
    }

    public void removeAll() {
        crew_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    static class CrewViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile;
        private final TextView memberName;
        private final TextView jobName;
        private final AppCompatActivity context;
        private final ShimmerDrawable shimmerDrawable;

        public CrewViewHolder(@NonNull View itemView, AppCompatActivity context) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.img_profile);
            this.memberName = itemView.findViewById(R.id.text_main_name);
            this.jobName = itemView.findViewById(R.id.text_sub_name);

            this.context = context;

            // region Create image placeholder animation using shimmer
            Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                    .setBaseColor(ContextCompat.getColor(itemView.getContext(), R.color.gray))
                    .setBaseAlpha(1)
                    .setHighlightColor(ContextCompat.getColor(itemView.getContext(), R.color.lightGray))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();
            this.shimmerDrawable = new ShimmerDrawable();
            this.shimmerDrawable.setShimmer(shimmer);
            // endregion
        }

        public void bind(CreditsResponse.CrewData crewData) {
            if (!Strings.isNullOrEmpty(crewData.getProfile_path())) {
                String imgUrl = StaticParameter.getTmdbImageUrl(StaticParameter.ProfileSize.W185, crewData.getProfile_path());
                // set profile image
                Glide.with(itemView)
                        .load(imgUrl)
                        .placeholder(shimmerDrawable)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_profile)
                        .centerCrop()
                        .into(profile);
            }

            // set cast name
            memberName.setText(crewData.getName());

            // set character name
            jobName.setText(crewData.getJob());

            // set item onClickListener
            itemView.setOnClickListener(v -> {
                navigateToPersonDetail(crewData.getId());
            });

        }


        private void navigateToPersonDetail(long id) {
            Intent intent = new Intent(context, PersonDetailsActivity.class);
            intent.putExtra(StaticParameter.ExtraDataKey.EXTRA_DATA_PERSON_ID_KEY, id);
            context.startActivity(intent);
            // set the custom transition animation
            context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }


}
