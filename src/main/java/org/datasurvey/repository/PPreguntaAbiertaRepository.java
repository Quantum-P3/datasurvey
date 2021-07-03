package org.datasurvey.repository;

import org.datasurvey.domain.PPreguntaAbierta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PPreguntaAbierta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPreguntaAbiertaRepository extends JpaRepository<PPreguntaAbierta, Long>, JpaSpecificationExecutor<PPreguntaAbierta> {}
