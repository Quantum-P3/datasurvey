package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EPreguntaAbiertaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EPreguntaAbierta.class);
        EPreguntaAbierta ePreguntaAbierta1 = new EPreguntaAbierta();
        ePreguntaAbierta1.setId(1L);
        EPreguntaAbierta ePreguntaAbierta2 = new EPreguntaAbierta();
        ePreguntaAbierta2.setId(ePreguntaAbierta1.getId());
        assertThat(ePreguntaAbierta1).isEqualTo(ePreguntaAbierta2);
        ePreguntaAbierta2.setId(2L);
        assertThat(ePreguntaAbierta1).isNotEqualTo(ePreguntaAbierta2);
        ePreguntaAbierta1.setId(null);
        assertThat(ePreguntaAbierta1).isNotEqualTo(ePreguntaAbierta2);
    }
}
