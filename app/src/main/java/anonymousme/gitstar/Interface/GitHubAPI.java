package anonymousme.gitstar.Interface;

import java.util.List;

import anonymousme.gitstar.Models.Project;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by affan on 7/4/17.
 */
public interface GitHubAPI {
    @GET("users/{user}/starred")
    Call<List<Project>> getProjectList(@Path("user") String user , @Query("access_token") String access_token);
}
