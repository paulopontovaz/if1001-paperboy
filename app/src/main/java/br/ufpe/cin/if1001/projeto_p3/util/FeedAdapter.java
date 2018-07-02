package br.ufpe.cin.if1001.projeto_p3.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.activities.ArticleListActivity;
import br.ufpe.cin.if1001.projeto_p3.db.SQLDataBaseHelper;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;

import static br.ufpe.cin.if1001.projeto_p3.util.Constants.*;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private ArrayList<Feed> feeds;
    private SQLDataBaseHelper db;

    private int rowLayout;
    private Context mContext;
    private WebView feedView;

    public FeedAdapter(ArrayList<Feed> list, int rowLayout, Context context) {
        this.feeds = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void clearData() {
        if (feeds != null)
            feeds.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final Feed currentFeed = feeds.get(position);

        viewHolder.title.setText(currentFeed.getTitle());
        viewHolder.link.setText(currentFeed.getLink());
        viewHolder.deleteFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                    .setTitle(R.string.excluirFeedTitle)
                    .setMessage(R.string.excluirFeedText)
                    .setPositiveButton(R.string.positiveButtonText, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLDataBaseHelper db = SQLDataBaseHelper.getInstance(mContext);
                            String link = viewHolder.link.getText().toString();
                            String title = viewHolder.title.getText().toString();

                            if(db.deleteFeed(link))
                                removeFeed(link, title);
                        }

                    })
                    .setNegativeButton(R.string.negativeButtonText, null)
                    .show();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent articleListActivity = new Intent(mContext, ArticleListActivity.class);
                articleListActivity.putExtra(ARTICLE_LIST_ACTIVITY_ACTION, GET_FEED_ARTICLES);
                articleListActivity.putExtra(FEED_LINK, currentFeed.getLink());
                mContext.startActivity(articleListActivity);
            }
        });
    }

    private void removeFeed (String link, String title) {
        for (int i = 0; i < feeds.size(); i++) {
            if (feeds.get(i).getLink() == link) {
                feeds.remove(i);
                notifyFeedRemoval(title);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, feeds.size());
                break;
            }
        }
    }

    private void notifyFeedRemoval (String title) {
        Toast.makeText(
                mContext,
                String.format("%s was removed!", title),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public int getItemCount() {
        return feeds == null ? 0 : feeds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView link;
        ImageButton deleteFeedButton;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedTitle);
            link = itemView.findViewById(R.id.feedLink);
            deleteFeedButton = itemView.findViewById(R.id.deleteFeedButton);
        }
    }
}
