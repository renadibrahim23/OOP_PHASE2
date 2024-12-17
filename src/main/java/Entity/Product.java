package Entity;

public class Product {
    private static int idCounter;
    private String name;
    private double price;
    private Category category;
    private int id;
    private int quantity;
    public Product(){this.id=++idCounter;}
    public Product(String name,double price,Category category,int quantity){
        this.name=name;
        this.price=price;
        this.category=category;
        this.id=++idCounter;
        this.quantity=quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Product.idCounter = idCounter;
    }
}