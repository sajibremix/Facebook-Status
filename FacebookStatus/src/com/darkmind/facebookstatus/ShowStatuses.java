package com.darkmind.facebookstatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ShowStatuses extends Activity {


    public static String title;
    String catname="";
    StartingPoint sp = new StartingPoint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_statuses);
        
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView tv = (TextView) findViewById(R.id.catid2);
        tv.setText(title);
        String st = title.toLowerCase().replaceAll("\\s+","");
        final ArrayList<String> catArr = new ArrayList();
        int id = this.getResources().getIdentifier(st, "raw", this.getPackageName());
        //int r = R.raw.amazingfacebookstatus;
        final String[][] arrays = read(id);

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
                final String tag = (String) v.getTag();
                Log.e("", "tag : " + tag);
                AndroidFacebookSample afs = new AndroidFacebookSample();
                afs.MSG = tag;
                Intent myIntent = new Intent(ShowStatuses.this,AndroidFacebookSample.class);
                startActivity(myIntent);
            }
        };

        LinearLayout l = (LinearLayout) findViewById(R.id.catlini);
        int i = 0;
        for(String s : catArr){
            Button newButton = new Button(this);
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
