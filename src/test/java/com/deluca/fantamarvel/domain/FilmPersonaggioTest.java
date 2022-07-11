package com.deluca.fantamarvel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deluca.fantamarvel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilmPersonaggioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilmPersonaggio.class);
        FilmPersonaggio filmPersonaggio1 = new FilmPersonaggio();
        filmPersonaggio1.setId(1L);
        FilmPersonaggio filmPersonaggio2 = new FilmPersonaggio();
        filmPersonaggio2.setId(filmPersonaggio1.getId());
        assertThat(filmPersonaggio1).isEqualTo(filmPersonaggio2);
        filmPersonaggio2.setId(2L);
        assertThat(filmPersonaggio1).isNotEqualTo(filmPersonaggio2);
        filmPersonaggio1.setId(null);
        assertThat(filmPersonaggio1).isNotEqualTo(filmPersonaggio2);
    }
}
