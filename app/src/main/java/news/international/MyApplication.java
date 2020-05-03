package news.international;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import news.international.interfaces.AdStatus;
import news.international.retrofit.ApiClient;
import news.international.retrofit.ApiInterface;
import news.international.utils.AppConstants;
import news.international.utils.MyPrefrences;
import news.international.wrapper.AdmobNativeAd;
import news.international.wrapper.FBNativeAd;

public class MyApplication extends Application {
    final String TAG = "Application";

    InterstitialAd mAdmobInterstitial;
    com.facebook.ads.InterstitialAd mFbInterstitial;
    AdmobNativeAd admobNativeAds;
    FBNativeAd fbNativeAds;
    public MyPrefrences myPrefrences;
    public int interstitialCount=0;


    public ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public void thingsAtStartApp() {
        myPrefrences=new MyPrefrences(this);

        MobileAds.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AudienceNetworkAds.initialize(this);
        FirebaseApp.initializeApp(this);
        FirebaseAnalytics.getInstance(this);

        if (!myPrefrences.getpremiunuser()) {
            loadInterstitialAdmob();
            loadInterstitialFB();
        }
    }

    public void loadInterstitialAdmob() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdmobInterstitial = new InterstitialAd(this);
        mAdmobInterstitial.setAdUnitId(AppConstants.ADMOBREAL_INTERSTITALID);
        mAdmobInterstitial.loadAd(adRequest);
    }

    public void loadInterstitialFB() {
        mFbInterstitial = new com.facebook.ads.InterstitialAd(getApplicationContext(), AppConstants.FBPlacementID_INTERSTTIAL);
        mFbInterstitial.loadAd();

    }

    public void setAdmobNativeClass(AdmobNativeAd admobNativeAds) {
        this.admobNativeAds = admobNativeAds;
    }

    public AdmobNativeAd getAdmobNativeClass() {
        return admobNativeAds;
    }

    public void setFBNativeClass(FBNativeAd fbNativeAds) {
        this.fbNativeAds = fbNativeAds;
    }

    public FBNativeAd getFBNativeClass() {
        return fbNativeAds;
    }

    public void showAd(final AdStatus adStatus) {
        if ((mFbInterstitial != null && mFbInterstitial.isAdLoaded())) {
            mFbInterstitial.setAdListener(new InterstitialAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    System.out.println();
                }
                @Override
                public void onAdLoaded(Ad ad) { }
                @Override
                public void onAdClicked(Ad ad) { }
                @Override
                public void onLoggingImpression(Ad ad) { }
                @Override
                public void onInterstitialDisplayed(Ad ad) { }
                @Override
                public void onInterstitialDismissed(Ad ad) {
                    adStatus.interstitialFocusClosed();
                }
            });
            mFbInterstitial.show();

        } else if ((mAdmobInterstitial != null && mAdmobInterstitial.isLoaded())) {
            mAdmobInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    adStatus.interstitialFocusClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });
            mAdmobInterstitial.show();
        } else {
            adStatus.interstitialFocusClosed();
            loadInterstitialAdmob();
            loadInterstitialFB();
        }

    }

}