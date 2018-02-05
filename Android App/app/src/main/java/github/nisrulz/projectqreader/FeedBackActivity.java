package github.nisrulz.projectqreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedBackActivity extends Activity {


    SubscribeAdapter subscribeListAdapter;
    List<Subscribe_pogos_list> subscribe_pogos_List = new ArrayList<>();
    RequestQueue mRequestQueue;
    RecyclerView recList;


    private final String Feedback_Url = "http://www.gopajibaba.com/traffic/getfeedback.php";


    RequestQueue requestQueue;


    //nlicno=sharedpref.getString("licno","");

    public static final String Name = "d_id";
    public static final String Name1 = "feedbk";
    private final String Forgot_Url = "http://www.gopajibaba.com/traffic/insertfeedback.php";

    String d_id = "";
    String feedbk = "";


    EditText Feedback;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);


        Feedback = (EditText) findViewById(R.id.etfeed);
        Submit = (Button) findViewById(R.id.etsubmit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Feedback();
                Intent mIntent = getIntent();
                finish();
                startActivity(mIntent);

            }
        });


        recList = (RecyclerView) findViewById(R.id.rvchaaneList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(new LinearLayoutManager(this));

        FeedBackData();


    }

    private void FeedBackData() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Feedback_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response:" + response.toString());

                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String U_id = jsonobj.getString("did");
                        String Feed_info = jsonobj.getString("feed");


                        Subscribe_pogos_list pogo = new Subscribe_pogos_list();
                        pogo.setUser_id(U_id);
                        pogo.setFeedback_data(Feed_info);


                        subscribe_pogos_List.add(pogo);
                    }
                    setSubscribelistAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },

                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError Error) {
                        Toast.makeText(FeedBackActivity.this, Error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setSubscribelistAdapter() {

        if (subscribe_pogos_List != null) {
            subscribeListAdapter = new SubscribeAdapter(this, subscribe_pogos_List);
            recList.setAdapter(subscribeListAdapter);
            subscribeListAdapter.notifyDataSetChanged();
        }
    }


    private void Feedback() {

        Intent i = getIntent();
        d_id = i.getStringExtra("value");

        System.out.println("feed d_id " + d_id);

        feedbk = Feedback.getText().toString();

        StringRequest stringrequest = new StringRequest(Request.Method.POST, Forgot_Url, new Response.Listener<String>() {

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
                        Toast.makeText(FeedBackActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
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
