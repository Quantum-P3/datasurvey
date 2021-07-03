package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.EPreguntaAbiertaRespuesta;
import org.datasurvey.repository.EPreguntaAbiertaRespuestaRepository;
import org.datasurvey.service.EPreguntaAbiertaRespuestaQueryService;
import org.datasurvey.service.EPreguntaAbiertaRespuestaService;
import org.datasurvey.service.criteria.EPreguntaAbiertaRespuestaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.EPreguntaAbiertaRespuesta}.
 */
@RestController
@RequestMapping("/api")
public class EPreguntaAbiertaRespuestaResource {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaRespuestaResource.class);

    private static final String ENTITY_NAME = "ePreguntaAbiertaRespuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EPreguntaAbiertaRespuestaService ePreguntaAbiertaRespuestaService;

    private final EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository;

    private final EPreguntaAbiertaRespuestaQueryService ePreguntaAbiertaRespuestaQueryService;

    public EPreguntaAbiertaRespuestaResource(
        EPreguntaAbiertaRespuestaService ePreguntaAbiertaRespuestaService,
        EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository,
        EPreguntaAbiertaRespuestaQueryService ePreguntaAbiertaRespuestaQueryService
    ) {
        this.ePreguntaAbiertaRespuestaService = ePreguntaAbiertaRespuestaService;
        this.ePreguntaAbiertaRespuestaRepository = ePreguntaAbiertaRespuestaRepository;
        this.ePreguntaAbiertaRespuestaQueryService = ePreguntaAbiertaRespuestaQueryService;
    }

    /**
     * {@code POST  /e-pregunta-abierta-respuestas} : Create a new ePreguntaAbiertaRespuesta.
     *
     * @param ePreguntaAbiertaRespuesta the ePreguntaAbiertaRespuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ePreguntaAbiertaRespuesta, or with status {@code 400 (Bad Request)} if the ePreguntaAbiertaRespuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-pregunta-abierta-respuestas")
    public ResponseEntity<EPreguntaAbiertaRespuesta> createEPreguntaAbiertaRespuesta(
        @Valid @RequestBody EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta
    ) throws URISyntaxException {
        log.debug("REST request to save EPreguntaAbiertaRespuesta : {}", ePreguntaAbiertaRespuesta);
        if (ePreguntaAbiertaRespuesta.getId() != null) {
            throw new BadRequestAlertException("A new ePreguntaAbiertaRespuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EPreguntaAbiertaRespuesta result = ePreguntaAbiertaRespuestaService.save(ePreguntaAbiertaRespuesta);
        return ResponseEntity
            .created(new URI("/api/e-pregunta-abierta-respuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-pregunta-abierta-respuestas/:id} : Updates an existing ePreguntaAbiertaRespuesta.
     *
     * @param id the id of the ePreguntaAbiertaRespuesta to save.
     * @param ePreguntaAbiertaRespuesta the ePreguntaAbiertaRespuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaAbiertaRespuesta,
     * or with status {@code 400 (Bad Request)} if the ePreguntaAbiertaRespuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaAbiertaRespuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-pregunta-abierta-respuestas/{id}")
    public ResponseEntity<EPreguntaAbiertaRespuesta> updateEPreguntaAbiertaRespuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta
    ) throws URISyntaxException {
        log.debug("REST request to update EPreguntaAbiertaRespuesta : {}, {}", id, ePreguntaAbiertaRespuesta);
        if (ePreguntaAbiertaRespuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaAbiertaRespuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaAbiertaRespuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EPreguntaAbiertaRespuesta result = ePreguntaAbiertaRespuestaService.save(ePreguntaAbiertaRespuesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaAbiertaRespuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-pregunta-abierta-respuestas/:id} : Partial updates given fields of an existing ePreguntaAbiertaRespuesta, field will ignore if it is null
     *
     * @param id the id of the ePreguntaAbiertaRespuesta to save.
     * @param ePreguntaAbiertaRespuesta the ePreguntaAbiertaRespuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaAbiertaRespuesta,
     * or with status {@code 400 (Bad Request)} if the ePreguntaAbiertaRespuesta is not valid,
     * or with status {@code 404 (Not Found)} if the ePreguntaAbiertaRespuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaAbiertaRespuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-pregunta-abierta-respuestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EPreguntaAbiertaRespuesta> partialUpdateEPreguntaAbiertaRespuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update EPreguntaAbiertaRespuesta partially : {}, {}", id, ePreguntaAbiertaRespuesta);
        if (ePreguntaAbiertaRespuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaAbiertaRespuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaAbiertaRespuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EPreguntaAbiertaRespuesta> result = ePreguntaAbiertaRespuestaService.partialUpdate(ePreguntaAbiertaRespuesta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaAbiertaRespuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /e-pregunta-abierta-respuestas} : get all the ePreguntaAbiertaRespuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ePreguntaAbiertaRespuestas in body.
     */
    @GetMapping("/e-pregunta-abierta-respuestas")
    public ResponseEntity<List<EPreguntaAbiertaRespuesta>> getAllEPreguntaAbiertaRespuestas(EPreguntaAbiertaRespuestaCriteria criteria) {
        log.debug("REST request to get EPreguntaAbiertaRespuestas by criteria: {}", criteria);
        List<EPreguntaAbiertaRespuesta> entityList = ePreguntaAbiertaRespuestaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /e-pregunta-abierta-respuestas/count} : count all the ePreguntaAbiertaRespuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/e-pregunta-abierta-respuestas/count")
    public ResponseEntity<Long> countEPreguntaAbiertaRespuestas(EPreguntaAbiertaRespuestaCriteria criteria) {
        log.debug("REST request to count EPreguntaAbiertaRespuestas by criteria: {}", criteria);
        return ResponseEntity.ok().body(ePreguntaAbiertaRespuestaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /e-pregunta-abierta-respuestas/:id} : get the "id" ePreguntaAbiertaRespuesta.
     *
     * @param id the id of the ePreguntaAbiertaRespuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ePreguntaAbiertaRespuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-pregunta-abierta-respuestas/{id}")
    public ResponseEntity<EPreguntaAbiertaRespuesta> getEPreguntaAbiertaRespuesta(@PathVariable Long id) {
        log.debug("REST request to get EPreguntaAbiertaRespuesta : {}", id);
        Optional<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ePreguntaAbiertaRespuesta);
    }

    /**
     * {@code DELETE  /e-pregunta-abierta-respuestas/:id} : delete the "id" ePreguntaAbiertaRespuesta.
     *
     * @param id the id of the ePreguntaAbiertaRespuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-pregunta-abierta-respuestas/{id}")
    public ResponseEntity<Void> deleteEPreguntaAbiertaRespuesta(@PathVariable Long id) {
        log.debug("REST request to delete EPreguntaAbiertaRespuesta : {}", id);
        ePreguntaAbiertaRespuestaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
