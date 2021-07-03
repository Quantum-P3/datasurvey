package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.ParametroAplicacion;
import org.datasurvey.repository.ParametroAplicacionRepository;
import org.datasurvey.service.ParametroAplicacionQueryService;
import org.datasurvey.service.ParametroAplicacionService;
import org.datasurvey.service.criteria.ParametroAplicacionCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.ParametroAplicacion}.
 */
@RestController
@RequestMapping("/api")
public class ParametroAplicacionResource {

    private final Logger log = LoggerFactory.getLogger(ParametroAplicacionResource.class);

    private static final String ENTITY_NAME = "parametroAplicacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametroAplicacionService parametroAplicacionService;

    private final ParametroAplicacionRepository parametroAplicacionRepository;

    private final ParametroAplicacionQueryService parametroAplicacionQueryService;

    public ParametroAplicacionResource(
        ParametroAplicacionService parametroAplicacionService,
        ParametroAplicacionRepository parametroAplicacionRepository,
        ParametroAplicacionQueryService parametroAplicacionQueryService
    ) {
        this.parametroAplicacionService = parametroAplicacionService;
        this.parametroAplicacionRepository = parametroAplicacionRepository;
        this.parametroAplicacionQueryService = parametroAplicacionQueryService;
    }

    /**
     * {@code POST  /parametro-aplicacions} : Create a new parametroAplicacion.
     *
     * @param parametroAplicacion the parametroAplicacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametroAplicacion, or with status {@code 400 (Bad Request)} if the parametroAplicacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametro-aplicacions")
    public ResponseEntity<ParametroAplicacion> createParametroAplicacion(@Valid @RequestBody ParametroAplicacion parametroAplicacion)
        throws URISyntaxException {
        log.debug("REST request to save ParametroAplicacion : {}", parametroAplicacion);
        if (parametroAplicacion.getId() != null) {
            throw new BadRequestAlertException("A new parametroAplicacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParametroAplicacion result = parametroAplicacionService.save(parametroAplicacion);
        return ResponseEntity
            .created(new URI("/api/parametro-aplicacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametro-aplicacions/:id} : Updates an existing parametroAplicacion.
     *
     * @param id the id of the parametroAplicacion to save.
     * @param parametroAplicacion the parametroAplicacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroAplicacion,
     * or with status {@code 400 (Bad Request)} if the parametroAplicacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametroAplicacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametro-aplicacions/{id}")
    public ResponseEntity<ParametroAplicacion> updateParametroAplicacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParametroAplicacion parametroAplicacion
    ) throws URISyntaxException {
        log.debug("REST request to update ParametroAplicacion : {}, {}", id, parametroAplicacion);
        if (parametroAplicacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroAplicacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroAplicacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParametroAplicacion result = parametroAplicacionService.save(parametroAplicacion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametroAplicacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametro-aplicacions/:id} : Partial updates given fields of an existing parametroAplicacion, field will ignore if it is null
     *
     * @param id the id of the parametroAplicacion to save.
     * @param parametroAplicacion the parametroAplicacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametroAplicacion,
     * or with status {@code 400 (Bad Request)} if the parametroAplicacion is not valid,
     * or with status {@code 404 (Not Found)} if the parametroAplicacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametroAplicacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametro-aplicacions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParametroAplicacion> partialUpdateParametroAplicacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParametroAplicacion parametroAplicacion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParametroAplicacion partially : {}, {}", id, parametroAplicacion);
        if (parametroAplicacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametroAplicacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroAplicacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParametroAplicacion> result = parametroAplicacionService.partialUpdate(parametroAplicacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametroAplicacion.getId().toString())
        );
    }

    /**
     * {@code GET  /parametro-aplicacions} : get all the parametroAplicacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametroAplicacions in body.
     */
    @GetMapping("/parametro-aplicacions")
    public ResponseEntity<List<ParametroAplicacion>> getAllParametroAplicacions(ParametroAplicacionCriteria criteria) {
        log.debug("REST request to get ParametroAplicacions by criteria: {}", criteria);
        List<ParametroAplicacion> entityList = parametroAplicacionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /parametro-aplicacions/count} : count all the parametroAplicacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parametro-aplicacions/count")
    public ResponseEntity<Long> countParametroAplicacions(ParametroAplicacionCriteria criteria) {
        log.debug("REST request to count ParametroAplicacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(parametroAplicacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parametro-aplicacions/:id} : get the "id" parametroAplicacion.
     *
     * @param id the id of the parametroAplicacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametroAplicacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametro-aplicacions/{id}")
    public ResponseEntity<ParametroAplicacion> getParametroAplicacion(@PathVariable Long id) {
        log.debug("REST request to get ParametroAplicacion : {}", id);
        Optional<ParametroAplicacion> parametroAplicacion = parametroAplicacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametroAplicacion);
    }

    /**
     * {@code DELETE  /parametro-aplicacions/:id} : delete the "id" parametroAplicacion.
     *
     * @param id the id of the parametroAplicacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametro-aplicacions/{id}")
    public ResponseEntity<Void> deleteParametroAplicacion(@PathVariable Long id) {
        log.debug("REST request to delete ParametroAplicacion : {}", id);
        parametroAplicacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
