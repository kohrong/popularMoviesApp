package fasttrack.jdeveloper.popularmoviesapp.views.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.data.MovieContract;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieDetailFragment;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieReviewsFragment;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieTrailersFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private static final String MOVIE = "MOVIE";
    private Movie movie;
    private Boolean isFavoriteMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = getIntent().getParcelableExtra(MOVIE);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite:
                Observable<Boolean> insertFavoriteObservable = Observable.just(onClickFavoriteMovie(item))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean onClickFavoriteMovie(MenuItem item) {
        if (isFavoriteMovie) {
            Uri uri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();
            int rowsDeleted = getContentResolver().delete(uri, MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, null);
            if (rowsDeleted > 0) {
                toggleFavoriteMovie(item);
            }
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RATING, movie.getVote_average());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_SYNOPSIS, movie.getOverview());
            contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());
            Uri uri = getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                toggleFavoriteMovie(item);
            }
        }

        return true;
    }

    private void toggleFavoriteMovie(MenuItem item) {
        if (isFavoriteMovie) {
            item.setIcon(R.mipmap.ic_fav_unfilled);
            isFavoriteMovie = false;
        } else {
            item.setIcon(R.mipmap.ic_fav_filled);
            isFavoriteMovie = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);

        Observable<Boolean> getFavoriteObservable = Observable.just(setFavoriteMovie(menu)).subscribeOn(Schedulers.io());
        return super.onCreateOptionsMenu(menu);
    }

    private Boolean setFavoriteMovie(Menu menu) {
        try {
            Uri uri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();
            Cursor cursor = getContentResolver().query(uri,
                    null,
                    MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                    null,
                    MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RATING);

            if (cursor.moveToNext()) isFavoriteMovie = true;
            setFavoriteIcon(menu.findItem(R.id.action_favorite));
            return true;
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
        }
    }

    private void setFavoriteIcon(MenuItem item) {
        item.setIcon(isFavoriteMovie ? R.mipmap.ic_fav_filled : R.mipmap.ic_fav_unfilled);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private MovieDetailFragment movieDetailFragment;
        private MovieTrailersFragment movieTrailersFragment;
        private MovieReviewsFragment movieReviewsFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (movieDetailFragment == null) {
                        movieDetailFragment = MovieDetailFragment.newInstance(movie);
                    }
                    return movieDetailFragment;
                case 1:
                    if (movieTrailersFragment == null) {
                        movieTrailersFragment = MovieTrailersFragment.newInstance(movie.getId().toString());
                    }
                    return movieTrailersFragment;
                case 2:
                    if (movieReviewsFragment == null) {
                        movieReviewsFragment = MovieReviewsFragment.newInstance(movie.getId().toString());
                    }
                    return movieReviewsFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.movie);
                case 1:
                    return getString(R.string.trailers);
                case 2:
                    return getString(R.string.reviews);
            }

            return null;
        }
    }
}
