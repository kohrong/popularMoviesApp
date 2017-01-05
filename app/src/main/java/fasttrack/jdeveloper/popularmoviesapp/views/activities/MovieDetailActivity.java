package fasttrack.jdeveloper.popularmoviesapp.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Globals;
import fasttrack.jdeveloper.popularmoviesapp.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE = "MOVIE";
    private Movie movie;
    private TextView title, average, releaseDate, synopsis;
    private ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = (Movie) getIntent().getSerializableExtra(MOVIE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.tv_movie_title);
        average = (TextView) findViewById(R.id.tv_movie_average);
        releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        synopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
        poster = (ImageView) findViewById(R.id.iv_movie_poster);

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
        }
        return super.onOptionsItemSelected(item);
    }
}
