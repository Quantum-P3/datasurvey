package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioEncuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioEncuesta.class);
        UsuarioEncuesta usuarioEncuesta1 = new UsuarioEncuesta();
        usuarioEncuesta1.setId(1L);
        UsuarioEncuesta usuarioEncuesta2 = new UsuarioEncuesta();
        usuarioEncuesta2.setId(usuarioEncuesta1.getId());
        assertThat(usuarioEncuesta1).isEqualTo(usuarioEncuesta2);
        usuarioEncuesta2.setId(2L);
        assertThat(usuarioEncuesta1).isNotEqualTo(usuarioEncuesta2);
        usuarioEncuesta1.setId(null);
        assertThat(usuarioEncuesta1).isNotEqualTo(usuarioEncuesta2);
    }
}
