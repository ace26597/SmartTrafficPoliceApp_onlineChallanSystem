package github.nisrulz.projectqreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Preface extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        Toast.makeText(Preface.this, "this is my Toast message Preface!!! =)",
                Toast.LENGTH_LONG).show();
    }
}
