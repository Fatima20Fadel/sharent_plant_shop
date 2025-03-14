package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.model.Favorite;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserService userService;

    public boolean toggleFavorite(Long plantId, String username) {
        User user = userService.findByUsername(username);
        Optional<Favorite> favorite = favoriteRepository.findByUserAndPlant(user, new Plant(plantId));

        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return false;
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setPlant(new Plant(plantId));
            favoriteRepository.save(newFavorite);
            return true;
        }
    }
}
