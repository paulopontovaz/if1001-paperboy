package br.ufpe.cin.if1001.projeto_p3.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;
    private SQLDataBaseHelper db;
    private int rowLayout;
    private Context mContext;

    public ArticleAdapter(ArrayList<Article> list, int rowLayout, Context context) {
        this.articles = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final Article currentArticle = articles.get(position);

        Locale.setDefault(Locale.getDefault());
        Date date = currentArticle.getPubDate();

        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            final String pubDateString = sdf.format(date);
            viewHolder.pubDate.setText(pubDateString);
        }

        viewHolder.title.setText(currentArticle.getTitle());

        Picasso.get()
                .load(currentArticle.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.viewLaterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db = SQLDataBaseHelper.getInstance(mContext);
                currentArticle.setReadLater(!currentArticle.isReadLater());
                if (db.setFavoriteReadLater(currentArticle, false, currentArticle.isReadLater())) {
                    int resourceId = currentArticle.isReadLater() ?
                        R.drawable.ic_watch_later_black_32dp : R.drawable.ic_access_time_black_32dp;
                    viewHolder.viewLaterButton.setImageResource(resourceId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return articles == null ? 0 : articles.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        ImageView image;
        ImageButton viewLaterButton;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            pubDate = itemView.findViewById(R.id.articlePubDate);
            image = itemView.findViewById(R.id.articleImage);
            viewLaterButton = itemView.findViewById(R.id.articleViewLaterButton);
        }
    }
}
