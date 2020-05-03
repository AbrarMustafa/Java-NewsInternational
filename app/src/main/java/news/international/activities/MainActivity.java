package news.international.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import news.international.interfaces.AdStatus;
import news.international.wrapper.AdmobNativeAd;
import news.international.wrapper.BannerAd;
import news.international.wrapper.FBNativeAd;
import news.international.MyApplication;
import news.international.R;
import news.international.fragments.CategoryNewsFragment;
import news.international.fragments.NewsFragment;
import news.international.fragments.SavedFragment;
import news.international.inAppPurchase.IabHelper;
import news.international.inAppPurchase.IabResult;
import news.international.inAppPurchase.Inventory;
import news.international.inAppPurchase.Purchase;
import news.international.models.NewsModel;
import news.international.utils.AppConstants;
import news.international.utils.MyPrefrences;

public class MainActivity extends AppCompatActivity  {
    public boolean isActivityRunning=true;
    String TAG="MainActivity";
    NewsModel selectedCategoryNewsModel=new NewsModel();
    BottomNavigationView bottomNavigation;
    public MyPrefrences myPrefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myPrefrences = new MyPrefrences(this);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        StartBillingSetup();
        findViewById(R.id.imageview_removeads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveAds();
            }
        });
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        openFragment(NewsFragment.newInstance(""));
                        return true;
                    case R.id.nav_saved:
                        openFragment(SavedFragment.newInstance(""));
                        return true;
                    case R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        return true;
                }
                return false;
            }
        });

        openFragment(NewsFragment.newInstance(""));

        if (!myPrefrences.getpremiunuser()) {
            loadNativeAds();
            loadBannerAds();
        }
    }

    private void loadBannerAds() {
        if(!isAnyNativeAdLoaded) {
            int TIME = 5000; //5000 ms (5 Seconds)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new BannerAd().loadBannerAd(MainActivity.this);
                }
            }, TIME);
        }
    }

    boolean isAnyNativeAdLoaded=false;
    private void loadNativeAds() {

        ((MyApplication)getApplicationContext()).setAdmobNativeClass(new AdmobNativeAd(new AdStatus(){
            @Override
            public void interstitialFocusClosed() { }
            @Override
            public void nativeAdLoaded() {
                isAnyNativeAdLoaded=true;
                findViewById(R.id.linear_bannerads).setVisibility(View.GONE);
            }
        }));
        ((MyApplication)getApplicationContext()).setFBNativeClass(new FBNativeAd(new AdStatus(){
            @Override
            public void interstitialFocusClosed() { }
            @Override
            public void nativeAdLoaded() {
                findViewById(R.id.linear_bannerads).setVisibility(View.GONE);
                isAnyNativeAdLoaded=true;
            }
        }));

        ((MyApplication)getApplicationContext()).getAdmobNativeClass().loadNewAds(this,AppConstants.ADMOBREAL_NATIVEID);
        ((MyApplication)getApplicationContext()).getFBNativeClass().loadNewAds(this,AppConstants.FBPlacementID_NATIVE_ONE);
        ((MyApplication)getApplicationContext()).getFBNativeClass().loadNewAds(this,AppConstants.FBPlacementID_NATIVE_TWO);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == AppConstants.CATEGORYREQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String category = data.getStringExtra(AppConstants.CATEGORIES);
                getCategoryNews(category);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }


    //=======================================CATEGORY NEWS==============================//
    public void getCategoryNews(String category) {

        ((MyApplication)getApplicationContext()).apiService.getCategoryNews(category).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<NewsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(NewsModel newsModel) {
                        selectedCategoryNewsModel=newsModel;
                        openFragment(CategoryNewsFragment.newInstance(""));
                    }
                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error"+e);
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }
    public NewsModel getSelectedCategoryNewsModel(){
        return selectedCategoryNewsModel;
    }

    //============================================billing setup=============================================//
    static final String ITEM_SKU = "adsremove";
    IabHelper mHelper;
    boolean billingSetUp;
    boolean mIsPremium = false;

    private void StartBillingSetup(){
        //https://www.techotopia.com/index.php/Integrating_Google_Play_In-app_Billing_into_an_Android_6_Application
        String base64EncodedPublicKey = getString(R.string.base64EncodedPublicKey);

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    billingSetUp=false;
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    billingSetUp=true;
                    Log.d(TAG, "In-app Billing is set up OK");
                    //first check from IabResult if ads are purchased.(This is helpful if user has uninstalled and installed app again)

                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        Log.d(TAG, "Error querying inventory. Another async operation in progress.");
//                        complain();
                    }



                }
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(ITEM_SKU);
            mIsPremium = (premiumPurchase != null );
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            myPrefrences.setpremiunuser(mIsPremium);

        }
    };

    private void RemoveAds(){
        if (billingSetUp) {
            try {
                if (mHelper != null) mHelper.flagEndAsync();

                mHelper.launchPurchaseFlow(MainActivity.this, ITEM_SKU, AppConstants.REMOVEADS_REQUESTCODE, mPurchaseFinishedListener, "mypurchasetoken");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this,"Something went wrong!! Please try again",Toast.LENGTH_LONG).show();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {

            if (result.isSuccess()){
                myPrefrences.setpremiunuser(true);
                Toast.makeText(MainActivity.this, "Item successfully purchased", Toast.LENGTH_SHORT).show();
            }
            else if (result.itemAlreadyPurchased()){
                Toast.makeText(MainActivity.this, "Item already purchased", Toast.LENGTH_SHORT).show();
            }
            else  if (result.isFailure()) {
                // Handle error
                return;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isActivityRunning=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityRunning=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isActivityRunning=false;
        finish();
    }

}
