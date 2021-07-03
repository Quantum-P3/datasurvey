package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.PPreguntaCerrada;
import org.datasurvey.repository.PPreguntaCerradaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PPreguntaCerrada}.
 */
@Service
@Transactional
public class PPreguntaCerradaService {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaService.class);

    private final PPreguntaCerradaRepository pPreguntaCerradaRepository;

    public PPreguntaCerradaService(PPreguntaCerradaRepository pPreguntaCerradaRepository) {
        this.pPreguntaCerradaRepository = pPreguntaCerradaRepository;
    }

    /**
     * Save a pPreguntaCerrada.
     *
     * @param pPreguntaCerrada the entity to save.
     * @return the persisted entity.
     */
    public PPreguntaCerrada save(PPreguntaCerrada pPreguntaCerrada) {
        log.debug("Request to save PPreguntaCerrada : {}", pPreguntaCerrada);
        return pPreguntaCerradaRepository.save(pPreguntaCerrada);
    }

    /**
     * Partially update a pPreguntaCerrada.
     *
     * @param pPreguntaCerrada the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PPreguntaCerrada> partialUpdate(PPreguntaCerrada pPreguntaCerrada) {
        log.debug("Request to partially update PPreguntaCerrada : {}", pPreguntaCerrada);

        return pPreguntaCerradaRepository
            .findById(pPreguntaCerrada.getId())
            .map(
                existingPPreguntaCerrada -> {
                    if (pPreguntaCerrada.getNombre() != null) {
                        existingPPreguntaCerrada.setNombre(pPreguntaCerrada.getNombre());
                    }
                    if (pPreguntaCerrada.getTipo() != null) {
                        existingPPreguntaCerrada.setTipo(pPreguntaCerrada.getTipo());
                    }
                    if (pPreguntaCerrada.getOpcional() != null) {
                        existingPPreguntaCerrada.setOpcional(pPreguntaCerrada.getOpcional());
                    }
                    if (pPreguntaCerrada.getOrden() != null) {
                        existingPPreguntaCerrada.setOrden(pPreguntaCerrada.getOrden());
                    }

                    return existingPPreguntaCerrada;
                }
            )
            .map(pPreguntaCerradaRepository::save);
    }

    /**
     * Get all the pPreguntaCerradas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaCerrada> findAll() {
        log.debug("Request to get all PPreguntaCerradas");
        return pPreguntaCerradaRepository.findAll();
    }

    /**
     * Get one pPreguntaCerrada by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PPreguntaCerrada> findOne(Long id) {
        log.debug("Request to get PPreguntaCerrada : {}", id);
        return pPreguntaCerradaRepository.findById(id);
    }

    /**
     * Delete the pPreguntaCerrada by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PPreguntaCerrada : {}", id);
        pPreguntaCerradaRepository.deleteById(id);
    }
}
