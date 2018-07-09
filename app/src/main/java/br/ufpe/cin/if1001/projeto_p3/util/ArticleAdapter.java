package br.ufpe.cin.if1001.projeto_p3.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.activities.ArticleListActivity;
import br.ufpe.cin.if1001.projeto_p3.activities.ReaderActivity;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Article;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.ARTICLE_ITEM;
import static br.ufpe.cin.if1001.projeto_p3.util.Constants.GET_READ_LATER_ARTICLES;

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

        if (currentArticle.isReadLater())
            viewHolder.viewLaterButton.setImageResource(R.drawable.ic_watch_later_black_32dp);

        Picasso.get()
                .load(currentArticle.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent readerActivity = new Intent(mContext, ReaderActivity.class);
                readerActivity.putExtra(ARTICLE_ITEM, currentArticle);
                ((Activity) mContext).startActivityForResult(readerActivity,1);
            }
        });

        viewHolder.viewLaterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db = SQLDataBaseHelper.getInstance(mContext);
                currentArticle.setReadLater(!currentArticle.isReadLater());


                if(!currentArticle.isReadLater() && ((ArticleListActivity) mContext).action.equals(GET_READ_LATER_ARTICLES)) {
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.deleteArticleTitle)
                            .setMessage(R.string.deleteArticleText)
                            .setPositiveButton(R.string.positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLDataBaseHelper db = SQLDataBaseHelper.getInstance(mContext);
                                    db.updateArticleFavoriteReadLater(currentArticle);
                                    removeArticle(currentArticle.getLink());
                                }

                            })
                            .setNegativeButton(R.string.negativeButtonText, null)
                            .show();
                }
                else
                    updateArticle(currentArticle);
            }

            private boolean updateArticle(Article article) {
                if (db.updateArticleFavoriteReadLater(article)) {
                    int resourceId = article.isReadLater() ?
                            R.drawable.ic_watch_later_black_32dp : R.drawable.ic_access_time_black_32dp;
                    viewHolder.viewLaterButton.setImageResource(resourceId);
                    return true;
                }

                return false;
            }
        });
    }



    private void removeArticle (String link) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getLink().equals(link)) {
                articles.remove(i);
                notifyArticleRemoval();
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, articles.size());
                break;
            }
        }
    }

    private void notifyArticleRemoval () {
        Toast.makeText(
                mContext,
                String.format("Artigo foi removido da lista \"Ler Depois\"!"),
                Toast.LENGTH_SHORT
        ).show();
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
