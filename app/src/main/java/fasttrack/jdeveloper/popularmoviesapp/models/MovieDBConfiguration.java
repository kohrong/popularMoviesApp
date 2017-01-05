package fasttrack.jdeveloper.popularmoviesapp.models;

import java.util.ArrayList;

/**
 * Created by jdeveloper on 3/1/17.
 */

public class MovieDBConfiguration {

    private ImageConfiguration images;
    private ArrayList<String> change_keys;

    public MovieDBConfiguration(ImageConfiguration images, ArrayList<String> change_keys) {
        this.images = images;
        this.change_keys = change_keys;
    }

    public ImageConfiguration getImages() {
        return images;
    }

    public void setImages(ImageConfiguration images) {
        this.images = images;
    }

    public ArrayList<String> getChange_keys() {
        return change_keys;
    }

    public void setChange_keys(ArrayList<String> change_keys) {
        this.change_keys = change_keys;
    }
}
