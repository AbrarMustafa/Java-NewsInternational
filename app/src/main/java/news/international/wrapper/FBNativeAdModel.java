package news.international.wrapper;

import com.facebook.ads.NativeAdLayout;
import com.google.gson.annotations.SerializedName;

public class FBNativeAdModel {
    boolean isAlreadyShown=false;
    NativeAdLayout nativeAdLayout;

    FBNativeAdModel(NativeAdLayout nativeAdLayout, boolean isAlreadyShown){
        this.isAlreadyShown=isAlreadyShown;
        this.nativeAdLayout=nativeAdLayout;
    }

    public boolean isAlreadyShown() {
        return isAlreadyShown;
    }

    public void setAlreadyShown(boolean alreadyShown) {
        isAlreadyShown = alreadyShown;
    }

    public NativeAdLayout getnativeAdLayout() {
        return nativeAdLayout;
    }

}
