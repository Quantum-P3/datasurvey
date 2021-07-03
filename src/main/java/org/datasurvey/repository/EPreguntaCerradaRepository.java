package org.datasurvey.repository;

import org.datasurvey.domain.EPreguntaCerrada;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EPreguntaCerrada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPreguntaCerradaRepository extends JpaRepository<EPreguntaCerrada, Long>, JpaSpecificationExecutor<EPreguntaCerrada> {}
