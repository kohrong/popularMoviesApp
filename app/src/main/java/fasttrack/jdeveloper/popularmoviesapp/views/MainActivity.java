package fasttrack.jdeveloper.popularmoviesapp.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.listeners.EndlessRecyclerViewScrollListener;
import fasttrack.jdeveloper.popularmoviesapp.models.Globals;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;
import fasttrack.jdeveloper.popularmoviesapp.models.MovieDBConfiguration;
import fasttrack.jdeveloper.popularmoviesapp.models.MoviesWrapper;
import fasttrack.jdeveloper.popularmoviesapp.network.MovieDBApi;
import fasttrack.jdeveloper.popularmoviesapp.views.activities.MovieDetailActivity;
import fasttrack.jdeveloper.popularmoviesapp.views.adapters.MoviesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MoviesAdapter.ListItemClickListener{

    private static final String SPINNER_POSITION = "SPINNER_POSITION";
    private static final String MOVIE = "MOVIE";
    private ArrayList<Movie> movies = new ArrayList<>();
    private MoviesWrapper moviesWrapper;
    private MoviesAdapter moviesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Boolean popularMovies = true;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            popularMovies = savedInstanceState.getBoolean(SPINNER_POSITION, true);
        }

        getMovieConfiguration();
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (spinner != null)
            outState.putBoolean(SPINNER_POSITION, spinner.getSelectedItemPosition() == 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movies_sorting_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (popularMovies) {
            spinner.setSelection(0);
        }
        else {
            spinner.setSelection(1);
        }
        return true;
    }

    private void initRecyclerView() {
        RecyclerView mMoviesRecylerView = (RecyclerView) findViewById(R.id.rv_movies);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mMoviesRecylerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(movies, this);
        mMoviesRecylerView.setAdapter(moviesAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };
        mMoviesRecylerView.addOnScrollListener(scrollListener);
    }

    private void loadNextDataFromApi() {
        if (getString(R.string.most_popular).equals(spinner.getSelectedItem())) {
            getPopularMovies(moviesWrapper.getPage() + 1);
        }
        else {
            getBestRatedMovies(moviesWrapper.getPage() + 1);
        }
    }

    private void getMovieConfiguration() {
        MovieDBApi movieDBApi = new MovieDBApi();
        Call<MovieDBConfiguration> call = movieDBApi.apiInterface.getConfiguration();
        call.enqueue(new Callback<MovieDBConfiguration>() {
            @Override
            public void onResponse(Call<MovieDBConfiguration> call, Response<MovieDBConfiguration> response) {
                Globals.IMAGE_BASE_URL = response.body().getImages().getBase_url();
                Globals.IMAGE_POSTER_SIZE = response.body().getImages().getPoster_sizes().get(2);
            }

            @Override
            public void onFailure(Call<MovieDBConfiguration> call, Throwable t) {

            }
        });
    }

    private void getPopularMovies(final int page) {
        if (Globals.IMAGE_POSTER_SIZE == null || Globals.IMAGE_POSTER_SIZE.isEmpty()) {
            getMovieConfiguration();
        }
        final MovieDBApi movieDBApi = new MovieDBApi();
        Call<MoviesWrapper> call = movieDBApi.apiInterface.getPopularMovies(page);
        call.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(Call<MoviesWrapper> call, Response<MoviesWrapper> response) {
                if (response.isSuccessful()) {
                    moviesWrapper = response.body();
                    updateMovieList(page);
                    popularMovies = true;
                }
            }

            @Override
            public void onFailure(Call<MoviesWrapper> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    showErrorNetworkDialog();
                }
            }
        });
    }

    private void getBestRatedMovies(final int page) {
        if (Globals.IMAGE_POSTER_SIZE == null || Globals.IMAGE_POSTER_SIZE.isEmpty()) {
            getMovieConfiguration();
        }
        final MovieDBApi movieDBApi = new MovieDBApi();
        Call<MoviesWrapper> call = movieDBApi.apiInterface.getBestRatedMovies(page);
        call.enqueue(new Callback<MoviesWrapper>() {
            @Override
            public void onResponse(Call<MoviesWrapper> call, Response<MoviesWrapper> response) {
                if (response.isSuccessful()) {
                    moviesWrapper = response.body();
                    updateMovieList(page);
                    popularMovies = false;
                }
            }

            @Override
            public void onFailure(Call<MoviesWrapper> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    showErrorNetworkDialog();
                }
            }
        });
    }

    private void showErrorNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.no_internet));
        builder.setMessage(getString(R.string.no_internet_body));

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if (getString(R.string.most_popular).equals(spinner.getSelectedItem())) {
                    getPopularMovies(1);
                }
                else {
                    getBestRatedMovies(1);
                }
            }
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
        Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
    }

    private void updateMovieList(int page) {
        if (page == 1) {
            movies.clear();
            movies = moviesWrapper.getResults();
            scrollListener.resetState();
        }
        else {
            movies.addAll(moviesWrapper.getResults());
        }
        moviesAdapter.setMovies(movies);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (getString(R.string.most_popular).equals(adapterView.getItemAtPosition(i))) {
            getPopularMovies(1);
        }
        else {
            getBestRatedMovies(1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE, movies.get(position));
        startActivity(intent);
    }
}
