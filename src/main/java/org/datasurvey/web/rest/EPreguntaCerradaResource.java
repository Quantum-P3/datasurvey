package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.repository.EPreguntaCerradaRepository;
import org.datasurvey.service.EPreguntaCerradaQueryService;
import org.datasurvey.service.EPreguntaCerradaService;
import org.datasurvey.service.criteria.EPreguntaCerradaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.EPreguntaCerrada}.
 */
@RestController
@RequestMapping("/api")
public class EPreguntaCerradaResource {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaResource.class);

    private static final String ENTITY_NAME = "ePreguntaCerrada";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EPreguntaCerradaService ePreguntaCerradaService;

    private final EPreguntaCerradaRepository ePreguntaCerradaRepository;

    private final EPreguntaCerradaQueryService ePreguntaCerradaQueryService;

    public EPreguntaCerradaResource(
        EPreguntaCerradaService ePreguntaCerradaService,
        EPreguntaCerradaRepository ePreguntaCerradaRepository,
        EPreguntaCerradaQueryService ePreguntaCerradaQueryService
    ) {
        this.ePreguntaCerradaService = ePreguntaCerradaService;
        this.ePreguntaCerradaRepository = ePreguntaCerradaRepository;
        this.ePreguntaCerradaQueryService = ePreguntaCerradaQueryService;
    }

    /**
     * {@code POST  /e-pregunta-cerradas} : Create a new ePreguntaCerrada.
     *
     * @param ePreguntaCerrada the ePreguntaCerrada to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ePreguntaCerrada, or with status {@code 400 (Bad Request)} if the ePreguntaCerrada has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-pregunta-cerradas")
    public ResponseEntity<EPreguntaCerrada> createEPreguntaCerrada(@Valid @RequestBody EPreguntaCerrada ePreguntaCerrada)
        throws URISyntaxException {
        log.debug("REST request to save EPreguntaCerrada : {}", ePreguntaCerrada);
        if (ePreguntaCerrada.getId() != null) {
            throw new BadRequestAlertException("A new ePreguntaCerrada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EPreguntaCerrada result = ePreguntaCerradaService.save(ePreguntaCerrada);
        return ResponseEntity
            .created(new URI("/api/e-pregunta-cerradas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-pregunta-cerradas/:id} : Updates an existing ePreguntaCerrada.
     *
     * @param id the id of the ePreguntaCerrada to save.
     * @param ePreguntaCerrada the ePreguntaCerrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaCerrada,
     * or with status {@code 400 (Bad Request)} if the ePreguntaCerrada is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaCerrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-pregunta-cerradas/{id}")
    public ResponseEntity<EPreguntaCerrada> updateEPreguntaCerrada(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EPreguntaCerrada ePreguntaCerrada
    ) throws URISyntaxException {
        log.debug("REST request to update EPreguntaCerrada : {}, {}", id, ePreguntaCerrada);
        if (ePreguntaCerrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaCerrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaCerradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EPreguntaCerrada result = ePreguntaCerradaService.save(ePreguntaCerrada);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaCerrada.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-pregunta-cerradas/:id} : Partial updates given fields of an existing ePreguntaCerrada, field will ignore if it is null
     *
     * @param id the id of the ePreguntaCerrada to save.
     * @param ePreguntaCerrada the ePreguntaCerrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaCerrada,
     * or with status {@code 400 (Bad Request)} if the ePreguntaCerrada is not valid,
     * or with status {@code 404 (Not Found)} if the ePreguntaCerrada is not found,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaCerrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-pregunta-cerradas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EPreguntaCerrada> partialUpdateEPreguntaCerrada(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EPreguntaCerrada ePreguntaCerrada
    ) throws URISyntaxException {
        log.debug("REST request to partial update EPreguntaCerrada partially : {}, {}", id, ePreguntaCerrada);
        if (ePreguntaCerrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaCerrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaCerradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EPreguntaCerrada> result = ePreguntaCerradaService.partialUpdate(ePreguntaCerrada);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaCerrada.getId().toString())
        );
    }

    /**
     * {@code GET  /e-pregunta-cerradas} : get all the ePreguntaCerradas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ePreguntaCerradas in body.
     */
    @GetMapping("/e-pregunta-cerradas")
    public ResponseEntity<List<EPreguntaCerrada>> getAllEPreguntaCerradas(EPreguntaCerradaCriteria criteria) {
        log.debug("REST request to get EPreguntaCerradas by criteria: {}", criteria);
        List<EPreguntaCerrada> entityList = ePreguntaCerradaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /e-pregunta-cerradas/count} : count all the ePreguntaCerradas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/e-pregunta-cerradas/count")
    public ResponseEntity<Long> countEPreguntaCerradas(EPreguntaCerradaCriteria criteria) {
        log.debug("REST request to count EPreguntaCerradas by criteria: {}", criteria);
        return ResponseEntity.ok().body(ePreguntaCerradaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /e-pregunta-cerradas/:id} : get the "id" ePreguntaCerrada.
     *
     * @param id the id of the ePreguntaCerrada to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ePreguntaCerrada, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-pregunta-cerradas/{id}")
    public ResponseEntity<EPreguntaCerrada> getEPreguntaCerrada(@PathVariable Long id) {
        log.debug("REST request to get EPreguntaCerrada : {}", id);
        Optional<EPreguntaCerrada> ePreguntaCerrada = ePreguntaCerradaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ePreguntaCerrada);
    }

    /**
     * {@code DELETE  /e-pregunta-cerradas/:id} : delete the "id" ePreguntaCerrada.
     *
     * @param id the id of the ePreguntaCerrada to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-pregunta-cerradas/{id}")
    public ResponseEntity<Void> deleteEPreguntaCerrada(@PathVariable Long id) {
        log.debug("REST request to delete EPreguntaCerrada : {}", id);
        ePreguntaCerradaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
