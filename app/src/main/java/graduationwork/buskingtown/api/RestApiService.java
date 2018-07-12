package graduationwork.buskingtown.api;

import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.Response;
import graduationwork.buskingtown.model.SignUp;
import graduationwork.buskingtown.model.User;
import graduationwork.buskingtown.model.UserDetail;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApiService {

    public  static  final String API_URL="http://d52f3fd5.ngrok.io/";

    //버스커 이미지업로드 포스트
//    @POST("media/busker_profile_image/")
//    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part file, @Part("file") RequestBody name);

//    @Multipart
//    @POST("media/busker_profile_image/")
//    Call<ResponseBody> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);

    @Multipart
    @POST("media/busker_profile_image/")
    Call<Response> uploadImage(@Part MultipartBody.Part image);

//
//    @Multipart
//    @POST("media/busker_profile_image/")
//    Call<ResponseBody> uploadImage(@Part("file\"; fileName=\"myFile.png\" ")RequestBody requestBodyFile, @Part("image") RequestBody requestBodyJson);

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

    //버스커 생성
    @POST("accounts/certification/")
    Call<Busker> postBusker (@Header("Authorization")String authToken, @Body Busker busker);

    //이미지 포함 버스커 생성
    @Multipart
    @POST("accounts/certification/")
    Call<Busker> postBuskerImage (@Header("Authorization")String authToken,
                             @Part("Busker") Busker busker,
                             @Part("file\"; filename=\"profile.png\" ") RequestBody busker_image);

//    //이미지 포함 버스커 생성
//    @POST("accounts/certification/")
//    Call<Busker> postBusker (@Header("Authorization")String authToken,
//                             @Body Busker busker,
//                             @Part("file\"; filename=\"profile.png\" ") MultipartBody.Part busker_image);
}
