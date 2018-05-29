package graduationwork.buskingtown.api;

import graduationwork.buskingtown.model.SignUp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApiService {

    public  static  final String API_URL="http://127.0.0.1:8000/";

    //회원가입을 위한 데이터 포스트
    @POST("sign_up/")
    Call<SignUp> postSignUp(@Body SignUp signUp);
}
