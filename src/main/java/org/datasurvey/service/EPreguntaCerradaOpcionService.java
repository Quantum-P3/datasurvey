package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.EPreguntaCerradaOpcion;
import org.datasurvey.repository.EPreguntaCerradaOpcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EPreguntaCerradaOpcion}.
 */
@Service
@Transactional
public class EPreguntaCerradaOpcionService {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaOpcionService.class);

    private final EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository;

    public EPreguntaCerradaOpcionService(EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository) {
        this.ePreguntaCerradaOpcionRepository = ePreguntaCerradaOpcionRepository;
    }

    /**
     * Save a ePreguntaCerradaOpcion.
     *
     * @param ePreguntaCerradaOpcion the entity to save.
     * @return the persisted entity.
     */
    public EPreguntaCerradaOpcion save(EPreguntaCerradaOpcion ePreguntaCerradaOpcion) {
        log.debug("Request to save EPreguntaCerradaOpcion : {}", ePreguntaCerradaOpcion);
        return ePreguntaCerradaOpcionRepository.save(ePreguntaCerradaOpcion);
    }

    /**
     * Partially update a ePreguntaCerradaOpcion.
     *
     * @param ePreguntaCerradaOpcion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EPreguntaCerradaOpcion> partialUpdate(EPreguntaCerradaOpcion ePreguntaCerradaOpcion) {
        log.debug("Request to partially update EPreguntaCerradaOpcion : {}", ePreguntaCerradaOpcion);

        return ePreguntaCerradaOpcionRepository
            .findById(ePreguntaCerradaOpcion.getId())
            .map(
                existingEPreguntaCerradaOpcion -> {
                    if (ePreguntaCerradaOpcion.getNombre() != null) {
                        existingEPreguntaCerradaOpcion.setNombre(ePreguntaCerradaOpcion.getNombre());
                    }
                    if (ePreguntaCerradaOpcion.getOrden() != null) {
                        existingEPreguntaCerradaOpcion.setOrden(ePreguntaCerradaOpcion.getOrden());
                    }
                    if (ePreguntaCerradaOpcion.getCantidad() != null) {
                        existingEPreguntaCerradaOpcion.setCantidad(ePreguntaCerradaOpcion.getCantidad());
                    }

                    return existingEPreguntaCerradaOpcion;
                }
            )
            .map(ePreguntaCerradaOpcionRepository::save);
    }

    /**
     * Get all the ePreguntaCerradaOpcions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaCerradaOpcion> findAll() {
        log.debug("Request to get all EPreguntaCerradaOpcions");
        return ePreguntaCerradaOpcionRepository.findAll();
    }

    /**
     * Get one ePreguntaCerradaOpcion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EPreguntaCerradaOpcion> findOne(Long id) {
        log.debug("Request to get EPreguntaCerradaOpcion : {}", id);
        return ePreguntaCerradaOpcionRepository.findById(id);
    }

    /**
     * Delete the ePreguntaCerradaOpcion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EPreguntaCerradaOpcion : {}", id);
        ePreguntaCerradaOpcionRepository.deleteById(id);
    }
}
