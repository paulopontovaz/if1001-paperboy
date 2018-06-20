package br.ufpe.cin.if1001.projeto_p3.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;

    private int rowLayout;
    private Context mContext;
    private WebView articleView;

    public ArticleAdapter(ArrayList<Article> list, int rowLayout, Context context) {
        this.articles = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void clearData() {
        if (articles != null)
            articles.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        Article currentArticle = articles.get(position);

        Locale.setDefault(Locale.getDefault());
        Date date = currentArticle.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String pubDateString = sdf.format(date);

        viewHolder.title.setText(currentArticle.getTitle());

        Picasso.get()
                .load(currentArticle.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                articleView = new WebView(mContext);

                articleView.getSettings().setLoadWithOverviewMode(true);

                String title = articles.get(viewHolder.getAdapterPosition()).getTitle();
                String content = articles.get(viewHolder.getAdapterPosition()).getContent();

                articleView.getSettings().setJavaScriptEnabled(true);
                articleView.setHorizontalScrollBarEnabled(false);
                articleView.setWebChromeClient(new WebChromeClient());
                articleView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content, null, "utf-8", null);
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

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.feedLink);
            pubDate = itemView.findViewById(R.id.pubDate);
            image = itemView.findViewById(R.id.image);
        }
    }
}
