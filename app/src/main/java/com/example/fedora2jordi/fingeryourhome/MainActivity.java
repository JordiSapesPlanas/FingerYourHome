package com.example.fedora2jordi.fingeryourhome;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private Button btn_new_scaner, btn_scaner_on, btn_scaner_off;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //btn_new_scaner = (Button)findViewById(R.id.btn_new_scan);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.


        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        Button btn_new;
        Button btn_save;
        Button btn_enable;
        Button btn_disable;
        EditText ed_ip;
        EditText ed_port;
        Handler handler;
        HttpThread httpThread;
        Switch sw_scan;
        Switch sw_notifications;
        PopupWindow popupWindow;
        EditText ed_name;


        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    String text =(String)msg.obj;
                    Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
            };
            View rootView ;
            switch(i){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_options, container, false);
                    btn_new = (Button) rootView.findViewById(R.id.btn_new_scan);
                    sw_notifications = (Switch)rootView.findViewById(R.id.btn_notifications);
                    sw_scan = (Switch)rootView.findViewById(R.id.btn_scan);
                    sw_notifications.setOnClickListener(this);
                    sw_scan.setOnClickListener(this);
                    btn_new.setOnClickListener(this);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_advanced_options, container, false);
                    btn_save =(Button) rootView.findViewById(R.id.btn_save);
                    ed_ip = (EditText)rootView.findViewById(R.id.ed_ip);
                    ed_port = (EditText)rootView.findViewById(R.id.ed_port);
                    btn_save.setOnClickListener(this);
                    SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String ip = preferences.getString("ip", "127.0.0.1");
                    String port = preferences.getString("port", "3000");
                    ed_ip.setText(ip);
                    ed_port.setText(port);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_users, container, false);

                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_options, container, false);

                    break;
            }

            return rootView;
        }


        @Override
        public void onClick(View view) {
            ConnectivityManager cnManager = (ConnectivityManager)getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cnManager.getActiveNetworkInfo();
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if(networkInfo != null && networkInfo.isConnected())


                switch (view.getId()){
                    case R.id.btn_new_scan:


                        popUp();


                        break;
                    case R.id.btn_notifications:
                        editor.putBoolean("notifications", sw_notifications.isChecked());
                        editor.commit();
                        break;
                    case R.id.btn_scan:
                        if(sw_scan.isChecked()) {
                            httpThread = new HttpThread("POST", "fingerprint/status", null);
                            httpThread.start();
                        }else{
                            httpThread = new HttpThread("DELETE", "fingerprint/status", null);
                        }
                        break;
                    case R.id.btn_save:
                        String ip =  ed_ip.getText().toString();
                        String port = ed_port.getText().toString();

                        editor.putString("ip", ip);
                        editor.putString("port", port);
                        editor.commit();
                        Toast.makeText(getActivity().getApplicationContext(), "Saved",
                                Toast.LENGTH_LONG).show();
                        ed_ip.setText(ip);
                        ed_port.setText(port);
                        break;
                }
            else{


                Toast.makeText(getActivity().getApplicationContext(), "No network, please be sure that is enabled",
                        Toast.LENGTH_LONG).show();
            }
        }

        private void popUp(){
            try {
// We need to get the instance of the LayoutInflater
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.pop_up_new_finger_print,
                        (ViewGroup) getActivity().findViewById(R.id.pop_up_element));
                Log.e("....", "......");

                int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                Log.e("....", " width: "+ width);
                int heigh = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                Log.e("....", " height: "+ heigh);

                popupWindow = new PopupWindow(layout, width-20, heigh/2, true);

                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                Button accept = (Button) layout.findViewById(R.id.btn_accept);
                Button cancel = (Button) layout.findViewById(R.id.btn_cancel);
                ed_name = (EditText) layout.findViewById(R.id.ed_name);
                cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
                });
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject j = new JSONObject();
                        try {
                            j.put("name", ed_name.getText().toString());
                            HttpThread thread = new HttpThread("POST", "fingerprint", null);
                            thread.start();
                            popupWindow.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            } catch (Exception e) {
               String s = e.toString();
                Log.e(" dksn ", s);
            }

        }


        private class HttpThread extends Thread{
            private String method;
            private JSONObject jsonObject;
            private String url;
            private OutputStream outputStream;
            private Message message ;
            private HttpThread(String method, String url, JSONObject jsonObject) {
                this.method = method;
                this.url = url;
                this.jsonObject = jsonObject;
            }

            @Override
            public void run() {
                try {

                    SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                    String ip = preferences.getString("ip", "127.0.0.1");
                    String port = preferences.getString("port", "3000");
                    URL ur = new URL("http://"+ip+":"+port+"/"+url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) ur.openConnection();
                    if(method.equals("GET")){
                        httpURLConnection.setDoOutput(false);
                    }else{
                        httpURLConnection.setDoOutput(true);
                    }
                    httpURLConnection.setRequestMethod(method);
                    outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(jsonObject.toString().getBytes());
                    if(httpURLConnection.getResponseCode() == 200){
                        message = new Message();
                        message.obj = "received 200";
                        handler.sendMessage(message);

                    }else{
                        message = new Message();
                        message.obj = "received" + httpURLConnection.getResponseCode();
                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    message = new Message();
                    message.obj = e.toString();
                    handler.sendMessage(message);

                }
            }
        }

    }

}
