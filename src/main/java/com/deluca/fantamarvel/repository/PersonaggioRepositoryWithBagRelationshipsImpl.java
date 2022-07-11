package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Personaggio;
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
public class PersonaggioRepositoryWithBagRelationshipsImpl implements PersonaggioRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Personaggio> fetchBagRelationships(Optional<Personaggio> personaggio) {
        return personaggio.map(this::fetchBonusmaluses);
    }

    @Override
    public Page<Personaggio> fetchBagRelationships(Page<Personaggio> personaggios) {
        return new PageImpl<>(
            fetchBagRelationships(personaggios.getContent()),
            personaggios.getPageable(),
            personaggios.getTotalElements()
        );
    }

    @Override
    public List<Personaggio> fetchBagRelationships(List<Personaggio> personaggios) {
        return Optional.of(personaggios).map(this::fetchBonusmaluses).orElse(Collections.emptyList());
    }

    Personaggio fetchBonusmaluses(Personaggio result) {
        return entityManager
            .createQuery(
                "select personaggio from Personaggio personaggio left join fetch personaggio.bonusmaluses where personaggio is :personaggio",
                Personaggio.class
            )
            .setParameter("personaggio", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Personaggio> fetchBonusmaluses(List<Personaggio> personaggios) {
        return entityManager
            .createQuery(
                "select distinct personaggio from Personaggio personaggio left join fetch personaggio.bonusmaluses where personaggio in :personaggios",
                Personaggio.class
            )
            .setParameter("personaggios", personaggios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
