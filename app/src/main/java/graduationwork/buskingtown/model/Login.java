package graduationwork.buskingtown.model;

public class Login {
    String username;
    String password;

    //암호 및 사용자 모델은 서버에서 다시 획득
    public Login(String user, String password){
        this.username = user;
        this.password = password;
    }
}
