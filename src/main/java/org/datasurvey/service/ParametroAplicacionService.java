package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.ParametroAplicacion;
import org.datasurvey.repository.ParametroAplicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParametroAplicacion}.
 */
@Service
@Transactional
public class ParametroAplicacionService {

    private final Logger log = LoggerFactory.getLogger(ParametroAplicacionService.class);

    private final ParametroAplicacionRepository parametroAplicacionRepository;

    public ParametroAplicacionService(ParametroAplicacionRepository parametroAplicacionRepository) {
        this.parametroAplicacionRepository = parametroAplicacionRepository;
    }

    /**
     * Save a parametroAplicacion.
     *
     * @param parametroAplicacion the entity to save.
     * @return the persisted entity.
     */
    public ParametroAplicacion save(ParametroAplicacion parametroAplicacion) {
        log.debug("Request to save ParametroAplicacion : {}", parametroAplicacion);
        return parametroAplicacionRepository.save(parametroAplicacion);
    }

    /**
     * Partially update a parametroAplicacion.
     *
     * @param parametroAplicacion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParametroAplicacion> partialUpdate(ParametroAplicacion parametroAplicacion) {
        log.debug("Request to partially update ParametroAplicacion : {}", parametroAplicacion);

        return parametroAplicacionRepository
            .findById(parametroAplicacion.getId())
            .map(
                existingParametroAplicacion -> {
                    if (parametroAplicacion.getMaxDiasEncuesta() != null) {
                        existingParametroAplicacion.setMaxDiasEncuesta(parametroAplicacion.getMaxDiasEncuesta());
                    }
                    if (parametroAplicacion.getMinDiasEncuesta() != null) {
                        existingParametroAplicacion.setMinDiasEncuesta(parametroAplicacion.getMinDiasEncuesta());
                    }
                    if (parametroAplicacion.getMaxCantidadPreguntas() != null) {
                        existingParametroAplicacion.setMaxCantidadPreguntas(parametroAplicacion.getMaxCantidadPreguntas());
                    }
                    if (parametroAplicacion.getMinCantidadPreguntas() != null) {
                        existingParametroAplicacion.setMinCantidadPreguntas(parametroAplicacion.getMinCantidadPreguntas());
                    }

                    return existingParametroAplicacion;
                }
            )
            .map(parametroAplicacionRepository::save);
    }

    /**
     * Get all the parametroAplicacions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParametroAplicacion> findAll() {
        log.debug("Request to get all ParametroAplicacions");
        return parametroAplicacionRepository.findAll();
    }

    /**
     * Get one parametroAplicacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParametroAplicacion> findOne(Long id) {
        log.debug("Request to get ParametroAplicacion : {}", id);
        return parametroAplicacionRepository.findById(id);
    }

    /**
     * Delete the parametroAplicacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParametroAplicacion : {}", id);
        parametroAplicacionRepository.deleteById(id);
    }
}
