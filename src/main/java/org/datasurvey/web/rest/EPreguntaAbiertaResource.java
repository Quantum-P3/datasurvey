package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.EPreguntaAbierta;
import org.datasurvey.repository.EPreguntaAbiertaRepository;
import org.datasurvey.service.EPreguntaAbiertaQueryService;
import org.datasurvey.service.EPreguntaAbiertaService;
import org.datasurvey.service.criteria.EPreguntaAbiertaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.EPreguntaAbierta}.
 */
@RestController
@RequestMapping("/api")
public class EPreguntaAbiertaResource {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaResource.class);

    private static final String ENTITY_NAME = "ePreguntaAbierta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EPreguntaAbiertaService ePreguntaAbiertaService;

    private final EPreguntaAbiertaRepository ePreguntaAbiertaRepository;

    private final EPreguntaAbiertaQueryService ePreguntaAbiertaQueryService;

    public EPreguntaAbiertaResource(
        EPreguntaAbiertaService ePreguntaAbiertaService,
        EPreguntaAbiertaRepository ePreguntaAbiertaRepository,
        EPreguntaAbiertaQueryService ePreguntaAbiertaQueryService
    ) {
        this.ePreguntaAbiertaService = ePreguntaAbiertaService;
        this.ePreguntaAbiertaRepository = ePreguntaAbiertaRepository;
        this.ePreguntaAbiertaQueryService = ePreguntaAbiertaQueryService;
    }

    /**
     * {@code POST  /e-pregunta-abiertas} : Create a new ePreguntaAbierta.
     *
     * @param ePreguntaAbierta the ePreguntaAbierta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ePreguntaAbierta, or with status {@code 400 (Bad Request)} if the ePreguntaAbierta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-pregunta-abiertas")
    public ResponseEntity<EPreguntaAbierta> createEPreguntaAbierta(@Valid @RequestBody EPreguntaAbierta ePreguntaAbierta)
        throws URISyntaxException {
        log.debug("REST request to save EPreguntaAbierta : {}", ePreguntaAbierta);
        if (ePreguntaAbierta.getId() != null) {
            throw new BadRequestAlertException("A new ePreguntaAbierta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EPreguntaAbierta result = ePreguntaAbiertaService.save(ePreguntaAbierta);
        return ResponseEntity
            .created(new URI("/api/e-pregunta-abiertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-pregunta-abiertas/:id} : Updates an existing ePreguntaAbierta.
     *
     * @param id the id of the ePreguntaAbierta to save.
     * @param ePreguntaAbierta the ePreguntaAbierta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaAbierta,
     * or with status {@code 400 (Bad Request)} if the ePreguntaAbierta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaAbierta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-pregunta-abiertas/{id}")
    public ResponseEntity<EPreguntaAbierta> updateEPreguntaAbierta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EPreguntaAbierta ePreguntaAbierta
    ) throws URISyntaxException {
        log.debug("REST request to update EPreguntaAbierta : {}, {}", id, ePreguntaAbierta);
        if (ePreguntaAbierta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaAbierta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaAbiertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EPreguntaAbierta result = ePreguntaAbiertaService.save(ePreguntaAbierta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaAbierta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-pregunta-abiertas/:id} : Partial updates given fields of an existing ePreguntaAbierta, field will ignore if it is null
     *
     * @param id the id of the ePreguntaAbierta to save.
     * @param ePreguntaAbierta the ePreguntaAbierta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaAbierta,
     * or with status {@code 400 (Bad Request)} if the ePreguntaAbierta is not valid,
     * or with status {@code 404 (Not Found)} if the ePreguntaAbierta is not found,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaAbierta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-pregunta-abiertas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EPreguntaAbierta> partialUpdateEPreguntaAbierta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EPreguntaAbierta ePreguntaAbierta
    ) throws URISyntaxException {
        log.debug("REST request to partial update EPreguntaAbierta partially : {}, {}", id, ePreguntaAbierta);
        if (ePreguntaAbierta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaAbierta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaAbiertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EPreguntaAbierta> result = ePreguntaAbiertaService.partialUpdate(ePreguntaAbierta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaAbierta.getId().toString())
        );
    }

    /**
     * {@code GET  /e-pregunta-abiertas} : get all the ePreguntaAbiertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ePreguntaAbiertas in body.
     */
    @GetMapping("/e-pregunta-abiertas")
    public ResponseEntity<List<EPreguntaAbierta>> getAllEPreguntaAbiertas(EPreguntaAbiertaCriteria criteria) {
        log.debug("REST request to get EPreguntaAbiertas by criteria: {}", criteria);
        List<EPreguntaAbierta> entityList = ePreguntaAbiertaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /e-pregunta-abiertas/count} : count all the ePreguntaAbiertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/e-pregunta-abiertas/count")
    public ResponseEntity<Long> countEPreguntaAbiertas(EPreguntaAbiertaCriteria criteria) {
        log.debug("REST request to count EPreguntaAbiertas by criteria: {}", criteria);
        return ResponseEntity.ok().body(ePreguntaAbiertaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /e-pregunta-abiertas/:id} : get the "id" ePreguntaAbierta.
     *
     * @param id the id of the ePreguntaAbierta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ePreguntaAbierta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-pregunta-abiertas/{id}")
    public ResponseEntity<EPreguntaAbierta> getEPreguntaAbierta(@PathVariable Long id) {
        log.debug("REST request to get EPreguntaAbierta : {}", id);
        Optional<EPreguntaAbierta> ePreguntaAbierta = ePreguntaAbiertaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ePreguntaAbierta);
    }

    /**
     * {@code DELETE  /e-pregunta-abiertas/:id} : delete the "id" ePreguntaAbierta.
     *
     * @param id the id of the ePreguntaAbierta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-pregunta-abiertas/{id}")
    public ResponseEntity<Void> deleteEPreguntaAbierta(@PathVariable Long id) {
        log.debug("REST request to delete EPreguntaAbierta : {}", id);
        ePreguntaAbiertaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
