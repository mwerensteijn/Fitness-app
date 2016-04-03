package com.mitchellwerensteijn.fitnessapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] drawerMenuItems = new String[]{"Workout", "History", "Settings"};
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CharSequence toolbarTitle;
    private ActionBarDrawerToggle drawerToggle;

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        toolbarTitle = "Fitness app";
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerMenuItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(toolbarTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(toolbarTitle);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        configureGraph();
        initializeWarmupViews(5);
    }

    public float dpToPixels(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public float pixelsToDP(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private ArrayList<RelativeLayout> warmupButtons = new ArrayList<RelativeLayout>();

    public void initializeWarmupViews(int warmups) {
        LayoutParams lp = new LayoutParams((int) dpToPixels(40), (int) dpToPixels(40));


        LinearLayout ll = (LinearLayout) findViewById(R.id.gridlayout_warmup);

        /*int spaceWidth = ll.getWidth();
        int buttonsPerRow = (int) (spaceWidth / dpToPixels(40));
*/

        for(int i = 0; i < warmups; i++) {
            Button b = new Button(this);
            b.setText("");
            b.setLayoutParams(lp);
            b.setTextColor(Color.WHITE);
            b.setBackgroundResource(R.drawable.inactive_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countRep(v);
                }
            });

            TextView t = new TextView(this);
            t.setText("3x60");
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) t.getLayoutParams();
            //rlp.addRule(RelativeLayout.ABOVE, b.getId());

            RelativeLayout rl = new RelativeLayout(this);
            rl.addView(t);
            rl.addView(b);

            warmupButtons.add(rl);
            ll.addView(warmupButtons.get(i));

            /*Space s = new Space(this);
            lp = new LinearLayout.LayoutParams(0, (int) dpToPixels(40), 1f);
            s.setLayoutParams(lp);

            ll.addView(s);*/
        }

        ll.invalidate();
    }

    public void countRep(View v) {
        Button b = (Button) v;

        if(b.getText().toString().equals("")) {
            b.setText(Integer.toString(5));
            b.setBackgroundResource(R.drawable.rounded_button);
        } else {
            int x = Integer.parseInt(b.getText().toString());
            if (x > 0) {
                b.setText(Integer.toString(x - 1));
            } else {
                b.setText("");
                b.setBackgroundResource(R.drawable.inactive_button);
            }
        }
    }

    private void configureGraph() {
        // in this example, a LineChart is initialized from xml
        mChart = (LineChart) findViewById(R.id.exercise_chart);
        mChart.setNoDataTextDescription("There is no history yet.");
        mChart.setDescription("");
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setDragEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        setGraphData();

        mChart.invalidate();
        mChart.animateY(600, Easing.EasingOption.EaseInBounce);
    }

    private void setGraphData() {
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("18/03/16");
        xVals.add("20/03/16");
        xVals.add("22/03/16");
        xVals.add("25/03/16");
        xVals.add("28/03/16");

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(72.5f, 0));
        yVals.add(new Entry(75f, 1));
        yVals.add(new Entry(75f, 2));
        yVals.add(new Entry(77.5f, 3));
        yVals.add(new Entry(80f, 4));

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setLineWidth(1.5f);
        set1.setCircleRadius(4f);
        set1.setHighlightEnabled(false);
        set1.setColor(Color.parseColor("#90CAF9"));
        set1.setValueTextSize(10f);

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);

        // set data
        mChart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        setTitle(drawerMenuItems[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle = title;
        getSupportActionBar().setTitle(toolbarTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
