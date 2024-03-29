package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Film;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FilmRepositoryWithBagRelationships {
    Optional<Film> fetchBagRelationships(Optional<Film> film);

    List<Film> fetchBagRelationships(List<Film> films);

    Page<Film> fetchBagRelationships(Page<Film> films);
}
