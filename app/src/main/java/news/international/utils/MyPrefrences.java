package news.international.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Abrarmustafa on 25/03/2017.
 */

public class MyPrefrences {
    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor ;



    public final static String ISPREMIUNUSER = "isPremiunUser";
    public final static String USERID = "userId";
    public final static String PROFILEIMAGE = "profileImage";
    public final static String ISLOGIN = "isLogin";
    public final static String TOKEN = "token";

    public MyPrefrences(Context context)
    {
        this.context=context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }


    public void setpremiunuser(boolean isPremium) {
        editor.putBoolean(ISPREMIUNUSER,isPremium);
        editor.apply();
    }

    public boolean getpremiunuser() {
        return prefs.getBoolean(ISPREMIUNUSER,false);
    }



    public void setUserId(String userId) {
        editor.putString(USERID,userId);
        editor.apply();
    }

    public String getUserId() {
        return prefs.getString(USERID,"");
    }



    public void setProfileImage(String profileImage) {
        editor.putString(PROFILEIMAGE,profileImage);
        editor.apply();
    }

    public String getProfileImage() {
        return prefs.getString(PROFILEIMAGE,"");
    }



    public void setLogin(boolean isLogin) {
        editor.putBoolean(ISLOGIN,isLogin);
        editor.apply();
    }

    public boolean isLogin() {
        return prefs.getBoolean(ISLOGIN,false);
    }


    public void setToken(String token) {
        editor.putString(TOKEN,token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(TOKEN,null);
    }
}

