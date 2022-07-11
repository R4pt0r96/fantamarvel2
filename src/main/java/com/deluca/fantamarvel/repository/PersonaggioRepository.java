package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Personaggio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personaggio entity.
 */
@Repository
public interface PersonaggioRepository extends PersonaggioRepositoryWithBagRelationships, JpaRepository<Personaggio, Long> {
    default Optional<Personaggio> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Personaggio> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Personaggio> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
