package com.proposal.Nature.Heaven.controller;


import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.model.Wishlist;
import com.proposal.Nature.Heaven.service.UserService;
import com.proposal.Nature.Heaven.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;

    @Autowired
    public WishlistController(WishlistService wishlistService, UserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }
    @GetMapping("/wishlist")
    public String showWishlist(Model model, @AuthenticationPrincipal User currentUser) {
        // Fetch wishlist items for the logged-in user
        List<Wishlist> wishlist = wishlistService.getUserWishlists(currentUser.getId());
        model.addAttribute("wishlistPlants", wishlist);
        return "wishlist";
    }

    //  Create a new wishlist for a user
    @PostMapping("/create")
    public ResponseEntity<Wishlist> createWishlist(@RequestParam String name, @RequestParam Long userId) {
        User user = userService.getUserById(userId);  // Fetch user by ID
        Wishlist wishlist = wishlistService.createWishlist(name, user);
        return ResponseEntity.ok(wishlist);
    }

    //  Add a plant to a wishlist
    @PostMapping("/wishlist/{wishlistId}/add")
    public ResponseEntity<String> addPlantToWishlist(@PathVariable Long wishlistId, @RequestParam Long plantId) {
        wishlistService.addPlantToWishlist(wishlistId, plantId);
        return ResponseEntity.ok("Plant added to wishlist");
    }

    //  Remove a plant from a wishlist
    @DeleteMapping("/{wishlistId}/remove")
    public ResponseEntity<String> removePlantFromWishlist(@PathVariable Long wishlistId, @RequestParam Long plantId) {
        wishlistService.removePlantFromWishlist(wishlistId, plantId);
        return ResponseEntity.ok("Plant removed from wishlist");
    }

    //  Get all wishlists for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getUserWishlists(@PathVariable Long userId) {
        List<Wishlist> wishlists = wishlistService.getUserWishlists(userId);
        return ResponseEntity.ok(wishlists);
    }
}
