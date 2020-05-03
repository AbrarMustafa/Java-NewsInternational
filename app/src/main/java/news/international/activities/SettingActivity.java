package news.international.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import news.international.MyApplication;
import news.international.R;
import news.international.models.LoginModel;
import news.international.models.SuccessModel;
import news.international.utils.MyPrefrences;

public class SettingActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    String TAG="setting";
    MyPrefrences myPrefrences;
    LoginButton fbloginButton;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        myPrefrences=new MyPrefrences(this);
        callbackManager= CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();

        fbloginButton = findViewById(R.id.fbloginButton);
        fbloginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        setupDialog();
        fbSignInResult();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,  data);
    }

    private void fbSignInResult( ) {
        refreshSignInButtons();
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String userId = object.getString("id");//https://graph.facebook.com/2876750592439240/picture?type=large
                                    myPrefrences.setUserId(userId);
                                    loginToDb(name,email,"fb",userId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                // App code
            }

            @Override
            public void onCancel() {
                System.out.println("");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("");
                // App code
            }
        });
    }

    private void refreshSignInButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!myPrefrences.isLogin()) {
                    fbloginButton.setVisibility(View.VISIBLE);
                }else {
                    fbloginButton.setVisibility(View.GONE);
                }
            }
        });

    }


    public void loginToDb(String name, String email, String accountType, String id) {

        alertDialog.show();
        ((MyApplication)getApplicationContext()).apiService.login(name,email,accountType ,id).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<LoginModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("error");
                    }
                    @Override
                    public void onNext(LoginModel _newsModel) {
                        if(_newsModel!=null&&_newsModel.getData()!=null&&_newsModel.getToken()!="") {
                            myPrefrences.setLogin(true);
                            myPrefrences.setToken(_newsModel.getToken());
                            updateToken();
                        }
                        isNotFullyLogin();
                    }
                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error"+e);
                        alertDialog.dismiss();
                        isNotFullyLogin();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
    public void isNotFullyLogin( ){
        if(!myPrefrences.isLogin())
            LoginManager.getInstance().logOut();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fbloginButton.invalidate();
            }
        });
    }

    //=======================================FCM TOKEN==============================//
    public void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                ((MyApplication)getApplicationContext()).apiService.setFCMToken("Token "+myPrefrences.getToken(),"android",newToken).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<SuccessModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                System.out.println("error");
                            }
                            @Override
                            public void onNext(SuccessModel successModel) {
                                if(successModel.getData().equals("success"))
                                    refreshSignInButtons();
                                System.out.println("newsModel");
                                alertDialog.dismiss();
                                isNotFullyLogin();
                            }
                            @Override
                            public void onError(Throwable e) {
                                alertDialog.dismiss();
                                System.out.println("error"+e);
                                isNotFullyLogin();
                            }
                            @Override
                            public void onComplete() {
                                System.out.println("onComplete");
                            }
                        });

            }
        });
    }


    public void setupDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
    }



}
