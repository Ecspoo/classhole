package com.ecspoo.classhole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int weekshowed = preferences.getInt("weekshowed", 0);
        int yyyy = preferences.getInt("yyyy", 0);
        int mm = preferences.getInt("mm", 0);
        int dd = preferences.getInt("dd", 0);
        int week;
        int[] trans={0, 6, 0, 1, 2, 3, 4, 5};
        Calendar firstMon = Calendar.getInstance();
        firstMon.set(yyyy, mm-1, dd);
        firstMon.set(yyyy, mm-1, dd-trans[firstMon.get(Calendar.DAY_OF_WEEK)]);
        Calendar today = Calendar.getInstance();

        if(firstMon.get(Calendar.YEAR)>today.get(Calendar.YEAR)||(firstMon.get(Calendar.YEAR)==today.get(Calendar.YEAR)&&firstMon.get(Calendar.DAY_OF_YEAR)>today.get(Calendar.DAY_OF_YEAR))){
            week = 0;
        }else{
            int days=0;
            Calendar lastday = Calendar.getInstance();
            lastday.set(firstMon.get(Calendar.YEAR), 11, 31);
            while(firstMon.get(Calendar.YEAR)<today.get(Calendar.YEAR)){
                days+=lastday.get(Calendar.DAY_OF_YEAR)-firstMon.get(Calendar.DAY_OF_YEAR);
                firstMon.set(firstMon.get(Calendar.YEAR)+1, 0, 1);
                lastday.set(firstMon.get(Calendar.YEAR), 11, 31);
                days++;
            }
            days+=today.get(Calendar.DAY_OF_YEAR)-firstMon.get(Calendar.DAY_OF_YEAR);
            week=days/7+1;
        }
        int intD = trans[today.get(Calendar.DAY_OF_WEEK)];
        int intC = today.get(Calendar.HOUR_OF_DAY);
        String ori = "";
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
        }catch(Exception e){
            ori = "彩蛋 6,7,8 5 5,6,7,8";
        }
        TextView textView = (TextView) findViewById(R.id.week);
        if(weekshowed==0||week==weekshowed){
            textView.setText("这是本周的课表（第"+week+"周）");
        }else {
            week = weekshowed;
            textView.setText("这是第" + week + "周的课表（非本周）");
        }
        if(week==0){
            textView.setText("还尚未开学");
        }
        if(ori.equals("彩蛋 6,7,8 5 5,6,7,8")||yyyy==0||mm==0||dd==0){
            textView.setText("尚未创建课表信息，或创建的信息不完整");
        }
        ori=ori.replace(Character.toString((char)0), "");
        ori=ori.replace(Character.toString((char)160), " ");
        ori=ori.replace("\r", "\n");
        ori=only1(ori, " ");
        ori=only1(ori, "\n");
        String[] sArr = ori.split("\n");
        String[][][] clas = new String[7][sArr.length][10];
        int[] count = {0, 0, 0, 0, 0, 0, 0};
        int i = 0;
        while(i<sArr.length){
            String[] tem = sArr[i].split(" ");
            if(tem.length<4){
                i++;
                continue;
            }
            int d;
            try{
                d = Integer.parseInt(tem[2])-1;
            }catch(Exception e){
                i++;
                continue;
            }
            if(tem[0].contains("for")){
                clas[d][count[d]][4]=tem[0].split("for")[1];
                tem[0]=tem[0].split("for")[0];
            }else{
                clas[d][count[d]][4]="";
            }
            if(tem[0].contains("from")){
                clas[d][count[d]][3]=tem[0].split("from")[1];
                tem[0]=tem[0].split("from")[0];
            }else{
                clas[d][count[d]][3]="";
            }
            if(tem[0].contains("@")){
                clas[d][count[d]][2]=tem[0].split("@")[1];
                clas[d][count[d]][2]=clas[d][count[d]][2].replace("东校区", "");
                clas[d][count[d]][2]=clas[d][count[d]][2].replace("西校区", "");
                clas[d][count[d]][2]=clas[d][count[d]][2]+"\n";
                tem[0]=tem[0].split("@")[0];
            }else{
                clas[d][count[d]][2]="";
            }
            if(tem[0].contains("#")){
                clas[d][count[d]][1]=tem[0].split("#")[1];
                tem[0]=tem[0].split("#")[0];
            }
            clas[d][count[d]][0]=tem[0];
            clas[d][count[d]][5] = tem[1];
            clas[d][count[d]][6] = "<split>"+tem[1].replace(",", "<split>")+"<split>";
            clas[d][count[d]][7] = tem[3];
            clas[d][count[d]][8] = "<split>"+tem[3].replace(",", "<split>")+"<split>";
            clas[d][count[d]][9] = sArr[i];
            count[d]++;
            i++;
        }
        showclass(clas, count, week);
        showtime(intD, intC);
        editor.putInt("weekshowed", 0);
        editor.commit();
    }
    String only1(String source, String key){
        while(source.contains(key+key)){
            source = source.replace(key+key, key);
        }
        return source;
    }
    void showclass(String[][][] clas, int[] count, int week){
        LinearLayout classtable = (LinearLayout)findViewById(R.id.classtable);
        classtable.setWeightSum(7);
        int d = 0;
        while(d<7){
            LinearLayout day = new LinearLayout(this);
            day.setOrientation(LinearLayout.VERTICAL);
            day.setWeightSum(12);
            String[] c = {"", "", "", "", "", "", "", "", "", "", "", "", "", ""};
            int i=0;
            while(i<count[d]){
                if(clas[d][i][6].contains("<split>"+week+"<split>")){
                    int j=1;
                    while(j<c.length) {
                        if(clas[d][i][8].contains("<split>"+j+"<split>")){
                            c[j]=""+i;
                        }
                        j++;
                    }
                }
                i++;
            }
            i=1;
            while(i<13){
                if(c[i].equals("")){
                    day.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                }else{
                    int j = 1;
                    while(c[i].equals(c[i+1])){
                        j++;
                        i++;
                    }
                    View b = new View(this);
                    b.setBackgroundColor(Color.BLACK);
                    View bb = new View(this);
                    bb.setBackgroundColor(Color.BLACK);
                    View bbb = new View(this);
                    bbb.setBackgroundColor(Color.BLACK);
                    View bbbb = new View(this);
                    bbbb.setBackgroundColor(Color.BLACK);
                    TextView text = new TextView(this);
                    View g = new View(this);
                    g.setBackgroundColor(Color.argb(20, 0, 0, 0));
                    View gg = new View(this);
                    gg.setBackgroundColor(Color.argb(20, 0, 0, 0));
                    text.setText(clas[d][Integer.parseInt(c[i])][2]+clas[d][Integer.parseInt(c[i])][0]);
                    text.setBackgroundColor(Color.argb(20, 0, 255, 255));
                    text.setTag(clas[d][Integer.parseInt(c[i])][9]);
                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.ecspoo.classhole.keys", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("classdetail", view.getTag().toString());
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            startActivity(intent);
                        }
                    });
                    LinearLayout bl = new LinearLayout(this);
                    bl.setWeightSum(1);
                    bl.addView(b, new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    bl.addView(text, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    bl.addView(bb, new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    LinearLayout black = new LinearLayout(this);
                    black.setOrientation(LinearLayout.VERTICAL);
                    black.setWeightSum(1);
                    black.addView(g, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1, 0));
                    black.addView(bbb, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2, 0));
                    black.addView(bl, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                    black.addView(bbbb, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2, 0));
                    black.addView(gg, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2, 0));
                    day.addView(black, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, j));
                }
                i++;
            }
            classtable.addView(day, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            View f = new View(this);
            if(d==6){
                f.setBackgroundColor(Color.argb(255, 0, 0, 0));
            }else{
                f.setBackgroundColor(Color.argb(20, 0, 0, 0));
            }
            classtable.addView(f, new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            d++;
        }
    }
    void showtime(int d, int h){
        LinearLayout time = (LinearLayout)findViewById(R.id.time);
        time.setWeightSum(7);
        View r = new View(this);
        r.setBackgroundColor(Color.RED);
        View rr = new View(this);
        rr.setBackgroundColor(Color.RED);
        View rrr = new View(this);
        rrr.setBackgroundColor(Color.RED);
        View rrrr = new View(this);
        rrrr.setBackgroundColor(Color.RED);
        View rrrrr = new View(this);
        rrrrr.setBackgroundColor(Color.RED);
        View w = new View(this);
        LinearLayout re = new LinearLayout(this);
        re.setWeightSum(1);
        re.setOrientation(LinearLayout.VERTICAL);
        re.addView(r, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4, 0));
        re.addView(w, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        re.addView(rr, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 4, 0));
        LinearLayout red = new LinearLayout(this);
        red.setWeightSum(1);
        red.addView(new View(this), new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        red.addView(rrr, new LinearLayout.LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        red.addView(re, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        red.addView(rrrr, new LinearLayout.LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        red.addView(new View(this), new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        LinearLayout R = new LinearLayout(this);
        R.setOrientation(LinearLayout.VERTICAL);
        R.setWeightSum(2);
        R.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        R.addView(rrrrr, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8, 0));
        R.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        LinearLayout RED = new LinearLayout(this);
        RED.setWeightSum(1);
        RED.addView(new View(this), new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        RED.addView(R, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        RED.addView(new View(this), new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        LinearLayout day = new LinearLayout(this);
        day.setOrientation(LinearLayout.VERTICAL);
        int[] sta={0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 13, 13, 5, 6, 7, 8, 14, 9, 10, 11, 12, 15};
        int c = sta[h];
        d++;
        if(c==15){
            c=0;
            d++;
        }
        if(c==0){
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, d-1));
            time.addView(red, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7-d));
            return;
        }
        if(c==13){
            day.setWeightSum(3);
            day.addView(RED, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
            day.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, d-1));
            time.addView(day, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7-d));
            return;
        }
        if(c==14){
            day.setWeightSum(3);
            day.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            day.addView(RED, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, d-1));
            time.addView(day, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7-d));
            return;
        }
        day.setWeightSum(12);
        day.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, c-1));
        day.addView(red, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        day.addView(new View(this), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 12-c));
        time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, d-1));
        time.addView(day, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        time.addView(new View(this), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7-d));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editclasses:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
            case R.id.otherweek:
                Intent intent1 = new Intent(this, ChooseweekActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
