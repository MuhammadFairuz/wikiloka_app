package aksamedia.wikiloka.Class;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aksamedia.wikiloka.R;

/**
 * Created by Dinar on 11/20/2016.
 */

public class class_adaptermessage extends RecyclerView.Adapter<class_adaptermessage.BarangViewHolder> {

    protected Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    List<class_message> cls;
    private View v;

    public class_adaptermessage(List<class_message> cls_message){
        this.cls = cls_message;
    }

    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat, parent, false);
        BarangViewHolder bvh = new BarangViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BarangViewHolder holder, int position) {
        class_message class_message = cls.get(position);
        GradientDrawable bgShape = (GradientDrawable) holder.tvMessage.getBackground();

        if (class_message.usernames.equals(new class_prosessql(context).baca_member()[0])){
            Picasso.with(v.getContext())
                    .load(R.drawable.ic_akun_24)
                    .fit()
                    .centerCrop()
                    .into(holder.imgMe);
            //holder.imgYou.setVisibility(View.GONE);
            bgShape.setColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            Picasso.with(v.getContext())
                    .load(R.drawable.ic_akun_24)
                    .fit()
                    .centerCrop()
                    .into(holder.imgYou);
            //holder.imgMe.setVisibility(View.GONE);
            bgShape.setColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.tvMessage.setText(class_message.message);

    }

    @Override
    public int getItemCount() {
        return cls.size();
    }

    public class BarangViewHolder extends RecyclerView.ViewHolder{
        ImageView imgYou, imgMe;
        TextView tvMessage;
        LinearLayout llChat;

        public BarangViewHolder(View itemView) {
            super(itemView);
            imgYou = (ImageView) itemView.findViewById(R.id.imgYou);
            imgMe = (ImageView) itemView.findViewById(R.id.imgMe);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llChat = (LinearLayout) itemView.findViewById(R.id.llChat);
            context = itemView.getContext();
        }
    }
}
