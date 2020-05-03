package news.international.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import news.international.R;
import news.international.utils.AppConstants;

public class ViewHolder_Ads extends RecyclerView.ViewHolder
{
    public LinearLayout linear_ads;

    public ViewHolder_Ads(View itemView) {
        super(itemView);
        linear_ads =  itemView.findViewById(R.id.linear_ads);
    }

}