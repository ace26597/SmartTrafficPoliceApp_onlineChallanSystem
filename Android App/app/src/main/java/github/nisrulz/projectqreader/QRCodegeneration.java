package github.nisrulz.projectqreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;

public class QRCodegeneration extends AppCompatActivity {
    ImageView img_result_qr1;

   ImageView qrCodeImageview;
    String QRcode=null;
    public final static int WIDTH=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodegeneration);
        qrCodeImageview=(ImageView) findViewById(R.id.img_result_qr);
        String content = "+919595798728";
        SharedPreferences sharedpref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //nname=sharedpref.getString("username", "");
        QRcode=sharedpref.getString("licno","");


       getID();
       Thread t = new Thread(new Runnable() {
           public void run() {
//               QRcode="This is My first QR code";
               try {
                   synchronized (this) {
                       wait(2000);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               try {
                                   Bitmap bitmap = null;

                                   bitmap = encodeAsBitmap(QRcode);
                                   qrCodeImageview.setImageBitmap(bitmap);

                               } catch (WriterException e) {
                                   e.printStackTrace();
                               } // end of catch block

                           } // end of run method
                       });

                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }



           }
       });
       t.start();

   }

    private void getID() {
        qrCodeImageview=(ImageView) findViewById(R.id.img_result_qr);

    }

    // this is method call from on create and return bitmap image of QRCode.
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }-
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method
}
