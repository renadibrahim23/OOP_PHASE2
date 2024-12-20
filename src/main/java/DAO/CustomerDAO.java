package DAO;
import Entity.Customer;
import Database.Database;

import java.util.Date;
import java.util.List;

public class CustomerDAO implements GenericDAO<Customer>{

    public void add(Customer customer){
        Database.customers.add(customer);

    }

    public Customer getById(int id){
        for(Customer c: Database.customers){
            if(c.getCustomerId()==id){
                return c;
            }
        }
        return null;
    }



    public void update(Customer customer){
        for(Customer c: Database.customers){
            if(c.getCustomerId()==customer.getCustomerId()){
                //need to figure out a way to update the username, password and dataOfBirth as well
                c.setUsername(customer.getUsername());
                c.setPassword(customer.getPassword());
                c.setDateOfBirth(customer.getDateOfBirth());
                c.setGender(customer.getGender());
                c.setAddress(customer.getAddress());
                c.setBalance(customer.getBalance());
                c.setInterests(customer.getInterests());
                c.setCart(customer.getCart());
            }
        }
    }

    public void delete(int customerId){
        Database.customers.removeIf(c -> c.getCustomerId() == customerId);
    }

    public Customer getCustomerByUsername(String username){
        for(Customer customer: Database.customers){
            if((customer.getUsername()).equals(username))return customer;
        }

        return null;

    }



    public List<Customer> getAllCustomers(){
        return Database.customers;
    }

    public void addInterest(String category,Customer customer) {
        if (category != null && !category.isEmpty() && !customer.getInterests().contains(category)) {
            customer.getInterests().add(category); //add only if not already present
            customer.setInterests(customer.getInterests());
        }
    }

    public Customer createNewCustomer(String username, String password, Date dateOfBirth, String address, Customer.Gender gender){
        Customer customer=new Customer(username,password,dateOfBirth,address,gender);
        Database.customers.add(customer);
        return customer;
    }

    public String getCustomerUsername(Customer customer){
        return customer.getUsername();
    }
    public int getCustomerId(Customer customer){
        return customer.getCustomerId();
    }

    public int getNumberOfCustomers(){
        return Database.customers.size();
    }

    public String showCustomerInfo(Customer customer){
        return customer.toString();
    }







}