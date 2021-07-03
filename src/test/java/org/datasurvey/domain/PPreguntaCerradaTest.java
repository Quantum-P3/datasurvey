package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PPreguntaCerradaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PPreguntaCerrada.class);
        PPreguntaCerrada pPreguntaCerrada1 = new PPreguntaCerrada();
        pPreguntaCerrada1.setId(1L);
        PPreguntaCerrada pPreguntaCerrada2 = new PPreguntaCerrada();
        pPreguntaCerrada2.setId(pPreguntaCerrada1.getId());
        assertThat(pPreguntaCerrada1).isEqualTo(pPreguntaCerrada2);
        pPreguntaCerrada2.setId(2L);
        assertThat(pPreguntaCerrada1).isNotEqualTo(pPreguntaCerrada2);
        pPreguntaCerrada1.setId(null);
        assertThat(pPreguntaCerrada1).isNotEqualTo(pPreguntaCerrada2);
    }
}
