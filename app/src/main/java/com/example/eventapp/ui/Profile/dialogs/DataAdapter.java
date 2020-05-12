package com.example.eventapp.ui.Profile.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.CircularTransformation;
import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.example.eventapp.messenger.Dialog;
import com.example.eventapp.ui.Profile.favourites.maFavouritesViewModel;
import com.example.eventapp.ui.Profile.my_events.myEventsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.eventapp.Utils.deleteLike;
import static com.example.eventapp.ui.Profile.favourites.maFavouritesViewModel.getFavData;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<User> dialogs;
    private Context context;
    public static boolean type_screen;
    private boolean is_delete =false;
    private String name;
    private String last_message ;
    private String image;

    public DataAdapter(Context context, List<User> dialogs) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.dialogs = dialogs;
    }




    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.dialogs_card, parent, false);
        return new DataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {

            name = dialogs.get(position).name;
            image = dialogs.get(position).photo;
            DialogsViewModel.getDialog(dialogs.get(position).id,holder.msg);


        Picasso.get()
                .load(image)
                .transform(new CircularTransformation(0))
                .into(holder.imageView);

            holder.nameView.setText(name);

            holder.cv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    final Bundle data = new Bundle();
                    data.putString("id", dialogs.get(position).id);
                    data.putString("name",dialogs.get(position).name);
                    Navigation.findNavController((AppCompatActivity) v.getContext(),R.id.nav_host_fragment).navigate(R.id.messenger,data);
                }
            });
    }

//    public  void deleteFromHolder(final DataAdapter.ViewHolder holder){
//        events_cards.remove(holder.getAdapterPosition());
//        notifyItemRemoved(holder.getAdapterPosition());
//        notifyItemRangeChanged(holder.getAdapterPosition(), events_cards.size());
//        //change data
//        //активируем триггер в классе myfavourites/myevents на изменение данных
//        if(type_screen){
//            maFavouritesViewModel.setData(events_cards);
//        }
//        else{
//            myEventsViewModel.setData(events_cards);
//        }
//    }
    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;
        final LinearLayout parent;
        final TextView msg;
        ImageView buttonDelete;
        CardView cv;
        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            imageView = (ImageView)view.findViewById(R.id.photo);
            nameView = (TextView) view.findViewById(R.id.name);
            parent = (LinearLayout)view.findViewById(R.id.parent_home);
            msg =(TextView)view.findViewById(R.id.last_msg);
        }
    }


}
