package news.international.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import news.international.NewsScreenType;
import news.international.R;
import news.international.models.NewsModel;


public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public static Activity              activity;
    String[]                            newsModel;
    RecyclerView                        recyclerView;

    public CategoryAdapter( Activity activity, String[] newsModel) {
        this.activity=activity;
        this.newsModel=newsModel;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.activity_category_items, viewGroup, false);

        return new ViewHolder_Category(activity, view, this.newsModel);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //////////////SETTING LIST ITEM//////////////////////***********
        final ViewHolder_Category viewHolder_news =(ViewHolder_Category)holder;
        String title= newsModel[position];

        try {
            viewHolder_news.textView_categoryheader.setText(position+ ": Category");
            viewHolder_news.textView_category.setText(title);
        } catch (Exception e) {
            System.out.println(""+e);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return newsModel.length ;
    }

    public int getItemViewType(int position)
    {
        return position;
    }

}