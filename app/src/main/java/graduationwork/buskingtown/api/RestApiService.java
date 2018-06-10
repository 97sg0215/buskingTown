package graduationwork.buskingtown.api;

import android.database.Observable;

import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.User;
import graduationwork.buskingtown.model.OauthToken;
import graduationwork.buskingtown.model.SignUp;
import graduationwork.buskingtown.model.UserDetail;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    public  static  final String API_URL="http://fdd6234b.ngrok.io/";

    //회원가입을 위한 데이터 포스트
    @POST("accounts/sign_up/")
    Call<SignUp> postSignUp(@Body SignUp signUp);

    //로그인을 위한 데이터 포스트
    @POST("accounts/login/")
    Call<User> login(@Body Login login);

    //유저 토큰 생성
    @GET("user/")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);

    //유저 개인 정보 받아오기
    @GET("user/{user_id}/")
    Call<UserDetail> getUserDetail (@Header("Authorization")String authToken, @Path("user_id")int id);

}
