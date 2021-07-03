package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EPreguntaAbiertaRespuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EPreguntaAbiertaRespuesta.class);
        EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta1 = new EPreguntaAbiertaRespuesta();
        ePreguntaAbiertaRespuesta1.setId(1L);
        EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta2 = new EPreguntaAbiertaRespuesta();
        ePreguntaAbiertaRespuesta2.setId(ePreguntaAbiertaRespuesta1.getId());
        assertThat(ePreguntaAbiertaRespuesta1).isEqualTo(ePreguntaAbiertaRespuesta2);
        ePreguntaAbiertaRespuesta2.setId(2L);
        assertThat(ePreguntaAbiertaRespuesta1).isNotEqualTo(ePreguntaAbiertaRespuesta2);
        ePreguntaAbiertaRespuesta1.setId(null);
        assertThat(ePreguntaAbiertaRespuesta1).isNotEqualTo(ePreguntaAbiertaRespuesta2);
    }
}
