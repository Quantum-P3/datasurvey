package org.datasurvey.repository;

import org.datasurvey.domain.EPreguntaCerradaOpcion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EPreguntaCerradaOpcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPreguntaCerradaOpcionRepository
    extends JpaRepository<EPreguntaCerradaOpcion, Long>, JpaSpecificationExecutor<EPreguntaCerradaOpcion> {}
