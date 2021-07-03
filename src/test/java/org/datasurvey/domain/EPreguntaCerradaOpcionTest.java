package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EPreguntaCerradaOpcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EPreguntaCerradaOpcion.class);
        EPreguntaCerradaOpcion ePreguntaCerradaOpcion1 = new EPreguntaCerradaOpcion();
        ePreguntaCerradaOpcion1.setId(1L);
        EPreguntaCerradaOpcion ePreguntaCerradaOpcion2 = new EPreguntaCerradaOpcion();
        ePreguntaCerradaOpcion2.setId(ePreguntaCerradaOpcion1.getId());
        assertThat(ePreguntaCerradaOpcion1).isEqualTo(ePreguntaCerradaOpcion2);
        ePreguntaCerradaOpcion2.setId(2L);
        assertThat(ePreguntaCerradaOpcion1).isNotEqualTo(ePreguntaCerradaOpcion2);
        ePreguntaCerradaOpcion1.setId(null);
        assertThat(ePreguntaCerradaOpcion1).isNotEqualTo(ePreguntaCerradaOpcion2);
    }
}
