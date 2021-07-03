package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EPreguntaCerradaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EPreguntaCerrada.class);
        EPreguntaCerrada ePreguntaCerrada1 = new EPreguntaCerrada();
        ePreguntaCerrada1.setId(1L);
        EPreguntaCerrada ePreguntaCerrada2 = new EPreguntaCerrada();
        ePreguntaCerrada2.setId(ePreguntaCerrada1.getId());
        assertThat(ePreguntaCerrada1).isEqualTo(ePreguntaCerrada2);
        ePreguntaCerrada2.setId(2L);
        assertThat(ePreguntaCerrada1).isNotEqualTo(ePreguntaCerrada2);
        ePreguntaCerrada1.setId(null);
        assertThat(ePreguntaCerrada1).isNotEqualTo(ePreguntaCerrada2);
    }
}
