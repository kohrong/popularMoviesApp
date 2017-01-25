package fasttrack.jdeveloper.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by jdeveloper on 3/1/17.
 */

public class MoviesWrapper {
    private int page;
    private ArrayList<Movie> results;
    private Integer total_results;
    private Integer total_pages;

    public MoviesWrapper() {
    }

    public MoviesWrapper(int page, ArrayList<Movie> results, Integer total_results, Integer total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }
}
