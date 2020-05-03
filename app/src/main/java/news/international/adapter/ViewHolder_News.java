package news.international.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import news.international.MyApplication;
import news.international.NewsScreenType;
import news.international.R;
import news.international.activities.MainActivity;
import news.international.activities.NewsDetailsActivity;
import news.international.activities.SettingActivity;
import news.international.fragments.SavedFragment;
import news.international.interfaces.AdStatus;
import news.international.models.SuccessModel;
import news.international.models.NewsModel;
import news.international.retrofit.ApiClient;
import news.international.retrofit.ApiInterface;
import news.international.utils.AppConstants;
import news.international.utils.MyPrefrences;

public class ViewHolder_News extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public Activity                                         activity;
    public NewsScreenType                                   newsScreenType;
    public List<NewsModel.News>                             newsModel;
    public MyPrefrences                                     myPrefrences;
    public ApiInterface                                     apiService;

    public ImageView                                        newsThumbnailImageView;
    public ProgressBar                                      progressloading;
    public LinearLayout                                     linear_parent;
    public RelativeLayout                                   relative_items;
    public SwipeLayout                                      swipeRevealLayout;
    public TextView                                         newsTitleTextView;
    public TextView                                         newsDetailTextView;
    public TextView                                         text_newsauthor;
    public ImageView                                        imageView_favoption;

    public ViewHolder_News(Activity activity, View itemView, List<NewsModel.News> newsModel, NewsScreenType newsScreenType) {
        super(itemView);

        this.activity=activity;
        this.newsScreenType=newsScreenType;
        this.newsModel=newsModel;
        myPrefrences=new MyPrefrences(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        newsThumbnailImageView =  itemView.findViewById(R.id.newsThumbnailImageView);
        progressloading =  itemView.findViewById(R.id.progressloading);
        linear_parent =  itemView.findViewById(R.id.linear_parent);
        relative_items =  itemView.findViewById(R.id.relative_items);
        swipeRevealLayout =  itemView.findViewById(R.id.swipeRevealLayout);
        newsTitleTextView =  itemView.findViewById(R.id.newsTitleTextView);
        newsDetailTextView =  itemView.findViewById(R.id.newsDetailTextView);
        text_newsauthor =  itemView.findViewById(R.id.text_newsauthor);
        imageView_favoption =  itemView.findViewById(R.id.imageView_favoption);

        relative_items.setOnClickListener(this);
        relative_items.setOnLongClickListener(this);
        imageView_favoption.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int position  = super.getAdapterPosition();
        int id=view.getId();
        if(id==R.id.relative_items)
            onitemclick(position);
        if(id==R.id.imageView_favoption)
            onitemfav(position);

        swipeRevealLayout.close(true);
    }

    public void onitemclick(final int position)
    {
        if (
                !((MyApplication) activity.getApplicationContext()).myPrefrences.getpremiunuser()&&
                        ((MyApplication) activity.getApplicationContext()).interstitialCount % AppConstants.ADSINTERSTITIAL_GAP == 0
        ) {

            ((MyApplication) activity.getApplicationContext()).showAd(new AdStatus() {
                @Override
                public void interstitialFocusClosed() {
                    loadActivity(position);
                }

                @Override
                public void nativeAdLoaded() {
                }
            });
        }else if (((MyApplication) activity.getApplicationContext()).interstitialCount % AppConstants.ADSINTERSTITIAL_GAP == AppConstants.ADSINTERSTITIAL_GAP-1) {
            ((MyApplication) activity.getApplicationContext()).loadInterstitialAdmob();
            ((MyApplication) activity.getApplicationContext()).loadInterstitialFB();
            loadActivity(position);
        } else {
            loadActivity(position);
        }
        ((MyApplication) activity.getApplicationContext()).interstitialCount++;

    }

    public void loadActivity(final int position)
    {
        Intent intent=new Intent(activity,NewsDetailsActivity.class);
        intent.putExtra(AppConstants.URL, newsModel.get(position).getUrl());
        activity.startActivity(intent);
    }
    @Override
    public boolean onLongClick(View v) {
        final int position  = super.getAdapterPosition();
        if(v.getId()==R.id.relative_items)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle(newsModel.get(position).getTitle());

            View view = LayoutInflater.from(activity).inflate(R.layout.activity_news_details,null, false);
            WebView webView=view.findViewById(R.id.webview_news);
            final ProgressBar progressBar=view.findViewById(R.id.progressBar);

            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.loadUrl(newsModel.get(position).getUrl());
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            alert.setView(view);
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        return false;
    }
    private void onitemfav(final int position) {
        imageView_favoption.setVisibility(View.GONE);
        ((MyApplication)activity.getApplicationContext()).apiService.setMarkedNews(
            "Token "+myPrefrences.getToken(),
            newsScreenType==NewsScreenType.SavedNews?false:true,
            newsModel.get(position).getId()
        ).subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe(new Observer<SuccessModel>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(SuccessModel successModel) {
                    if(successModel.getData().equals("success"))

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(newsScreenType==NewsScreenType.SavedNews)
                                    ((MainActivity)activity).openFragment(SavedFragment.newInstance(""));

                            }
                        });
                }
                @Override
                public void onError(Throwable e) {
                    System.out.println("error"+e);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView_favoption.setVisibility(View.VISIBLE);
                            settingDialog();
                        }
                    });

                }
                @Override
                public void onComplete() { }
            });
    }

    public void settingDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_loading, null);
        TextView textView=layoutView.findViewById(R.id.textViewDialogDetail);
        ((TextView)layoutView.findViewById(R.id.textViewDialogTitle)).setText(!myPrefrences.isLogin()?"Login Required":"InternetConnection Failed");
        textView.setText("Login");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, SettingActivity.class));
            }
        });
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(true);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}