package github.nisrulz.projectqreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Police_Login extends AppCompatActivity {

    Button btnReg;
    Button btnLogin;
    String Police_id = null, Police_name = null;
    JSONParser jsonParser = new JSONParser();
    EditText etmobile, etPassword;
    String mobile, Password;
    CommonFunctions cf;
    JSONObject json;
    UserSessionManager session;
    private static final String TAG = "GCMRelated";

    String gcmid;
    String message = "failed";
    private static String url = "http://www.gopajibaba.com/traffic/police_login.php";

    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_login_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Police Officer Login");
        setTitle("Police Officer Login");
        setTitle(Html.fromHtml("<font color='#ffffff'>Police Officer Login</font>"));

        cf = new CommonFunctions(this);
        session = new UserSessionManager(this);


        etmobile = (EditText) findViewById(R.id.etLoginmobile);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cf.checkInternet()) {

                    if (etPassword.getText().toString().trim().length() > 0 && etmobile.getText().toString().trim().length() > 0) {
                        if (true || false) {

                            new AsyncLogin().execute();
                        } else {
                            cf.showMessage("Invalid Email address ");
                        }

                    } else {
                        cf.showMessage("Please enter email and password");

                    }
                } else {
                    cf.showMessage("No internet connect found try again");
                }

            }
        });


    }


    class AsyncLogin extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Password = etPassword.getText().toString().trim();
            mobile = etmobile.getText().toString().trim();
            Toast.makeText(getApplicationContext(), Password + "\n" + mobile, Toast.LENGTH_SHORT).show();

        }


        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("mobileno", mobile));


            // getting JSON Object
            // Note that create product url accepts POST method
            json = jsonParser.makeHttpRequest(url,
                    "POST", params);

            // check log cat fro response
            Log.d("Response", json.toString());

            // check for success tag
            try {
                result = json.getInt("success");
                message = json.getString("message");
                Police_id = json.getString("police_id");
                Police_name = json.getString("police_name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done


            if (result == 0) {
                cf.showMessage(message);

                btnLogin.setEnabled(true);
            } else {

                btnLogin.setEnabled(true);
                if (result == 1) {
                    SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("police_id", Police_id);
                    editor.putString("police_name", Police_name);
                    editor.apply();
                    cf.showMessage(message);
                    Toast.makeText(getApplicationContext(), Police_id, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(Police_Login.this, PoliceDashBorad.class);
                    i.putExtra("srno", Police_id);
                    i.putExtra("name",Police_name);
                    startActivity(i);
                    finish();
                }
            }
        }
    }
}
