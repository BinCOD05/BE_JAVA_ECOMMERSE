package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.web.Controller.Request.AddCartRequest;
import vn.web.Controller.Response.CartItemResponse;
import vn.web.Controller.Response.CartResponse;
import vn.web.Converter.CartItemMapper;
import vn.web.Exception.ResourceNotFoundException;
import vn.web.Model.*;
import vn.web.Repository.CartItemRepository;
import vn.web.Repository.CartRepository;
import vn.web.Repository.ProductRepository;
import vn.web.Repository.UserRepository;
import vn.web.Services.CartService;
import vn.web.Util.SecurityUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CartServiceImpl  implements CartService {

    private final CartRepository cartRepository ;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository ;
    private final UserRepository userRepository;
    private  final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public Object addToCart(AddCartRequest req) {

        UserEntity  user = SecurityUtils.getCurrentUser();

        Product product = productRepository.findByIdFullInfo(req.getProductId()).orElseThrow(() -> new ResourceNotFoundException("khong tim thay san pham "));

        Inventory inventory = product.getInventory();
        if(inventory == null ){
            throw new RuntimeException("không có trong kho");
        }

        long stock = inventory.getQuantity() ;

        if(stock < req.getQuantity()){
            throw new RuntimeException("Không còn hàng");
        }

        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() ->{
            Cart newCart = new Cart() ;
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existedProduct = cart.getCartItemSet().stream().filter(p -> p.getProduct().getId().equals(req.getProductId())).findFirst();
        if(existedProduct.isPresent()){
            CartItem existingItem = existedProduct.get();

            long newQuantity = existingItem.getQuantity() + req.getQuantity() ;
            if(newQuantity > stock){
                throw  new RuntimeException("khong du hang be oiii");
            }

            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        }else {
            CartItem cartItem = new CartItem() ;
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setProduct(product);
            cartItemRepository.save(cartItem);
        }
        return null ;
    }

    @Override
    @Transactional
    public CartResponse getCartList() {
            UserEntity user = SecurityUtils.getCurrentUser();
            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng trống"));

            List<CartItemResponse> cartItemResponses = cart.getCartItemSet().stream()
                    .map(cartItemMapper::toDTOResponse)
                    .collect(Collectors.toList());

//            BigDecimal totalPrice = cartItemResponses.stream()
//                    .map(item -> {
//                        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
//                    })
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPrice = cart.getCartItemSet().stream()
                // [QUAN TRỌNG] Chỉ lọc những item nào đang được chọn
                .filter(item -> Boolean.TRUE.equals(item.getSelected()))
                .map(item -> {
                    BigDecimal price = item.getProduct().getPrice();
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            CartResponse cartResponse = new CartResponse();
            cartResponse.setId(cart.getId());
            cartResponse.setCartItemResponses(cartItemResponses);
            cartResponse.setTotalItems((long)cartItemResponses.size()); // size() trả về int/long tùy list
            cartResponse.setTotalPrice(totalPrice);

            return cartResponse;
        }


    @Override
    @Transactional
    public CartResponse updateQuantity(Long quantity, Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow( () -> new ResourceNotFoundException("khong tim thay san pham"));

        Long currentId = SecurityUtils.getCurrentId() ;
        if(!cartItem.getCart().getUser().getId().equals(currentId)){
            throw  new RuntimeException("DUNG LAI NGAYY");
        }

        if(quantity < 0 ){
            cartItemRepository.delete(cartItem);
            return getCartList();
        }

        long stock = cartItem.getProduct().getInventory().getQuantity();

        if(quantity > stock){
            throw  new RuntimeException("khong duoc dau be oiii");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return getCartList();
    }

    @Override
    @Transactional
    public CartResponse selectCartItem(Boolean selected, Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("khong tim thay"));
        Long currentId = SecurityUtils.getCurrentId() ;
        if(!cartItem.getCart().getUser().getId().equals(currentId)){
            throw new RuntimeException("dung lai di");
        }
        cartItem.setSelected(selected);
        cartItemRepository.save(cartItem);
        return getCartList();
    }

    @Override
    @Transactional
    public CartResponse delete(Long itemId) {

        UserEntity user = SecurityUtils.getCurrentUser();

        CartItem cartItem  = cartItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("khong tim thay san pham "));

        if(!cartItem.getCart().getUser().getId().equals(user.getId())){
            throw new RuntimeException("DUNG LAI DIIII");
        }

        cartItemRepository.delete(cartItem);

        return getCartList();
    }


}
