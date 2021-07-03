package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.EPreguntaAbiertaRespuesta;
import org.datasurvey.repository.EPreguntaAbiertaRespuestaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EPreguntaAbiertaRespuesta}.
 */
@Service
@Transactional
public class EPreguntaAbiertaRespuestaService {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaRespuestaService.class);

    private final EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository;

    public EPreguntaAbiertaRespuestaService(EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository) {
        this.ePreguntaAbiertaRespuestaRepository = ePreguntaAbiertaRespuestaRepository;
    }

    /**
     * Save a ePreguntaAbiertaRespuesta.
     *
     * @param ePreguntaAbiertaRespuesta the entity to save.
     * @return the persisted entity.
     */
    public EPreguntaAbiertaRespuesta save(EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta) {
        log.debug("Request to save EPreguntaAbiertaRespuesta : {}", ePreguntaAbiertaRespuesta);
        return ePreguntaAbiertaRespuestaRepository.save(ePreguntaAbiertaRespuesta);
    }

    /**
     * Partially update a ePreguntaAbiertaRespuesta.
     *
     * @param ePreguntaAbiertaRespuesta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EPreguntaAbiertaRespuesta> partialUpdate(EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta) {
        log.debug("Request to partially update EPreguntaAbiertaRespuesta : {}", ePreguntaAbiertaRespuesta);

        return ePreguntaAbiertaRespuestaRepository
            .findById(ePreguntaAbiertaRespuesta.getId())
            .map(
                existingEPreguntaAbiertaRespuesta -> {
                    if (ePreguntaAbiertaRespuesta.getRespuesta() != null) {
                        existingEPreguntaAbiertaRespuesta.setRespuesta(ePreguntaAbiertaRespuesta.getRespuesta());
                    }

                    return existingEPreguntaAbiertaRespuesta;
                }
            )
            .map(ePreguntaAbiertaRespuestaRepository::save);
    }

    /**
     * Get all the ePreguntaAbiertaRespuestas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaAbiertaRespuesta> findAll() {
        log.debug("Request to get all EPreguntaAbiertaRespuestas");
        return ePreguntaAbiertaRespuestaRepository.findAll();
    }

    /**
     * Get one ePreguntaAbiertaRespuesta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EPreguntaAbiertaRespuesta> findOne(Long id) {
        log.debug("Request to get EPreguntaAbiertaRespuesta : {}", id);
        return ePreguntaAbiertaRespuestaRepository.findById(id);
    }

    /**
     * Delete the ePreguntaAbiertaRespuesta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EPreguntaAbiertaRespuesta : {}", id);
        ePreguntaAbiertaRespuestaRepository.deleteById(id);
    }
}
