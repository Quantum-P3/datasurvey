package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantillaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plantilla.class);
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setId(1L);
        Plantilla plantilla2 = new Plantilla();
        plantilla2.setId(plantilla1.getId());
        assertThat(plantilla1).isEqualTo(plantilla2);
        plantilla2.setId(2L);
        assertThat(plantilla1).isNotEqualTo(plantilla2);
        plantilla1.setId(null);
        assertThat(plantilla1).isNotEqualTo(plantilla2);
    }
}
