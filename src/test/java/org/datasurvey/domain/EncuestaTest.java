package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EncuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Encuesta.class);
        Encuesta encuesta1 = new Encuesta();
        encuesta1.setId(1L);
        Encuesta encuesta2 = new Encuesta();
        encuesta2.setId(encuesta1.getId());
        assertThat(encuesta1).isEqualTo(encuesta2);
        encuesta2.setId(2L);
        assertThat(encuesta1).isNotEqualTo(encuesta2);
        encuesta1.setId(null);
        assertThat(encuesta1).isNotEqualTo(encuesta2);
    }
}
