
package news.international.wrapper;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

import news.international.R;
import news.international.interfaces.AdStatus;

public class FBNativeAd {
    private static final String TAG = "FB";

    public ArrayList<FBNativeAdModel> nativeAdsArray=new ArrayList();
    Activity activity;
    AdStatus adStatus;

    public FBNativeAd(AdStatus adStatus) {
        this.adStatus=adStatus;
    }

    public void loadNewAds(final Activity activity,String placementId) {
        this.activity=activity;

        final NativeBannerAd mNativeBannerAd = new NativeBannerAd(activity ,placementId);
        mNativeBannerAd.setAdListener(new NativeAdListener(){
            @Override
            public void onError(Ad ad, AdError error) {
                System.out.println(error);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                NativeAdLayout mAdView = (NativeAdLayout) activity.getLayoutInflater().inflate(R.layout.native_fb, null);
                FrameLayout mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);
                if (mNativeBannerAd == null || mNativeBannerAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    return;
                }
                // Unregister last ad
                mNativeBannerAd.unregisterView();
                nativeAdsArray.add(new FBNativeAdModel(mAdView,false));

                // Using the AdChoicesView is optional, but your native ad unit should
                // be clearly delineated from the rest of your app content. See
                // https://developers.facebook.com/docs/audience-network/guidelines/native-ads#native
                // for details. We recommend using the AdChoicesView.
                AdOptionsView adOptionsView = new AdOptionsView(
                        activity,
                        mNativeBannerAd,
                        mAdView,
                        AdOptionsView.Orientation.HORIZONTAL,
                        20);
                mAdChoicesContainer.removeAllViews();
                mAdChoicesContainer.addView(adOptionsView);

                inflateAd(mNativeBannerAd, mAdView);

                // Registering a touch listener to log which ad component receives the touch event.
                // We always return false from onTouch so that we don't swallow the touch event (which
                // would prevent click events from reaching the NativeAd control).
                // The touch listener could be used to do animations.
                mNativeBannerAd.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = view.getId();
                            if (i == R.id.native_ad_call_to_action) {
                                Log.d(TAG, "Call to action button clicked");
                            } else if (i == R.id.native_icon_view) {
                                Log.d(TAG, "Main image clicked");
                            } else {
                                Log.d(TAG, "Other ad component clicked");
                            }
                        }
                        return false;
                    }
                });
                adStatus.nativeAdLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "onLoggingImpression");
            }

            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.d(TAG, "onMediaDownloaded");
            }

        });
        mNativeBannerAd.loadAd(NativeAdBase.MediaCacheFlag.ALL);

    }


    private void inflateAd(NativeBannerAd nativeBannerAd, View adView) {
        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(
                adView,
                nativeAdIconView,
                clickableViews
        );

        sponsoredLabel.setText("Sponsored");
    }

}
