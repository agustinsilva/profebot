package ar.com.profebot.intelligent.module;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.profebot.activities.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ar.com.profebot.service.ExpressionsManager;

public class IAModuleClient extends AsyncTask<String, Void, Void> {

    private String json;
    private Integer timeout;
    private Context context;

    public IAModuleClient(String root, String term, String termContext, Context context) {
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
                    // Save received string
                    saveEquationsFromIAModule(sb.toString(), context);
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

    private void saveEquationsFromIAModule(String equations, Context context) {
        //Get equations from memory
        String pendingExercisesJson = PreferenceManager.getDefaultSharedPreferences(context).getString("pendingExercises","");
        JSONObject equationsJsonToStore = new JSONObject();
        if (!"".equals(pendingExercisesJson)){
            try{
                equationsJsonToStore = new JSONObject(pendingExercisesJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            String json = "{\"equations\":\"" + equations + "\"}";
            JSONObject equationsJson = new JSONObject(json);
            String equationStr = equationsJson.getString("equations");
            List<String> equationItems = Arrays.asList(equationStr.split("\\s*;\\s*"));
            for (Iterator<String> equationString = equationItems.iterator(); equationString.hasNext();) {
                JSONObject pnObj = new JSONObject();
                String newEquation = equationString.next();
                if(newEquation.contains("x")){
                    pnObj.put("equation", ExpressionsManager.removeDecimals(newEquation));
                    equationsJsonToStore.accumulate("pendingExercises", pnObj);
                }
            }
            //Save equations in memory
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString("pendingExercises",equationsJsonToStore.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
