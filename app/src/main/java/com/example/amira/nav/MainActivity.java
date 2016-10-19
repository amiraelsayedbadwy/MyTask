package com.example.amira.nav;




import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements  LocationListener {


    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    long datestart;
    long dateend;
    long spend;
    long satrt,end;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    private LocationManager locationManager;

    Location myLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        setupToolbar();

        DataModel[] drawerItem = new DataModel[2];

        drawerItem[0] = new DataModel(R.drawable.connect, "Home");
        drawerItem[1] = new DataModel(R.drawable.fixtures, "Fragment2");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }

    public void click(View v){

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

            return;
        }


        GetLocationTask task = new GetLocationTask();
        task.execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MainFragment();
                break;
            case 1:
                fragment = new frament2();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    class GetLocationTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog waitDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {

            waitDialog.setMessage("GPS Loading. Please wait...");
            waitDialog.setIndeterminate(true);
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                while (myLocation == null) {

                    publishProgress();

                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MapsActivity.this);

                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                    if (myLocation != null) {

                        // onLocationChanged(location);
                        break;
                    }
                    Thread.sleep(5000);
                }



            }
            catch (Exception e) {
                Log.d("test", e.toString());
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void ...x) {

            // locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, MapsActivity.this, null);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MainActivity.this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);


        }

        @Override
        protected void onPostExecute(Void result) {

            waitDialog.dismiss();

            String str = "lat=" + String.valueOf(myLocation.getLatitude()) + " ,lng=" +String.valueOf(myLocation.getLongitude());

            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        datestart = System.currentTimeMillis();
        SimpleDateFormat formater=new SimpleDateFormat("hh:mm:ss");
        String dates=formater.format(new Date(datestart));
        Toast.makeText(getApplicationContext(),"time is"+dates,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dateend = System.currentTimeMillis()-datestart;
        SimpleDateFormat formater=new SimpleDateFormat("hh:mm:ss");
        String date=formater.format(new Date(dateend));
        Toast.makeText(getApplicationContext(),"time is ended"+date,Toast.LENGTH_LONG).show();

    }
    @Override

    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();

        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Leave application?");
        // Setting Dialog Message


        SimpleDateFormat formater=new SimpleDateFormat("hh:mm:ss");
        String date=formater.format(new Date(dateend));
        alertDialog.setMessage("You spend "+date);
        // Setting Icon to Dialog
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event

                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

}
