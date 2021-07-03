package org.datasurvey.repository;

import org.datasurvey.domain.ParametroAplicacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParametroAplicacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametroAplicacionRepository
    extends JpaRepository<ParametroAplicacion, Long>, JpaSpecificationExecutor<ParametroAplicacion> {}
