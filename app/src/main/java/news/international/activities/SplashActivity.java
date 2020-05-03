package news.international.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import news.international.MyApplication;
import news.international.R;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progress_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress_splash=findViewById(R.id.progress_splash);
        ((MyApplication)getApplication()).thingsAtStartApp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress_splash.setMax(100);
        progress_splash.setProgress(10);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_splash.setProgress(80);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }, 800);
//        getFbToken();
    }

    private void getFbToken() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                System.out.println(key);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
