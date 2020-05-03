package news.international.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import news.international.MyApplication;
import news.international.NewsScreenType;
import news.international.R;
import news.international.models.NewsModel;
import news.international.utils.AppConstants;
import news.international.wrapper.AdmobNativeAdModel;
import news.international.wrapper.FBNativeAdModel;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public static Activity                                          activity;
    List<NewsModel.News>                                            newsModel;
    RecyclerView                                                    recyclerView;
    NewsScreenType                                                  newsScreenType;

    public NewsAdapter(NewsScreenType newsScreenType,Activity activity, NewsModel newsModel) {
        this.activity=activity;
        this.newsModel=newsModel.getNews();
        this.newsScreenType=newsScreenType;
        getSpaceForAds();
    }

    private void getSpaceForAds() {
        for(int item=1;item<(newsModel.size())/ AppConstants.ADS_GAP;item++)
        {
            newsModel.add(AppConstants.ADS_GAP*item,null);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        int layoutID;
        //====================================setting layout======================//
        if(position==0)
            layoutID=R.layout.fragment_news_featured;
        else if(newsModel.get(position)==null)
            layoutID=R.layout.native_adapteritem;
        else
            layoutID=R.layout.fragment_news_items;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutID, viewGroup, false);

        //====================================setting holder======================//
        if(newsModel.get(position)==null)
            return new ViewHolder_Ads(view);
        else
            return new ViewHolder_News(activity, view, this.newsModel,newsScreenType);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //////////////SETTING LIST ITEM//////////////////////***********
        if (holder instanceof ViewHolder_Ads) {
            ArrayList<FBNativeAdModel> fbnativeAdsArray = ((MyApplication) activity.getApplicationContext()).getFBNativeClass().nativeAdsArray;
            ArrayList<AdmobNativeAdModel> admobnativeAdsArray = ((MyApplication) activity.getApplicationContext()).getAdmobNativeClass().nativeAdsArray;
            final ViewHolder_Ads viewHolder_ads = (ViewHolder_Ads) holder;

            Random random = new Random();
            int adOption = random.nextInt(2);
            if ((fbnativeAdsArray != null && fbnativeAdsArray.size() > 0) &&
                    (adOption == 0 || admobnativeAdsArray != null && admobnativeAdsArray.size() == 0 && adOption == 1)
            ) {
                Random r = new Random();
                int adindex = r.nextInt(fbnativeAdsArray.size());
                if (viewHolder_ads.linear_ads != null && viewHolder_ads.linear_ads.getChildCount() == 0) {
                    NativeAdLayout nativeAdLayout = fbnativeAdsArray.get(adindex).getnativeAdLayout();
                    if (nativeAdLayout.getParent() != null) {
                        ((ViewGroup) nativeAdLayout.getParent()).removeView(nativeAdLayout);
                    }
                    viewHolder_ads.linear_ads.addView(nativeAdLayout);
                }
            } else if ((admobnativeAdsArray != null && admobnativeAdsArray.size() > 0) &&
                    (adOption == 1 || fbnativeAdsArray != null && fbnativeAdsArray.size() == 0 && adOption == 0)
            ) {
                Random r = new Random();
                int adindex = r.nextInt(admobnativeAdsArray.size());
                if (viewHolder_ads.linear_ads != null && viewHolder_ads.linear_ads.getChildCount() == 0) {
                    UnifiedNativeAdView unifiedNativeAdView = admobnativeAdsArray.get(adindex).getUnifiedNativeAdView();
                    if (unifiedNativeAdView.getParent() != null) {
                        ((ViewGroup) unifiedNativeAdView.getParent()).removeView(unifiedNativeAdView);
                    }
                    viewHolder_ads.linear_ads.addView(unifiedNativeAdView);
                }
            }
        }
        else
        {
            final ViewHolder_News viewHolder_news = (ViewHolder_News) holder;

            String title = newsModel.get(position).getTitle();
            String description = newsModel.get(position).getDescription();
            String author = newsModel.get(position).getAuthor();

            try {

                viewHolder_news.imageView_favoption.setImageDrawable(activity.getResources().getDrawable(newsScreenType == NewsScreenType.SavedNews ? R.drawable.unsaved : R.drawable.saved));
                viewHolder_news.newsTitleTextView.setText(title);
                viewHolder_news.newsDetailTextView.setText(description);
                viewHolder_news.text_newsauthor.setText(author);


                Picasso.get().load(newsModel.get(position).getImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        viewHolder_news.newsThumbnailImageView.setImageBitmap(bitmap);
                        viewHolder_news.progressloading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        System.out.println("test= onBitmapFailed");
                        viewHolder_news.newsThumbnailImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.thumbnail));
                        viewHolder_news.progressloading.setVisibility(View.GONE);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } catch (Exception e) {
                System.out.println("" + e);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return newsModel.size() ;
    }

    public int getItemViewType(int position)
    {
        return position;
    }

}