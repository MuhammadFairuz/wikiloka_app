package aksamedia.wikiloka.Class;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aksamedia.wikiloka.Activity.activity_detail_inbox;
import aksamedia.wikiloka.R;

/**
 * Created by Dinar on 11/19/2016.
 */

public class class_adapterinbox extends RecyclerView.Adapter<class_adapterinbox.BarangViewHolder> {

    String picture;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    List<class_inbox> cls;
    private View v;

    public class_adapterinbox(List<class_inbox> cls_inbox){
        this.cls = cls_inbox;
    }

    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listinbox, parent, false);
        BarangViewHolder bvh = new BarangViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BarangViewHolder holder, int position) {
        class_inbox class_inbox = cls.get(position);
        Picasso.with(v.getContext())
                .load(class_inbox.picture)
                .fit()
                .centerCrop()
                .into(holder.imgIklan);
        picture = class_inbox.picture;
        holder.tv_judul.setText(class_inbox.title);
        holder.tv_nama.setText(class_inbox.last_sender);
        holder.tv_message.setText(class_inbox.last_message);
        holder.tv_key.setText(class_inbox.key);
    }

    @Override
    public int getItemCount() {
        return cls.size();
    }

    public class BarangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgIklan;
        TextView tv_judul, tv_nama, tv_waktu, tv_message, tv_key;
        LinearLayout llMain;
        public BarangViewHolder(View itemView) {
            super(itemView);
            imgIklan = (ImageView) itemView.findViewById(R.id.imgIklan);
            tv_judul = (TextView) itemView.findViewById(R.id.tv_judul);
            tv_nama = (TextView) itemView.findViewById(R.id.tv_nama);
            tv_waktu = (TextView) itemView.findViewById(R.id.tv_waktu);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_key = (TextView) itemView.findViewById(R.id.tv_key);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            llMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == llMain.getId()){
                Intent intent = new Intent(v.getContext(),activity_detail_inbox.class);
                intent.putExtra("key_inbox", tv_key.getText().toString());
                intent.putExtra("title_inbox", tv_judul.getText().toString());
                intent.putExtra("picture_inbox", picture);
                v.getContext().startActivity(intent);
            }
        }
    }
}
