package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.FilmPersonaggio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FilmPersonaggio entity.
 */
@Repository
public interface FilmPersonaggioRepository extends JpaRepository<FilmPersonaggio, Long> {
    default Optional<FilmPersonaggio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FilmPersonaggio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FilmPersonaggio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct filmPersonaggio from FilmPersonaggio filmPersonaggio left join fetch filmPersonaggio.film left join fetch filmPersonaggio.personaggio",
        countQuery = "select count(distinct filmPersonaggio) from FilmPersonaggio filmPersonaggio"
    )
    Page<FilmPersonaggio> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct filmPersonaggio from FilmPersonaggio filmPersonaggio left join fetch filmPersonaggio.film left join fetch filmPersonaggio.personaggio"
    )
    List<FilmPersonaggio> findAllWithToOneRelationships();

    @Query(
        "select filmPersonaggio from FilmPersonaggio filmPersonaggio left join fetch filmPersonaggio.film left join fetch filmPersonaggio.personaggio where filmPersonaggio.id =:id"
    )
    Optional<FilmPersonaggio> findOneWithToOneRelationships(@Param("id") Long id);
}
