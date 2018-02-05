package github.nisrulz.projectqreader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import java.util.List;

public class ViewDocuments extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    public String Lic_no;
    String respnc="",filepath2;
    Context context;
    public ProgressDialog progress;
    String docname=null;ImageView doc;
    TextView tvLice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_documents);
        Button btnAdd;
        Intent intent = getIntent();
        doc=(ImageView) findViewById(R.id.imageView);
        tvLice = (TextView) findViewById(R.id.tvLice);
        Lic_no = intent.getStringExtra("lic");
        tvLice.setText("Licence No : "+Lic_no);
       // Lic_no="Ght123899";
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Driving Licence");
        categories.add("PUC Slip");
        categories.add("Insurance");
        categories.add("RC Book");
        // method.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        btnAdd = (Button) findViewById(R.id.getdocument);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!docname.equals(null) )
                {
                    if(!Lic_no.equals(null)){
                        getdetails();
                    }
                }
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Licence Number " + Lic_no)
                .setMessage("Get Documents from " + Lic_no)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ViewDocuments.this, PoliceDashBorad.class);

                        startActivity(i);

                    }
                }).show();





    }
    public void getdetails(){
        new AsyncGet().execute();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        docname=item;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        String item = arg0.getItemAtPosition(0).toString();
        docname=item;
        // Showing selected spinner item
        Toast.makeText(arg0.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ViewDocuments.this, PoliceDashBorad.class));
        finish();
    }


    class AsyncGet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Log.d(tag,"in asynch task");
            Toast.makeText(getApplicationContext(), "Lic_no: " +Lic_no , Toast.LENGTH_SHORT).show();
        }


        protected String doInBackground(String... args) {



            ArrayList<NameValuePair> register = new ArrayList<NameValuePair>();
            //Toast.makeText(getApplicationContext(), "Attendance...", Toast.LENGTH_SHORT).show();

            System.out.println("Value sended for server"+Lic_no);
            register.add(new BasicNameValuePair("lice_no", Lic_no));
            register.add(new BasicNameValuePair("docname",docname));
            //register.add(new BasicNameValuePair("branch_name", txtbranch));

            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------01");
            System.out.println("--------------Licence No and doc name ..."+Lic_no+docname);
            System.out.println("--------------01");System.out.println("--------------01");

            System.out.println("--------------01");


            try {
                HttpPost httppost;
                System.out.println("--------------1");
                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("--------------2");

                httppost = new HttpPost(
                        "http://www.gopajibaba.com/traffic/dct.php");

                System.out.println("--------------3 httppost called...");

                httppost.setEntity(new UrlEncodedFormEntity(register));
                System.out.println("--------------4");

                ResponseHandler<String> resHandler = new BasicResponseHandler();
                // HttpResponse respnc = httpclient.execute(httppost);

                respnc = " ";
                respnc = httpclient.execute(httppost, resHandler);

                System.out.println("resp " + respnc);

                // pass JSON response for parsing
                parseJSON2(respnc);

                System.out.println("...............len" + respnc.length());
            } catch (Exception e) {

                // TODO: handle exception
                Log.e("log_tag", "ERROR IN HTTP CON " + e.toString());

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
                    .resetViewBeforeLoading(false) // default
                    .delayBeforeLoading(1000)
                    .cacheInMemory(false) // default
                    .cacheOnDisk(false) // default
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getApplicationContext())
                    .defaultDisplayImageOptions(options)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader.getInstance().init(config);

            ImageLoader imageLoader = ImageLoader.getInstance();

            doc.setVisibility(View.VISIBLE);

            imageLoader.displayImage(filepath2, doc, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progress=new ProgressDialog(ViewDocuments.this);
                    progress.setMessage("Downloading Documents");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();

                    final int totalProgressTime = 100;
                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            int jumpTime = 0;

                            while(jumpTime < totalProgressTime) {
                                try {
                                    sleep(200);
                                    jumpTime += 5;
                                    progress.setProgress(jumpTime);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();


                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Toast.makeText(getApplicationContext(),failReason.toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.dismiss();

                }
            });







        }

    }
    private void parseJSON2(String result) {
        // TODO Auto-generated method stub
        try {

            JSONArray main = new JSONArray(result);
            System.out.println("JSON 2......");
            System.out.println(main.length());
            for (int i = 0; i < main.length(); i++) {

                JSONObject jsonObj = main.getJSONObject(i);

                filepath2 = jsonObj.getString("url");
                //strFinalAbsentRollNo=strFinalAbsentRollNo+" "+str;

	            						/*strFinal=strFinal+" "+strRollNo[i];
	            						System.out.println("Helllooooooo");
	            						edt_staff_name.setText(strFinal);
	            					txtViewabscentRollNo.setText(strFinal);*/
                //Toast.makeText(getApplicationContext(),str , Toast.LENGTH_SHORT).show();
            }
            System.out.println("Path for download the image"+filepath2);
            Log.e("Path download the image",filepath2);
            //txtViewabscentRollNo.setText(strFinalAbsentRollNo);


        } catch (Exception e) {
            System.out.println(e);
        }



    }



}
