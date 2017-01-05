package fasttrack.jdeveloper.popularmoviesapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import fasttrack.jdeveloper.popularmoviesapp.models.MovieDBConfiguration;
import fasttrack.jdeveloper.popularmoviesapp.models.MoviesWrapper;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jdeveloper on 3/1/17.
 */

public class MovieDBApi {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String apikey = "put your apikey here";

    OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor(
                    new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
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
                        }
                    }).build();

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(defaultHttpClient)
            .build();

    public ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    public interface ApiInterface {

        @GET("configuration")
        Call<MovieDBConfiguration> getConfiguration();

        @GET("movie/popular")
        Call<MoviesWrapper> getPopularMovies(@Query("page") Integer page);

        @GET("movie/top_rated")
        Call<MoviesWrapper> getBestRatedMovies(@Query("page") Integer page);
    }
}
