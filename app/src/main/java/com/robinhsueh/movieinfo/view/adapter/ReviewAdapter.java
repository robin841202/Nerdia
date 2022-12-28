package com.robinhsueh.movieinfo.view.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
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
import com.robinhsueh.movieinfo.R;
import com.robinhsueh.movieinfo.model.ReviewsResponse;
import com.robinhsueh.movieinfo.model.ReviewsResponse.ReviewData;
import com.robinhsueh.movieinfo.model.StaticParameter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.button.MaterialButton;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.github.giangpham96.expandabletextview.ExpandableTextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final String LOG_TAG = "ReviewAdapter";

    private ArrayList<ReviewData> review_list;
    private final AppCompatActivity context;

    public ReviewAdapter(AppCompatActivity context) {
        this.review_list = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(review_list.get(position));
    }

    @Override
    public int getItemCount() {
        return review_list.size();
    }

    public void appendItems(ArrayList<ReviewData> items) {
        int startPosition = review_list.size();
        review_list.addAll(items);
        // refresh partially
        notifyItemRangeInserted(startPosition, items.size());
    }

    public void setItems(ArrayList<ReviewData> items) {
        review_list = items;
        notifyDataSetChanged();
    }

    public void removeAll() {
        review_list.clear();
        notifyDataSetChanged();
    }


    /**
     * ViewHolder which set data to views in one itemView
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatar;
        private final TextView authorName;
        private final TextView reviewTime;
        private ExpandableTextView content;
        private MaterialButton externalSourceBtn;
        private MaterialButton ratingBtn;
        private final ShimmerDrawable shimmerDrawable;
        private final AppCompatActivity context;

        public ReviewViewHolder(@NonNull View itemView, AppCompatActivity context) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.img_avatar);
            this.authorName = itemView.findViewById(R.id.text_author_name);
            this.reviewTime = itemView.findViewById(R.id.text_review_time);
            this.content = itemView.findViewById(R.id.expandText_review);
            this.externalSourceBtn = itemView.findViewById(R.id.btn_external_source);
            this.ratingBtn = itemView.findViewById(R.id.btn_rating);

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

        public void bind(ReviewData reviewData) {

            ReviewsResponse.AuthorDetail authorDetail = reviewData.getAuthorDetail();
            if (authorDetail != null) {

                // set avatar
                if (!Strings.isNullOrEmpty(authorDetail.avatarPath)) {
                    String imgUrl;
                    if (authorDetail.avatarPath.contains("gravatar")) { // Using gravatar
                        // result: https://www.gravatar.com/avatar/bf3b87ecb40599290d764e6d73c86319.jpg
                        imgUrl = authorDetail.avatarPath.substring(1);
                    } else { // image file on tmdb
                        imgUrl = StaticParameter.getTmdbImageUrl(StaticParameter.ProfileSize.W45, authorDetail.avatarPath);
                    }

                    // set avatar image
                    Glide.with(itemView)
                            .load(imgUrl)
                            .placeholder(shimmerDrawable)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_profile)
                            .centerCrop()
                            .into(avatar);
                }

                // set rating text
                ratingBtn.setText(String.valueOf(authorDetail.rating));
            }

            // set name
            authorName.setText(reviewData.getAuthor());

            // set review time
            reviewTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TAIWAN).format(reviewData.getUpdateTime()));

            // set content - using ExpandableTextView library setOriginalText function to show contents, do not use setText
            content.setOriginalText(Html.fromHtml(reviewData.getContent()).toString());

            // set externalSourceBtn
            if (!Strings.isNullOrEmpty(reviewData.getSourceUrl())) {
                // show button
                externalSourceBtn.setVisibility(View.VISIBLE);
                // set listener
                externalSourceBtn.setOnClickListener(v -> {
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewData.getSourceUrl()));
                    context.startActivity(urlIntent);
                });
            } else {
                // hide button
                externalSourceBtn.setVisibility(View.GONE);
            }
        }
    }
}
