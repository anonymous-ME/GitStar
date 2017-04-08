package anonymousme.gitstar.Activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import anonymousme.gitstar.Adapter.ProjectAdapter;
import anonymousme.gitstar.Const.Users;
import anonymousme.gitstar.Interface.GitHubAPI;
import anonymousme.gitstar.Models.Project;
import anonymousme.gitstar.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjectAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = new ProgressDialog(Home.this);
        mProgress.setCancelable(false);
        mProgress.setMessage("Loading...");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ProjectAdapter(Home.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);



        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        update();
    }



    private void update(){
        if (!swipeContainer.isRefreshing())
            mProgress.show();
        mAdapter.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubAPI api = retrofit.create(GitHubAPI.class);

        for(final String usr : Users.usernames) {

            Call<List<Project>> projectListCall = api.getProjectList(usr, "14c11d585eb69aaa23b8022f95c73cae7c442d5d");

            projectListCall.enqueue(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    for (Project project:response.body())
                        mAdapter.add(project);

                    if(usr.equals("jaredly")){
                        if (swipeContainer.isRefreshing())
                            swipeContainer.setRefreshing(false);
                        else
                            mProgress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {

                }
            });
        }

        mAdapter.randomize();
        mAdapter.notifyDataSetChanged();
    }

}