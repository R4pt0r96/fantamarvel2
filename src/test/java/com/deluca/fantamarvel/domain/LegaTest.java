package com.deluca.fantamarvel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deluca.fantamarvel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LegaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lega.class);
        Lega lega1 = new Lega();
        lega1.setId(1L);
        Lega lega2 = new Lega();
        lega2.setId(lega1.getId());
        assertThat(lega1).isEqualTo(lega2);
        lega2.setId(2L);
        assertThat(lega1).isNotEqualTo(lega2);
        lega1.setId(null);
        assertThat(lega1).isNotEqualTo(lega2);
    }
}
