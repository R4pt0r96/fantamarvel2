package com.deluca.fantamarvel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deluca.fantamarvel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonaggioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personaggio.class);
        Personaggio personaggio1 = new Personaggio();
        personaggio1.setId(1L);
        Personaggio personaggio2 = new Personaggio();
        personaggio2.setId(personaggio1.getId());
        assertThat(personaggio1).isEqualTo(personaggio2);
        personaggio2.setId(2L);
        assertThat(personaggio1).isNotEqualTo(personaggio2);
        personaggio1.setId(null);
        assertThat(personaggio1).isNotEqualTo(personaggio2);
    }
}
