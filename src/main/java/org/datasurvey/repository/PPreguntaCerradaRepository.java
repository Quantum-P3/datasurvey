package org.datasurvey.repository;

import org.datasurvey.domain.PPreguntaCerrada;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PPreguntaCerrada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPreguntaCerradaRepository extends JpaRepository<PPreguntaCerrada, Long>, JpaSpecificationExecutor<PPreguntaCerrada> {}
