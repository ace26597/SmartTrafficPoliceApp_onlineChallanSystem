package github.nisrulz.projectqreader;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class
Custm_Login extends AppCompatActivity {
    String mail, contact;
    String srno = "";
    String fineamount;
    int srnon = 0;
    private String respnc = "";
    private static String urlbalence = "http://www.gopajibaba.com/traffic/checkfinebalence.php";
    Button btnReg;
    Button btnLogin;
    String feed = "";
    JSONParser jsonParser = new JSONParser();

    NotificationManager NM;
    EditText etEmail, etPassword;
    String Email, Password;
    CommonFunctions cf;
    JSONObject json;
    String Lice_no = null, name = null;
    String message = "failed";
    private static String url = "http://www.gopajibaba.com/traffic/customer_login.php";

    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("User Login");

        setTitle(Html.fromHtml("<font color='#ffffff'>User Login</font>"));
        new getNotification().execute();
        Button forgotpassword = (Button) findViewById(R.id.forgotpass);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Custm_Login.this, MailActivity.class);
                startActivity(intent);
            }
        });
        cf = new CommonFunctions(this);

        btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Custm_Login.this, Activity_User_Registration.class);
                startActivity(i);
            }
        });


        etEmail = (EditText) findViewById(R.id.etLoginEmail);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cf.checkInternet()) {

                    if (etPassword.getText().toString().trim().length() > 0 && etEmail.getText().toString().trim().length() > 0) {
                        if (cf.checkEmail(etEmail.getText().toString().trim())) {

                            new AsyncLogin().execute();
                        } else {
                            cf.showMessage("Invalid Email address ");
                            etEmail.setError("Enter Valid Email address");
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
            Email = etEmail.getText().toString().trim();
            Toast.makeText(getApplicationContext(), Password + "\n" + Email, Toast.LENGTH_SHORT).show();

        }


        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("mobileno", Email));


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
                Lice_no = json.getString("license_number");
                name = json.getString("name");
                mail = json.getString("email");
                contact = json.getString("mobile");
                srnon = json.getInt("sr_no");

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
                    editor.putString("username", name);
                    editor.putString("licno", Lice_no);
                    editor.putString("email", mail);
                    editor.putString("mobile", contact);
                    editor.apply();
                    new AsyncBalence().execute();
                    //cf.showMessage(message);
                    Toast.makeText(getApplicationContext(), "Licence No.\n" + Lice_no + "\n name \n " + name + "\n srno" + srnon, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    class AsyncBalence extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Checking Balence" + "\n" + Email, Toast.LENGTH_SHORT).show();

        }


        protected String doInBackground(String... args) {


            ArrayList<NameValuePair> register = new ArrayList<NameValuePair>();
            System.out.println("--------------01");
            register.add(new BasicNameValuePair("srno", String.valueOf(srnon)));


            try {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(urlbalence);

                System.out.println("--------------3");

                httppost.setEntity(new UrlEncodedFormEntity(register));
                System.out.println("--------------4");

                ResponseHandler<String> resHandler = new BasicResponseHandler();
                // HttpResponse respnc = httpclient.execute(httppost);

                respnc = " ";
                respnc = httpclient.execute(httppost, resHandler);
                System.out.println("resp " + respnc);
                System.out.println("...............len" + respnc.length());

                // collect response

            } catch (Exception e) {

                // TODO: handle exception
                Log.e("log_tag", "ERROR IN HTTP CON " + e.toString());

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            try {
                JSONArray main = new JSONArray(respnc);


                for (int i = 0; i < main.length(); i++) {

                    JSONObject jsonObj = main.getJSONObject(i);
                    fineamount = jsonObj.getString("fineamount");
                    System.out.println("Available Balence ........" + fineamount);

                    SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("fineamount", fineamount);
                    editor.apply();


                }

            } catch (Exception e) {
            }

            Intent i = new Intent(Custm_Login.this, CDashboard.class);
            i.putExtra("name", name);
            i.putExtra("amt", fineamount);
            startActivity(i);

        }
    }


    class getNotification extends AsyncTask<String, String, String> {

        // TODO Auto-generated method stub

        @Override
        protected String doInBackground(String... params) {


            ArrayList<NameValuePair> register = new ArrayList<NameValuePair>();

            System.out.println("--------------01");


            try

            {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(
                        "http://www.gopajibaba.com/traffic/getnotice.php");

                System.out.println("--------------3");

                httppost.setEntity(new UrlEncodedFormEntity(register));
                System.out.println("--------------4");

                ResponseHandler<String> resHandler = new BasicResponseHandler();
                // HttpResponse respnc = httpclient.execute(httppost);

                respnc = " ";
                respnc = httpclient.execute(httppost, resHandler);


                System.out.println("resp " + respnc);

                System.out.println("...............len" + respnc.length());

                // collect response
                //   parseJSON_Login(respnc);


            } catch (
                    Exception e)

            {

                // TODO: handle exception
                Log.e("log_tag", "ERROR IN HTTP CON " + e.toString());

            }


            return null;
        }


        protected void onPostExecute(String file_url) {

            try {

              //  Toast.makeText(Custm_Login.this, "responce" + respnc, Toast.LENGTH_SHORT).show();
                JSONArray main = new JSONArray(respnc);


                for (int i = 0; i < main.length(); i++) {

                    JSONObject jsonObj = main.getJSONObject(i);

                    feed = jsonObj.getString("feed");
                }
                   // NewNotify();
                    Toast.makeText(Custm_Login.this, "feed" + feed, Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(Custm_Login.this)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("Traffic App notification")
                                    .setContentText(feed+"!");
// Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(Custm_Login.this, Custm_Login.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(Custm_Login.this);
// Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(Custm_Login.class);
// Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                    int mId=100;
                    mNotificationManager.notify(mId, mBuilder.build());


                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();



            } catch (Exception e) {
            }

        }

    }


}