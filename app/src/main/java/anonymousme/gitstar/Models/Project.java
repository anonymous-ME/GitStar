package anonymousme.gitstar.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by affan on 7/4/17.
 */

public class Project {

    @SerializedName("name")
    @Expose
    private String title;

    @SerializedName("html_url")
    @Expose
    private String url;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("stargazers_count")
    @Expose
    private long stars;

    @SerializedName("forks_count")
    @Expose
    private long forks;

    public Project(String title, String url, String description, long stars, long forks) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.stars = stars;
        this.forks = forks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

}
