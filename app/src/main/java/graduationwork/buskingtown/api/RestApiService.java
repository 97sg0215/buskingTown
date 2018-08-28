package graduationwork.buskingtown.api;

import java.util.List;

import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connections;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.User;
import graduationwork.buskingtown.model.SignUp;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApiService {

    public  static  final String API_URL="http://6a25c8ac.ngrok.io/";

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
    Call<User> getUserDetail (@Header("Authorization")String authToken, @Path("user_id")int id);

    //버스커 생성
    @Multipart
    @POST("accounts/certification/")
    Call<Busker> postBusker (@Header("Authorization")String authToken,
                             @Part("user") RequestBody user,
                             @Part("busker_name") RequestBody busker_name,
                             @Part("team_name") RequestBody team_name,
                             @Part("busker_tag") RequestBody busker_tag,
                             @Part("busker_phone") RequestBody busker_phone);

    //버스커 객체 삭제 > 인증시 오류 해결을 위해
    @DELETE("accounts/delete/{busker_id}/")
    Call<Busker> deleteBusker (@Header("Authorization")String autoToken, @Path("busker_id")int id);

    //버스커 리스트 불러오기
    @GET("/busker/")
    Call<List<Busker>> top_10_busker (@Header("Authorization")String autoToken);

    //각 버스커 불러오기
    @GET("/busker/{busker_id}/")
    Call<Busker> buskerDetail (@Header("Authorization")String autoToken, @Path("busker_id")int id);

    //팔로잉API
    @POST("/accounts/following/")
    Call<Connections> follow (@Header("Authorization")String autoToken, @Body Connections connection);

    //팔로우하는 버스커 목록 불러오기 user = 일반 유저 , following = 버스커 유저
    @GET("/followingList/")
    Call<List<Connections>> get_followings (@Header("Authorization")String autoToken);

    //언팔로우API
    @DELETE("/accounts/unfollowing/{connection_id}/")
    Call<Connections> unfollow (@Header("Authorization")String autoToken, @Path("connection_id")int connection_id);
}
