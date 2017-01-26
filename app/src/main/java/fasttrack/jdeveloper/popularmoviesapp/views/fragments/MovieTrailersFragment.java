package fasttrack.jdeveloper.popularmoviesapp.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.UnknownHostException;
import java.util.ArrayList;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Trailer;
import fasttrack.jdeveloper.popularmoviesapp.models.TrailersWrapper;
import fasttrack.jdeveloper.popularmoviesapp.network.MovieDBReactiveApi;
import fasttrack.jdeveloper.popularmoviesapp.views.adapters.TrailersAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MovieTrailersFragment extends Fragment implements TrailersAdapter.ListItemClickListener{
    private static final String MOVIE_ID = "MOVIE_ID";
    private static final String YOUTUBE_ORIGIN = "YouTube";

    private String movieId;
    private TrailersAdapter trailersAdapter;
    private RecyclerView trailersRecyclerView;
    private ArrayList<Trailer> trailers = new ArrayList<>();

    public MovieTrailersFragment() {
        // Required empty public constructor
    }

    public static MovieTrailersFragment newInstance(String movieId) {
        MovieTrailersFragment fragment = new MovieTrailersFragment();
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
        View view = inflater.inflate(R.layout.fragment_movie_trailers, container, false);

        getTrailers();
        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        trailersRecyclerView = (RecyclerView) view.findViewById(R.id.rv_trailers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersAdapter = new TrailersAdapter(trailers, this);
        trailersRecyclerView.setAdapter(trailersAdapter);
    }

    private void getTrailers() {
        MovieDBReactiveApi movieDBReactiveApi = new MovieDBReactiveApi();
        Observable<TrailersWrapper> configurationObservable = movieDBReactiveApi.apiInterface.getTrailers(movieId);
        configurationObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailersWrapper -> {
                    trailers = trailersWrapper.getResults();
                    trailersAdapter.updateList(trailers);
                }, error -> {
                    if (error instanceof UnknownHostException) {
                        Log.d("NETWORK", error.getMessage());
                    }
                });
    }

    @Override
    public void onListItemClick(int position) {
        switch (trailers.get(position).getSite()) {
            case YOUTUBE_ORIGIN:
                String url = "https://youtu.be/" + trailers.get(position).getKey();
                startTrailerIntent(url);
        }
    }

    private void startTrailerIntent(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
