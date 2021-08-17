package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.domain.EPreguntaCerradaOpcion;
import org.datasurvey.repository.EPreguntaCerradaOpcionRepository;
import org.datasurvey.service.EPreguntaCerradaOpcionQueryService;
import org.datasurvey.service.EPreguntaCerradaOpcionService;
import org.datasurvey.service.criteria.EPreguntaCerradaOpcionCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.EPreguntaCerradaOpcion}.
 */
@RestController
@RequestMapping("/api")
public class EPreguntaCerradaOpcionResource {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaOpcionResource.class);

    private static final String ENTITY_NAME = "ePreguntaCerradaOpcion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EPreguntaCerradaOpcionService ePreguntaCerradaOpcionService;

    private final EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository;

    private final EPreguntaCerradaOpcionQueryService ePreguntaCerradaOpcionQueryService;

    public EPreguntaCerradaOpcionResource(
        EPreguntaCerradaOpcionService ePreguntaCerradaOpcionService,
        EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository,
        EPreguntaCerradaOpcionQueryService ePreguntaCerradaOpcionQueryService
    ) {
        this.ePreguntaCerradaOpcionService = ePreguntaCerradaOpcionService;
        this.ePreguntaCerradaOpcionRepository = ePreguntaCerradaOpcionRepository;
        this.ePreguntaCerradaOpcionQueryService = ePreguntaCerradaOpcionQueryService;
    }

    /**
     * {@code POST  /e-pregunta-cerrada-opcions} : Create a new ePreguntaCerradaOpcion.
     *
     * @param ePreguntaCerradaOpcion the ePreguntaCerradaOpcion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ePreguntaCerradaOpcion, or with status {@code 400 (Bad Request)} if the ePreguntaCerradaOpcion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<EPreguntaCerradaOpcion> createEPreguntaCerradaOpcion(
        @Valid @RequestBody EPreguntaCerradaOpcion ePreguntaCerradaOpcion,
        @PathVariable(value = "id", required = false) final Long id
    ) throws URISyntaxException {
        EPreguntaCerrada ePreguntaCerrada = new EPreguntaCerrada();
        ePreguntaCerrada.setId(id);
        ePreguntaCerradaOpcion.setEPreguntaCerrada(ePreguntaCerrada);

        log.debug("REST request to save EPreguntaCerradaOpcion : {}", ePreguntaCerradaOpcion);
        if (ePreguntaCerradaOpcion.getId() != null) {
            throw new BadRequestAlertException("A new ePreguntaCerradaOpcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EPreguntaCerradaOpcion result = ePreguntaCerradaOpcionService.save(ePreguntaCerradaOpcion);
        return ResponseEntity
            .created(new URI("/api/e-pregunta-cerrada-opcions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/e-pregunta-cerrada-opcions/count/{id}")
    public ResponseEntity<EPreguntaCerradaOpcion> updateOpcionCount(@PathVariable(value = "id", required = false) final Long id) {
        System.out.println(id);
        EPreguntaCerradaOpcion updatedOpcion = ePreguntaCerradaOpcionService.findOne(id).get();
        System.out.println(updatedOpcion);
        int cantidad = updatedOpcion.getCantidad() + 1;
        updatedOpcion.setCantidad(cantidad);
        this.ePreguntaCerradaOpcionService.partialUpdate(updatedOpcion);
        return ResponseEntity.ok(updatedOpcion);
    }

    /**
     * {@code PUT  /e-pregunta-cerrada-opcions/:id} : Updates an existing ePreguntaCerradaOpcion.
     *
     * @param id                     the id of the ePreguntaCerradaOpcion to save.
     * @param ePreguntaCerradaOpcion the ePreguntaCerradaOpcion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaCerradaOpcion,
     * or with status {@code 400 (Bad Request)} if the ePreguntaCerradaOpcion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaCerradaOpcion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<EPreguntaCerradaOpcion> updateEPreguntaCerradaOpcion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EPreguntaCerradaOpcion ePreguntaCerradaOpcion
    ) throws URISyntaxException {
        log.debug("REST request to update EPreguntaCerradaOpcion : {}, {}", id, ePreguntaCerradaOpcion);
        if (ePreguntaCerradaOpcion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaCerradaOpcion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaCerradaOpcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EPreguntaCerradaOpcion result = ePreguntaCerradaOpcionService.save(ePreguntaCerradaOpcion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaCerradaOpcion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /e-pregunta-cerrada-opcions/:id} : Partial updates given fields of an existing ePreguntaCerradaOpcion, field will ignore if it is null
     *
     * @param id                     the id of the ePreguntaCerradaOpcion to save.
     * @param ePreguntaCerradaOpcion the ePreguntaCerradaOpcion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ePreguntaCerradaOpcion,
     * or with status {@code 400 (Bad Request)} if the ePreguntaCerradaOpcion is not valid,
     * or with status {@code 404 (Not Found)} if the ePreguntaCerradaOpcion is not found,
     * or with status {@code 500 (Internal Server Error)} if the ePreguntaCerradaOpcion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/e-pregunta-cerrada-opcions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EPreguntaCerradaOpcion> partialUpdateEPreguntaCerradaOpcion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EPreguntaCerradaOpcion ePreguntaCerradaOpcion
    ) throws URISyntaxException {
        log.debug("REST request to partial update EPreguntaCerradaOpcion partially : {}, {}", id, ePreguntaCerradaOpcion);
        if (ePreguntaCerradaOpcion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ePreguntaCerradaOpcion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ePreguntaCerradaOpcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EPreguntaCerradaOpcion> result = ePreguntaCerradaOpcionService.partialUpdate(ePreguntaCerradaOpcion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ePreguntaCerradaOpcion.getId().toString())
        );
    }

    /**
     * {@code GET  /e-pregunta-cerrada-opcions} : get all the ePreguntaCerradaOpcions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ePreguntaCerradaOpcions in body.
     */
    @GetMapping("/e-pregunta-cerrada-opcions")
    public ResponseEntity<List<EPreguntaCerradaOpcion>> getAllEPreguntaCerradaOpcions(EPreguntaCerradaOpcionCriteria criteria) {
        log.debug("REST request to get EPreguntaCerradaOpcions by criteria: {}", criteria);
        List<EPreguntaCerradaOpcion> entityList = ePreguntaCerradaOpcionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /e-pregunta-cerrada-opcions/count} : count all the ePreguntaCerradaOpcions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/e-pregunta-cerrada-opcions/count")
    public ResponseEntity<Long> countEPreguntaCerradaOpcions(EPreguntaCerradaOpcionCriteria criteria) {
        log.debug("REST request to count EPreguntaCerradaOpcions by criteria: {}", criteria);
        return ResponseEntity.ok().body(ePreguntaCerradaOpcionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /e-pregunta-cerrada-opcions/:id} : get the "id" ePreguntaCerradaOpcion.
     *
     * @param id the id of the ePreguntaCerradaOpcion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ePreguntaCerradaOpcion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<EPreguntaCerradaOpcion> getEPreguntaCerradaOpcion(@PathVariable Long id) {
        log.debug("REST request to get EPreguntaCerradaOpcion : {}", id);
        Optional<EPreguntaCerradaOpcion> ePreguntaCerradaOpcion = ePreguntaCerradaOpcionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ePreguntaCerradaOpcion);
    }

    /**
     * {@code DELETE  /e-pregunta-cerrada-opcions/:id} : delete the "id" ePreguntaCerradaOpcion.
     *
     * @param id the id of the ePreguntaCerradaOpcion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-pregunta-cerrada-opcions/{id}")
    public ResponseEntity<Void> deleteEPreguntaCerradaOpcion(@PathVariable Long id) {
        log.debug("REST request to delete EPreguntaCerradaOpcion : {}", id);
        ePreguntaCerradaOpcionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/e-pregunta-cerrada-opcions/deleteMany")
    public ResponseEntity<Void> deleteManyEPreguntaCerradaOpcion(@Valid @RequestBody int[] ids) {
        for (int id : ids) {
            ePreguntaCerradaOpcionService.delete((long) id);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, Arrays.toString(ids)))
            .build();
    }
}
