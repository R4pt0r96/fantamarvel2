package com.deluca.fantamarvel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deluca.fantamarvel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BonusMalusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonusMalus.class);
        BonusMalus bonusMalus1 = new BonusMalus();
        bonusMalus1.setId(1L);
        BonusMalus bonusMalus2 = new BonusMalus();
        bonusMalus2.setId(bonusMalus1.getId());
        assertThat(bonusMalus1).isEqualTo(bonusMalus2);
        bonusMalus2.setId(2L);
        assertThat(bonusMalus1).isNotEqualTo(bonusMalus2);
        bonusMalus1.setId(null);
        assertThat(bonusMalus1).isNotEqualTo(bonusMalus2);
    }
}
