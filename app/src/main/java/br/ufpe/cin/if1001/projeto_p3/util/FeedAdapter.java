package br.ufpe.cin.if1001.projeto_p3.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufpe.cin.if1001.projeto_p3.R;
import br.ufpe.cin.if1001.projeto_p3.domain.Feed;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private ArrayList<Feed> feeds;

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

        Feed currentFeed = feeds.get(position);

        viewHolder.title.setText(currentFeed.getTitle());
        viewHolder.link.setText(currentFeed.getLink());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                feedView = new WebView(mContext);

                feedView.getSettings().setLoadWithOverviewMode(true);

                String title = feeds.get(viewHolder.getAdapterPosition()).getTitle();
                String link = feeds.get(viewHolder.getAdapterPosition()).getLink();

                feedView.getSettings().setJavaScriptEnabled(true);
                feedView.setHorizontalScrollBarEnabled(false);
                feedView.setWebChromeClient(new WebChromeClient());
                feedView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + link, null, "utf-8", null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feeds == null ? 0 : feeds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView link;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.feedLink);
            link = itemView.findViewById(R.id.feedLink);
        }
    }
}
