package DAO;

import Database.Database;
import Entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDAO implements GenericDAO<Cart> {

    public List<Cart> getAllCarts() {
        return new ArrayList<>(Database.carts);
    }

    public Cart getById(int id) {
        for (Cart cart : Database.carts) {
            if (cart.getCartId() == id) {
                return cart;
            }
        }
        return null;
    }

    public void delete(int cartId) {
        Database.carts.removeIf(cart -> cart.getCartId() == cartId);
    }

    public void update(Cart cart) {
        for (int i = 0; i < Database.carts.size(); i++) {
            if (Database.carts.get(i).getCartId() == cart.getCartId()) {
                Database.carts.set(i, cart);
            }
        }
    }

    public void add(Cart cart) {
        for (Cart existingCart : Database.carts) {
            if (existingCart.getCartId() == cart.getCartId()) {
                throw new IllegalArgumentException("Cart with ID " + cart.getCartId() + " already exists");
            }
        }
        // Add only if no cart with the same ID is found
        Database.carts.add(cart);
    }

    public static int getDefaultCartId(int userId) {
        for (Cart cart : Database.carts) {
            if (cart.getUserId() == userId) {
                return cart.getCartId();
            }
        }
        // Instead of throwing an exception, return -1 or handle gracefully
        return -1; // Returning -1 to indicate no cart found for the given user
    }

    public Cart findCartByCustomerId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        // Check if a cart already exists for this userId
        for (Cart cart : Database.carts) {
            if (cart.getUserId() == userId) {
                System.out.println("Cart found for userId: " + userId + ", cartId: " + cart.getCartId());
                return cart; // Return the found cart
            }
        }

        // If no cart is found, create a new one
        Cart newCart = new Cart(userId);
        Database.carts.add(newCart); // Add the newly created cart to the list
        System.out.println("New cart created for userId: " + userId + ", cartId: " + newCart.getCartId());
        return newCart; // Return the new cart
    }



}