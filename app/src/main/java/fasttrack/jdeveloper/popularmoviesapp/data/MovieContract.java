package fasttrack.jdeveloper.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jdeveloper on 21/1/17.
 */

public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "fasttrack.jdeveloper.popularmoviesapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movieSynopsis";
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_POSTER_PATH = "moviePosterPath";
    }
}
