package ie.ul.studenttimetableul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int fragmentToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if(savedInstanceState != null)
        {
            fragmentToShow = savedInstanceState.getInt("currentFragment", 0);
        }
        else {
            Intent intent = getIntent();
            fragmentToShow = getIntent().getExtras().getInt("FRAGMENT_TO_SHOW");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (fragmentToShow){
            case 0:
                setTitle("Home");
                Home home = new Home();
                fragmentManager.beginTransaction().replace(R.id.fragment, home).commit();
                break;
            case 1:
                setTitle("Timetable");
                Timetable timetable = new Timetable();
                fragmentManager.beginTransaction().replace(R.id.fragment, timetable).commit();
                break;
            case 2:
                setTitle("Modules");
                Modules modules = new Modules();
                fragmentManager.beginTransaction().replace(R.id.fragment, modules).commit();
                break;
            case 3:
                setTitle("Assignments");
                Assignments assignments = new Assignments();
                fragmentManager.beginTransaction().replace(R.id.fragment, assignments).commit();
                break;
            case 4:
                setTitle("Settings");
                Settings settings = new Settings();
                fragmentManager.beginTransaction().replace(R.id.fragment, settings).commit();
                break;
            default:
                setTitle("Home");
                home = new Home();
                fragmentManager.beginTransaction().replace(R.id.fragment, home).commit();
                break;
        }
        System.out.println("Frag = " + fragmentToShow);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragmentToShow > 0){
            fragmentToShow = 0;
            setTitle("Home");
            Home home = new Home();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, home).commit();
        }
        else if(fragmentToShow == 0) {
            System.out.println("HERE");
            finishAffinity();
            //super.onBackPressed();
        }
    }

    //Disable three Dot button / Action Button
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }*/

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentToShow = 0;
            setTitle("Home");
            Home home = new Home();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, home).commit();
        }
        else if (id == R.id.nav_timetable) {
            fragmentToShow = 1;
            setTitle("Timetable");
            Timetable timetable = new Timetable();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, timetable).commit();
        }
        else if (id == R.id.nav_modules) {
            fragmentToShow = 2;
            setTitle("Modules");
            Modules modules = new Modules();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, modules).commit();
        }
        else if (id == R.id.nav_assignments) {
            fragmentToShow = 3;
            setTitle("Assignments");
            Assignments assignments = new Assignments();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, assignments).commit();
        }
        else if (id == R.id.nav_settings) {
            fragmentToShow = 4;
            setTitle("Settings");
            Settings settings = new Settings();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, settings).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment", fragmentToShow);
    }

}
