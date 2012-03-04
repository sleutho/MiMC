package com.sl.mimc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MiMCActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MiMCActivityGridAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MiMCActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                
                if (position == 0) {
                	BufferedReader in = null;
                    try {
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet();
                        request.setURI(new URI("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest&format=html"));
                        HttpResponse response = client.execute(request);
                        in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        String NL = System.getProperty("line.separator");
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        in.close();
                        String page = sb.toString();
                        System.out.println(page);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (in != null) {
                            try {
                                in.close();
                                } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        
        
    }
}