package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.repository.PlantillaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Plantilla}.
 */
@Service
@Transactional
public class PlantillaService {

    private final Logger log = LoggerFactory.getLogger(PlantillaService.class);

    private final PlantillaRepository plantillaRepository;

    public PlantillaService(PlantillaRepository plantillaRepository) {
        this.plantillaRepository = plantillaRepository;
    }

    /**
     * Save a plantilla.
     *
     * @param plantilla the entity to save.
     * @return the persisted entity.
     */
    public Plantilla save(Plantilla plantilla) {
        log.debug("Request to save Plantilla : {}", plantilla);
        return plantillaRepository.save(plantilla);
    }

    /**
     * Partially update a plantilla.
     *
     * @param plantilla the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Plantilla> partialUpdate(Plantilla plantilla) {
        log.debug("Request to partially update Plantilla : {}", plantilla);

        return plantillaRepository
            .findById(plantilla.getId())
            .map(
                existingPlantilla -> {
                    if (plantilla.getNombre() != null) {
                        existingPlantilla.setNombre(plantilla.getNombre());
                    }
                    if (plantilla.getDescripcion() != null) {
                        existingPlantilla.setDescripcion(plantilla.getDescripcion());
                    }
                    if (plantilla.getFechaCreacion() != null) {
                        existingPlantilla.setFechaCreacion(plantilla.getFechaCreacion());
                    }
                    if (plantilla.getFechaPublicacionTienda() != null) {
                        existingPlantilla.setFechaPublicacionTienda(plantilla.getFechaPublicacionTienda());
                    }
                    if (plantilla.getEstado() != null) {
                        existingPlantilla.setEstado(plantilla.getEstado());
                    }
                    if (plantilla.getPrecio() != null) {
                        existingPlantilla.setPrecio(plantilla.getPrecio());
                    }

                    return existingPlantilla;
                }
            )
            .map(plantillaRepository::save);
    }

    /**
     * Get all the plantillas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Plantilla> findAll() {
        log.debug("Request to get all Plantillas");
        return plantillaRepository.findAll();
    }

    /**
     * Get one plantilla by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Plantilla> findOne(Long id) {
        log.debug("Request to get Plantilla : {}", id);
        return plantillaRepository.findById(id);
    }

    /**
     * Delete the plantilla by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Plantilla : {}", id);
        plantillaRepository.deleteById(id);
    }
}
