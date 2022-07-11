package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Squadra;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SquadraRepositoryWithBagRelationshipsImpl implements SquadraRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Squadra> fetchBagRelationships(Optional<Squadra> squadra) {
        return squadra.map(this::fetchPersonaggios);
    }

    @Override
    public Page<Squadra> fetchBagRelationships(Page<Squadra> squadras) {
        return new PageImpl<>(fetchBagRelationships(squadras.getContent()), squadras.getPageable(), squadras.getTotalElements());
    }

    @Override
    public List<Squadra> fetchBagRelationships(List<Squadra> squadras) {
        return Optional.of(squadras).map(this::fetchPersonaggios).orElse(Collections.emptyList());
    }

    Squadra fetchPersonaggios(Squadra result) {
        return entityManager
            .createQuery(
                "select squadra from Squadra squadra left join fetch squadra.personaggios where squadra is :squadra",
                Squadra.class
            )
            .setParameter("squadra", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Squadra> fetchPersonaggios(List<Squadra> squadras) {
        return entityManager
            .createQuery(
                "select distinct squadra from Squadra squadra left join fetch squadra.personaggios where squadra in :squadras",
                Squadra.class
            )
            .setParameter("squadras", squadras)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
