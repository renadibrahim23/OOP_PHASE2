package DAO;
import Database.Database;
import Entity.Admin;

import java.util.Date;
import java.util.List;

public class AdminDAO implements GenericDAO<Admin> {

    public AdminDAO(){}

    public  void add(Admin admin){
        Database.admins.add(admin);
    }

    public Admin getById(int id){
        for(Admin a: Database.admins){
            if(a.getAdminId()==id){
                return a;
            }
        }
        return null;
    }

    public void update(Admin admin){
        for(Admin a: Database.admins){
            if(a.getAdminId()==admin.getAdminId()){
                a.setUsername(admin.getUsername());
                a.setPassword(admin.getPassword());
                a.setDateOfBirth(admin.getDateOfBirth());
                a.setRole(admin.getRole());
                a.setWorkingHours(admin.getWorkingHours());

            }
        }
    }

    public void delete(int id){
        Database.admins.removeIf(a -> a.getAdminId() == id);
    }




    public static List<Admin> getAllAdmins(){
        return Database.admins;
    }



    public Admin getAdminByUsername(String username){
        for(Admin a: Database.admins){
            if(a.getUsername().equals(username))return a;

        }
        return null;
    }

    public String getPassword(Admin admin){
        return admin.getPassword();
    }

    public void createNewAdmin(String username, String password, Date dateOfBirth,String role,double workingHours){
        Admin admin=new Admin(username,password,dateOfBirth,role,workingHours);
        Database.admins.add(admin);


    }

    public String getAdminUsername(Admin admin){
        return admin.getUsername();
    }

    public String getAdminPassword(Admin admin){
        return admin.getPassword();
    }
    public double getAdminWorkingHours(Admin admin){
        return admin.getWorkingHours();
    }

    public String getAdminRole(Admin admin){
        return admin.getRole();
    }

    public int getAdminId(Admin admin){
        return admin.getAdminId();
    }


}