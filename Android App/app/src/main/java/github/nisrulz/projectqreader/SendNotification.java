package github.nisrulz.projectqreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SendNotification extends AppCompatActivity {
    EditText noti; Button Submit;
    String d_id,feedbk;

    public static final String Name = "d_id";
    public static final String Name1 = "feedbk";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti);

        noti = (EditText) findViewById(R.id.etfeed);
        Submit = (Button) findViewById(R.id.etsubmit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Feedback();
                Toast.makeText(SendNotification.this, "Notification send succussfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(SendNotification.this, PoliceDashBorad.class));
        finish();
    }


    private void Feedback() {

        Intent i = getIntent();
        d_id = i.getStringExtra("value");

        System.out.println("feed d_id " + d_id);

        feedbk = noti.getText().toString().trim();

        String notice_url="http://www.gopajibaba.com/traffic/insertnotice.php";
        StringRequest stringrequest = new StringRequest(Request.Method.POST, notice_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Response login " + response.toString());

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(SendNotification.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put(Name, d_id);
                params.put(Name1, feedbk);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);

    }



}
