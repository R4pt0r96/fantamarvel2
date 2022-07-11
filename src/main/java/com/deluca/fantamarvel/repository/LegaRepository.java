package com.deluca.fantamarvel.repository;

import com.deluca.fantamarvel.domain.Lega;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LegaRepository extends JpaRepository<Lega, Long> {}
