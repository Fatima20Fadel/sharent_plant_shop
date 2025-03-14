package com.proposal.Nature.Heaven.repository;
import com.proposal.Nature.Heaven.model.Favorite;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
    public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
        Optional<Favorite> findByUserAndPlant(User user, Plant plant);
    }

