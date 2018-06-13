package com.gustavojung.pin1;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EventListener;

public class HorizontalAdapter extends  RecyclerView.Adapter<HorizontalAdapter.ViewHolder>{

    private final static String TAG = "RecyclerViewAdapter";

    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;
    SharedPreferences sharedPreferencesConteudo ;

    public HorizontalAdapter(Context context, ArrayList<Integer> imageUrls) {

            mImages = imageUrls;
            mContext = context;
             }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
           ViewHolder vh = new ViewHolder(view);
           return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.imgView);


        holder.imgView.setImageResource(mImages.get(position));
        final  int imagem = mImages.get(position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                final Conteudos c =new Conteudos();


                dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.content_clicked);

                final CheckBox checkBox = dialog.findViewById(R.id.checkFav);
                final TextView descricaoSer = dialog.findViewById(R.id.textodescricao);
                final TextView nomeSelectedS = dialog.findViewById(R.id.text_nome_serie);
                final TextView nTemporadas =  dialog.findViewById(R.id.nTemporadas);
                final RatingBar rating = dialog.findViewById(R.id.rating);
                final ValueEventListener favorito;


                ImageView imagemN = dialog.findViewById(R.id.img_view);
                imagemN.setImageResource(imagem);

                TextView txtclose = (TextView) dialog.findViewById(R.id.txtcloseFilmeClicked);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                    });

                ValueEventListener getValues = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                                    if(String.valueOf(uniqueKeySnapshot.child("id").getValue()).equalsIgnoreCase(String.valueOf(imagem))){
                                            nomeSelectedS.setText(uniqueKeySnapshot.child("nome").getValue().toString());
                                            descricaoSer.setText(uniqueKeySnapshot.child("descricao").getValue().toString());
                                            nTemporadas.setText(uniqueKeySnapshot.child("temporadas").getValue().toString());
                                            rating.setRating(((Long)uniqueKeySnapshot.child("rating").getValue()));
                                    }
                            }

                        }else{
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                FirebaseDatabase.getInstance().getReference().child("Conteudo").addValueEventListener(getValues);

                 favorito = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                    //uniqueKeySnapshot retorna os Ids dos usuários
                                    if (uniqueKeySnapshot.getKey().toString().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        for (DataSnapshot d : uniqueKeySnapshot.getChildren()) {
                                            System.out.println(d);
                                            for (DataSnapshot ds : d.getChildren()) {
                                                //ds retorna os ints de cada imagem dos favoritos
                                                if (String.valueOf(ds.getValue()).equalsIgnoreCase(String.valueOf(imagem))) {
                                                    ((CheckBox) checkBox).setChecked(true);
                                                    break;
                                                } else {
                                                    ((CheckBox)checkBox).setChecked(false);
                                                }
                                            }
                                        }
                                    }else {
                                }
                                }
                        } else {
                              }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                FirebaseDatabase.getInstance().getReference().child("ids").addValueEventListener(favorito);


                checkBox.setOnClickListener(new View.OnClickListener() {

                    ValueEventListener removeListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for(DataSnapshot d: dataSnapshot.getChildren()){
                                    if(String.valueOf(d.getValue()).equalsIgnoreCase(String.valueOf(imagem))){
                                        d.getRef().setValue(null);
                                    }
                                }
                            }else{
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };


                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("ids").removeEventListener(favorito);
                        User u = new User();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        DatabaseReference base = FirebaseDatabase.getInstance().getReference().child("ids").child(uid).child("favoritos");
                        u.setId(uid);
                        DatabaseReference pushRef = base.push();
                        System.out.println(pushRef);
                        if (((CheckBox) v).isChecked()) {
                            u.getFavoritos().add(String.valueOf(imagem));
                            pushRef.setValue(String.valueOf(imagem));
                            Toast.makeText(mContext, "Adicionado aos Favoritos!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("ids").child(uid).child("favoritos").removeEventListener(removeListener);
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("ids").child(uid).child("favoritos").addValueEventListener(removeListener);
                            Toast.makeText(mContext, "Removido dos Favoritos!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.series);
            layout = itemView.findViewById(R.id.layoutItem);
        }

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }
    }
}