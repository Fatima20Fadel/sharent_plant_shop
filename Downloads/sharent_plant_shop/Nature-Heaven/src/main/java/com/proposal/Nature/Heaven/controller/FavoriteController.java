package com.proposal.Nature.Heaven.controller;

import com.proposal.Nature.Heaven.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle/{plantId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long plantId, Principal principal) {
        boolean favorited = favoriteService.toggleFavorite(plantId, principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("favorited", favorited));
    }
}
