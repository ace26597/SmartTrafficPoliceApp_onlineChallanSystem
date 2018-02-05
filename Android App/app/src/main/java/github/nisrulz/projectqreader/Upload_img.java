package github.nisrulz.projectqreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.widget.Spinner;
public class Upload_img extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    JSONParser jsonParser = new JSONParser();
    JSONArray type;
    CommonFunctions cf;
    String liceno;
    String docname=null;
    JSONObject json;
    static String message = "failed";
    int result = 0;
    Button btnChoose,btnType,btnSize,btnColor;
    Button btnAdd;

    ProgressDialog prgDialog;
    String encodedString,size="",Prodtype="",color="",prodid;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String username1=sharedpref.getString("username", "");
        liceno=sharedpref.getString("licno","");
        toolbar.setTitle(username1);
        imgView=(ImageView) findViewById(R.id.imageViewProduct);
        btnChoose = (Button) findViewById(R.id.btnShopImageChoose);
        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        cf = new CommonFunctions(this);
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Driving Licence");
        categories.add("PUC Slip");
        categories.add("Insurance");
        categories.add("RC Book");

        // Creating adapter for spinnerm
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
        btnAdd = (Button) findViewById(R.id.uploadimage);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage(v);
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
checkPer();
    }

    void checkPer(){
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Upload_img.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Upload_img.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Upload_img.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Upload_img.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        docname=item;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);
    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
//                BitmapFactory.Options options = null;
//                options = new BitmapFactory.Options();
//                options.inSampleSize = 3;
//                Bitmap bitmap_iv = BitmapFactory.decodeFile(imgPath,
//                        options);
                Bitmap bitmap_iv = decodeSampledBitmapFromFile(imgPath,200,100);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap_iv.compress(Bitmap.CompressFormat.PNG, 50, stream);
                // Set the Image in ImageView
                imgView.setImageBitmap(bitmap_iv);
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1]+System.currentTimeMillis();
                // Put file name in Async Http Post Param which will used in Php web app



            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if(cf.checkInternet()) {
            if (true) {
                if (imgPath != null && !imgPath.isEmpty()) {
                    prgDialog.setMessage("Converting Image to Binary Data");
                    prgDialog.show();
                    // Convert image to String using Base64
                    encodeImagetoString();
                    // When Image is not selected from Gallery
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "You must select image from gallery before you try to upload",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                cf.showMessage("Please enter all fileds");
            }
        }else {
            cf.showMessage("No internet connection found");
        }
    }
    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
//                BitmapFactory.Options options = null;
//                options = new BitmapFactory.Options();
//                options.inSampleSize = 3;
//                bitmap = BitmapFactory.decodeFile(imgPath,
//                        options);
                bitmap = decodeSampledBitmapFromFile(imgPath,200,100);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);

                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {

        params.put("srno", fileName);
        params.put("docname",docname);
        params.put("lice_no",liceno);

        Log.e("Filr Name", fileName+docname+liceno);

       // Toast.makeText(getApplicationContext(),fileName+"\n"+docname+"\n"+liceno,Toast.LENGTH_LONG).show();


        prgDialog.setMessage("Uploading Image Please Wait");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://www.gopajibaba.com/traffic/upload_doc.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();

                       // new AsyncAdd().execute();
                        Log.e("", response);
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
//                                            + statusCode, Toast.LENGTH_LONG)
//                                    .show();
                            Toast.makeText(getApplicationContext(),
                                    "Error Try Again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }








    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 3;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
/*

    class AsyncAdd extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setMessage("Uploading data please wait");
        }


        protected String doInBackground(String... args) {


            //  String post = etPost.getText().toString().trim();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("shop_id", shop_id));
            params.add(new BasicNameValuePair("shop_name", shop_nama));
            params.add(new BasicNameValuePair("prod_imag", "http://www.gopajibaba.com/Mall_Project/pics/"+fileName+".png"));

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
         * *

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            prgDialog.hide();
            cf.showMessage(message);

        }
    }
*/
}
