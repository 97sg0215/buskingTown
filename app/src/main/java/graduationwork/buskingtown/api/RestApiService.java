package graduationwork.buskingtown.api;

import java.util.List;

import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.ChangePassword;
import graduationwork.buskingtown.model.CoinManagement;
import graduationwork.buskingtown.model.ConcertReservation;
import graduationwork.buskingtown.model.Connections;
import graduationwork.buskingtown.model.LendLocation;
import graduationwork.buskingtown.model.LendLocationOption;
import graduationwork.buskingtown.model.LikePost;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.Post;
import graduationwork.buskingtown.model.PracticeReservation;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.PurchaseCoin;
import graduationwork.buskingtown.model.RoadConcert;
import graduationwork.buskingtown.model.SupportCoin;
import graduationwork.buskingtown.model.User;
import graduationwork.buskingtown.model.SignUp;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApiService {

    public static final String API_URL = "http://buskingtown.pythonanywhere.com/";

    //회원가입을 위한 데이터 포스트
    @POST("accounts/sign_up/")
    Call<SignUp> postSignUp(@Body SignUp signUp);

    //로그인을 위한 데이터 포스트
    @POST("accounts/login/")
    Call<User> login(@Body Login login);

    //비밀번호 변경
    @PUT("accounts/changePassword/{user_id}/")
    Call<ResponseBody> changePassword(@Header("Authorization") String authToken, @Path("user_id") int id, @Body ChangePassword changePassword);

    //회원탈퇴
    @DELETE("accounts/deleteUser/{user_id}/")
    Call<User> withdrawal(@Header("Authorization") String authToken, @Path("user_id") int id);

    //유저 토큰 생성
    @GET("user/")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);

    //유저 개인 정보 받아오기
    @GET("user/{user_id}/")
    Call<User> getUserDetail(@Header("Authorization") String authToken, @Path("user_id") int id);

    //유저 프로필 업데이트
    @Multipart
    @PUT("accounts/update_profile/{user_id}/")
    Call<Profile> updateProfile(@Header("Authorization") String authToken,
                                @Path("user_id") int id,
                                @Part("user") RequestBody user,
                                @Part MultipartBody.Part user_image);

    @Multipart
    @PUT("accounts/update_profile/{user_id}/")
    Call<Profile> updateCoin(@Header("Authorization") String authToken,
                             @Path("user_id") int id,
                             @Part("user") RequestBody user,
                             @Part("purchase_coin") RequestBody purchase_coin);

    //유저 리스트 받아오기 (검색을 위함)
    @GET("user/")
    Call<List<User>> userList(@Header("Authorization") String autoToken);

    //버스커 생성
    @Multipart
    @POST("accounts/certification/")
    Call<Busker> postBusker(@Header("Authorization") String authToken,
                            @Part("user") RequestBody user,
                            @Part("busker_name") RequestBody busker_name,
                            @Part("busker_type") RequestBody busker_type,
                            @Part("team_name") RequestBody team_name,
                            @Part("busker_tag") RequestBody busker_tag,
                            @Part("busker_phone") RequestBody busker_phone,
                            @Part MultipartBody.Part busker_image);

    //받은 코인
    @GET("accounts/coin/{busker_id}/")
    Call<List<SupportCoin>> getCoin(@Header("Authorization") String authToken, @Path("busker_id") int busker);

    //버스커 객체 삭제 > 인증시 오류 해결을 위해
    @DELETE("accounts/delete/{busker_id}/")
    Call<Busker> deleteBusker(@Header("Authorization") String autoToken, @Path("busker_id") int id);

    //버스커 리스트 불러오기
    @GET("busker/")
    Call<List<Busker>> all_busker(@Header("Authorization") String autoToken);

    //각 버스커 불러오기
    @GET("busker/{busker_id}/")
    Call<Busker> buskerDetail(@Header("Authorization") String autoToken, @Path("busker_id") int id);

    //버스커 팀 체크
    @GET("accounts/buskerTeamCheck/{team_name}/")
    Call<List<Busker>> buskerTeam(@Header("Authorization") String autoToken, @Path("team_name") String team_name);

    //게시물 작성
    @Multipart
    @POST("busking/postUpload/")
    Call<Post> postUpload(@Header("Authorization") String authToken,
                          @Part("busker") RequestBody busker,
                          @Part MultipartBody.Part post_image,
                          @Part("content") RequestBody content);

    @DELETE("busking/postDelete/{post_id}/")
    Call<Post> postDelete(@Header("Authorization") String authToken, @Path("post_id") int id);

    //게시물 리스트
    @GET("busking/buskerPostList/{busker_id}/")
    Call<List<Post>> postList(@Header("Authorization") String authToken, @Path("busker_id") int id);

    //유저 좋아요 목록
    @GET("accounts/likeCheck/{user_id}/")
    Call<List<LikePost>> getLikePost(@Header("Authorization") String authToken, @Path("user_id") int user);

    //게시물 좋아요
    @POST("busking/likePost/")
    Call<LikePost> likePost(@Header("Authorization") String authToken, @Body LikePost likePost);

    //게시물 좋아요 취소
    @DELETE("busking/unlikePost/{like_post_id}/")
    Call<LikePost> unlikePost(@Header("Authorization") String authToken, @Path("like_post_id") int like_post_id);

    //팔로잉API
    @POST("accounts/following/")
    Call<Connections> follow(@Header("Authorization") String autoToken, @Body Connections connection);

    //팔로우하는 모든 버스커 목록 불러오기 user = 일반 유저 , following = 버스커 유저
    @GET("accounts/allFollowList/")
    Call<List<Connections>> get_all_followings(@Header("Authorization") String autoToken);

    //버스커별 팔로워 목록
    @GET("accounts/followerList/{busker_id}/")
    Call<List<Connections>> get_followers(@Header("Authorization") String autoToken, @Path("busker_id") int id);

    //일반 유저별 팔로잉 목록
    @GET("accounts/followingList/{user_id}/")
    Call<List<Connections>> get_followings(@Header("Authorization") String autoToken, @Path("user_id") int id);

    //언팔로우API
    @DELETE("accounts/unfollowing/{connection_id}/")
    Call<Connections> unfollow(@Header("Authorization") String autoToken, @Path("connection_id") int connection_id);

    //랭킹 API
    @GET("accounts/buskerRank/")
    Call<List<Busker>> get_ranker(@Header("Authorization") String autoToken);

    //장소 제공
    @Multipart
    @POST("rentLocation/postProvide/")
    Call<LendLocation> rentLocation(@Header("Authorization") String autoToken,
                                    @Part("user") RequestBody user,
                                    @Part("provide_type") RequestBody provide_type,
                                    @Part("provider_phone") RequestBody provider_phone,
                                    @Part("provider_email") RequestBody provider_email,
                                    @Part("provide_location_name") RequestBody provide_location_name,
                                    @Part("provide_start_date") RequestBody provide_start_date,
                                    @Part("provide_end_date") RequestBody provide_end_date,
                                    @Part("provide_start_time") RequestBody provide_start_time,
                                    @Part("provide_end_time") RequestBody provide_end_time,
                                    @Part("provide_location") RequestBody provide_location,
                                    @Part("provide_lon") RequestBody provide_lon,
                                    @Part("provide_lat") RequestBody provide_lat,
                                    @Part("provide_description") RequestBody provide_description,
                                    @Part("provide_rule") RequestBody provide_rule,
                                    @Part("provide_refund_rule") RequestBody provide_refund_rule,
                                    @Part MultipartBody.Part provide_image
    );

    @POST("rentLocation/postProvideOption/")
    Call<LendLocationOption> rentLocationOption(@Header("Authorization") String autoToken, @Body LendLocationOption lendOption);

    @GET("rentLocation/provideUserList/{user_id}/")
    Call<List<LendLocation>> provideList(@Header("Authorization") String autoToken, @Path("user_id") int user_id);

    @GET("rentLocation/provideOptionList/{provide_id}/")
    Call<List<LendLocationOption>> provideOptionList(@Header("Authorization") String autoToken, @Path("provide_id") int provide_id);

    //대여 화면
    @GET("rentLocation/practiceRoomList/")
    Call<List<LendLocation>> practiceRoomList(@Header("Authorization") String autoToken);

    @GET("rentLocation/concertRoomList/")
    Call<List<LendLocation>> concertRoomList(@Header("Authorization") String autoToken);

    @GET("rentLocation/provideList/{provide_id}/")
    Call<LendLocation> roomInfo(@Header("Authorization") String autoToken, @Path("provide_id") int provide);

    //예약체크
    @GET("rentLocation/reservationPracticeRoom/{provide}/{provide_option}/{practice_date}/")
    Call<List<PracticeReservation>> reservationCheckPractice(@Header("Authorization") String autoToken, @Path("provide") int provide, @Path("provide_option") int provide_option, @Path("practice_date") String practice_date);

    //예약하기
    @POST("rentLocation/reservationPracticeRoom/")
    Call<PracticeReservation> reservationPractice(@Header("Authorization") String autoToken, @Body PracticeReservation practiceReservation);

    //예약확인
    @GET("accounts/reservationRoomCheck/{busker_id}/")
    Call<List<PracticeReservation>> reservationBuskerCheck(@Header("Authorization") String autoToken, @Path("busker_id") int busker);

    //예약취소
    @DELETE("rentLocation/cancelReservationPracticeRoom/{reservation_id}/")
    Call<PracticeReservation> cancelReservation(@Header("Authorization") String autoToken, @Path("reservation_id") int reservation_id);

    //콘서트
    //예약체크
    @GET("rentLocation/reservationConcertRoom/{provide}/{provide_option}/{concert_date}/")
    Call<List<ConcertReservation>> reservationCheckConcert(@Header("Authorization") String autoToken, @Path("provide") int provide, @Path("provide_option") int provide_option, @Path("concert_date") String concert_date);

    //예약하기
    @POST("rentLocation/reservationConcertRoom/")
    Call<ConcertReservation> reservationConcert(@Header("Authorization") String autoToken, @Body ConcertReservation concertReservation);

    //예약확인
    @GET("accounts/reservationConcertCheck/{busker_id}/")
    Call<List<ConcertReservation>> reservationBuskerConcertCheck(@Header("Authorization") String autoToken, @Path("busker_id") int busker);

    //예약취소
    @DELETE("rentLocation/cancelReservationConcertRoom/{reservation_id}/")
    Call<ConcertReservation> cancelConcertReservation(@Header("Authorization") String autoToken, @Path("reservation_id") int reservation_id);

    //supportCoin
    @POST("busking/supportCoin/")
    Call<SupportCoin> supportCoin(@Header("Authorization") String autoToken, @Body SupportCoin supportCoin);

    //받은 코인 업데이트
    @Multipart
    @PUT("accounts/buskerDetail/{busker_id}/")
    Call<Busker> updateReceivedCoin(@Header("Authorization") String autoToken,
                                    @Path("busker_id") int busker_id,
                                    @Part("busker_id") RequestBody busker,
                                    @Part("received_coin") RequestBody purchase_coin);

    @PUT("busking/supportCoin/{support_id}/")
    Call<SupportCoin> supportCoinCheck(@Header("Authorization") String autoToken, @Path("support_id") int id, @Body SupportCoin supportCoin);

    //코인 구매
    @POST("accounts/purchaseCoin/")
    Call<PurchaseCoin> purchaseCoin(@Header("Authorization") String autoToken, @Body PurchaseCoin purchaseCoin);

    //사용자 코인 통계
    @GET("accounts/userCoinManagement/{user_id}/{start_date}/{end_date}/")
    Call<List<CoinManagement>> coinView(@Header("Authorization") String autoToken, @Path("user_id") int user, @Path("start_date") String start_date, @Path("end_date") String end_date);

    //길거리 공연 예약
    @POST("busking/reservationRoadConcert/")
    Call<RoadConcert> reservationRoadConcert(@Header("Authorization") String autoToken, @Body RoadConcert roadConcert);

    //길거리 공연 예약 리스트
    @GET("busking/reservationCheckRoadConcert/{road_address}/{road_concert_date}/")
    Call<List<RoadConcert>> getReservationRoadConcert(@Header("Authorization") String autoToken, @Path("road_address") String road_address, @Path("road_concert_date") String road_concert_date);

    //길거리 공연 지난 예약 리스트
    @GET("accounts/previousRoadConcertList/{busker_id}/")
    Call<List<RoadConcert>> getPreviousReservationRoadConcert(@Header("Authorization") String autoToken, @Path("busker_id") int busker_id);

    //길거리 공연 앞으로 예약 리스트
    @GET("accounts/nextRoadConcertList/{busker_id}/")
    Call<List<RoadConcert>> getNextReservationRoadConcert(@Header("Authorization") String autoToken, @Path("busker_id") int busker_id);

    //라이브 공연
    @GET("busking/liveBusking/{road_concert_date}/{current_time}/")
    Call<List<RoadConcert>> getLiveBuking(@Header("Authorization") String autoToken, @Path("road_concert_date") String road_concert_date, @Path("current_time") String current_time);

    //팔로워 수 통계 조회
    @GET("accounts/followerStatics/{busker_id}/{start_date}/{end_date}/")
    Call<List<Connections>> getFollowerStatistic(@Header("Authorization") String autoToken, @Path("busker_id") int busker, @Path("start_date") String start_date, @Path("end_date") String end_date);

    //받은 코인 수 통계 조회
    @GET("busking/supportCoinStatics/{busker_id}/{start_date}/{end_date}/")
    Call<List<SupportCoin>> getCoinStatistic(@Header("Authorization") String autoToken, @Path("busker_id") int busker, @Path("start_date") String start_date, @Path("end_date") String end_date);
}
