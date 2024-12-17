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
        for (Cart ExistingCart : Database.carts) { // check if the cart exists
            if (ExistingCart.getCartId() == cart.getCartId()) {
                throw new IllegalArgumentException("cart with ID " + cart.getCartId() + " already exists");
            } else {
                Database.carts.add(cart);
            }
        }
    }

    public int getDefaultCartId(int userId) {
        for (Cart cart : Database.carts) { // Assuming `carts` is a list of all carts
            if (cart.getUserId() == userId) { // Filter carts by `userId`
                return cart.getCartId();
            }
        }
        throw new IllegalStateException("No cart found for user ID: " + userId);
    }


}