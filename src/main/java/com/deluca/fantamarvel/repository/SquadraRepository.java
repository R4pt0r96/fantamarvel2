package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Squadra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Squadra entity.
 */
@Repository
public interface SquadraRepository extends SquadraRepositoryWithBagRelationships, JpaRepository<Squadra, Long> {
    default Optional<Squadra> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Squadra> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Squadra> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct squadra from Squadra squadra left join fetch squadra.film left join fetch squadra.lega left join fetch squadra.userExtended",
        countQuery = "select count(distinct squadra) from Squadra squadra"
    )
    Page<Squadra> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct squadra from Squadra squadra left join fetch squadra.film left join fetch squadra.lega left join fetch squadra.userExtended"
    )
    List<Squadra> findAllWithToOneRelationships();

    @Query(
        "select squadra from Squadra squadra left join fetch squadra.film left join fetch squadra.lega left join fetch squadra.userExtended where squadra.id =:id"
    )
    Optional<Squadra> findOneWithToOneRelationships(@Param("id") Long id);

    // @Query("select s from Squadra s where s.userExtended.id =:idUser and s.film.id=:idFilm")
    // Optional<Squadra> findSquadraByIdFilmAndUser(@Param("idUser") Long idUser, @Param("idFilm") Long idFilm);

    @Query(
        "select distinct squadra from Squadra squadra left join fetch squadra.film left join fetch squadra.lega left join fetch squadra.userExtended where squadra.film.isActive=true"
    )
    List<Squadra> findSquadreOfFilmActive();
}
