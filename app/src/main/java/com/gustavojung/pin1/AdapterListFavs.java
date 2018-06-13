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

public class AdapterListFavs extends  RecyclerView.Adapter<AdapterListFavs.ViewHolderAdapter>{

    private final static String TAG = "RecyclerViewAdapterList";

    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;

    public AdapterListFavs(Context context, ArrayList<Integer> imageUrls) {
        mImages = imageUrls;
        mContext = context;
    }

    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolderAdapter vh = new ViewHolderAdapter(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderAdapter holder, final int position) {
        Log.d(TAG, "Bind Feito");
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

                ImageView imagemN = dialog.findViewById(R.id.img_view);
                imagemN.setImageResource(imagem);

                TextView txtclose = (TextView) dialog.findViewById(R.id.txtcloseFilmeClicked);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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


    public class ViewHolderAdapter extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private ImageView imgView;

        public ViewHolderAdapter(View itemView) {
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
