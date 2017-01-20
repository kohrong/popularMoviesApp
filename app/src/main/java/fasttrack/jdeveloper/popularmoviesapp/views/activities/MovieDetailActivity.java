package fasttrack.jdeveloper.popularmoviesapp.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Globals;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE = "MOVIE";
    private Movie movie;
    @BindView(R.id.tv_movie_title) TextView title;
    @BindView(R.id.tv_movie_average) TextView average;
    @BindView(R.id.tv_movie_release_date) TextView releaseDate;
    @BindView(R.id.tv_movie_synopsis) TextView synopsis;
    @BindView(R.id.iv_movie_poster) ImageView poster;
    private Boolean isFavoriteMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = getIntent().getParcelableExtra(MOVIE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title.setText(movie.getOriginal_title());
        average.setText(String.valueOf(movie.getVote_average()));
        releaseDate.setText(movie.getRelease_date());
        synopsis.setText(movie.getOverview());

        if (movie.getPoster_path() != null) {
            String url = Globals.IMAGE_BASE_URL + "/" + Globals.IMAGE_POSTER_SIZE + movie.getPoster_path();

            Picasso.with(this).load(url)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.rectangle)
                    .error(R.drawable.rectangle)
                    .into(poster);
        }
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
}
