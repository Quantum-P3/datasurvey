package org.datasurvey.repository;

import org.datasurvey.domain.UsuarioEncuesta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UsuarioEncuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioEncuestaRepository extends JpaRepository<UsuarioEncuesta, Long>, JpaSpecificationExecutor<UsuarioEncuesta> {}
