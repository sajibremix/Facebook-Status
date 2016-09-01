package com.darkmind.facebookstatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.android.Facebook;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class StartingPoint extends Activity{

	public static Facebook fb;
    SharedPreferences sp;
    Button newButton;
	TextView header;
	ImageView button;
	String catname="";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startingpoint);
        showCatagories();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        if(!isNetworkAvailable()){
    		Toast.makeText(getBaseContext(), "Network Connection is Off", Toast.LENGTH_LONG).show();
    	}
        
    }
    
    
    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    
    private void showCatagories() {
        header = (TextView)findViewById(R.id.textView);
        header.setText("Categories");

        final ArrayList<String> catArr = new ArrayList();
        int r = R.raw.categoriesname;
        final String[][] arrays = read(r);

        if(arrays == null){
            catname="no such file";
        }else{

            for (String[] array : arrays) {
                for (String v : array) {
                    catname = v ;
                }
                catArr.add(catname);
            }
        }
        View.OnClickListener myClickLIstener= new View.OnClickListener() {
            public void onClick(View v) {
                String tag = (String) v.getTag();
                Log.e("", "tag : " + tag);
                ShowStatuses st = new ShowStatuses();
                st.title = tag;
                Intent myIntent = new Intent(StartingPoint.this,ShowStatuses.class);
                startActivity(myIntent);
            }
        };

        LinearLayout l = (LinearLayout) findViewById(R.id.catlini);
        int i = 0;
        for(String s : catArr){
            newButton = new Button(this);
            newButton.setText(catArr.get(i++));
            newButton.setBackgroundColor(0xFF99D6D6);
            newButton.setOnClickListener(myClickLIstener);
            newButton.setTag(s);
            l.addView(newButton);
        }
    }


    public String[][] read(int r) {
        Workbook workbook = null;

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            InputStream iS = getResources().openRawResource(r);

            workbook = Workbook.getWorkbook(iS);
            Sheet sheet = workbook.getSheet(0);

            int rowCount = sheet.getRows();
            String[][] result = new String[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                Cell[] row = sheet.getRow(i);

                result[i] = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    result[i][j] = row[j].getContents();
                }
            }
            return result;


        } catch (BiffException e) {
            catname=catname+ e.toString();

        } catch (IOException e) {
            catname=catname+ e.toString();
        } catch (Exception e) {
            catname=catname+ e.toString();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return null;
    }

}
