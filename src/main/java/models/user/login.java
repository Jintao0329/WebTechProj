package models.user;

public interface login {
    public boolean verify(String password);

    public String getAccountType();
}
