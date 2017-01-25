package fasttrack.jdeveloper.popularmoviesapp.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.listeners.EndlessRecyclerViewScrollListener;
import fasttrack.jdeveloper.popularmoviesapp.models.Review;
import fasttrack.jdeveloper.popularmoviesapp.models.ReviewsWrapper;
import fasttrack.jdeveloper.popularmoviesapp.network.MovieDBReactiveApi;
import fasttrack.jdeveloper.popularmoviesapp.views.adapters.ReviewsAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MovieReviewsFragment extends Fragment {

    private static final String MOVIE_ID = "MOVIE_ID";

    private String movieId;
    private ReviewsAdapter reviewsAdapter;
    private ReviewsWrapper reviewsWrapper;
    private ArrayList<Review> reviews = new ArrayList<>();
    private EndlessRecyclerViewScrollListener scrollListener;

    public MovieReviewsFragment() {
        // Required empty public constructor
    }

    public static MovieReviewsFragment newInstance(String movieId) {
        MovieReviewsFragment fragment = new MovieReviewsFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getString(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        setUpRecyclerView(view);
        getReviews(1);

        return view;
    }

    private void setUpRecyclerView(View view) {
        RecyclerView reviewsRecyclerView = (RecyclerView) view.findViewById(R.id.rv_reviews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        reviewsAdapter = new ReviewsAdapter(reviews);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getReviews(reviewsWrapper.getPage() + 1);
            }
        };
        reviewsRecyclerView.addOnScrollListener(scrollListener);
    }

    private void getReviews(int page) {
        MovieDBReactiveApi movieDBReactiveApi = new MovieDBReactiveApi();
        Observable<ReviewsWrapper> reviewsWrapperObservable = movieDBReactiveApi.apiInterface.getReviews(movieId, page);
        reviewsWrapperObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewsWrapper -> {
                    this.reviewsWrapper = reviewsWrapper;
                    updateReviewList(page);
                }, error -> {
                    if (error instanceof UnknownHostException) {
                        showErrorNetworkDialog();
                    }
                });
    }

    private void updateReviewList(int page) {
        if (page == 1) {
            reviews.clear();
            reviews = reviewsWrapper.getResults();
            scrollListener.resetState();
        }
        else {
            reviews.addAll(reviewsWrapper.getResults());
        }
        reviewsAdapter.updateReviews(reviews);
    }

    private void showErrorNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.no_internet));
        builder.setMessage(getString(R.string.no_internet_body));

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });

        builder.setPositiveButton(getString(R.string.retry), (dialog, which) -> {
            dialog.dismiss();
            getReviews(1);
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
        Toast.makeText(getContext(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
    }
}
