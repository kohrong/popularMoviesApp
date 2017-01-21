package fasttrack.jdeveloper.popularmoviesapp.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Globals;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;

public class MovieDetailFragment extends Fragment {

    private static final String MOVIE = "movie";

    private Movie movie;

    private TextView title;
    private TextView average;
    private TextView releaseDate;
    private TextView synopsis;
    private ImageView poster;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        title = (TextView) view.findViewById(R.id.tv_movie_title);
        title.setText(movie.getOriginal_title());
        average = (TextView) view.findViewById(R.id.tv_movie_average);
        average.setText(String.valueOf(movie.getVote_average()));
        releaseDate = (TextView) view.findViewById(R.id.tv_movie_release_date);
        releaseDate.setText(movie.getRelease_date());
        synopsis = (TextView) view.findViewById(R.id.tv_movie_synopsis);
        synopsis.setText(movie.getOverview());

        if (movie.getPoster_path() != null) {
            poster = (ImageView) view.findViewById(R.id.iv_movie_poster);
            String url = Globals.IMAGE_BASE_URL + "/" + Globals.IMAGE_POSTER_SIZE + movie.getPoster_path();

            Picasso.with(getContext()).load(url)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.rectangle)
                    .error(R.drawable.rectangle)
                    .into(poster);
        }

        return view;
    }
}
