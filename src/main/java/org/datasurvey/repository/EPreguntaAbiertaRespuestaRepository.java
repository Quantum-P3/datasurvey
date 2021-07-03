package org.datasurvey.repository;

import org.datasurvey.domain.EPreguntaAbiertaRespuesta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EPreguntaAbiertaRespuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPreguntaAbiertaRespuestaRepository
    extends JpaRepository<EPreguntaAbiertaRespuesta, Long>, JpaSpecificationExecutor<EPreguntaAbiertaRespuesta> {}
