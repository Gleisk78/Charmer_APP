package com.example.charmer;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecycleViewpostAdapter extends FirestoreRecyclerAdapter<PostGaleria, RecycleViewpostAdapter.ViewHolder> {

    private final FirebaseFirestore mFirestore;
    Activity activity;
    int cont = 0;
    FirebaseAuth mAuth;
    String usuaactual;
    List<String> person_like = new ArrayList<>();
    String existe = "";



    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public RecycleViewpostAdapter(@NonNull FirestoreRecyclerOptions<PostGaleria> options, Activity activity) {
        super(options);

        this.activity = activity;
        Context con;


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        usuaactual = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        person_like.add("");

    }



    @Override
    protected void onBindViewHolder(@NonNull RecycleViewpostAdapter.ViewHolder holder, int position, @NonNull PostGaleria postGaleria) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        String idusua = postGaleria.getId();
        String nombre = postGaleria.getNombre();
        holder.nameusu.setText(postGaleria.getNombre());
        holder.like.setText(postGaleria.getLikes());
        holder.descripcion.setText(postGaleria.getDescripcion());
        holder.tags.setText(postGaleria.getEtiquetas());

        /*String fechapublicacion = postGaleria.getFecha().toString();
        */
        String FTUSU_Uri, FTPST_Uri;


        FTPST_Uri = postGaleria.getImagen_post();
        FTUSU_Uri = postGaleria.getImagen_Usuario();
        Picasso.with(holder.postusu.getContext()).load(FTPST_Uri).into(holder.postusu);
        Picasso.with(holder.fotousu.getContext()).load(FTUSU_Uri).into(holder.fotousu);

        //File img_posteo = new File(FTPST_Uri);

        if(!(idusua.equals(usuaactual))){
            holder.btn_eliminar_post.setVisibility(View.INVISIBLE);
        }


        holder.btn_share.setOnClickListener(v -> {
            /*
            Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("image/jpeg");
            String mensaje = "Mira este posteo de " + nombre;
            compartir.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img_posteo));
            activity.startActivity(Intent.createChooser(compartir, mensaje));*/
        });


        holder.btn_likes.setOnClickListener(v -> {

            String num_like;
            int e = 0;

            for(int i = 0; i < person_like.size(); i++){

                if(person_like.get(i).equals(idusua)){

                    existe = "si";
                    e = i;
                    break;

                }else {
                    existe = "no";
                }
            }



            if(existe.equals("si")){
                num_like = postGaleria.getLikes();
                quitar_like(id, num_like);
                person_like.remove(e);


            }else {
                holder.btn_likes.setBackgroundResource(R.drawable.coracon);
                num_like = postGaleria.getLikes();
                añadir_like(id, num_like);
                person_like.add(idusua);
                cont++;
            }


        });

        holder.btn_eliminar_post.setOnClickListener(v -> {

            if(idusua.equals(usuaactual)){

                deletePost(id);

            }else {
                Toast.makeText(activity.getApplicationContext(), "No es tu post", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity.getApplicationContext(), idusua, Toast.LENGTH_SHORT).show();
                Toast.makeText(activity.getApplicationContext(), usuaactual, Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void añadir_like(String id, String num_like) {
        num_like = String.valueOf(parseInt(num_like) + 1);

        Map<String, Object> map = new HashMap<>();

        map.put("Likes", num_like);

        mFirestore.collection("posteos").document(id).update(map).addOnSuccessListener(unused -> Toast.makeText(activity, "Diste Like", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(activity, "Error al agregar Like", Toast.LENGTH_SHORT).show());

    }

    private void quitar_like(String id, String num_like) {
        num_like = String.valueOf(parseInt(num_like) - 1);

        Map<String, Object> map = new HashMap<>();

        map.put("Likes", num_like);

        mFirestore.collection("posteos").document(id).update(map).addOnSuccessListener(unused -> Toast.makeText(activity, "Quitaste tu Like", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(activity, "Error al quitar Like", Toast.LENGTH_SHORT).show());

    }


    private  void  deletePost(String id){

        mFirestore.collection("posteos").document(id).delete().addOnSuccessListener(unused -> Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(activity, "Error al eliminar publicación", Toast.LENGTH_SHORT).show());
    }

    // Viculando a layout item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        return new ViewHolder(v);
    }


    //Referenciar
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView fotousu, postusu, btn_eliminar_post;
        TextView nameusu, like, descripcion, tags;
        Button btn_likes, btn_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             fotousu = itemView.findViewById(R.id.img_usuariopost);
             postusu = itemView.findViewById(R.id.publicacion);
             nameusu = itemView.findViewById(R.id.nombreusupost);
             like = itemView.findViewById(R.id.cant_like);
             descripcion = itemView.findViewById(R.id.Descri_post);
             tags = itemView.findViewById(R.id.Etiquetas);
             btn_eliminar_post = itemView.findViewById(R.id.btn_eliminar_publicacion);
             btn_likes = itemView.findViewById(R.id.btn_like);
             btn_share =  itemView.findViewById(R.id.btn_compartir);
        }
    }
}
