package github.nisrulz.projectqreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class GetQRcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
   public String PLice_no=null,respnc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b);

    }

    public void QrScanner(View view){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }

    @Override
    public void handleResult(Result QRcodeData) {
        // Do something with the result here

        Log.e("handler", QRcodeData.getText()); // Prints scan results
        Log.e("handler", QRcodeData.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        PLice_no=QRcodeData.getText();
        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedpref.edit();
        //PLice_no="Ght123899";
        editor.putString("PLice_no",PLice_no);
        editor.apply();

        new AlertDialog.Builder(this)
                .setTitle("Licence Number " + QRcodeData.getText())
                .setMessage("Get Documents from " + QRcodeData.getText())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getdata(PLice_no);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();



        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
    public void getdata(String Licence_no){
        Intent i=new Intent(GetQRcode.this,ViewDocuments.class);
        i.putExtra("lic",Licence_no);
        startActivity(i);

    }

}