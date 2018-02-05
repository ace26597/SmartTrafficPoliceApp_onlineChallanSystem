package github.nisrulz.projectqreader;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class FineCollection extends AppCompatActivity {
    public String policeid, policename, licno;
    public String type;
    int countSize = -1;
    AppLocationService appLocationService;
    public double latitude;
    public double longitude;
    Location location;
    final Context context=this;
    int fineamt = 0;
    public String fine, vehicleno;
    public String vno;
    public String respnc = "";
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParser1 = new JSONParser();
    CommonFunctions cf;
    JSONObject json, json1;
    String message = "failed";
    int result = 0;
    public String Email = null, address = null, custname = null;
    Button btnpolicename, btnpoliceid, btnliceno, selectfine, getfine, btnvehicle_no;
    EditText edtvehicle_no;
    String[] listtype = {"1. Not carrying valid licence while driving",
            "2. Not carrying documents as required",
            "3. Dangerous Lane cutting",
            "4. Moving against One-Way",
            "5. Overtaking dangerously",
            "6. Jumping Signal (driving at red light)",
            "7. Driving on Footpath",
            "8. Parking Violations",
            "9. Stopping at Pedestrian Crossing or Crossing Stop Line",
            "10. Horn offences",
            "11. Number Plate Offences",
            "12. Charging Excess Fare",
            "13. Misbehavior with Passenger",
            "14. Using Mobile Phones while driving",
            "15. Driving without Helmet",
            "16. Driving without valid licence",
            "17. Driving at a speed exceeding as mentioned in MVA:112",
            "18. Driving under influence of Drugs or Alcohols",
            "19. Illegal racing on road",
            "20. Using Loudspeaker beyond specified limit",
            "21. Carriage of goods which are of dangerous & hazardous nature to human life",
            "22. Overloading a vehicle beyond extent limit",
            "23. Driving of vehicle without legal authority",
            "24. Disturbance in free flow of traffic",
            "25. Allowing the vehicle to be driven by a person who does not possess a valid licence"};
    private static String url = "http://www.gopajibaba.com/traffic/fine_collection.php";
    private static String email_url = "http://www.gopajibaba.com/traffic/get_email.php";
    String[] fineAmount = {"100", "100", "100", "100", "100", "100", "100", "100", "100", "200", "300", "400", "500", "600", "700", "500", "1000", "300", "1500", "2000", "3000", "3000", "50", "1000", "4000"};
    String bal = "";
    int remainBalence = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_collection);
        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //nname=sharedpref.getString("username", "");

        policeid = sharedpref.getString("police_id", "");
        policename = sharedpref.getString("police_name", "");
        licno = sharedpref.getString("PLice_no", "");
        bal = sharedpref.getString("remainBalence", "");
        remainBalence = Integer.parseInt(bal);
        edtvehicle_no = (EditText) findViewById(R.id.edtvehicle_no);
        btnpolicename = (Button) findViewById(R.id.btnpolicename);
        btnpoliceid = (Button) findViewById(R.id.tvPoliceId);
        btnliceno = (Button) findViewById(R.id.btnlice_no);
        selectfine = (Button) findViewById(R.id.rulebreak);
        getfine = (Button) findViewById(R.id.getFine);

        btnpolicename.setText("Police Name : " + policename);
        btnpoliceid.setText("Police Id : " + policeid);
        btnliceno.setText("User Licence No : " + licno);
        appLocationService = new AppLocationService(FineCollection.this);
        cf = new CommonFunctions(this);
        getfine.setEnabled(false);


        selectfine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!licno.equals(null)) {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(FineCollection.this);
                    builder.setTitle("Select Which Rule Break");
                    builder.setSingleChoiceItems(listtype, countSize, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            countSize = item;
                            type = listtype[item];
                            selectfine.setText("Rule Break : \n" + listtype[item]);

                        }
                    })
                            // Set the action buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on OK
                                    //  You can write the code  to save the selected item herese
                                    fine = fineAmount[countSize];
                                    getfine.setEnabled(true);
                                    getfine.setText("Accept Fine Amount : " + fineAmount[countSize]);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on Cancel

                                }
                            });

                    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Customer Licence No. First", Toast.LENGTH_SHORT).show();

                }


            }
        });

        getfine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                fineamt = sharedpref.getInt("balence", 0);

                if (fineamt == 1 && remainBalence > Integer.parseInt(fine)) {
                    if (!licno.equals(null)) {
                        if (cf.checkEditText(edtvehicle_no)) {
                            new AsyncGetFine().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Vehicle No. First", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Customer Licence No. First", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new android.app.AlertDialog.Builder(FineCollection.this)
                            .setTitle("LESS BALENCE IN YOUR WALLET")
                            .setMessage("PLEASE COLLECT CASH FROM USER ")
                            .setPositiveButton(R.string.yes1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    new AsyncGetFine().execute();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }
            }
        });


        location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = appLocationService
                    .getLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("", String.valueOf(latitude) + "  " + String.valueOf(longitude));
        } else {

            showSettingsAlert();
        }


    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                FineCollection.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        FineCollection.this.startActivity(intent);
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


    class AsyncGetFine extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vno = edtvehicle_no.getText().toString().trim();


            Toast.makeText(getApplicationContext(), licno + "\n" + fine + "\n" + policeid + "\n" + vno, Toast.LENGTH_SHORT).show();

        }


        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fine_amt", fine));
            params.add(new BasicNameValuePair("lice_no", licno));
            params.add(new BasicNameValuePair("vno", vno));
            params.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
            params.add(new BasicNameValuePair("lng", String.valueOf(longitude)));
            params.add(new BasicNameValuePair("police_id", policeid));


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

                getfine.setEnabled(true);
            } else {

                getfine.setEnabled(true);
                if (result == 1) {
                    getEmailId();
                    updateBalence();
                    Toast.makeText(getApplicationContext(), "Fine Collected Successfully", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                    //TextView text = (TextView) dialog.findViewById(R.id.text);
                   // text.setText("Android custom dialog example!");
                    //ImageView image = (ImageView) dialog.findViewById(R.id.image);
                    //image.setImageResource(R.drawable.ic_launcher);

                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(FineCollection.this, PoliceDashBorad.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    private void updateBalence() {
        new AsyncUpdateBalence().execute();
    }

    class AsyncUpdateBalence extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {


            ArrayList<NameValuePair> register = new ArrayList<NameValuePair>();
            //Toast.makeText(getApplicationContext(), "Attendance...", Toast.LENGTH_SHORT).show();

            System.out.println("Value sended for server" + licno);
            register.add(new BasicNameValuePair("liceno", licno));
            register.add(new BasicNameValuePair("fine", String.valueOf(remainBalence - Integer.parseInt(fine))));
            //register.add(new BasicNameValuePair("branch_name", txtbranch));

            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------Licence No and fine for update ..." + licno +String.valueOf(remainBalence - Integer.parseInt(fine)));
            System.out.println("--------------01");
            System.out.println("--------------01");

            System.out.println("--------------01");


            try {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(
                        "http://www.gopajibaba.com/traffic/updatebalence.php");

                System.out.println("--------------3 httppost called...");

                httppost.setEntity(new UrlEncodedFormEntity(register));
                System.out.println("--------------4");

                ResponseHandler<String> resHandler = new BasicResponseHandler();
                // HttpResponse respnc = httpclient.execute(httppost);

                respnc = " ";
                respnc = httpclient.execute(httppost, resHandler);

                System.out.println("resp " + respnc);

                // pass JSON response for parsing


                System.out.println("...............len" + respnc.length());
            } catch (Exception e) {

                // TODO: handle exception
                Log.e("log_tag", "ERROR IN HTTP CON " + e.toString());

            }


            return null;
        }
    }

    public void getEmailId() {
        new AsyncEmail().execute();
    }

    class AsyncEmail extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        protected String doInBackground(String... args) {


            ArrayList<NameValuePair> register = new ArrayList<NameValuePair>();
            //Toast.makeText(getApplicationContext(), "Attendance...", Toast.LENGTH_SHORT).show();

            System.out.println("Value sended for server" + licno);
            register.add(new BasicNameValuePair("lice_no", licno));
            //register.add(new BasicNameValuePair("branch_name", txtbranch));

            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------Licence No and doc name ..." + licno);
            System.out.println("--------------01");
            System.out.println("--------------01");

            System.out.println("--------------01");


            try {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(
                        "http://www.gopajibaba.com/traffic/get_email.php");

                System.out.println("--------------3 httppost called...");

                httppost.setEntity(new UrlEncodedFormEntity(register));
                System.out.println("--------------4");

                ResponseHandler<String> resHandler = new BasicResponseHandler();
                // HttpResponse respnc = httpclient.execute(httppost);

                respnc = " ";
                respnc = httpclient.execute(httppost, resHandler);

                System.out.println("resp " + respnc);

                // pass JSON response for parsing
                try {

                    JSONArray main = new JSONArray(respnc);
                    System.out.println("JSON 2......");
                    System.out.println(main.length());
                    for (int i = 0; i < main.length(); i++) {

                        JSONObject jsonObj = main.getJSONObject(i);

                        Email = jsonObj.getString("email");
                        custname = jsonObj.getString("name");
                        //strFcustnameinalAbsentRollNo=strFinalAbsentRollNo+" "+str;

	            						/*strFinal=strFinal+" "+strRollNo[i];
                                        System.out.println("Helllooooooo");
	            						edt_staff_name.setText(strFinal);
	            					txtViewabscentRollNo.setText(strFinal);*/
                        //Toast.makeText(getApplicationContext(),str , Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("Path for send email " + Email);
                    //Log.e("Path download the image",filepath2);
                    //txtViewabscentRollNo.setText(strFinalAbsentRollNo);


                } catch (Exception e) {
                    System.out.println(e);
                }


                System.out.println("...............len" + respnc.length());
            } catch (Exception e) {

                // TODO: handle exception
                Log.e("log_tag", "ERROR IN HTTP CON " + e.toString());

            }


            String[] toArr = {"krishna4741@gmail.com", "apostleprojectwork@gmail.com", Email};

            String mode;
            Mail m = new Mail("trafficappupdate@gmail.com", "9595798728");

            //   m.addAttachment();
            m.setTo(toArr);
            m.setFrom("collegetpoinfo@gmail.com");
            m.setSubject("Traffic Police Application " + " Rule Voiletion Mail");
            m.setBody("Hi," + custname + "\t License No. : " + licno +
                    "\n\nYour have break the rule  \n\t\"" + listtype[countSize]+" \"\n" + "You have successfully paid the fine of rupees \nINR \u20B9" + fine + "/- to the traffic officer. \nOfficer Name : " + policename + "   (Officer id:" + policeid + ")" +
                    "\nAt location " + " Lattitude : " + latitude + "\t" +
                    "\tLongitude : " + longitude + "\n" + ""
                    + "\n\nYOUR REMAINING BALENCE IS " + "\u20B9 " + (remainBalence - Integer.parseInt(fine)) + "/-"
                    + "\n(Plz Go through www.latlong.net/ for getting the details of address) + \nFrom the vehicle number : " + vno

                    + "\n \n" +
                    " \n:::::::::: Keep Driving Safe :::::::::: \n" + ":::::::::: Follow All Driving Rules :::::::::::"
                    + "\n\n::: Traffic App Wishes You a Happy Journey. :::");


            try {


                m.send();

            } catch (Exception e) {
                //       Toast.makeText(getApplicationContext(), "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);

            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done


        }
    }


}



