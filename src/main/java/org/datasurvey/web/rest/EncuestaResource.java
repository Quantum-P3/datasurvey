package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.repository.EncuestaRepository;
import org.datasurvey.service.EncuestaQueryService;
import org.datasurvey.service.EncuestaService;
import org.datasurvey.service.criteria.EncuestaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.Encuesta}.
 */
@RestController
@RequestMapping("/api")
public class EncuestaResource {

    private final Logger log = LoggerFactory.getLogger(EncuestaResource.class);

    private static final String ENTITY_NAME = "encuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EncuestaService encuestaService;

    private final EncuestaRepository encuestaRepository;

    private final EncuestaQueryService encuestaQueryService;

    public EncuestaResource(
        EncuestaService encuestaService,
        EncuestaRepository encuestaRepository,
        EncuestaQueryService encuestaQueryService
    ) {
        this.encuestaService = encuestaService;
        this.encuestaRepository = encuestaRepository;
        this.encuestaQueryService = encuestaQueryService;
    }

    /**
     * {@code POST  /encuestas} : Create a new encuesta.
     *
     * @param encuesta the encuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new encuesta, or with status {@code 400 (Bad Request)} if the encuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/encuestas")
    public ResponseEntity<Encuesta> createEncuesta(@Valid @RequestBody Encuesta encuesta) throws URISyntaxException {
        log.debug("REST request to save Encuesta : {}", encuesta);
        if (encuesta.getId() != null) {
            throw new BadRequestAlertException("A new encuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Encuesta result = encuestaService.save(encuesta);
        return ResponseEntity
            .created(new URI("/api/encuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /encuestas/:id} : Updates an existing encuesta.
     *
     * @param id the id of the encuesta to save.
     * @param encuesta the encuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encuesta,
     * or with status {@code 400 (Bad Request)} if the encuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the encuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/encuestas/{id}")
    public ResponseEntity<Encuesta> updateEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Encuesta : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Encuesta result = encuestaService.save(encuesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /encuestas/:id} : Partial updates given fields of an existing encuesta, field will ignore if it is null
     *
     * @param id the id of the encuesta to save.
     * @param encuesta the encuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encuesta,
     * or with status {@code 400 (Bad Request)} if the encuesta is not valid,
     * or with status {@code 404 (Not Found)} if the encuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the encuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/encuestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Encuesta> partialUpdateEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Encuesta partially : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Encuesta> result = encuestaService.partialUpdate(encuesta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /encuestas} : get all the encuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of encuestas in body.
     */
    @GetMapping("/encuestas")
    public ResponseEntity<List<Encuesta>> getAllEncuestas(EncuestaCriteria criteria) {
        log.debug("REST request to get Encuestas by criteria: {}", criteria);
        List<Encuesta> entityList = encuestaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /encuestas/count} : count all the encuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/encuestas/count")
    public ResponseEntity<Long> countEncuestas(EncuestaCriteria criteria) {
        log.debug("REST request to count Encuestas by criteria: {}", criteria);
        return ResponseEntity.ok().body(encuestaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /encuestas/:id} : get the "id" encuesta.
     *
     * @param id the id of the encuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the encuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/encuestas/{id}")
    public ResponseEntity<Encuesta> getEncuesta(@PathVariable Long id) {
        log.debug("REST request to get Encuesta : {}", id);
        Optional<Encuesta> encuesta = encuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(encuesta);
    }

    /**
     * {@code DELETE  /encuestas/:id} : delete the "id" encuesta.
     *
     * @param id the id of the encuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/encuestas/{id}")
    public ResponseEntity<Void> deleteEncuesta(@PathVariable Long id) {
        log.debug("REST request to delete Encuesta : {}", id);
        encuestaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
