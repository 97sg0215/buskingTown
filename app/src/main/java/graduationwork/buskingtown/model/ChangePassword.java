package graduationwork.buskingtown.model;

public class ChangePassword {

    String old_password;
    String new_password;

    public ChangePassword(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
