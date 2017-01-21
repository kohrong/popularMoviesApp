package fasttrack.jdeveloper.popularmoviesapp.views.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieDetailFragment;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieReviewsFragment;
import fasttrack.jdeveloper.popularmoviesapp.views.fragments.MovieTrailersFragment;

public class MovieDetailActivity extends AppCompatActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
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

        // Instantiate a ViewPager and a PagerAdapter.
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
                toggleFavoriteIcon(item);
                //TODO insert or remove the movie from the database based on current value of isFavoriteMovie
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleFavoriteIcon(MenuItem item) {
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
        setFavoriteIcon(menu.findItem(R.id.action_favorite));
        return super.onCreateOptionsMenu(menu);
    }

    private void setFavoriteIcon(MenuItem item) {
        item.setIcon(isFavoriteMovie ? R.mipmap.ic_fav_filled : R.mipmap.ic_fav_unfilled);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
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
                        movieReviewsFragment = MovieReviewsFragment.newInstance(null, null);
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
