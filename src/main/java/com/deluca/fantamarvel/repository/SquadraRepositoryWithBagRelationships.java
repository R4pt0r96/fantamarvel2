package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Squadra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SquadraRepositoryWithBagRelationships {
    Optional<Squadra> fetchBagRelationships(Optional<Squadra> squadra);

    List<Squadra> fetchBagRelationships(List<Squadra> squadras);

    Page<Squadra> fetchBagRelationships(Page<Squadra> squadras);
}
