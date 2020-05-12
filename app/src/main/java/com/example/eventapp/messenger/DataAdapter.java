package com.example.eventapp.messenger;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.CircularTransformation;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Message> messages;
    private Context context;
    public static boolean type_screen;
    private boolean is_delete =false;
    private String autor;
    private String text ;
    private long time;

    public DataAdapter(Context context, List<Message> messages) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.messages = messages;
    }




    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.message_lay, parent, false);
        return new DataAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {

            autor = messages.get(position).getAutor();
            text = messages.get(position).getTextMessage();
            time = messages.get(position).getTimeMessage();

            String usr = MainActivity.getCurrentUser().getUid();
            String aut = messages.get(position).getAutor();

            if(usr.equals(aut)){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.cv.setLayoutParams(params);
                //holder.cv.setCardBackgroundColor(Color.argb(50,176,0,32));
                holder.parent.setBackgroundColor(Color.argb(50,176,0,32));
            }
            else {
                holder.cv.setLeft(1);
            }
            holder.textMessage.setText(text);
        holder.timeMessage.setText(DateFormat.format("HH:mm dd.MM", time));
//        Picasso.get()
//                .load(image)
//                .transform(new CircularTransformation(0))
//                .into(holder.imageView);
//
//            holder.cv.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    final Bundle data = new Bundle();
////                    MainActivity.recyclerPosition = position;
////                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.event, data);
//                }
//            });
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
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textMessage, timeMessage;
        CardView cv;
        LinearLayout parent;

        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            textMessage = (TextView)view.findViewById(R.id.tvMessage);
            timeMessage = (TextView)view.findViewById(R.id.tvTime);
            parent = (LinearLayout) view.findViewById(R.id.parentLay);
        }
    }

}
