package DAO;
import Database.Database;
import Entity.Category;
import Entity.Product;

import java.util.List;


public class CategoryDAO implements GenericDAO<Category> {


    public  Category getById(int id ){
        for(Category category:Database.categories){
            if(category.getId()==id){
                return category;
            }

        }
        return null;
    }
    public void add(Category category){
        Database.categories.add(category);
    }
    public Category createCategory(String name,List<Product> products ){
        Category category=new Category(name,products);
        return category;
    }

    public void update(Category updatedCategory) {
        for (Category category : Database.categories) {
            if (category.getId() == updatedCategory.getId()) {
                int index = Database.categories.indexOf(category);
                Database.categories.set(index, updatedCategory);

            }

        }

    }
    public void delete(int id){
        for(Category category:Database.categories){
            if(category.getId()==id){
                Database.categories.remove(category);
            }
        }
    }

    public List<Category> getAllCategories(){
        return Database.categories;
    }
    public void getCategoriesInfo(List<Category> categories) {
        for (Category category : categories) {
            System.out.println("Category ID: " + category.getId() +
                    ", Name: " + category.getName() +
                    ",Products in category: "+ category.getProductsInCategory());
        }

    }
    public boolean isValidCategory(Category category){
        return category != null && category.getId() >= 0 &&
                category.getName() != null && category.getProductsInCategory()!= null;
    }

    public Category parseCategory(String input) {
        // Optional additional parsing/validation logic
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        return new Category(input.trim());
    }




}