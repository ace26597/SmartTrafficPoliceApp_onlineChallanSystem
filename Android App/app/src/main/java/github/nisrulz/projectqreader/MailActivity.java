package github.nisrulz.projectqreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailActivity extends Activity {


    RequestQueue requestQueue;

    //  List<Cust_list> pack_list = new ArrayList<>();


    public static final String Name = "email";
    public static final String Name1 = "new_pass";
    private final String Forgot_Url = "http://www.gopajibaba.com/traffic/forgotpassword.php";

    String email = "";
    String new_pass = "";


    private static final String _CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    //  Random string length
    private static final int RANDOM_STR_LENGTH = 12;

    Random random = new Random();


    private static final String username = "trafficappupdate@gmail.com";
    private static final String password = "9595798728";
    private EditText emailEdit;

    String email_id, subject, message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        emailEdit = (EditText) findViewById(R.id.email_id1);

        Button sendButton = (Button) findViewById(R.id.send);

        password();

        String a = password().toString();

        new_pass = a;

        System.out.println("The value of new_pass or a " + new_pass);


        final StringBuffer message = new StringBuffer("Your new password is ");
        message.append(a);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_id = emailEdit.getText().toString();
                subject = "Your password";
                // message = "Your new password is ";
                sendMail(email_id, subject, message);

                GetForgotPAsswordOperation();
            }
        });
    }

    private void GetForgotPAsswordOperation() {

        email = email_id.toString();
        // new_pass = a;

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
                        Toast.makeText(MailActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put(Name, email);
                params.put(Name1, new_pass);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);

    }


    private int getRandomNumber() {
        int randomInt = 0;
        randomInt = random.nextInt(_CHAR.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    private String password() {

        StringBuffer randStr = new StringBuffer();

        for (int i = 0; i < RANDOM_STR_LENGTH; i++) {

            int number = getRandomNumber();
            char ch = _CHAR.charAt(number);
            randStr.append(ch);

        }
        System.out.println("Random password " + randStr.toString());
        return randStr.toString();
    }

    private void sendMail(String email, String subject, StringBuffer messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, StringBuffer messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("!!Alert!!", "TrafficApp Update"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(String.valueOf(messageBody));
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MailActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Intent i = new Intent(MailActivity.this, MainLoginPage.class);
            startActivity(i);
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
