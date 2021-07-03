package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PPreguntaAbiertaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PPreguntaAbierta.class);
        PPreguntaAbierta pPreguntaAbierta1 = new PPreguntaAbierta();
        pPreguntaAbierta1.setId(1L);
        PPreguntaAbierta pPreguntaAbierta2 = new PPreguntaAbierta();
        pPreguntaAbierta2.setId(pPreguntaAbierta1.getId());
        assertThat(pPreguntaAbierta1).isEqualTo(pPreguntaAbierta2);
        pPreguntaAbierta2.setId(2L);
        assertThat(pPreguntaAbierta1).isNotEqualTo(pPreguntaAbierta2);
        pPreguntaAbierta1.setId(null);
        assertThat(pPreguntaAbierta1).isNotEqualTo(pPreguntaAbierta2);
    }
}
