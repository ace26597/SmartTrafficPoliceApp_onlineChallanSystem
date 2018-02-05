package github.nisrulz.projectqreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class    MainLoginPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlay);
        Toast.makeText(this, "This is Login Page ",
                Toast.LENGTH_LONG).show();
        Button cust_login_bttn = (Button) findViewById(R.id.cust_login_bttn);
        cust_login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLoginPage.this, Custm_Login.class);
                startActivity(intent);
            }
        });
        Button police_login_bttn = (Button) findViewById(R.id.police_login_bttn);
        police_login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLoginPage.this, Police_Login.class);
                startActivity(intent);
            }
        });
    }

}