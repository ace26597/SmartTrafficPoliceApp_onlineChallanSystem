package github.nisrulz.projectqreader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_User_Registration extends ActionBarActivity {

    Button btnSignin;
    EditText license_number, etName, etage, etgender, etemail, etmobileno, etPassword, etConfirmPassword, etAddress;
    String shop_act_number, shop_name, shop_contact, shop_email, shop_address, shop_owner_name, shop_owner_contact, owner_email, shop_password;
    String lice_number, name, age, gender, mail, mobileno, password, address;
    Location location;
    JSONParser jsonParser = new JSONParser();
    AppLocationService appLocationService;
    double latitude;
    double longitude;
    CommonFunctions cf;
    private static String url = "http://www.gopajibaba.com/traffic/user_reg.php";
    int result = 0;
    private RadioGroup radioGroup;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__user__registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>User Registration</font>"));
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar1);
        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>User Registration</font>"));

        cf = new CommonFunctions(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnSignin = (Button) findViewById(R.id.btnUserSignUp);

        license_number = (EditText) findViewById(R.id.license_number);
        etName = (EditText) findViewById(R.id.etName);
        etage = (EditText) findViewById(R.id.etage);

        etemail = (EditText) findViewById(R.id.etemail);
        etmobileno = (EditText) findViewById(R.id.etmobileno);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cf.checkEditText(license_number) && cf.checkEditText(etName) && cf.checkEditText(etage)
                        && cf.checkEditText(etmobileno) && cf.checkEditText(etPassword)
                        && cf.checkEditText(etConfirmPassword)) {


                    if (cf.checkEmail(etemail.getText().toString().trim())) {

                        if (cf.checkEditText(etAddress)) {
                            if (etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {

                                btnSignin.setEnabled(false);
                                int selectedId = radioGroup.getCheckedRadioButtonId();

                                // find the radiobutton by returned id
                                radioSexButton = (RadioButton) findViewById(selectedId);


                                new AsyncRegistration().execute();
                            } else {
                                cf.showMessage("Password don't match ");
                                //showSettingsAlert();

                            }


                        } else {
                            cf.showMessage("Please turn on gps");
                            showSettingsAlert();

                        }

                    } else {

                        cf.showMessage("Invalid Email Address");

                    }


                } else {


                    cf.showMessage("Please enter all fields ");
                }


            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Activity_User_Registration.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Activity_User_Registration.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address").trim();
                    break;
                default:
                    locationAddress = "";
            }
            etAddress.setText(locationAddress);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Background Async Task
     */
    class AsyncRegistration extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lice_number = license_number.getText().toString().trim();
            name = etName.getText().toString().trim();
            age = etage.getText().toString().trim();
            gender = radioSexButton.getText().toString();
            mail = etemail.getText().toString().trim();
            mobileno = etmobileno.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            address = etAddress.getText().toString().trim();
            SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putString("username", name);
            editor.putString("licno", lice_number);
            editor.apply();
        }


        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("lice_no", lice_number));
            params.add(new BasicNameValuePair("u_name", name));
            params.add(new BasicNameValuePair("u_age", age));
            params.add(new BasicNameValuePair("u_sex", gender));
            params.add(new BasicNameValuePair("u_email", mail));
            params.add(new BasicNameValuePair("u_mob", mobileno));
            params.add(new BasicNameValuePair("u_pwd", password));
            params.add(new BasicNameValuePair("u_add", address));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url,
                    "POST", params);

            // check log cat fro response
            Log.d("Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully
                    result = 1;


                } else {
                    // failed
                    if (success == 2) {
                        result = 2;
                    } else {
                        result = 0;
                    }
                }
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


            if (result == 1) {

                btnSignin.setEnabled(true);
                cf.showMessage("Succesfully Registered");
                Intent i = new Intent(getApplicationContext(), Custm_Login.class);
                startActivity(i);

            } else {


                btnSignin.setEnabled(true);
                if (result == 2) {
                    cf.showMessage("User is already registered by this email address");
                } else {
                    cf.showMessage("Failed please try again");

                }
            }
        }
    }


}