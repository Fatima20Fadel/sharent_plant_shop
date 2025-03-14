package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.User;
import java.util.List;
import com.proposal.Nature.Heaven.model.Wishlist;
import com.proposal.Nature.Heaven.repository.PlantRepository;
import com.proposal.Nature.Heaven.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, PlantRepository plantRepository) {
        this.wishlistRepository = wishlistRepository;
        this.plantRepository = plantRepository;
    }

    public Wishlist createWishlist(String name, User user) {
        Wishlist wishlist = new Wishlist(name, user);
        return wishlistRepository.save(wishlist);
    }

    public void addPlantToWishlist(Long wishlistId, Long productId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        Plant plant = plantRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        wishlist.getPlants().add(plant);
        wishlistRepository.save(wishlist);
    }

    public void removePlantFromWishlist(Long wishlistId, Long productId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        Plant product = plantRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        wishlist.getPlants().remove(product);
        wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getUserWishlists(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }
}
