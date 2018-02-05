package github.nisrulz.projectqreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;

public class PoliceDashBorad extends AppCompatActivity {
    TextView tvcollectfine, tvgetvehivledetails, tvCustLogout;
    String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_dash_borad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Police DashBoard");
        setTitle(Html.fromHtml("<font color='#ffffff'>Police DashBoard</font>"));

        Intent i = getIntent();
        pid = i.getStringExtra("srno");

        String Police_name = i.getStringExtra("name");
        if(Police_name!= null) {
            Alerter.create(this)

                    .setText("Wel-Come " + Police_name)
                    .setDuration(3000)
                    .show();
        }
        TextView tvscanqrcode = (TextView) findViewById(R.id.tvscanqrcode);
        TextView tv = (TextView) findViewById(R.id.tvSendnoti);
        tvscanqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PoliceDashBorad.this, MainActivity.class);
                startActivity(i);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PoliceDashBorad.this, SendNotification.class);
                i.putExtra("value", pid);
                startActivity(i);
            }
        });

        tvcollectfine = (TextView) findViewById(R.id.tvcollectfine);
        tvcollectfine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PoliceDashBorad.this, FineCollection.class);
                startActivity(i);
            }
        });
        tvCustLogout = (TextView) findViewById(R.id.tvCustLogout);
        tvCustLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PoliceDashBorad.this, Police_Login.class);

                startActivity(i);
                finish();
            }
        });

        tvgetvehivledetails = (TextView) findViewById(R.id.tvgetvehivledetails);
        tvgetvehivledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PoliceDashBorad.this, GetVehicleDetailByText.class);
                startActivity(i);
            }
        });
    }

}
