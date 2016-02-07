package uk.tomhomewood.whattowatch.titles;

import java.util.List;

/**
 * Created by Tom on 06/02/2016.
 */
public class Title {

    private final int id;
    private TitleType type;
    private String name, tagline, description;
    private String bannerPath, posterPath;
    private int rating;
    //private String infoUrl;
    private List<Genre> genres;

    public Title(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public TitleType getType() {
        return type;
    }

    public Title setType(TitleType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Title setName(String name) {
        this.name = name;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public Title setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Title setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public Title setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public Title setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Title setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public Title setRating(int rating) {
        this.rating = rating;
        return this;
    }
/*
    public String getInfoUrl() {
        return infoUrl;
    }

    public Title setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
        return this;
    }
*/
}