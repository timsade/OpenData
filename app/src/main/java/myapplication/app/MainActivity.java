package myapplication.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{


    private TextView tv;
    public static final int RESULT_Main = 1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        startActivityForResult(new Intent(MainActivity.this, Login.class), RESULT_Main);

    }

    private void startup(Intent i)
    {
        // Récupère l'identifiant
        //int user = i.getIntExtra("userid",-1);

        Intent intent = new Intent(MainActivity.this, Data.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == RESULT_Main && resultCode == RESULT_CANCELED)
            finish();
        else
            startup(data);
    }




}