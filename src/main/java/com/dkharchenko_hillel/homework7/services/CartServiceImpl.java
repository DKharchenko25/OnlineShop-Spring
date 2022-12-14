package com.dkharchenko_hillel.homework7.services;

import com.dkharchenko_hillel.homework7.NotFoundException;
import com.dkharchenko_hillel.homework7.models.Cart;
import com.dkharchenko_hillel.homework7.models.Person;
import com.dkharchenko_hillel.homework7.models.Product;
import com.dkharchenko_hillel.homework7.reposiroties.CartRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final PersonService personService;
    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, PersonService personService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.personService = personService;
        this.productService = productService;
    }

    @Override
    public void addCartByPersonUsername(@NonNull String username) {
        Cart cart = new Cart(personService.getPersonByUsername(username));
        personService.getPersonByUsername(username).getCarts().add(cart);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartById(@NonNull Long id) {
        if (cartRepository.findById(id).isPresent()) {
            Cart cart = cartRepository.findById(id).get();
            personService.getPersonById(cart.getPerson().getId()).getCarts().remove(cart);
            cartRepository.deleteById(id);
        } else {
            log.error(getExceptionMessage(id));
            throw new NotFoundException(getExceptionMessage(id));
        }
    }

    @Override
    public Cart getCartById(@NonNull Long id) {
        if (cartRepository.findById(id).isPresent()) {
            return cartRepository.findById(id).get();
        } else {
            log.error(getExceptionMessage(id));
            throw new NotFoundException(getExceptionMessage(id));
        }
    }

    @Override
    public List<Cart> getAllCarts() {
        return (List<Cart>) cartRepository.findAll();
    }

    @Override
    public List<Cart> getAllPersonCarts(@NonNull String username) {
        Person person = personService.getPersonByUsername(username);
        return getAllCarts().stream().filter(cart -> cart.getPerson().getUsername().equals(person.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public void addProductByProductId(@NonNull Long cartId, @NonNull Long productId) {
        if (cartRepository.findById(cartId).isPresent()) {
            Cart cart = cartRepository.findById(cartId).get();
            Product product = productService.getProductById(productId);
            checkContainsProduct(cart, product);
            cart.getProducts().add(product);
            increaseAmountAndSum(cart, product);
        } else {
            log.error(getExceptionMessage(cartId));
            throw new NotFoundException(getExceptionMessage(cartId));
        }
    }

    @Override
    public void removeProductByProductId(@NonNull Long cartId, @NonNull Long productId) {
        if (cartRepository.findById(cartId).isPresent()) {
            Cart cart = cartRepository.findById(cartId).get();
            Product product = productService.getProductById(productId);
            checkNotContainsProduct(cart, product);
            decreaseAmountAndSum(cart, product);
            cart.getProducts().remove(product);
        } else {
            log.error(getExceptionMessage(cartId));
            throw new NotFoundException(getExceptionMessage(cartId));
        }
    }

    @Override
    public void removeAllProductsById(@NonNull Long id) {
        if (cartRepository.findById(id).isPresent()) {
            Cart cart = cartRepository.findById(id).get();
            cart.getProducts().clear();
            cart.setSum(new BigDecimal("0.00"));
            cart.setAmountOfProducts(0);
        } else {
            log.error(getExceptionMessage(id));
            throw new NotFoundException(getExceptionMessage(id));
        }
    }

    private void checkNotContainsProduct(Cart cart, Product product) {
        if (!cart.getProducts().contains(product)) {
            log.error("Cart don't contains product with ID #" + product.getId());
            throw new NotFoundException("Cart don't contains product with ID #" + product.getId());
        }
    }

    private void checkContainsProduct(Cart cart, Product product) {
        if (cart.getProducts().contains(product)) {
            log.error("Cart is already contains product with ID #" + product.getId());
            throw new NotFoundException("Cart is already contains product with ID #" + product.getId());
        }
    }

    private void increaseAmountAndSum(Cart cart, Product product) {
        cart.setAmountOfProducts(cart.getAmountOfProducts() + 1);
        cart.setSum(cart.getSum().add(product.getPrice()));
    }

    private void decreaseAmountAndSum(Cart cart, Product product) {
        if (cart.getSum().compareTo(new BigDecimal("0.00")) != 0
                && cart.getAmountOfProducts().compareTo(0) != 0) {
            cart.setAmountOfProducts(cart.getAmountOfProducts() - 1);
            cart.setSum(cart.getSum().subtract(product.getPrice()));
        } else {
            cart.setSum(new BigDecimal("0.00"));
            cart.setAmountOfProducts(0);
        }
    }

    private static String getExceptionMessage(Long id) {
        return "Cart with ID #" + id + " is not found";
    }
}
