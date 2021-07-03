package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.PPreguntaCerradaOpcion;
import org.datasurvey.repository.PPreguntaCerradaOpcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PPreguntaCerradaOpcion}.
 */
@Service
@Transactional
public class PPreguntaCerradaOpcionService {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaOpcionService.class);

    private final PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository;

    public PPreguntaCerradaOpcionService(PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository) {
        this.pPreguntaCerradaOpcionRepository = pPreguntaCerradaOpcionRepository;
    }

    /**
     * Save a pPreguntaCerradaOpcion.
     *
     * @param pPreguntaCerradaOpcion the entity to save.
     * @return the persisted entity.
     */
    public PPreguntaCerradaOpcion save(PPreguntaCerradaOpcion pPreguntaCerradaOpcion) {
        log.debug("Request to save PPreguntaCerradaOpcion : {}", pPreguntaCerradaOpcion);
        return pPreguntaCerradaOpcionRepository.save(pPreguntaCerradaOpcion);
    }

    /**
     * Partially update a pPreguntaCerradaOpcion.
     *
     * @param pPreguntaCerradaOpcion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PPreguntaCerradaOpcion> partialUpdate(PPreguntaCerradaOpcion pPreguntaCerradaOpcion) {
        log.debug("Request to partially update PPreguntaCerradaOpcion : {}", pPreguntaCerradaOpcion);

        return pPreguntaCerradaOpcionRepository
            .findById(pPreguntaCerradaOpcion.getId())
            .map(
                existingPPreguntaCerradaOpcion -> {
                    if (pPreguntaCerradaOpcion.getNombre() != null) {
                        existingPPreguntaCerradaOpcion.setNombre(pPreguntaCerradaOpcion.getNombre());
                    }
                    if (pPreguntaCerradaOpcion.getOrden() != null) {
                        existingPPreguntaCerradaOpcion.setOrden(pPreguntaCerradaOpcion.getOrden());
                    }

                    return existingPPreguntaCerradaOpcion;
                }
            )
            .map(pPreguntaCerradaOpcionRepository::save);
    }

    /**
     * Get all the pPreguntaCerradaOpcions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaCerradaOpcion> findAll() {
        log.debug("Request to get all PPreguntaCerradaOpcions");
        return pPreguntaCerradaOpcionRepository.findAll();
    }

    /**
     * Get one pPreguntaCerradaOpcion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PPreguntaCerradaOpcion> findOne(Long id) {
        log.debug("Request to get PPreguntaCerradaOpcion : {}", id);
        return pPreguntaCerradaOpcionRepository.findById(id);
    }

    /**
     * Delete the pPreguntaCerradaOpcion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PPreguntaCerradaOpcion : {}", id);
        pPreguntaCerradaOpcionRepository.deleteById(id);
    }
}
