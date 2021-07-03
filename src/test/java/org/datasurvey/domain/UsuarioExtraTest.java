package org.datasurvey.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.datasurvey.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioExtra.class);
        UsuarioExtra usuarioExtra1 = new UsuarioExtra();
        usuarioExtra1.setId(1L);
        UsuarioExtra usuarioExtra2 = new UsuarioExtra();
        usuarioExtra2.setId(usuarioExtra1.getId());
        assertThat(usuarioExtra1).isEqualTo(usuarioExtra2);
        usuarioExtra2.setId(2L);
        assertThat(usuarioExtra1).isNotEqualTo(usuarioExtra2);
        usuarioExtra1.setId(null);
        assertThat(usuarioExtra1).isNotEqualTo(usuarioExtra2);
    }
}
