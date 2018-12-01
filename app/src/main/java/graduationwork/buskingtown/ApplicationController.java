package graduationwork.buskingtown;

import android.app.Application;
import graduationwork.buskingtown.api.RestApiService;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static graduationwork.buskingtown.api.RestApiService.API_URL;

//네트워크 연결 메소드화
public class ApplicationController extends Application {
    public final static String TAG = "BuskingTown";
    private static ApplicationController instance;

    public static ApplicationController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
    }

    private RestApiService restApiService;

    public RestApiService getRestApiService() {
        return restApiService;
    }


    public void buildNetworkService() {
        synchronized (ApplicationController.class) {
            if (restApiService == null) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.interceptors().add(logging);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                restApiService = retrofit.create(RestApiService.class);
            }
        }
    }

}