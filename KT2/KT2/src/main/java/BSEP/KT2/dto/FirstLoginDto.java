package BSEP.KT2.dto;

public class FirstLoginDto {
    private String username;
    private boolean isFirstLogin;

    public FirstLoginDto(String username, boolean isFirstLogin){
        this.username=username;
        this.isFirstLogin=isFirstLogin;
    }

    public String getUsername(){
        return username;
    }

    public boolean getIsFirstLogin(){
        return isFirstLogin;
    }
}
