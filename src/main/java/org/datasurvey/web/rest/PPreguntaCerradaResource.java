package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.PPreguntaCerrada;
import org.datasurvey.repository.PPreguntaCerradaRepository;
import org.datasurvey.service.PPreguntaCerradaQueryService;
import org.datasurvey.service.PPreguntaCerradaService;
import org.datasurvey.service.criteria.PPreguntaCerradaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.PPreguntaCerrada}.
 */
@RestController
@RequestMapping("/api")
public class PPreguntaCerradaResource {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaResource.class);

    private static final String ENTITY_NAME = "pPreguntaCerrada";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PPreguntaCerradaService pPreguntaCerradaService;

    private final PPreguntaCerradaRepository pPreguntaCerradaRepository;

    private final PPreguntaCerradaQueryService pPreguntaCerradaQueryService;

    public PPreguntaCerradaResource(
        PPreguntaCerradaService pPreguntaCerradaService,
        PPreguntaCerradaRepository pPreguntaCerradaRepository,
        PPreguntaCerradaQueryService pPreguntaCerradaQueryService
    ) {
        this.pPreguntaCerradaService = pPreguntaCerradaService;
        this.pPreguntaCerradaRepository = pPreguntaCerradaRepository;
        this.pPreguntaCerradaQueryService = pPreguntaCerradaQueryService;
    }

    /**
     * {@code POST  /p-pregunta-cerradas} : Create a new pPreguntaCerrada.
     *
     * @param pPreguntaCerrada the pPreguntaCerrada to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pPreguntaCerrada, or with status {@code 400 (Bad Request)} if the pPreguntaCerrada has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/p-pregunta-cerradas")
    public ResponseEntity<PPreguntaCerrada> createPPreguntaCerrada(@Valid @RequestBody PPreguntaCerrada pPreguntaCerrada)
        throws URISyntaxException {
        log.debug("REST request to save PPreguntaCerrada : {}", pPreguntaCerrada);
        if (pPreguntaCerrada.getId() != null) {
            throw new BadRequestAlertException("A new pPreguntaCerrada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PPreguntaCerrada result = pPreguntaCerradaService.save(pPreguntaCerrada);
        return ResponseEntity
            .created(new URI("/api/p-pregunta-cerradas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /p-pregunta-cerradas/:id} : Updates an existing pPreguntaCerrada.
     *
     * @param id the id of the pPreguntaCerrada to save.
     * @param pPreguntaCerrada the pPreguntaCerrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaCerrada,
     * or with status {@code 400 (Bad Request)} if the pPreguntaCerrada is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaCerrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/p-pregunta-cerradas/{id}")
    public ResponseEntity<PPreguntaCerrada> updatePPreguntaCerrada(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PPreguntaCerrada pPreguntaCerrada
    ) throws URISyntaxException {
        log.debug("REST request to update PPreguntaCerrada : {}, {}", id, pPreguntaCerrada);
        if (pPreguntaCerrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaCerrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaCerradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PPreguntaCerrada result = pPreguntaCerradaService.save(pPreguntaCerrada);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaCerrada.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /p-pregunta-cerradas/:id} : Partial updates given fields of an existing pPreguntaCerrada, field will ignore if it is null
     *
     * @param id the id of the pPreguntaCerrada to save.
     * @param pPreguntaCerrada the pPreguntaCerrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pPreguntaCerrada,
     * or with status {@code 400 (Bad Request)} if the pPreguntaCerrada is not valid,
     * or with status {@code 404 (Not Found)} if the pPreguntaCerrada is not found,
     * or with status {@code 500 (Internal Server Error)} if the pPreguntaCerrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/p-pregunta-cerradas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PPreguntaCerrada> partialUpdatePPreguntaCerrada(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PPreguntaCerrada pPreguntaCerrada
    ) throws URISyntaxException {
        log.debug("REST request to partial update PPreguntaCerrada partially : {}, {}", id, pPreguntaCerrada);
        if (pPreguntaCerrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pPreguntaCerrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pPreguntaCerradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PPreguntaCerrada> result = pPreguntaCerradaService.partialUpdate(pPreguntaCerrada);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pPreguntaCerrada.getId().toString())
        );
    }

    /**
     * {@code GET  /p-pregunta-cerradas} : get all the pPreguntaCerradas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pPreguntaCerradas in body.
     */
    @GetMapping("/p-pregunta-cerradas")
    public ResponseEntity<List<PPreguntaCerrada>> getAllPPreguntaCerradas(PPreguntaCerradaCriteria criteria) {
        log.debug("REST request to get PPreguntaCerradas by criteria: {}", criteria);
        List<PPreguntaCerrada> entityList = pPreguntaCerradaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /p-pregunta-cerradas/count} : count all the pPreguntaCerradas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/p-pregunta-cerradas/count")
    public ResponseEntity<Long> countPPreguntaCerradas(PPreguntaCerradaCriteria criteria) {
        log.debug("REST request to count PPreguntaCerradas by criteria: {}", criteria);
        return ResponseEntity.ok().body(pPreguntaCerradaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /p-pregunta-cerradas/:id} : get the "id" pPreguntaCerrada.
     *
     * @param id the id of the pPreguntaCerrada to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pPreguntaCerrada, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/p-pregunta-cerradas/{id}")
    public ResponseEntity<PPreguntaCerrada> getPPreguntaCerrada(@PathVariable Long id) {
        log.debug("REST request to get PPreguntaCerrada : {}", id);
        Optional<PPreguntaCerrada> pPreguntaCerrada = pPreguntaCerradaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pPreguntaCerrada);
    }

    /**
     * {@code DELETE  /p-pregunta-cerradas/:id} : delete the "id" pPreguntaCerrada.
     *
     * @param id the id of the pPreguntaCerrada to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/p-pregunta-cerradas/{id}")
    public ResponseEntity<Void> deletePPreguntaCerrada(@PathVariable Long id) {
        log.debug("REST request to delete PPreguntaCerrada : {}", id);
        pPreguntaCerradaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
