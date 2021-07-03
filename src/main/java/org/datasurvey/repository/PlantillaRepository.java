package org.datasurvey.repository;

import org.datasurvey.domain.Plantilla;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plantilla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long>, JpaSpecificationExecutor<Plantilla> {}
