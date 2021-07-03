package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.PPreguntaCerradaOpcion;
import org.datasurvey.repository.PPreguntaCerradaOpcionRepository;
import org.datasurvey.service.PPreguntaCerradaOpcionQueryService;
import org.datasurvey.service.PPreguntaCerradaOpcionService;
import org.datasurvey.service.criteria.PPreguntaCerradaOpcionCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.PPreguntaCerradaOpcion}.
 */
@RestController
@RequestMapping("/api")
public class PPreguntaCerradaOpcionResource {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaOpcionResource.class);

    private static final String ENTITY_NAME = "pPreguntaCerradaOpcion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PPreguntaCerradaOpcionService pPreguntaCerradaOpcionService;

    private final PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository;

    private final PPreguntaCerradaOpcionQueryService pPreguntaCerradaOpcionQueryService;

    public PPreguntaCerradaOpcionResource(
        PPreguntaCerradaOpcionService pPreguntaCerradaOpcionService,
        PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository,
        PPreguntaCerradaOpcionQueryService pPreguntaCerradaOpcionQueryService
    ) {
        this.pPreguntaCerradaOpcionService = pPreguntaCerradaOpcionService;
        this.pPreguntaCerradaOpcionRepository = pPreguntaCerradaOpcionRepository;
        this.pPreguntaCerradaOpcionQueryService = pPreguntaCerradaOpcionQueryService;
    }

    /**
     * {@code POST  /p-pregunta-cerrada-opcions} : Create a new pPreguntaCerradaOpcion.
     *
     * @param pPreguntaCerradaOpcion the pPreguntaCerradaOpcion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pPreguntaCerradaOpcion, or with status {@code 400 (Bad Request)} if the pPreguntaCerradaOpcion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/p-pregunta-cerrada-opcions")
    public ResponseEntity<PPreguntaCerradaOpcion> createPPreguntaCerradaOpcion(
        @Valid @RequestBody PPreguntaCerradaOpcion pPreguntaCerradaOpcion
    ) throws URISyntaxException {
        log.debug("REST request to save PPreguntaCerradaOpcion : {}", pPreguntaCerradaOpcion);
        if (pPreguntaCerradaOpcion.getId() != null) {
            throw new BadRequestAlertException("A new pPreguntaCerradaOpcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPreguntaCerradaOpcion result = pPreguntaCerradaOpcionService.save(pPreguntaCerradaOpcion);
        return ResponseEntity
            .created(new URI("/api/p-pregunta-cerrada-opcions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /p-pregunta-cerrada-opcions/:id} : Updates an existing pPreguntaCerradaOpcion.
     *
     * @param id the id of the pPreguntaCerradaOpcion to save.
     * @param pPreguntaCerradaOpcion the pPreguntaCerradaOpcion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaCerradaOpcion,
     * or with status {@code 400 (Bad Request)} if the pPreguntaCerradaOpcion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaCerradaOpcion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/p-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<PPreguntaCerradaOpcion> updatePPreguntaCerradaOpcion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PPreguntaCerradaOpcion pPreguntaCerradaOpcion
    ) throws URISyntaxException {
        log.debug("REST request to update PPreguntaCerradaOpcion : {}, {}", id, pPreguntaCerradaOpcion);
        if (pPreguntaCerradaOpcion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaCerradaOpcion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaCerradaOpcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PPreguntaCerradaOpcion result = pPreguntaCerradaOpcionService.save(pPreguntaCerradaOpcion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaCerradaOpcion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /p-pregunta-cerrada-opcions/:id} : Partial updates given fields of an existing pPreguntaCerradaOpcion, field will ignore if it is null
     *
     * @param id the id of the pPreguntaCerradaOpcion to save.
     * @param pPreguntaCerradaOpcion the pPreguntaCerradaOpcion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaCerradaOpcion,
     * or with status {@code 400 (Bad Request)} if the pPreguntaCerradaOpcion is not valid,
     * or with status {@code 404 (Not Found)} if the pPreguntaCerradaOpcion is not found,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaCerradaOpcion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/p-pregunta-cerrada-opcions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PPreguntaCerradaOpcion> partialUpdatePPreguntaCerradaOpcion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PPreguntaCerradaOpcion pPreguntaCerradaOpcion
    ) throws URISyntaxException {
        log.debug("REST request to partial update PPreguntaCerradaOpcion partially : {}, {}", id, pPreguntaCerradaOpcion);
        if (pPreguntaCerradaOpcion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaCerradaOpcion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaCerradaOpcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PPreguntaCerradaOpcion> result = pPreguntaCerradaOpcionService.partialUpdate(pPreguntaCerradaOpcion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaCerradaOpcion.getId().toString())
        );
    }

    /**
     * {@code GET  /p-pregunta-cerrada-opcions} : get all the pPreguntaCerradaOpcions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pPreguntaCerradaOpcions in body.
     */
    @GetMapping("/p-pregunta-cerrada-opcions")
    public ResponseEntity<List<PPreguntaCerradaOpcion>> getAllPPreguntaCerradaOpcions(PPreguntaCerradaOpcionCriteria criteria) {
        log.debug("REST request to get PPreguntaCerradaOpcions by criteria: {}", criteria);
        List<PPreguntaCerradaOpcion> entityList = pPreguntaCerradaOpcionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /p-pregunta-cerrada-opcions/count} : count all the pPreguntaCerradaOpcions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/p-pregunta-cerrada-opcions/count")
    public ResponseEntity<Long> countPPreguntaCerradaOpcions(PPreguntaCerradaOpcionCriteria criteria) {
        log.debug("REST request to count PPreguntaCerradaOpcions by criteria: {}", criteria);
        return ResponseEntity.ok().body(pPreguntaCerradaOpcionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /p-pregunta-cerrada-opcions/:id} : get the "id" pPreguntaCerradaOpcion.
     *
     * @param id the id of the pPreguntaCerradaOpcion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pPreguntaCerradaOpcion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/p-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<PPreguntaCerradaOpcion> getPPreguntaCerradaOpcion(@PathVariable Long id) {
        log.debug("REST request to get PPreguntaCerradaOpcion : {}", id);
        Optional<PPreguntaCerradaOpcion> pPreguntaCerradaOpcion = pPreguntaCerradaOpcionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPreguntaCerradaOpcion);
    }

    /**
     * {@code DELETE  /p-pregunta-cerrada-opcions/:id} : delete the "id" pPreguntaCerradaOpcion.
     *
     * @param id the id of the pPreguntaCerradaOpcion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/p-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<Void> deletePPreguntaCerradaOpcion(@PathVariable Long id) {
        log.debug("REST request to delete PPreguntaCerradaOpcion : {}", id);
        pPreguntaCerradaOpcionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
