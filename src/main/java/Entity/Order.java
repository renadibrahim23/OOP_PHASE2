package Entity;

public class Order {
    private static int idCounter;
    private String paymentMethod;
    private int orderId;
    private int userId;
    private double discount;
    private double tax;
    private double shippingFee;
    private double checkOutTotal;
    private String product;
    private double price;


    public Order(){this.orderId=++idCounter;}

    public Order(String product , double price){
        this.product = product;
        this.price = price;

    }
    public Order(String paymentMethod, double discount, double tax, double shippingFee, int userId) {
        this.paymentMethod = paymentMethod;
        this.discount = discount;
        this.tax = tax;
        this.shippingFee = shippingFee;
        this.userId = userId;
        this.orderId=++idCounter;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId(){ return userId; }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUserId(int userId){this.userId = userId; }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getCheckOutTotal() {
        return checkOutTotal;
    }

    public void setCheckOutTotal(double checkOutTotal) {
        this.checkOutTotal = checkOutTotal;
    }

    public String toString(){
        return "OrderId: "+orderId+" CheckoutTotal: "+checkOutTotal;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Order.idCounter = idCounter;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}