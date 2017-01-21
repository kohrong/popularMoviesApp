package fasttrack.jdeveloper.popularmoviesapp.models;

/**
 * Created by jdeveloper on 20/1/17.
 */

public class Trailer {

    private String id;
    private String iso_639_1;
    private String iso3166_1;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

    public Trailer(String id, String iso_639_1, String iso3166_1, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso3166_1 = iso3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso3166_1() {
        return iso3166_1;
    }

    public void setIso3166_1(String iso3166_1) {
        this.iso3166_1 = iso3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
