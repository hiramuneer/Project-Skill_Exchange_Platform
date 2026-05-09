public class User{
    private int userId;
    private String fullName;
    private String email;

    public User(int userId, String fullName, String email){
        this.userId   = userId;
        this.fullName = fullName;
        this.email    = email;
    }

    public int    getUserId(){
        return userId;
    }
    public String getFullName(){
        return fullName;
    }
    public String getEmail(){
        return email;
    }
}
