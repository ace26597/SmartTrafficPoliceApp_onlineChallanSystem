package github.nisrulz.projectqreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetVehicleDetailByText extends AppCompatActivity {
    public String lic=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_vehicle_detail_by_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Get Documents details");
        final EditText liceno=(EditText) findViewById(R.id.liceno1);
        Button getdoc=(Button) findViewById(R.id.getdoc);


        getdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((liceno.getText().toString().trim().length()> 0)) {
                    lic = liceno.getText().toString().trim();
                    Toast.makeText(getApplicationContext(),"value sended"+lic,Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(GetVehicleDetailByText.this,ViewDocuments.class);
                    i.putExtra("lic",lic);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter the License No.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(GetVehicleDetailByText.this, PoliceDashBorad.class));
        finish();
    }
}
