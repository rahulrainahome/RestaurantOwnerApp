package com.pro.ownerapp.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pro.ownerapp.R;
import com.pro.ownerapp.util.Constant;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText txtMessage;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        txtMessage = (EditText) findViewById(R.id.editTxtMsg);
        btnSend = (Button) findViewById(R.id.btnSendMsg);
        btnSend.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Information info = new Information();
            info.execute("");
        }
    };


    public class Information extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            //show progressbar and inactive screen interaction.
            btnSend.setEnabled(false);
            txtMessage.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(Constant.SEND_GCM_WEB_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("title", "Notification & Offers");
                jsonRequest.put("message", "some message");

                //Create output stream and write data-request
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonRequest.toString()); // should be fine if my getQuery is encoded right yes?
                writer.flush();
                writer.close();
                os.close();

                //Open and check the connectivity
                urlConnection.connect();
                int statusCode = urlConnection.getResponseCode();
                String str = urlConnection.getResponseMessage();
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    return dta.toString();
                } else {
                    return "ERROR";
                }
            } catch (Exception e) {
                return "EXCEPTION";
            }

        }


        @Override
        protected void onPostExecute(String s) {

            //Again enable the UI.
            btnSend.setEnabled(true);
            txtMessage.setEnabled(true);

            if (s.equals("ERROR"))
            {
                Toast.makeText(getApplicationContext(), "Unable to send message.\nPlease check internet connection.", Toast.LENGTH_LONG).show();
            }
            else if (s.equals("EXCEPTION"))
            {
                Toast.makeText(getApplicationContext(), "Some internal error occurred.\nPlease restart the application.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Message Sent Successfully...", Toast.LENGTH_LONG).show();
            }
        }
    }

}
