package com.gustavojung.pin1;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String nome_user;
    Dialog myDialog;
    Spinner spinner;
    Uri img_url;

    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<Integer> mImageUrls1 = new ArrayList<>();
    private ArrayList<Integer> mImageUrls2 = new ArrayList<>();
    private ArrayList<Integer> mImageUrls3 = new ArrayList<>();
    private static final int RC_SIGN_IN = 123;
    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    FirebaseAuth auth;


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        TextView name;
        View header = navigationView.getHeaderView(0);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            setUserInfo();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN);

            if (auth.getCurrentUser() != null) {
                setUserInfo();
            }
        }
            addImages();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(layoutManager);
        list.setAdapter(new HorizontalAdapter(this, mImageUrls));


        RecyclerView list2 = (RecyclerView) findViewById(R.id.list2);
        list2.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        list2.setAdapter(new HorizontalAdapter(this, mImageUrls1));


        RecyclerView list3 = (RecyclerView) findViewById(R.id.list3);
        list3.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        list3.setAdapter(new HorizontalAdapter(this, mImageUrls2));


        RecyclerView list4 = (RecyclerView) findViewById(R.id.list4);
        list4.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        list4.setAdapter(new HorizontalAdapter(this, mImageUrls3));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                setUserInfo();
                Toast.makeText(this, "Login realizado com Sucesso", Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }
                Log.e("Main activity", "Erro de conexão: ", response.getError());
            }
        }
    }

    private void setUserInfo(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        TextView name;
        View header = navigationView.getHeaderView(0);

        mDisplayImageView = (ImageView) header.findViewById(R.id.foto_usuario);
        mNameTextView = (TextView) header.findViewById(R.id.nome_usuario);
        Glide.with(navigationView)
                .load(auth.getCurrentUser().getPhotoUrl())
                .into(mDisplayImageView);
        mNameTextView.setText(auth.getCurrentUser().getDisplayName());
    }

    public Activity contexto() {
        return this;
    }

    private void addImages(){
        mImageUrls.add(R.drawable.gotham);
        mImageUrls.add(R.drawable.between);
        mImageUrls.add(R.drawable.dexter);


        mImageUrls1.add(R.drawable.houseofcards);
        mImageUrls1.add(R.drawable.lost);
        mImageUrls1.add(R.drawable.orphanblack);
        mImageUrls1.add(R.drawable.sherlock);
        mImageUrls1.add(R.drawable.slasher);


        mImageUrls2.add(R.drawable.atypical);
        mImageUrls2.add(R.drawable.howimetyourmother);
        mImageUrls2.add(R.drawable.lacasadepapel);
        mImageUrls2.add(R.drawable.santaclaritadiet);
        mImageUrls2.add(R.drawable.vikings);
        mImageUrls2.add(R.drawable.theranch);


        mImageUrls3.add(R.drawable.atypical);
        mImageUrls3.add(R.drawable.howimetyourmother);
        mImageUrls3.add(R.drawable.lacasadepapel);
        mImageUrls3.add(R.drawable.santaclaritadiet);
        mImageUrls3.add(R.drawable.vikings);
        mImageUrls3.add(R.drawable.theranch);
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

        if (id == R.id.list_fav) {

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

        } else if (id == R.id.nav_camera) {

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
            msg.setTitle("Deseja sair da sua conta?");
            msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO Achar aonde colocar o signout
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(contexto(),MainActivity.class));
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reproduzir(View v) {
        Intent intent = new Intent(this, Controle.class);
        startActivity(intent);

    }

}
