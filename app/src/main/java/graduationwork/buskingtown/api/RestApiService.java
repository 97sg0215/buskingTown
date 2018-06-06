package graduationwork.buskingtown.api;

import android.database.Observable;

import graduationwork.buskingtown.model.OauthToken;
import graduationwork.buskingtown.model.SignUp;
import graduationwork.buskingtown.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApiService {

    public  static  final String API_URL="http://127.0.0.1:8000/";

    //회원가입을 위한 데이터 포스트
    @POST("sign_up/")
    Call<SignUp> postSignUp(@Body SignUp signUp);

    //토큰 생성을 위한 포스트
    @POST("/o/oauth2/token")
    Observable<OauthToken> getAccessToken(@Query("grant_type") String grantType, @Query("username") String username, @Query("password") String password);

//    @GET("user/")
//    Class<Profile>  getPublicUserProfile (@Body Profile profile);

    @GET("user")
    Call<User> getUserById(@Query("user_name") String username);
}
