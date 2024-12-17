package Entity;

import java.util.Date;

public class Admin extends User{
    private static int idCounter = 0; // static counter for generating IDs
    private int adminId;
    private String role;
    private double workingHours;

    public Admin(){
        this.adminId=++idCounter;
    }

    public Admin(String username, String password, Date dateOfBirth){
        super(username,password,dateOfBirth);
        this.adminId=++idCounter;

    }

    public Admin(String username, String password, Date dateOfBirth, String role, double workingHours){
        super(username,password,dateOfBirth);
        this.adminId=++idCounter;
        this.role=role;
        this.workingHours=workingHours;
    }

    /*public Admin(String username, String password, Date dateOfBirth, String role, double workingHours) {
        super(username,password,dateOfBirth);
        this.role=role;
        this.workingHours=workingHours;
    }*/

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }

    public int getAdminId() {
        return adminId;
    }

    /*public void setAdminId(int adminId) {
        this.adminId = adminId;
    }*/

}