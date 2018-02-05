
package github.nisrulz.projectqreader;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusforapp.fancydialog.FancyAlertDialog;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {
    // UI
    private TextView text;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private Button btnCheckPermissions, viewdoc;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    public static int flag = 0;
    public static String licen = "";

    // QREader
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private String respnc = "";
    private String remainBalence = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        text = (TextView) findViewById(R.id.code_info);
        viewdoc = (Button) findViewById(R.id.viewdocuments);

        final Button stateBtn = (Button) findViewById(R.id.btn_start_stop);
        // change of reader state in dynamic
        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrEader.isCameraRunning()) {
                    stateBtn.setText("Start QREader");
                    qrEader.stop();
                } else {
                    stateBtn.setText("Stop QREader");
                    qrEader.start();
                }
            }
        });

        viewdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    //PLice_no="Ght123899";
                    editor.putString("PLice_no", licen);
                    editor.apply();

                    Intent i = new Intent(MainActivity.this, ViewDocuments.class);
                    i.putExtra("lic", licen);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Data not found in QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stateBtn.setVisibility(View.VISIBLE);
        Button restartbtn = (Button) findViewById(R.id.btn_restart_activity);
        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });

        // Setup SurfaceView
        // -----------------
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);
        // Init QREader
        // ------------
        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        licen = data;
                        text.setText(data);
                        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpref.edit();
                        editor.putString("PLice_no", licen);
                        editor.apply();
                        getEmailId();
                        flag = 1;
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkpermission();
        // Init and Start with SurfaceView
        // -------------------------------
        qrEader.initAndStart(mySurfaceView);
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

            System.out.println("Value sended for server" + licen);
            register.add(new BasicNameValuePair("srno", licen));
            //register.add(new BasicNameValuePair("branch_name", txtbranch));

            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------Licence No and doc name ..." + licen);
            System.out.println("--------------01");
            System.out.println("--------------01");

            System.out.println("--------------01");


            try {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(
                        "http://www.gopajibaba.com/traffic/lice_fine.php");

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

                        remainBalence = jsonObj.getString("fineamount");

                        //strFcustnameinalAbsentRollNo=strFinalAbsentRollNo+" "+str;

	            						/*strFinal=strFinal+" "+strRollNo[i];
                                        System.out.println("Helllooooooo");
	            						edt_staff_name.setText(strFinal);
	            					txtViewabscentRollNo.setText(strFinal);*/
                        //Toast.makeText(getApplicationContext(),str , Toast.LENGTH_SHORT).show();
                    }

                    SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("remainBalence", remainBalence);
                    editor.apply();


                    System.out.println("Remaining Balence " + remainBalence);
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

            return null;
        }

        protected void onPostExecute(String file_url) {
            int bal = Integer.parseInt(remainBalence);
          //  Toast.makeText(MainActivity.this, "bal  " + bal, Toast.LENGTH_SHORT).show();
            if (bal > 100) {

                SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putInt("balence", 1);
                editor.apply();
                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                        .setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_computing))
                        .setTextTitle("BALENCE")
                        .setTextSubTitle("\u20B9 " + remainBalence + ".00 / \u20B9 5000.00")
                        .setBody("Account balence for the license no. " + licen + " is " + remainBalence)
                        .setNegativeColor(R.color.colorNegative)
                        .setNegativeButtonText("Later")
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {

                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonText("View Documents")
                        .setPositiveColor(R.color.colorPositive)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {

                                Intent i = new Intent(MainActivity.this, ViewDocuments.class);
                                i.putExtra("lic", licen);
                                startActivity(i);
                                Toast.makeText(MainActivity.this, "View Documents", Toast.LENGTH_SHORT).show();
                            }
                        }).setAlertFont("Roboto-Bold.ttf")
                       /* .setAutoHide(true)*/
                        .build();


                alert.setButtonsGravity(FancyAlertDialog.PanelGravity.CENTER);
                alert.show();








            } else {
                SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putInt("balence", 0);
                editor.apply();



                FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                        .setImageRecourse(R.drawable.ic_cloud_computing)
                        .setTextTitle("LESS BALENCE")
                        .setTextSubTitle("\u20B9 " + remainBalence + ".00 / \u20B9 5000.00")
                        .setBody("Account balence for the license no. " + licen + " is " + remainBalence + "is too low.. You must CASH from user.")
                        .setNegativeColor(R.color.colorNegative)
                        .setNegativeButtonText("Later")
                        .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButtonText("View Documents")
                        .setPositiveColor(R.color.colorPositive)
                        .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {

                                Intent i = new Intent(MainActivity.this, ViewDocuments.class);
                                i.putExtra("lic", licen);
                                startActivity(i);
                                Toast.makeText(MainActivity.this, "View Documents", Toast.LENGTH_SHORT).show();
                            }
                        }).setAlertFont("Roboto-Bold.ttf")
                        .setTitleFont("Roboto-Bold.ttf")
                        .setTitleColor(R.color.colorNegative)
                       /* .setAutoHide(true)*/
                        .build();


                alert.setButtonsGravity(FancyAlertDialog.PanelGravity.CENTER);
                alert.show();


            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Cleanup in onPause()
        // --------------------
        qrEader.releaseAndCleanup();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        qrEader.releaseAndCleanup();
        startActivity(new Intent(MainActivity.this, PoliceDashBorad.class));
        finish();
    }


    public void checkpermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[2])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {

        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}
    




