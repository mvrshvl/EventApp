package com.example.eventapp.ui.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.eventapp.Utils.deleteLike;
import static com.example.eventapp.ui.Profile.favourites.maFavouritesViewModel.getFavData;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Favourites> favourite_cards;
    private List<Event> events_cards;
    private Context context;
    public static boolean type_screen;
    private boolean is_delete =false;
    private String name;
    private String type ;
    private long likes ;
    private String image;

    public DataAdapter(Context context, List<Event> home_fragments) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.events_cards = home_fragments;
    }



    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.favourites_card, parent, false);
        return new DataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {

            name = events_cards.get(position).getName().toString();
            type = events_cards.get(position).getType().toString();
            likes = events_cards.get(position).getLike();
            image = events_cards.get(position).getImages_path1().toString();
            Picasso.get().load(image).fit().centerCrop().into(holder.imageView);//.fit


            holder.nameView.setText(name);
            holder.typeView.setText(type);
            holder.likeView.setText(likes + " Нравится");
            holder.cv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    final Bundle data = new Bundle();
                    data.putString("id", events_cards.get(position).getId());
                    MainActivity.recyclerPosition = position;
                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.event, data);
                }
            });
            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type_screen) {
                        favourite_cards = getFavData();
                        for (Favourites fe : favourite_cards) {
                            if (fe.getEvent().equals(events_cards.get(position).getId())) {
                                deleteLike(events_cards.get(position));
                                deleteFromHolder(holder);
                            }
                        }
                    }
                    else{
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context,R.style.dialogStyle);
                        mDialogBuilder
                                .setCancelable(true)
                                .setTitle("После удаления запись будет не доступна")
                                .setPositiveButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                Utils.deletePost(events_cards.get(position));
                                                deleteFromHolder(holder);
                                            }
                                        })
                                .setNegativeButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });

                        //Создаем AlertDialog:
                        AlertDialog alertDialog = mDialogBuilder.create();
                        //и отображаем его:
                        alertDialog.show();
                        alertDialog.getWindow().setLayout(1000,450);

                    }
                }
            });

    }

    public  void deleteFromHolder(final DataAdapter.ViewHolder holder){
        events_cards.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
        notifyItemRangeChanged(holder.getAdapterPosition(), events_cards.size());
    }
    @Override
    public int getItemCount() {
        return events_cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, typeView,likeView;
        ImageView buttonDelete;
        CardView cv;
        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.card_view_fav);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            typeView = (TextView)view.findViewById(R.id.type);
            likeView = (TextView) view.findViewById(R.id.likes);
            buttonDelete = (ImageView) view.findViewById(R.id.b_delete);
        }
    }

}
