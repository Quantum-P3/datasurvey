package org.datasurvey.repository;

import org.datasurvey.domain.EPreguntaAbierta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EPreguntaAbierta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPreguntaAbiertaRepository extends JpaRepository<EPreguntaAbierta, Long>, JpaSpecificationExecutor<EPreguntaAbierta> {}
