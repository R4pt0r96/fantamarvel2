package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.BonusMalus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BonusMalus entity.
 */
@Repository
public interface BonusMalusRepository extends JpaRepository<BonusMalus, Long> {
    default Optional<BonusMalus> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BonusMalus> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BonusMalus> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct bonusMalus from BonusMalus bonusMalus left join fetch bonusMalus.film",
        countQuery = "select count(distinct bonusMalus) from BonusMalus bonusMalus"
    )
    Page<BonusMalus> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct bonusMalus from BonusMalus bonusMalus left join fetch bonusMalus.film")
    List<BonusMalus> findAllWithToOneRelationships();

    @Query("select bonusMalus from BonusMalus bonusMalus left join fetch bonusMalus.film where bonusMalus.id =:id")
    Optional<BonusMalus> findOneWithToOneRelationships(@Param("id") Long id);
}
