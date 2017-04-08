package anonymousme.gitstar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import anonymousme.gitstar.R;

public class Login extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref= getSharedPreferences("myPref", getApplicationContext().MODE_PRIVATE);
        editor=sharedPref.edit();

        String token = sharedPref.getString("user_id", "");

        try {
            if (isValidToken(token)) {
                getApplicationContext().startActivity(new Intent(getApplicationContext(), Home.class));
            }
        }catch (Exception e){
            editor.putString("user_id", "0");
            editor.commit();
        }

    }

    public void login(View view) {


        // Save your string in SharedPref
        editor.putString("user_id", ((TextView) findViewById(R.id.token)).getText()+"");
        editor.commit();
        finish();
        getApplicationContext().startActivity(new Intent(getApplicationContext(), Home.class));

    }

    boolean isValidToken(String token){
        if(token == null)
            return false;
        else if (token.equals("0"))
            return false;
        else if (token.equals(""))
            return false;
        return true;
    }


    public void sign_up(View view) {
        Intent myIntent = new Intent(getApplicationContext(), ProjectView.class);
        myIntent.putExtra("url","https://github.com/settings/tokens/new");
        myIntent.putExtra("title", "Github Token");
        getApplicationContext().startActivity(myIntent);
    }
}
