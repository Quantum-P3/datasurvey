package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametroAplicacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametroAplicacion.class);
        ParametroAplicacion parametroAplicacion1 = new ParametroAplicacion();
        parametroAplicacion1.setId(1L);
        ParametroAplicacion parametroAplicacion2 = new ParametroAplicacion();
        parametroAplicacion2.setId(parametroAplicacion1.getId());
        assertThat(parametroAplicacion1).isEqualTo(parametroAplicacion2);
        parametroAplicacion2.setId(2L);
        assertThat(parametroAplicacion1).isNotEqualTo(parametroAplicacion2);
        parametroAplicacion1.setId(null);
        assertThat(parametroAplicacion1).isNotEqualTo(parametroAplicacion2);
    }
}
