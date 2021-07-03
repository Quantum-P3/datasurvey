package org.datasurvey.repository;

import org.datasurvey.domain.PPreguntaCerradaOpcion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PPreguntaCerradaOpcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPreguntaCerradaOpcionRepository
    extends JpaRepository<PPreguntaCerradaOpcion, Long>, JpaSpecificationExecutor<PPreguntaCerradaOpcion> {}
