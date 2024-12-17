package Service;
import DAO.CustomerDAO;
import DAO.OrderDAO;
import Entity.Customer;
import Entity.Order;


import java.util.ArrayList;
import java.util.List;


public class OrderService {
    private  OrderDAO orderDAO=new OrderDAO();
    private  CustomerDAO customerDAO=new CustomerDAO();


    public OrderService(){}

    //constructor
    public OrderService(OrderDAO orderDAO , CustomerDAO customerDAO ){
        this.orderDAO = orderDAO;
        this.customerDAO = customerDAO;
    }


    public void placeOrder(Order order) {
        if (order == null || order.getOrderId() < 0 || order.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Order details are invalid.");
        }

        if (orderDAO.getById(order.getOrderId()) == null) {
            orderDAO.add(order);
            System.out.println("Order has been placed successfully.");
        } else {
            System.out.println("An order with this ID already exists.");
        }
    }

    public void updateOrder(Order order){
        if (order == null || order.getOrderId() < 0 || order.getPaymentMethod() == null){
            throw new IllegalArgumentException("Order details are invalid.");
        }
        if (orderDAO.getById((order.getOrderId())) == null){
            orderDAO.update(order);
            System.out.println("order has been updated successfully");
        }
        else {
            System.out.println("order with this ID does not exist");
        }
    }
    public void cancelOrder(int orderId){
        if (orderId < 0){
            System.out.println("invalid order ID");
        }
        if (orderDAO.getById(orderId) != null){
            orderDAO.delete(orderId);
            System.out.println("order has been cancelled successfully");
        }
        else {
            System.out.println("Order with this ID does not exist");
        }
    }
    public Order getOrderById(int orderId){
        if (orderId < 0){
            System.out.println("invalid Order ID");
        }
        Order order = orderDAO.getById((orderId));
        if (order != null){
            return order;
        } else{
            throw new IllegalArgumentException("Invalid order ID.");
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        if (orders.isEmpty()) {
            throw new IllegalStateException("No orders found.");
        }
        return orders;
    }

    public List<Order> getOrdersByCustomerId(int customerId) {

        Customer customer = customerDAO.getById(customerId); //get the specific customer by id
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return new ArrayList<>(); // Return empty list if the customer is not found
        }
        return customer.getOrders();
    }

}