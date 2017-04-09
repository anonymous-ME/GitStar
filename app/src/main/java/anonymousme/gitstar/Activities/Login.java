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
        sharedPref= getSharedPreferences("myPref", Login.this.MODE_PRIVATE);
        editor=sharedPref.edit();

        String token = sharedPref.getString("user_id", "");

        try {
            if (isValidToken(token)) {
                finish();
                Intent intent = new Intent(Login.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Login.this.startActivity(intent);
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
        Intent intent = new Intent(Login.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Login.this.startActivity(intent);

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
        Intent myIntent = new Intent(Login.this, ProjectView.class);
        myIntent.putExtra("url","https://github.com/settings/tokens/new");
        myIntent.putExtra("title", "Github Token");
        Login.this.startActivity(myIntent);
    }
}
