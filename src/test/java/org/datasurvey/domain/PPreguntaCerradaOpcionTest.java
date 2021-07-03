package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PPreguntaCerradaOpcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PPreguntaCerradaOpcion.class);
        PPreguntaCerradaOpcion pPreguntaCerradaOpcion1 = new PPreguntaCerradaOpcion();
        pPreguntaCerradaOpcion1.setId(1L);
        PPreguntaCerradaOpcion pPreguntaCerradaOpcion2 = new PPreguntaCerradaOpcion();
        pPreguntaCerradaOpcion2.setId(pPreguntaCerradaOpcion1.getId());
        assertThat(pPreguntaCerradaOpcion1).isEqualTo(pPreguntaCerradaOpcion2);
        pPreguntaCerradaOpcion2.setId(2L);
        assertThat(pPreguntaCerradaOpcion1).isNotEqualTo(pPreguntaCerradaOpcion2);
        pPreguntaCerradaOpcion1.setId(null);
        assertThat(pPreguntaCerradaOpcion1).isNotEqualTo(pPreguntaCerradaOpcion2);
    }
}
