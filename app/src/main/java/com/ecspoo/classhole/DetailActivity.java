package com.ecspoo.classhole;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        LinearLayout details = (LinearLayout)findViewById(R.id.details);
        details.setWeightSum(8);
        String[] tem = preferences.getString("classdetail", "").split(" ");
        String[] detail = new String[8];
        if(tem[0].contains("for")){
            detail[0]=tem[0].split("for")[1];
            tem[0]=tem[0].split("for")[0];
        }else{
            detail[0]="";
        }
        if(tem[0].contains("from")){
            detail[1]=tem[0].split("from")[1];
            tem[0]=tem[0].split("from")[0];
        }else{
            detail[1]="";
        }
        if(tem[0].contains("@")){
            detail[7]=tem[0].split("@")[1];
            tem[0]=tem[0].split("@")[0];
        }else{
            detail[7]="";
        }
        if(tem[0].contains("#")){
            detail[2]=tem[0].split("#")[1];
            tem[0]=tem[0].split("#")[0];
        }else{
            detail[2]="";
        }
        detail[3]=tem[0];
        detail[4]=tem[1].replace(",", ", ");
        detail[5]=tem[2];
        detail[6]=tem[3].replace(",", ", ");
        int i=0;
        while (i<8){
            TextView textView = new TextView(this);
            textView.setText(detail[i]);
            details.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
            i++;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detailback:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
