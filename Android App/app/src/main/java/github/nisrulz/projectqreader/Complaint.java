package github.nisrulz.projectqreader;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Complaint extends AppCompatActivity {


    Button btnEmr;
    Button btnLogin;
    String Police_id = null, Police_name = null;
    JSONParser jsonParser = new JSONParser();
    EditText etmobile, etPassword;
    String mobile, Password;
    CommonFunctions cf;
    JSONObject json;
    String message = "failed";
    private static String url = "http://www.gopajibaba.com/traffic/complaint.php";

    int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comp);


        cf = new CommonFunctions(this);


        etmobile = (EditText) findViewById(R.id.compl);

        btnLogin = (Button) findViewById(R.id.bcompl);
        btnEmr = (Button) findViewById(R.id.btnEmr);
        btnEmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogWithListview();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cf.checkInternet()) {

                    if (etmobile.getText().toString().trim().length() > 0) {
                        if (true) {

                            new AsyncLogin().execute();
                        } else {
                            cf.showMessage("Invalid Email address ");
                        }

                    } else {
                        etmobile.setError("Please Enter Complaint Discription");
                        cf.showMessage("Please Enter Complaint Discription");

                    }
                } else {
                    cf.showMessage("No internet connect found try again");
                }

            }
        });


    }


    public void ShowAlertDialogWithListview() {
        List<String> mlist = new ArrayList<String>();
        mlist.add("Police");
        mlist.add("Hospital");

        //Create sequence of items
        final CharSequence[] list = mlist.toArray(new String[mlist.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Emergency Call");
        dialogBuilder.setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String tel = "9503531792";//Hospital contact number

                if (item == 0) {
                    tel = "7040237207";   //Police contact number

                }
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + tel)); // you
                    // put
                    // desired
                    // phone
                    // number
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("dialing example", "Call failed", e);

                }
            }

        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }


    class AsyncLogin extends AsyncTask<String, String, String> {
        String email, contact, dis;


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            //nname=sharedpref.getString("username", "");
            email = sharedpref.getString("email", "");
            contact = sharedpref.getString("mobile", "");
            dis = etmobile.getText().toString().trim();


        }


        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("contact", contact));
            params.add(new BasicNameValuePair("dis", dis));


            // getting JSON Object
            // Note that create product url accepts POST method
            json = jsonParser.makeHttpRequest(url,
                    "POST", params);

            // check log cat fro response
         //   Log.d("Response", json.toString());

            // check for success tag

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            Toast.makeText(Complaint.this, "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Complaint.this, MainActivity.class));

        }
    }
}
