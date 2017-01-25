package fasttrack.jdeveloper.popularmoviesapp.network;

import fasttrack.jdeveloper.popularmoviesapp.models.MovieDBConfiguration;
import fasttrack.jdeveloper.popularmoviesapp.models.MoviesWrapper;
import fasttrack.jdeveloper.popularmoviesapp.models.ReviewsWrapper;
import fasttrack.jdeveloper.popularmoviesapp.models.TrailersWrapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jdeveloper on 7/1/17.
 */

public class MovieDBReactiveApi {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String apikey = "insert your apikey here";

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(
                    chain -> {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", apikey)
                                .build();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }).build();

    Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build();

    public MovieDBApiInterface apiInterface = retrofit.create(MovieDBApiInterface.class);

    public interface MovieDBApiInterface {

        @GET("configuration")
        Observable<MovieDBConfiguration> getConfiguration();

        @GET("movie/popular")
        Observable<MoviesWrapper> getPopularMovies(@Query("page") Integer page);

        @GET("movie/top_rated")
        Observable<MoviesWrapper> getBestRatedMovies(@Query("page") Integer page);

        @GET("movie/{movie_id}/videos")
        Observable<TrailersWrapper> getTrailers(@Path("movie_id") String movieId);

        @GET("movie/{movie_id}/reviews")
        Observable<ReviewsWrapper> getReviews(@Path("movie_id") String movieId, @Query("page") Integer page);
    }
}
