package fasttrack.jdeveloper.popularmoviesapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import java.util.ArrayList;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Review;

/**
 * Created by jdeveloper on 20/1/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private ArrayList<Review> reviews;

    public ReviewsAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.cell_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void updateReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder  extends RecyclerView.ViewHolder {

        private TextView author;
        private DocumentView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.tv_review_author);
            content = (DocumentView) itemView.findViewById(R.id.tv_review_content);
        }

        public void bind(Review review) {
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
    }
}
