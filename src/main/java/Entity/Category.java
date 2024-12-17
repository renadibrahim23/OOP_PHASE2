package Entity;
import java.util.List;

public class Category {

    private static int idCounter = 0;
    String name;
    List<Product> productsInCategory;
    int id;

    public Category(){}
    public Category(String name,List<Product> productsInCategory){
        this.name=name;
        this.productsInCategory=productsInCategory;
        this.id=++idCounter;
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

    public List<Product> getProductsInCategory() {
        return productsInCategory;
    }

    public void setProductsInCategory(List<Product> productsInCategory) {
        this.productsInCategory = productsInCategory;
    }

    public static void setIdCounter(int idCounter) {
        Category.idCounter = idCounter;
    }

    public static int getIdCounter() {
        return idCounter;
    }
}