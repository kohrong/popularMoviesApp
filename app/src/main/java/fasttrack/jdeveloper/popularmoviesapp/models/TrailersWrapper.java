package fasttrack.jdeveloper.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by jdeveloper on 20/1/17.
 */

public class TrailersWrapper {
    private Integer id;
    private ArrayList<Trailer> results;

    public TrailersWrapper(Integer id, ArrayList<Trailer> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }
}
