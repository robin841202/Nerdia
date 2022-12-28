package com.robinhsueh.movieinfo.view.adapter;

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
import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.robinhsueh.movieinfo.model.person.PersonData;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.common.base.Strings;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private final String LOG_TAG = "PeopleAdapter";

    private ArrayList<PersonData> people_list;
    private final IPeopleListener listener;

    public interface IPeopleListener {
        /**
         * Person item onClick Event
         */
        void onPersonClick(PersonData person);
    }


    public PeopleAdapter(IPeopleListener listener) {
        this.people_list = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PeopleViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        holder.bind(people_list.get(position));
    }

    @Override
    public int getItemCount() {
        return people_list.size();
    }

    public void appendPeople(ArrayList<PersonData> people) {
        int startPosition = people_list.size();
        people_list.addAll(people);
        // refresh partially
        notifyItemRangeInserted(startPosition, people.size());
    }

    public void setPeople(ArrayList<PersonData> people) {
        people_list = people;
        notifyDataSetChanged();
    }

    public void removeAll() {
        people_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class PeopleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile;
        private final TextView title;
        private final TextView subTitle;
        private final IPeopleListener listener;
        private final ShimmerDrawable shimmerDrawable;

        public PeopleViewHolder(@NonNull View itemView, IPeopleListener listener) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.img_profile);
            this.title = itemView.findViewById(R.id.text_main_name);
            this.subTitle = itemView.findViewById(R.id.text_sub_name);

            this.listener = listener;

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

        public void bind(PersonData personData) {
            if (!Strings.isNullOrEmpty(personData.getProfilePath())) {
                String imgUrl = StaticParameter.getTmdbImageUrl(StaticParameter.ProfileSize.W185, personData.getProfilePath());
                // set profile image
                Glide.with(itemView)
                        .load(imgUrl)
                        .placeholder(shimmerDrawable)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_profile)
                        .centerCrop()
                        .into(profile);
            }

            // set name
            title.setText(personData.getName());

            // hide subTitle TextView
            subTitle.setVisibility(View.GONE);

            // set item onClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // callback onPersonClick method and pass personData
                    listener.onPersonClick(personData);
                }
            });

        }

    }


}
