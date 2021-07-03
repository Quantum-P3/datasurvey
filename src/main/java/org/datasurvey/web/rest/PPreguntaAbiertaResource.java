package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.PPreguntaAbierta;
import org.datasurvey.repository.PPreguntaAbiertaRepository;
import org.datasurvey.service.PPreguntaAbiertaQueryService;
import org.datasurvey.service.PPreguntaAbiertaService;
import org.datasurvey.service.criteria.PPreguntaAbiertaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.PPreguntaAbierta}.
 */
@RestController
@RequestMapping("/api")
public class PPreguntaAbiertaResource {

    private final Logger log = LoggerFactory.getLogger(PPreguntaAbiertaResource.class);

    private static final String ENTITY_NAME = "pPreguntaAbierta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PPreguntaAbiertaService pPreguntaAbiertaService;

    private final PPreguntaAbiertaRepository pPreguntaAbiertaRepository;

    private final PPreguntaAbiertaQueryService pPreguntaAbiertaQueryService;

    public PPreguntaAbiertaResource(
        PPreguntaAbiertaService pPreguntaAbiertaService,
        PPreguntaAbiertaRepository pPreguntaAbiertaRepository,
        PPreguntaAbiertaQueryService pPreguntaAbiertaQueryService
    ) {
        this.pPreguntaAbiertaService = pPreguntaAbiertaService;
        this.pPreguntaAbiertaRepository = pPreguntaAbiertaRepository;
        this.pPreguntaAbiertaQueryService = pPreguntaAbiertaQueryService;
    }

    /**
     * {@code POST  /p-pregunta-abiertas} : Create a new pPreguntaAbierta.
     *
     * @param pPreguntaAbierta the pPreguntaAbierta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pPreguntaAbierta, or with status {@code 400 (Bad Request)} if the pPreguntaAbierta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/p-pregunta-abiertas")
    public ResponseEntity<PPreguntaAbierta> createPPreguntaAbierta(@Valid @RequestBody PPreguntaAbierta pPreguntaAbierta)
        throws URISyntaxException {
        log.debug("REST request to save PPreguntaAbierta : {}", pPreguntaAbierta);
        if (pPreguntaAbierta.getId() != null) {
            throw new BadRequestAlertException("A new pPreguntaAbierta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPreguntaAbierta result = pPreguntaAbiertaService.save(pPreguntaAbierta);
        return ResponseEntity
            .created(new URI("/api/p-pregunta-abiertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /p-pregunta-abiertas/:id} : Updates an existing pPreguntaAbierta.
     *
     * @param id the id of the pPreguntaAbierta to save.
     * @param pPreguntaAbierta the pPreguntaAbierta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaAbierta,
     * or with status {@code 400 (Bad Request)} if the pPreguntaAbierta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaAbierta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/p-pregunta-abiertas/{id}")
    public ResponseEntity<PPreguntaAbierta> updatePPreguntaAbierta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PPreguntaAbierta pPreguntaAbierta
    ) throws URISyntaxException {
        log.debug("REST request to update PPreguntaAbierta : {}, {}", id, pPreguntaAbierta);
        if (pPreguntaAbierta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaAbierta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaAbiertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PPreguntaAbierta result = pPreguntaAbiertaService.save(pPreguntaAbierta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaAbierta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /p-pregunta-abiertas/:id} : Partial updates given fields of an existing pPreguntaAbierta, field will ignore if it is null
     *
     * @param id the id of the pPreguntaAbierta to save.
     * @param pPreguntaAbierta the pPreguntaAbierta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaAbierta,
     * or with status {@code 400 (Bad Request)} if the pPreguntaAbierta is not valid,
     * or with status {@code 404 (Not Found)} if the pPreguntaAbierta is not found,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaAbierta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/p-pregunta-abiertas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PPreguntaAbierta> partialUpdatePPreguntaAbierta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PPreguntaAbierta pPreguntaAbierta
    ) throws URISyntaxException {
        log.debug("REST request to partial update PPreguntaAbierta partially : {}, {}", id, pPreguntaAbierta);
        if (pPreguntaAbierta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaAbierta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaAbiertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PPreguntaAbierta> result = pPreguntaAbiertaService.partialUpdate(pPreguntaAbierta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaAbierta.getId().toString())
        );
    }

    /**
     * {@code GET  /p-pregunta-abiertas} : get all the pPreguntaAbiertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pPreguntaAbiertas in body.
     */
    @GetMapping("/p-pregunta-abiertas")
    public ResponseEntity<List<PPreguntaAbierta>> getAllPPreguntaAbiertas(PPreguntaAbiertaCriteria criteria) {
        log.debug("REST request to get PPreguntaAbiertas by criteria: {}", criteria);
        List<PPreguntaAbierta> entityList = pPreguntaAbiertaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /p-pregunta-abiertas/count} : count all the pPreguntaAbiertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/p-pregunta-abiertas/count")
    public ResponseEntity<Long> countPPreguntaAbiertas(PPreguntaAbiertaCriteria criteria) {
        log.debug("REST request to count PPreguntaAbiertas by criteria: {}", criteria);
        return ResponseEntity.ok().body(pPreguntaAbiertaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /p-pregunta-abiertas/:id} : get the "id" pPreguntaAbierta.
     *
     * @param id the id of the pPreguntaAbierta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pPreguntaAbierta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/p-pregunta-abiertas/{id}")
    public ResponseEntity<PPreguntaAbierta> getPPreguntaAbierta(@PathVariable Long id) {
        log.debug("REST request to get PPreguntaAbierta : {}", id);
        Optional<PPreguntaAbierta> pPreguntaAbierta = pPreguntaAbiertaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPreguntaAbierta);
    }

    /**
     * {@code DELETE  /p-pregunta-abiertas/:id} : delete the "id" pPreguntaAbierta.
     *
     * @param id the id of the pPreguntaAbierta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/p-pregunta-abiertas/{id}")
    public ResponseEntity<Void> deletePPreguntaAbierta(@PathVariable Long id) {
        log.debug("REST request to delete PPreguntaAbierta : {}", id);
        pPreguntaAbiertaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
