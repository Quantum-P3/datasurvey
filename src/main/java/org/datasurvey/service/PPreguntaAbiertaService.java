package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.PPreguntaAbierta;
import org.datasurvey.repository.PPreguntaAbiertaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PPreguntaAbierta}.
 */
@Service
@Transactional
public class PPreguntaAbiertaService {

    private final Logger log = LoggerFactory.getLogger(PPreguntaAbiertaService.class);

    private final PPreguntaAbiertaRepository pPreguntaAbiertaRepository;

    public PPreguntaAbiertaService(PPreguntaAbiertaRepository pPreguntaAbiertaRepository) {
        this.pPreguntaAbiertaRepository = pPreguntaAbiertaRepository;
    }

    /**
     * Save a pPreguntaAbierta.
     *
     * @param pPreguntaAbierta the entity to save.
     * @return the persisted entity.
     */
    public PPreguntaAbierta save(PPreguntaAbierta pPreguntaAbierta) {
        log.debug("Request to save PPreguntaAbierta : {}", pPreguntaAbierta);
        return pPreguntaAbiertaRepository.save(pPreguntaAbierta);
    }

    /**
     * Partially update a pPreguntaAbierta.
     *
     * @param pPreguntaAbierta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PPreguntaAbierta> partialUpdate(PPreguntaAbierta pPreguntaAbierta) {
        log.debug("Request to partially update PPreguntaAbierta : {}", pPreguntaAbierta);

        return pPreguntaAbiertaRepository
            .findById(pPreguntaAbierta.getId())
            .map(
                existingPPreguntaAbierta -> {
                    if (pPreguntaAbierta.getNombre() != null) {
                        existingPPreguntaAbierta.setNombre(pPreguntaAbierta.getNombre());
                    }
                    if (pPreguntaAbierta.getOpcional() != null) {
                        existingPPreguntaAbierta.setOpcional(pPreguntaAbierta.getOpcional());
                    }
                    if (pPreguntaAbierta.getOrden() != null) {
                        existingPPreguntaAbierta.setOrden(pPreguntaAbierta.getOrden());
                    }

                    return existingPPreguntaAbierta;
                }
            )
            .map(pPreguntaAbiertaRepository::save);
    }

    /**
     * Get all the pPreguntaAbiertas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaAbierta> findAll() {
        log.debug("Request to get all PPreguntaAbiertas");
        return pPreguntaAbiertaRepository.findAll();
    }

    /**
     * Get one pPreguntaAbierta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PPreguntaAbierta> findOne(Long id) {
        log.debug("Request to get PPreguntaAbierta : {}", id);
        return pPreguntaAbiertaRepository.findById(id);
    }

    /**
     * Delete the pPreguntaAbierta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PPreguntaAbierta : {}", id);
        pPreguntaAbiertaRepository.deleteById(id);
    }
}
