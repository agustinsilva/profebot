package ar.com.profebot.intelligent.module;

import android.os.AsyncTask;
import android.util.Log;

import com.profebot.activities.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ar.com.profebot.activities.ModuloInteligenteTestActivity;

public class IAModuleClient extends AsyncTask<String, Void, Void> {

    private String json;
    private Integer timeout;
    private ModuloInteligenteTestActivity context;

    public IAModuleClient(String root, String term, String termContext, ModuloInteligenteTestActivity context) {
        this.json = "{\"root\": \"" + root + "\",\"term\":\"" + term + "\",\"context\":\"" + termContext + "\"}";
        this.timeout = 3600000;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        try {
            URL u = new URL(context.getString(R.string.url));
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod(context.getString(R.string.method));

            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            Log.i("HTTP Client", "HTTP status code : " + status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    Log.i("HTTP Client", "Received String : " + sb.toString());
                    //return received string
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            context.updateUI(sb.toString());
                        }
                    });
            }
        } catch (MalformedURLException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return null;
    }
}
