package news.international.retrofit;

import news.international.BuildConfig;
import news.international.models.NewsModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://64.227.10.213:8000/newApi/app/v1/";
    //    public static final String BASE_URL = "http://192.168.100.18:8000/newApi/app/v1/";
//    public static final String BASE_URL = "http://64.227.10.213:8000/newApi/app/v1/";
//    public static final String BASE_URL = "http://10.0.2.2:8000/newApi/app/v1/";
//    public static final String BASE_URL = "http://localhost:8000/newApi/app/v1/";
    //    public static final String BASE_URL = "http://127.0.0.1:8000/newApi/app/v1/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
            {
                @Override public void log(String message)
                {
                    System.out.println(" Logging it :" +message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);
        }





        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

}