package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.EPreguntaAbierta;
import org.datasurvey.repository.EPreguntaAbiertaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EPreguntaAbierta}.
 */
@Service
@Transactional
public class EPreguntaAbiertaService {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaService.class);

    private final EPreguntaAbiertaRepository ePreguntaAbiertaRepository;

    public EPreguntaAbiertaService(EPreguntaAbiertaRepository ePreguntaAbiertaRepository) {
        this.ePreguntaAbiertaRepository = ePreguntaAbiertaRepository;
    }

    /**
     * Save a ePreguntaAbierta.
     *
     * @param ePreguntaAbierta the entity to save.
     * @return the persisted entity.
     */
    public EPreguntaAbierta save(EPreguntaAbierta ePreguntaAbierta) {
        log.debug("Request to save EPreguntaAbierta : {}", ePreguntaAbierta);
        return ePreguntaAbiertaRepository.save(ePreguntaAbierta);
    }

    /**
     * Partially update a ePreguntaAbierta.
     *
     * @param ePreguntaAbierta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EPreguntaAbierta> partialUpdate(EPreguntaAbierta ePreguntaAbierta) {
        log.debug("Request to partially update EPreguntaAbierta : {}", ePreguntaAbierta);

        return ePreguntaAbiertaRepository
            .findById(ePreguntaAbierta.getId())
            .map(
                existingEPreguntaAbierta -> {
                    if (ePreguntaAbierta.getNombre() != null) {
                        existingEPreguntaAbierta.setNombre(ePreguntaAbierta.getNombre());
                    }
                    if (ePreguntaAbierta.getOpcional() != null) {
                        existingEPreguntaAbierta.setOpcional(ePreguntaAbierta.getOpcional());
                    }
                    if (ePreguntaAbierta.getOrden() != null) {
                        existingEPreguntaAbierta.setOrden(ePreguntaAbierta.getOrden());
                    }

                    return existingEPreguntaAbierta;
                }
            )
            .map(ePreguntaAbiertaRepository::save);
    }

    /**
     * Get all the ePreguntaAbiertas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaAbierta> findAll() {
        log.debug("Request to get all EPreguntaAbiertas");
        return ePreguntaAbiertaRepository.findAll();
    }

    /**
     * Get one ePreguntaAbierta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EPreguntaAbierta> findOne(Long id) {
        log.debug("Request to get EPreguntaAbierta : {}", id);
        return ePreguntaAbiertaRepository.findById(id);
    }

    /**
     * Delete the ePreguntaAbierta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EPreguntaAbierta : {}", id);
        ePreguntaAbiertaRepository.deleteById(id);
    }
}
