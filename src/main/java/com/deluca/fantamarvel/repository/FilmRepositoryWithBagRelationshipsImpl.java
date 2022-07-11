package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Film;
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
public class FilmRepositoryWithBagRelationshipsImpl implements FilmRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Film> fetchBagRelationships(Optional<Film> film) {
        return film.map(this::fetchUserExtendeds);
    }

    @Override
    public Page<Film> fetchBagRelationships(Page<Film> films) {
        return new PageImpl<>(fetchBagRelationships(films.getContent()), films.getPageable(), films.getTotalElements());
    }

    @Override
    public List<Film> fetchBagRelationships(List<Film> films) {
        return Optional.of(films).map(this::fetchUserExtendeds).orElse(Collections.emptyList());
    }

    Film fetchUserExtendeds(Film result) {
        return entityManager
            .createQuery("select film from Film film left join fetch film.userExtendeds where film is :film", Film.class)
            .setParameter("film", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Film> fetchUserExtendeds(List<Film> films) {
        return entityManager
            .createQuery("select distinct film from Film film left join fetch film.userExtendeds where film in :films", Film.class)
            .setParameter("films", films)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
