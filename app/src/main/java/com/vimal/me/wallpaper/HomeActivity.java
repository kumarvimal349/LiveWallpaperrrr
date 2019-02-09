package com.vimal.me.wallpaper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.vimal.me.wallpaper.Adapter.MyFragmentAdapter;
import com.vimal.me.wallpaper.Common.Common;

import static com.vimal.me.wallpaper.Common.Common.SIGN_IN_REQUEST_CODE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    DrawerLayout drawer;
    NavigationView navigationView;

    BottomNavigationView menu_bottom;

    Context context;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Common.PERMISSSION_REQUEST_CODE:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"You need accept this permission to download image",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }


    //Override  method onActivityResult By  ctlr + o


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == Common.SIGN_IN_REQUEST_CODE)
        {
            if(requestCode == RESULT_OK)
            {
                Snackbar.make(drawer,new StringBuilder("Welcome ")
                        .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                                .toString()),Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Wallpaper HD");
        setSupportActionBar(toolbar);


        menu_bottom = (BottomNavigationView)findViewById(R.id.navigation);

        menu_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_upload) {
                    startActivity(new Intent(HomeActivity.this,UploadWallpaper.class));

                }


             else   if(item.getItemId()==R.id.rate)
                {
                    Intent rate = new Intent(Intent.ACTION_VIEW);
                    rate.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.vimal.me.a9bestfoodsforyou"));
                    startActivity(rate);
                   return true;
                }
                else   if(item.getItemId()==R.id.exit)
                {
                    finish();
                }
                return false;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check if not sign_in then navigate sign_in page
        if(FirebaseAuth.getInstance().getCurrentUser()==null)

        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                    Common.SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(drawer,new StringBuilder("Welcome ")
                    .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                            .toString()),Snackbar.LENGTH_LONG)
                    .show();

            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Common.PERMISSSION_REQUEST_CODE);
            }

            viewPager  = (ViewPager)findViewById(R.id.viewPager);
            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),this);
            viewPager.setAdapter(adapter);

            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);


            loadUserInformation();

        }


        // Request Runtime permisssion

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Common.PERMISSSION_REQUEST_CODE);
        }

        viewPager  = (ViewPager)findViewById(R.id.viewPager);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void loadUserInformation() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            View headerLayout = navigationView.getHeaderView(0);
            TextView txt_email = (TextView) headerLayout.findViewById(R.id.txt_email);
            txt_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent fshare = new Intent(Intent.ACTION_SEND);
            fshare.setType("text/plain");
            String shareBody = "Your body here";
            String shareSub = "Your Subject here";
            fshare.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            fshare.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(fshare, "Share using"));
            return true;
        }

        else if(id==R.id.exit)
        {
            finish();
            return true;
        }
      else
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(this,HomeActivity.class));
            Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();


        } else if (id == R.id.profile) {
            startActivity(new Intent(this,Aboutus.class));
            Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.exit) {
            finish();
            Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.rate) {

            Intent rate = new Intent(Intent.ACTION_VIEW);
            rate.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.vimal.me.a9bestfoodsforyou"));
            startActivity(rate);
            Toast.makeText(this,"Rate us",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.more) {
            Intent moreapp = new Intent(Intent.ACTION_VIEW);
            moreapp.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.vimal.me.a9bestfoodsforyou"));
            startActivity(moreapp);

        Toast.makeText(this,"More App",Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.share) {
            Intent fshare = new Intent(Intent.ACTION_SEND);
            fshare.setType("text/plain");
            String shareBody = "Your body here";
            String shareSub = "Your Subject here";
            fshare.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            fshare.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(fshare, "Share using"));
            Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.sendfeedback) {

            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "kumarvimal349@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));


            Toast.makeText(this,"Send Feedback",Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}