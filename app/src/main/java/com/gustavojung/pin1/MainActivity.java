package com.gustavojung.pin1;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
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
    private ArrayList<Integer> imageUrls = new ArrayList<>();
    private static final int RC_SIGN_IN = 123;
    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    FirebaseAuth auth;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    SharedPreferences sharedPreferences ;
    private RecyclerView listaFavoritos;
    private ArrayList<Integer> mFavoritos = new ArrayList<>();
    private AdapterListFavs myAdapter;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        myAdapter = new AdapterListFavs(contexto(),mFavoritos) ;

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

        if (resultCode == RESULT_OK) {

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);

            mDisplayImageView = (ImageView) header.findViewById(R.id.foto_usuario);

            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the SD Card.
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    // show the image to the user
                    mDisplayImageView.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }


    }

    public void onImageGalleryClicked(View v) {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
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

        for(Integer i:mImageUrls){
            imageUrls.add(i);
        }
        for(Integer a:mImageUrls1){
            imageUrls.add(a);
        }
        for(Integer b:mImageUrls2){
            imageUrls.add(b);
        }
        for(Integer c:mImageUrls3){
            imageUrls.add(c);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.list_fav) {

            TextView txtclose;
            myDialog.setContentView(R.layout.pop_lista_favoritos);
            txtclose = (TextView) myDialog.findViewById(R.id.txtcloseFav);

            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            listaFavoritos = myDialog.findViewById(R.id.recycler_view);

            ValueEventListener getValues = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                          //uniqueKeySnapshot retorna os Ids dos usuários
                            if (uniqueKeySnapshot.getKey().toString().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                mFavoritos.clear();
                                for(DataSnapshot d : uniqueKeySnapshot.getChildren()) {
                                   //d retorna as keys e os values dos favoritos de cada usuário
                                    for (DataSnapshot ds : d.getChildren()) {
                                        //ds retorna os ints de cada imagem dos favoritos
                                        for (Integer imagem : imageUrls) {
                                            if (String.valueOf(ds.getValue()).equalsIgnoreCase(String.valueOf(imagem))) {
                                                 mFavoritos.add(imagem);
                                            }
                                        }

                                        }
                                    }
                            }else {
                            }
                        }
                    }else{
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            FirebaseDatabase.getInstance().getReference().child("ids").addValueEventListener(getValues);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(contexto(), LinearLayoutManager.VERTICAL, false);
            RecyclerView listaFav = listaFavoritos;
            listaFav.setAdapter(new AdapterListFavs(contexto(),mFavoritos));
            listaFav.getAdapter().notifyDataSetChanged();
            listaFav.setLayoutManager(layoutManager1);


            myDialog.show();



        } else if (id == R.id.nav_camera) {

            TextView txtclose;
            myDialog.setContentView(R.layout.popup_menu1);
            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Alterações salvas!", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });


            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Spinner mySpinner = (Spinner) myDialog.findViewById(R.id.listIdiomasAudio);
            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"spinner1", Context.MODE_PRIVATE);
            int valueId = sharedPreferences.getInt(auth.getCurrentUser().getUid() + "spinner1",5);

            mySpinner.setSelection(valueId);

            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"spinner1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(auth.getCurrentUser().getUid()+"spinner1",position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            Spinner mySpinner1 = (Spinner) myDialog.findViewById(R.id.listIdiomasLegenda);
            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"spinner2", Context.MODE_PRIVATE);
            int valueId1 = sharedPreferences.getInt(auth.getCurrentUser().getUid() + "spinner2",5);

            mySpinner1.setSelection(valueId1);

            mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"spinner2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(auth.getCurrentUser().getUid()+"spinner2",position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });


            myDialog.show();

        } else if (id == R.id.nav_gallery) {

            TextView txtclose2;
            myDialog.setContentView(R.layout.popup_menu2);
            txtclose2 = myDialog.findViewById(R.id.txtclose2);

            txtclose2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Alterações salvas!", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            Switch mySwitch = (Switch) myDialog.findViewById(R.id.toggleSound);
            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid(), Context.MODE_PRIVATE);
            boolean value = sharedPreferences.getBoolean(auth.getCurrentUser().getUid(),false);

            mySwitch.setChecked(value);

            if(value != true){
                ((Switch) myDialog.findViewById(R.id.toggleSound)).setText("Ativado");
            }else{
                ((Switch) myDialog.findViewById(R.id.toggleSound)).setText("Desativado");
            }

            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(auth.getCurrentUser().getUid(),isChecked);
                            editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleSound)).setText("Desativado");
                         } else {
                            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(auth.getCurrentUser().getUid(),isChecked);
                            editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleSound)).setText("Ativado");
                        }
                }
            });



            Switch mySwitch1 = (Switch) myDialog.findViewById(R.id.toggleNotes);
            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"notes", Context.MODE_PRIVATE);
            boolean value1 = sharedPreferences.getBoolean(auth.getCurrentUser().getUid()+"notes",false);

            mySwitch1.setChecked(value1);

            if(value1 != true){
                ((Switch) myDialog.findViewById(R.id.toggleNotes)).setText("Ativado");
            }else{
                ((Switch) myDialog.findViewById(R.id.toggleNotes)).setText("Desativado");
            }

            mySwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"notes", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(auth.getCurrentUser().getUid()+"notes",isChecked);
                        editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleNotes)).setText("Desativado");
                    } else {
                        sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"notes", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(auth.getCurrentUser().getUid()+"notes",isChecked);
                        editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleNotes)).setText("Ativado");
                    }
                }
            });


            Switch mySwitch2 = (Switch) myDialog.findViewById(R.id.toggleSaveSearch);
            sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"search", Context.MODE_PRIVATE);
            boolean value2 = sharedPreferences.getBoolean(auth.getCurrentUser().getUid()+"search",false);

            mySwitch2.setChecked(value2);

            if(value2 != true){
                ((Switch) myDialog.findViewById(R.id.toggleSaveSearch)).setText("Ativado");
            }else{
                ((Switch) myDialog.findViewById(R.id.toggleSaveSearch)).setText("Desativado");
            }

            mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"search", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(auth.getCurrentUser().getUid()+"search",isChecked);
                        editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleSaveSearch)).setText("Desativado");
                    } else {
                        sharedPreferences = getSharedPreferences(auth.getCurrentUser().getUid()+"search", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(auth.getCurrentUser().getUid()+"search",isChecked);
                        editor.apply();
                        ((Switch) myDialog.findViewById(R.id.toggleSaveSearch)).setText("Ativado");
                    }
                }
            });


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

    public FirebaseUser fireConection(){
        return auth.getCurrentUser();
    }

}
