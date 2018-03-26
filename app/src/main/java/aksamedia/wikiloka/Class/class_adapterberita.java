package aksamedia.wikiloka.Class;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aksamedia.wikiloka.Activity.activity_detailberita;
import aksamedia.wikiloka.R;

/**
 * Created by Dinar on 9/21/2016.
 */
public class class_adapterberita extends RecyclerView.Adapter<class_adapterberita.BeritaViewHolder> {
    List<class_berita> c_berita;
    private View v;
    static Context context;
    private class_berita clb;

    public class_adapterberita(List<class_berita> c_berita) {
        this.c_berita = c_berita;
    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv_berita;
        TextView judul_berita, seo_berita, sumber_berita;
        ImageView foto_berita;

        public BeritaViewHolder(View itemView) {
            super(itemView);
            Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), "font_serif.ttf");
            cv_berita = (CardView)itemView.findViewById(R.id.cv_berita);
            judul_berita = (TextView) itemView.findViewById(R.id.judul_berita);
            foto_berita = (ImageView) itemView.findViewById(R.id.foto_berita);
            seo_berita = (TextView) itemView.findViewById(R.id.seo_berita);
            sumber_berita = (TextView) itemView.findViewById(R.id.sumber_berita);
            foto_berita.setOnClickListener(this);
            judul_berita.setTypeface(font);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == foto_berita.getId()){
                Intent intent = new Intent(v.getContext(),activity_detailberita.class);
                intent.putExtra("sumber_berita", sumber_berita.getText().toString());
                intent.putExtra("seo_berita", seo_berita.getText().toString());
                v.getContext().startActivity(intent);
                //((AppCompatActivity) context).finish();
            }
        }
    }

    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listberita, parent, false);
        BeritaViewHolder bvh = new BeritaViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int i) {
        clb = c_berita.get(i);
        context = v.getContext();

        holder.judul_berita.setText(clb.judul_berita);
        holder.sumber_berita.setText(clb.sumber_berita);

        holder.seo_berita.setText(clb.seo_berita);
        Picasso.with(v.getContext())
                .load(clb.url)
                .fit()
                .centerCrop()
                .into(holder.foto_berita);
    }

    @Override
    public int getItemCount() {
        return c_berita.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
