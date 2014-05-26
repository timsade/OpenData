package myapplication.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity
{

    Button btnRegister;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;

    private static String KEY_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);

        /********************************/
    /* Définit le nom de l'Activity */
        /********************************/

        setTitle("Enregistrer nouveau compte");

        /**********************************************************/
    /* Importation des caractéristiques des champs et boutons */
        /**********************************************************/

        inputFullName = (EditText) findViewById(R.id.et_user);
        inputEmail = (EditText) findViewById(R.id.et_mail);
        inputPassword = (EditText) findViewById(R.id.et_passwd);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        /*******************************/
    /* Clic sur le bouton Register */
        /*******************************/

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                /***************************************/
        /* On récupère le contenu des EditText */
                /***************************************/

                final String name = inputFullName.getText().toString();
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();


                /*************************/
        /* Teste du mot de passe */
                /*************************/

                if(isValidPassword(password))
                {
                    /***************************/
          /* Teste de l'adresse mail */
                    /***************************/
                    if(isValidEmail(email))
                    {

                        /******************************************************/
            /* On construit la liste des paramètres de la requête */
                        /******************************************************/

                        Thread t = new Thread()
                        {

                            public void run()
                            {

                                Looper.prepare();
                                // On se connecte au serveur afin de communiquer avec le PHP
                                DefaultHttpClient client = new DefaultHttpClient();
                                HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

                                HttpResponse response;
                                HttpEntity entity;


                                // On établit un lien avec le script PHP
                                HttpPost post = new HttpPost("http://tristanguillevin.fr/register.php");

                                List<NameValuePair> nvps = new ArrayList<NameValuePair>();

                                nvps.add(new BasicNameValuePair("tag", "register"));
                                nvps.add(new BasicNameValuePair("name", name));
                                nvps.add(new BasicNameValuePair("email", email));
                                nvps.add(new BasicNameValuePair("password", password));

                                post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                                // On passe les paramètres login et password qui vont être récupérés
                                // par le script PHP en post

                                try{

                                     post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    
                                    // On récupère le résultat du script
    
                                    response = client.execute(post);
    
                                    entity = response.getEntity();
                                    InputStream is = entity.getContent();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                                    StringBuilder sb = new StringBuilder();
                                    String line = reader.readLine();
                                    sb.append(line + "\n");
                                    is.close();

                                    /***************************/
                                    /* Résultats de la requête */
                                    /***************************/
    
                                    String result = sb.toString();

                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                    JSONObject jObj = new JSONObject(result);



                                    /**********************************************/
                                       /* Si le résultat de la requête n'est pas nul */
                                    /**********************************************/



                                    if (jObj.getString(KEY_SUCCESS) != null)
                                    {
                                        String res = jObj.getString(KEY_SUCCESS);

                                        /*********************************************************/
                                             /* Si il vaut 1, l'utilisateur est maintenant enregistré */
                                        /*********************************************************/

                                        if(Integer.parseInt(res) == 1)
                                        {

                                            Toast.makeText(getApplicationContext(), "Utilisateur inscrit !", Toast.LENGTH_SHORT).show();

                                            /***************************************/
                                             /* Lancement de l'Activity "DashBoard" */
                                            /***************************************/

                                            Intent login = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(login);

                                            /****************************/
                                              /* Ferme l'Activity "Login" */
                                            /****************************/

                                            finish();
                                        }

                                        /*****************************************/
                                           /* Si il vaut 0, erreur d'enregistrement */
                                        /*****************************************/

                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), jObj.get("error_msg").toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }
                                catch (Exception e)
                                {
                                    e.getStackTrace();
                                }


                                Looper.loop();

                            }

                        };

                        t.start();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Adresse mail incorrecte", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Le mot de passe doit faire au moins 8 caractère", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /***************************/
  /* Teste de l'adresse mail */
    /***************************/

    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean isValidEmail(String email)
    {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /******************************************************/
  /* Teste si le mot de passe fait plus de 8 caractères */
    /******************************************************/

    public static boolean isValidPassword(String password)
    {
        if(password.length() >= 8)
        return true;
        else
        return false;
    }
}