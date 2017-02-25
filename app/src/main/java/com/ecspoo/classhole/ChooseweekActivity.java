package com.ecspoo.classhole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChooseweekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseweek);
        LinearLayout chooseclass = (LinearLayout)findViewById(R.id.activity_chooseweek);
        LinearLayout[] buttonarr = new LinearLayout[6];
        Button[] button = new Button[21];
        int i=1;
        int j, k;
        while(i<6) {
            j=1;
            buttonarr[i]=new LinearLayout(this);
            buttonarr[i].setWeightSum(4);
            while (j<5) {
                k=i*4-4+j;
                button[k] = new Button(this);
                button[k].setText("第" + k + "周");
                button[k].setTag(""+k);
                buttonarr[i].addView(button[k], new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                j++;
            }
            chooseclass.addView(buttonarr[i], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1));
            i++;

        }
        button[0] = new Button(this);
        button[0].setText("本周");
        button[0].setTag("0");
        buttonarr[0]=new LinearLayout(this);
        buttonarr[0].setWeightSum(4);
        buttonarr[0].addView(button[0], new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        chooseclass.addView(buttonarr[0], new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        k=0;
        while(k<button.length){
            button[k].setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(ChooseweekActivity.this, MainActivity.class);
                    startActivity(intent);
                    editor.putInt("weekshowed", Integer.parseInt(view.getTag().toString()));
                    editor.commit();
                }
            });
            k++;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chooseweek, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chooseweekback:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
