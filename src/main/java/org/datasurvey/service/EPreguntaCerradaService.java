package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.repository.EPreguntaCerradaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EPreguntaCerrada}.
 */
@Service
@Transactional
public class EPreguntaCerradaService {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaService.class);

    private final EPreguntaCerradaRepository ePreguntaCerradaRepository;

    public EPreguntaCerradaService(EPreguntaCerradaRepository ePreguntaCerradaRepository) {
        this.ePreguntaCerradaRepository = ePreguntaCerradaRepository;
    }

    /**
     * Save a ePreguntaCerrada.
     *
     * @param ePreguntaCerrada the entity to save.
     * @return the persisted entity.
     */
    public EPreguntaCerrada save(EPreguntaCerrada ePreguntaCerrada) {
        log.debug("Request to save EPreguntaCerrada : {}", ePreguntaCerrada);
        return ePreguntaCerradaRepository.save(ePreguntaCerrada);
    }

    /**
     * Partially update a ePreguntaCerrada.
     *
     * @param ePreguntaCerrada the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EPreguntaCerrada> partialUpdate(EPreguntaCerrada ePreguntaCerrada) {
        log.debug("Request to partially update EPreguntaCerrada : {}", ePreguntaCerrada);

        return ePreguntaCerradaRepository
            .findById(ePreguntaCerrada.getId())
            .map(
                existingEPreguntaCerrada -> {
                    if (ePreguntaCerrada.getNombre() != null) {
                        existingEPreguntaCerrada.setNombre(ePreguntaCerrada.getNombre());
                    }
                    if (ePreguntaCerrada.getTipo() != null) {
                        existingEPreguntaCerrada.setTipo(ePreguntaCerrada.getTipo());
                    }
                    if (ePreguntaCerrada.getOpcional() != null) {
                        existingEPreguntaCerrada.setOpcional(ePreguntaCerrada.getOpcional());
                    }
                    if (ePreguntaCerrada.getOrden() != null) {
                        existingEPreguntaCerrada.setOrden(ePreguntaCerrada.getOrden());
                    }

                    return existingEPreguntaCerrada;
                }
            )
            .map(ePreguntaCerradaRepository::save);
    }

    /**
     * Get all the ePreguntaCerradas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaCerrada> findAll() {
        log.debug("Request to get all EPreguntaCerradas");
        return ePreguntaCerradaRepository.findAll();
    }

    /**
     * Get one ePreguntaCerrada by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EPreguntaCerrada> findOne(Long id) {
        log.debug("Request to get EPreguntaCerrada : {}", id);
        return ePreguntaCerradaRepository.findById(id);
    }

    /**
     * Delete the ePreguntaCerrada by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EPreguntaCerrada : {}", id);
        ePreguntaCerradaRepository.deleteById(id);
    }
}
