package Entity;

import java.util.Date;

public abstract class User {

    private String username;
    private String password;
    private Date dateOfBirth;

    public User(){}

    public User(String username, String password, Date dateOfbirth){
        this.username=username;
        this.password=password;
        this.dateOfBirth=dateOfBirth;
    }


    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setDateOfBirth(Date dateOfBirth){
        this.dateOfBirth=dateOfBirth;
    }
    public String getUsername(){
        return username;
    }


}