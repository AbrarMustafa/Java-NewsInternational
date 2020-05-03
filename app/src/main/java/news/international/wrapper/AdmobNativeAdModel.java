package news.international.wrapper;

import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.gson.annotations.SerializedName;

public class AdmobNativeAdModel {
    boolean isAlreadyShown=false;
    UnifiedNativeAdView unifiedNativeAdView;

    AdmobNativeAdModel(UnifiedNativeAdView unifiedNativeAdView,boolean isAlreadyShown){
        this.isAlreadyShown=isAlreadyShown;
        this.unifiedNativeAdView=unifiedNativeAdView;
    }

    public boolean isAlreadyShown() {
        return isAlreadyShown;
    }

    public void setAlreadyShown(boolean alreadyShown) {
        isAlreadyShown = alreadyShown;
    }

    public UnifiedNativeAdView getUnifiedNativeAdView() {
        return unifiedNativeAdView;
    }

}
