package graduationwork.buskingtown;

import android.app.Application;

import graduationwork.buskingtown.api.RestApiService;

import android.app.Application;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static graduationwork.buskingtown.api.RestApiService.API_URL;


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
                Log.i(TAG, API_URL);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                restApiService = retrofit.create(RestApiService.class);
            }
        }
    }
}