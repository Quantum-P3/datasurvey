package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.repository.PlantillaRepository;
import org.datasurvey.service.PlantillaQueryService;
import org.datasurvey.service.PlantillaService;
import org.datasurvey.service.criteria.PlantillaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.Plantilla}.
 */
@RestController
@RequestMapping("/api")
public class PlantillaResource {

    private final Logger log = LoggerFactory.getLogger(PlantillaResource.class);

    private static final String ENTITY_NAME = "plantilla";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantillaService plantillaService;

    private final PlantillaRepository plantillaRepository;

    private final PlantillaQueryService plantillaQueryService;

    public PlantillaResource(
        PlantillaService plantillaService,
        PlantillaRepository plantillaRepository,
        PlantillaQueryService plantillaQueryService
    ) {
        this.plantillaService = plantillaService;
        this.plantillaRepository = plantillaRepository;
        this.plantillaQueryService = plantillaQueryService;
    }

    /**
     * {@code POST  /plantillas} : Create a new plantilla.
     *
     * @param plantilla the plantilla to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantilla, or with status {@code 400 (Bad Request)} if the plantilla has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantillas")
    public ResponseEntity<Plantilla> createPlantilla(@Valid @RequestBody Plantilla plantilla) throws URISyntaxException {
        log.debug("REST request to save Plantilla : {}", plantilla);
        if (plantilla.getId() != null) {
            throw new BadRequestAlertException("A new plantilla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plantilla result = plantillaService.save(plantilla);
        return ResponseEntity
            .created(new URI("/api/plantillas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plantillas/:id} : Updates an existing plantilla.
     *
     * @param id the id of the plantilla to save.
     * @param plantilla the plantilla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantilla,
     * or with status {@code 400 (Bad Request)} if the plantilla is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantilla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantillas/{id}")
    public ResponseEntity<Plantilla> updatePlantilla(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Plantilla plantilla
    ) throws URISyntaxException {
        log.debug("REST request to update Plantilla : {}, {}", id, plantilla);
        if (plantilla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantilla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantillaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Plantilla result = plantillaService.save(plantilla);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantilla.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plantillas/:id} : Partial updates given fields of an existing plantilla, field will ignore if it is null
     *
     * @param id the id of the plantilla to save.
     * @param plantilla the plantilla to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantilla,
     * or with status {@code 400 (Bad Request)} if the plantilla is not valid,
     * or with status {@code 404 (Not Found)} if the plantilla is not found,
     * or with status {@code 500 (Internal Server Error)} if the plantilla couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plantillas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Plantilla> partialUpdatePlantilla(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Plantilla plantilla
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plantilla partially : {}, {}", id, plantilla);
        if (plantilla.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantilla.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantillaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Plantilla> result = plantillaService.partialUpdate(plantilla);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantilla.getId().toString())
        );
    }

    /**
     * {@code GET  /plantillas} : get all the plantillas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantillas in body.
     */
    @GetMapping("/plantillas")
    public ResponseEntity<List<Plantilla>> getAllPlantillas(PlantillaCriteria criteria) {
        log.debug("REST request to get Plantillas by criteria: {}", criteria);
        List<Plantilla> entityList = plantillaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /plantillas/count} : count all the plantillas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/plantillas/count")
    public ResponseEntity<Long> countPlantillas(PlantillaCriteria criteria) {
        log.debug("REST request to count Plantillas by criteria: {}", criteria);
        return ResponseEntity.ok().body(plantillaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plantillas/:id} : get the "id" plantilla.
     *
     * @param id the id of the plantilla to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantilla, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantillas/{id}")
    public ResponseEntity<Plantilla> getPlantilla(@PathVariable Long id) {
        log.debug("REST request to get Plantilla : {}", id);
        Optional<Plantilla> plantilla = plantillaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plantilla);
    }

    /**
     * {@code DELETE  /plantillas/:id} : delete the "id" plantilla.
     *
     * @param id the id of the plantilla to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantillas/{id}")
    public ResponseEntity<Void> deletePlantilla(@PathVariable Long id) {
        log.debug("REST request to delete Plantilla : {}", id);
        plantillaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
