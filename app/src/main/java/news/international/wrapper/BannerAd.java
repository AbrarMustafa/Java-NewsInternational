package news.international.wrapper;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

import news.international.R;
import news.international.utils.AppConstants;

public class BannerAd {

    //----------------------------------Ads Rendering   -------------------------------//
    boolean isAnyAdLoaded=false;
    public void loadBannerAd(final Activity mainActivity){

        mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isAnyAdLoaded) {

                            // ==Fb Rendering   -------------------------------//
                            com.facebook.ads.AdView fbView = new com.facebook.ads.AdView(mainActivity, AppConstants.FBPlacementID_BANNER, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

                            // Find the Ad Container
                            final LinearLayout fbContainer = mainActivity.findViewById(R.id.fbAdView);
                            fbContainer.addView(fbView);
                            fbView.setAdListener(new com.facebook.ads.AdListener() {
                                @Override
                                public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                    // Ad error callback
                                    System.out.println("");
                                }

                                @Override
                                public void onAdLoaded(com.facebook.ads.Ad ad) {
                                    if (!isAnyAdLoaded) {
                                        isAnyAdLoaded = true;
                                        fbContainer.setVisibility(View.VISIBLE);
                                    } else
                                        fbContainer.setVisibility(View.GONE);
                                    // Ad loaded callback
                                }

                                @Override
                                public void onAdClicked(com.facebook.ads.Ad ad) {
                                    // Ad clicked callback
                                }

                                @Override
                                public void onLoggingImpression(com.facebook.ads.Ad ad) {
                                    // Ad impression logged callback
                                }
                            });
                            fbView.loadAd();


//                            // ==ADMOB Rendering   -------------------------------//

                            final AdView admobView = new AdView(mainActivity);
                            admobView.setAdSize(AdSize.BANNER);
                            admobView.setAdUnitId(AppConstants.ADMOBREAL_BANNERID);

                            final LinearLayout admobContainer = mainActivity.findViewById(R.id.admobAdView);
                            admobContainer.addView(admobView);

                            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
                            admobView.setAdListener(new com.google.android.gms.ads.AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // Code to be executed when an ad finishes loading.
                                    if (!isAnyAdLoaded) {
                                        isAnyAdLoaded = true;
                                        admobContainer.setVisibility(View.VISIBLE);
                                    } else
                                        admobContainer.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdFailedToLoad(int errorCode) {
                                    System.out.println("error=onAdFailedToLoad");
                                    // Code to be executed when an ad request fails.
                                }

                                @Override
                                public void onAdOpened() {
                                    // Code to be executed when an ad opens an overlay that
                                    // covers the screen.
                                }

                                @Override
                                public void onAdClicked() {
                                    // Code to be executed when the user clicks on an ad.
                                }

                                @Override
                                public void onAdLeftApplication() {
                                    // Code to be executed when the user has left the app.
                                }

                                @Override
                                public void onAdClosed() {
                                    // Code to be executed when the user is about to return
                                    // to the app after tapping on an ad.
                                }
                            });
                            admobView.loadAd(adRequest);

                        }
                    }

                });

    }
}
