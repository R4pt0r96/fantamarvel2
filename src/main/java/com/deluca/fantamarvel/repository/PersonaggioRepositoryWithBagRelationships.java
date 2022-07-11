package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Personaggio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PersonaggioRepositoryWithBagRelationships {
    Optional<Personaggio> fetchBagRelationships(Optional<Personaggio> personaggio);

    List<Personaggio> fetchBagRelationships(List<Personaggio> personaggios);

    Page<Personaggio> fetchBagRelationships(Page<Personaggio> personaggios);
}
