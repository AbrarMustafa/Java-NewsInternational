package news.international.retrofit;


import io.reactivex.Observable;
import news.international.models.LoginModel;
import news.international.models.SuccessModel;
import news.international.models.NewsModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("login/")
    Observable<LoginModel> login(@Field("username") String username, @Field("email") String email, @Field("accountType") String accountType, @Field("accessToken") String accessToken);

    @GET("recentnews/")
    Observable<NewsModel> getLatestNews();

    @GET("marknews/")
    Observable<NewsModel> getMarkedNews(@Header("Authorization") String authorization);

    @GET("categorynews/")
    Observable<NewsModel> getCategoryNews(@Query("category") String category);

    @FormUrlEncoded
    @POST("updatetoken/")
    Observable<SuccessModel> setFCMToken(@Header("Authorization") String authorization,@Field("deviceType") String deviceType, @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST("marknews/")
    Observable<SuccessModel> setMarkedNews(@Header("Authorization") String authorization,@Field("setMarked") boolean setMarked, @Field("newsId") int newsId);

}
