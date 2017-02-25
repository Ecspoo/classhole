package com.ecspoo.classhole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EditActivity extends AppCompatActivity {
    static String ori = "";
    static int yyyy, mm, dd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        yyyy = preferences.getInt("yyyy", 0);
        mm = preferences.getInt("mm", 0);
        dd = preferences.getInt("dd", 0);
        String f = "VeryImportant";
        try {
            FileInputStream fileInputStream = this.openFileInput(f);
            byte[] bytes = new byte[fileInputStream.available()];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while(fileInputStream.read(bytes)!=-1){
                byteArrayOutputStream.write(bytes, 0, bytes.length);
            }
            fileInputStream.close();
            byteArrayOutputStream.close();
            ori = new String(byteArrayOutputStream.toByteArray());
            ori=ori.replace(Character.toString((char)0), "");
        }catch(Exception e){
            ori = "彩蛋 6,7,8 5 5,6,7,8";
        }
        EditText editText = (EditText) findViewById(R.id.editclassesedittext);
        EditText editTextY = (EditText)findViewById(R.id.yyyy);
        EditText editTextM = (EditText)findViewById(R.id.mm);
        EditText editTextD = (EditText)findViewById(R.id.dd);
        if(!ori.equals("彩蛋 6,7,8 5 5,6,7,8")){
            editText.setText(ori);
            editText.setSelection(ori.length());
        }
        if(yyyy!=0){
            editTextY.setText(""+yyyy);
        }
        if(mm!=0){
            editTextM.setText(""+mm);
        }
        if(dd!=0){
            editTextD.setText(""+dd);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        EditText editText = (EditText) findViewById(R.id.editclassesedittext);
        EditText editTextY = (EditText)findViewById(R.id.yyyy);
        EditText editTextM = (EditText)findViewById(R.id.mm);
        EditText editTextD = (EditText)findViewById(R.id.dd);
        switch (item.getItemId()) {
            case R.id.editclassesreset:
                if(ori.equals("彩蛋 6,7,8 5 5,6,7,8")){
                    editText.setText("");
                }else{
                    editText.setText(ori);
                    editText.setSelection(ori.length());
                }
                if(yyyy==0){
                    editTextY.setText("");
                }else{
                    editTextY.setText(""+yyyy);
                }
                if(mm==0){
                    editTextM.setText("");
                }else{
                    editTextM.setText(""+mm);
                }
                if(dd==0){
                    editTextD.setText("");
                }else{
                    editTextD.setText(""+dd);
                }
                return true;
            case R.id.editclassesfinish:
                String write = editText.getText().toString();
                if(write.equals("")){
                    write = "彩蛋 6,7,8 5 5,6,7,8";
                }
                String y = editTextY.getText().toString();
                String m = editTextM.getText().toString();
                String d = editTextD.getText().toString();
                String f = "VeryImportant";
                try{
                    FileOutputStream fileOutputStream = openFileOutput(f, MODE_PRIVATE);
                    fileOutputStream.write(write.getBytes());
                    fileOutputStream.close();
                    editor.putInt("yyyy", Integer.parseInt(y));
                    editor.putInt("mm", Integer.parseInt(m));
                    editor.putInt("dd", Integer.parseInt(d));
                    editor.commit();
                }catch (Exception e){
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
