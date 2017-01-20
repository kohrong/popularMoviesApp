package fasttrack.jdeveloper.popularmoviesapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import fasttrack.jdeveloper.popularmoviesapp.enums.SpinnerSortCriteria;
import fasttrack.jdeveloper.popularmoviesapp.listeners.EndlessRecyclerViewScrollListener;
import fasttrack.jdeveloper.popularmoviesapp.models.Globals;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;
import fasttrack.jdeveloper.popularmoviesapp.models.MovieDBConfiguration;
import fasttrack.jdeveloper.popularmoviesapp.models.MoviesWrapper;
import fasttrack.jdeveloper.popularmoviesapp.network.MovieDBReactiveApi;
import fasttrack.jdeveloper.popularmoviesapp.views.adapters.MoviesAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MoviesAdapter.ListItemClickListener{

    private static final String SPINNER_POSITION = "SPINNER_POSITION";
    private static final String MOVIE = "MOVIE";
    private ArrayList<Movie> movies = new ArrayList<>();
    private MoviesWrapper moviesWrapper;
    private MoviesAdapter moviesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int sortCriteria = 0;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            sortCriteria = savedInstanceState.getInt(SPINNER_POSITION, 0);
        }

        getMovieConfigurationByRx();
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
        spinner.setSelection(sortCriteria);
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
                getMovies(moviesWrapper.getPage() + 1);
            }
        };
        mMoviesRecylerView.addOnScrollListener(scrollListener);
    }

    private void getMovies(int page) {
        String currentSortCriteria = (String) spinner.getSelectedItem();
        if (getString(R.string.most_popular).equals(currentSortCriteria)) {
            getPopularMoviesByRx(page);
        }
        else if (getString(R.string.best_rated).equals(currentSortCriteria)) {
            getBestRatedMoviesByRx(page);
        }
        else if (getString(R.string.favorites).equals(currentSortCriteria)) {
            getFavoritesMovies();
        }
    }

    private void getMovieConfigurationByRx() {
        MovieDBReactiveApi movieDBReactiveApi = new MovieDBReactiveApi();
        Observable<MovieDBConfiguration> configurationObservable = movieDBReactiveApi.apiInterface.getConfiguration();
        configurationObservable.subscribeOn(Schedulers.io())
                .subscribe(configuration -> {
                    Log.d("TEST_RX", configuration.getImages().getBase_url());
                    setConfiguration(configuration);
                });
    }

    private void setConfiguration(MovieDBConfiguration configuration) {
        Globals.IMAGE_BASE_URL = configuration.getImages().getBase_url();
        Globals.IMAGE_POSTER_SIZE = configuration.getImages().getPoster_sizes().get(2);
    }

    private void getPopularMoviesByRx(final int page) {
        if (Globals.IMAGE_POSTER_SIZE == null || Globals.IMAGE_POSTER_SIZE.isEmpty()) {
            getMovieConfigurationByRx();
        }

        MovieDBReactiveApi movieDBReactiveApi = new MovieDBReactiveApi();
        Observable<MoviesWrapper> moviesWrapperObservable = movieDBReactiveApi.apiInterface.getPopularMovies(page);
        moviesWrapperSubscriber(moviesWrapperObservable, page, SpinnerSortCriteria.MOST_POPULAR);
    }

    private void getBestRatedMoviesByRx(final int page) {
        if (Globals.IMAGE_POSTER_SIZE == null || Globals.IMAGE_POSTER_SIZE.isEmpty()) {
            getMovieConfigurationByRx();
        }

        MovieDBReactiveApi movieDBReactiveApi = new MovieDBReactiveApi();
        Observable<MoviesWrapper> moviesWrapperObservable = movieDBReactiveApi.apiInterface.getBestRatedMovies(page);
        moviesWrapperSubscriber(moviesWrapperObservable, page, SpinnerSortCriteria.BEST_RATED);
    }

    private void getFavoritesMovies() {
        //TODO get favorites movies from the database via content provider and update the UI
    }

    private void moviesWrapperSubscriber(Observable<MoviesWrapper> observable, final int page, int sortCriteria) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesWrapper -> {
                    this.moviesWrapper = moviesWrapper;
                    updateMovieList(page);
                    this.sortCriteria = sortCriteria;
                }, error -> {
                    if (error instanceof UnknownHostException) {
                        showErrorNetworkDialog();
                    }
                });
    }

    private void showErrorNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.no_internet));
        builder.setMessage(getString(R.string.no_internet_body));

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
            finish();
        });

        builder.setPositiveButton(getString(R.string.retry), (dialog, which) -> {
            dialog.dismiss();
            getMovies(1);
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
        getMovies(1);
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
