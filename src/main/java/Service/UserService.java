package Service;


import java.util.Date;

public abstract class UserService {

    public abstract void signUp(String username, String password, Date dateOfBirth);
    public abstract boolean logIn(String username, String password);
    protected static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            System.out.println("Username cannot be empty or null.");
            return false;
        }

        if (username.length() < 3 || username.length() > 15) {
            System.out.println("Username must be between 3 and 15 characters long.");
            return false;
        }

        // Allow alphanumeric characters, underscores, and periods
        String regex = "^[a-zA-Z0-9._]+$";
        if (!username.matches(regex)) {
            System.out.println("Username can only contain letters, numbers, periods, and underscores.");
            return false;
        }

        return true;
    }
    protected static boolean isValidPassword(String password){
        if(password==null || password.length()<8){
            System.out.println("The password has to be at least 8 characters long");
            return false;
        }

        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$";

        if(!password.matches(regex)){
            System.out.println("password has to have at least one capital letter, a digit and a special character.");
            return false;
        }

        return true;


    }









}