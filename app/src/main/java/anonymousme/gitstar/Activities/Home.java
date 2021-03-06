package anonymousme.gitstar.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref= getSharedPreferences("myPref", Home.this.MODE_PRIVATE);
        mProgress = new ProgressDialog(Home.this);
        mProgress.setCancelable(false);
        mProgress.setMessage("Loading...");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ProjectAdapter(Home.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Home.this);
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
        for (final String usr : Users.usernames) {

            Call<List<Project>> projectListCall = api.getProjectList(usr, sharedPref.getString("user_id", ""));

            projectListCall.enqueue(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    try {
                        for (Project project : response.body())
                            mAdapter.add(project);

                        if (usr.equals("jaredly")) {
                            if (swipeContainer.isRefreshing())
                                swipeContainer.setRefreshing(false);
                            else
                                mProgress.dismiss();
                            mAdapter.randomize();
                        }
                    } catch (Exception e) {
                        if (e.getMessage().contains("invoke interface method")) {
                            sharedPref= getSharedPreferences("myPref", Home.this.MODE_PRIVATE);
                            editor=sharedPref.edit();
                            editor.putString("user_id", "0");
                            editor.commit();
                            finish();
                            Toast.makeText(Home.this,"Your access token is expired/invalid.",Toast.LENGTH_SHORT).show();
                            Home.this.startActivity(new Intent(Home.this, Login.class));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {
                    if (swipeContainer.isRefreshing())
                        swipeContainer.setRefreshing(false);
                    else
                        mProgress.dismiss();
                    Toast.makeText(Home.this,"Unable to load the repositories.",Toast.LENGTH_SHORT).show();
                }
            });
        }
        mAdapter.randomize();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    public void logout(MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Would you like to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    sharedPref= getSharedPreferences("myPref", Home.this.MODE_PRIVATE);
                    editor=sharedPref.edit();

                    editor.putString("user_id", "0");
                    editor.commit();

                    finish();
                    Home.this.startActivity(new Intent(Home.this, Login.class));
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // user doesn't want to logout
            }
        })
        .show();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}