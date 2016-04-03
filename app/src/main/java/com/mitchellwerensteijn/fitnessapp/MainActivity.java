package com.mitchellwerensteijn.fitnessapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar;
    private android.support.design.widget.TabLayout mTabs;
    private FragmentPagerAdapter adapterViewPager;

    private ArrayList<RelativeLayout> warmupButtons = new ArrayList<RelativeLayout>();
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        initToolbar();
        initViewPager();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item

                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar

        toolbar.inflateMenu(R.menu.workout_menu);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void initViewPager(){
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("Squat"));
        mTabs.addTab(mTabs.newTab().setText("Bench Press"));
        mTabs.addTab(mTabs.newTab().setText("Barbell Row"));
        mTabs.addTab(mTabs.newTab().setText("Pull up"));
        mTabs.addTab(mTabs.newTab().setText("Chin up"));
        mTabs.addTab(mTabs.newTab().setText("Bicep curl"));
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        final ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 6;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return ExerciseFragment.newInstance(0, "Squat");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ExerciseFragment.newInstance(1, "Bench press");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ExerciseFragment.newInstance(2, "Barbell row");
                case 3: // Fragment # 1 - This will show SecondFragment
                    return ExerciseFragment.newInstance(3, "Pull up");
                case 4: // Fragment # 1 - This will show SecondFragment
                    return ExerciseFragment.newInstance(4, "Chin up");
                case 5: // Fragment # 1 - This will show SecondFragment
                    return ExerciseFragment.newInstance(5, "Bicep curl");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }



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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_menu, menu);

        //MenuItem menuSearchItem = menu.findItem(R.id.my_search);
        // Get the SearchView and set the searchable configuration

        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menuSearchItem.getActionView();

        //SearchView searchView = (SearchView) menuSearchItem.getActionView();
        // Assumes current activity is the searchable activity

        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // About option clicked.

                return true;
            case R.id.action_exit:
                // Exit option clicked.

                return true;
            case R.id.action_settings:
                // Settings option clicked.

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ExerciseFragment extends Fragment {
        private String title;
        private int page;

        public static ExerciseFragment newInstance(int page, String title) {
            ExerciseFragment fragmentFirst = new ExerciseFragment();
            Bundle args = new Bundle();
            args.putInt("someInt", page);
            args.putString("someTitle", title);
            fragmentFirst.setArguments(args);
            return fragmentFirst;
        }

        // Store instance variables based on arguments passed
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.exercise_fragment, container, false);
            TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
            tvLabel.setText(page + " -- " + title);

            return view;
        }
    }
}
