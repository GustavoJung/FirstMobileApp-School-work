package com.gustavojung.pin1;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    Dialog myDialog;
    Spinner spinner;
    String erro="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         myDialog= new Dialog(this);



        RecyclerView list = (RecyclerView)findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        list.setAdapter(new HorizontalAdapter(new String[]{String.valueOf(R.drawable.gotham), String.valueOf(R.drawable.between),String.valueOf(R.drawable.dexter)}));

        RecyclerView list2 = (RecyclerView)findViewById(R.id.list2);
        list2.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        list2.setAdapter(new HorizontalAdapter(new String[]{String.valueOf(R.drawable.houseofcards),
                String.valueOf(R.drawable.lost),String.valueOf(R.drawable.orphanblack),
                String.valueOf(R.drawable.sherlock),String.valueOf(R.drawable.slasher)}));

        RecyclerView list3 = (RecyclerView)findViewById(R.id.list3);
        list3.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        list3.setAdapter(new HorizontalAdapter(new String[]{String.valueOf(R.drawable.atypical),
                String.valueOf(R.drawable.howimetyourmother),String.valueOf(R.drawable.lacasadepapel),
                String.valueOf(R.drawable.santaclaritadiet),String.valueOf(R.drawable.vikings),String.valueOf(R.drawable.theranch)}));

        RecyclerView list4 = (RecyclerView)findViewById(R.id.list4);
        list4.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        list4.setAdapter(new HorizontalAdapter(new String[]{String.valueOf(R.drawable.atypical),
                String.valueOf(R.drawable.howimetyourmother),String.valueOf(R.drawable.lacasadepapel),
                String.valueOf(R.drawable.santaclaritadiet),String.valueOf(R.drawable.vikings),String.valueOf(R.drawable.theranch)}));





        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            TextView txtclose;
            myDialog.setContentView(R.layout.popup_menu1);
            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);

            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            myDialog.show();

        } else if (id == R.id.nav_gallery) {

            TextView txtclose2;
            myDialog.setContentView(R.layout.popup_menu2);
            txtclose2 = myDialog.findViewById(R.id.txtclose2);

            txtclose2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        } else if (id == R.id.nav_slideshow) {

            TextView txtclose3;
            myDialog.setContentView(R.layout.popup_menu3);
            txtclose3 = myDialog.findViewById(R.id.txtclose3);

            txtclose3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();

        } else if (id == R.id.nav_manage) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setTitle("Deseja fechar o aplicativo?");
            msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });

            msg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            AlertDialog alert = msg.create();
            alert.show();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
