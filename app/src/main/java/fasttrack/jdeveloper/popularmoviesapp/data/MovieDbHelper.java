package fasttrack.jdeveloper.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jdeveloper on 21/1/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 4;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RATING + " DOUBLE, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
