package news.international.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import news.international.MyApplication;
import news.international.NewsScreenType;
import news.international.R;
import news.international.activities.MainActivity;
import news.international.fragments.SavedFragment;
import news.international.models.NewsModel;
import news.international.models.SuccessModel;
import news.international.retrofit.ApiClient;
import news.international.retrofit.ApiInterface;
import news.international.utils.AppConstants;
import news.international.utils.MyPrefrences;

public class ViewHolder_Category extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public Activity                                         activity;
    public String[]                                         newsModel;
    public TextView                                         textView_category;
    public TextView                                         textView_categoryheader;
    public LinearLayout                                     linear_category_item;

    public ViewHolder_Category(Activity activity, View itemView, String[] newsModel) {
        super(itemView);

        this.activity=activity;
        this.newsModel=newsModel;
        textView_category =  itemView.findViewById(R.id.textView_category);
        textView_categoryheader =  itemView.findViewById(R.id.textView_categoryheader);
        linear_category_item =  itemView.findViewById(R.id.linear_category_item);

        linear_category_item.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int position  = super.getAdapterPosition();
        int id=view.getId();
        if(id==R.id.linear_category_item)
            onitemclick(position);
    }

    public void onitemclick(int position)
    {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.CATEGORIES, newsModel[position]);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

}