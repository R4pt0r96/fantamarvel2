package com.deluca.fantamarvel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deluca.fantamarvel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SquadraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Squadra.class);
        Squadra squadra1 = new Squadra();
        squadra1.setId(1L);
        Squadra squadra2 = new Squadra();
        squadra2.setId(squadra1.getId());
        assertThat(squadra1).isEqualTo(squadra2);
        squadra2.setId(2L);
        assertThat(squadra1).isNotEqualTo(squadra2);
        squadra1.setId(null);
        assertThat(squadra1).isNotEqualTo(squadra2);
    }
}
