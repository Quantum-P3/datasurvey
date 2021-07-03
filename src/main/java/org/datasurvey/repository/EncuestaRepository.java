package org.datasurvey.repository;

import org.datasurvey.domain.Encuesta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Encuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long>, JpaSpecificationExecutor<Encuesta> {}
