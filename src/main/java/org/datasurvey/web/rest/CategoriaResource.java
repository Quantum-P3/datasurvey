package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.Categoria;
import org.datasurvey.repository.CategoriaRepository;
import org.datasurvey.service.CategoriaQueryService;
import org.datasurvey.service.CategoriaService;
import org.datasurvey.service.criteria.CategoriaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.Categoria}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    private static final String ENTITY_NAME = "categoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaService categoriaService;

    private final CategoriaRepository categoriaRepository;

    private final CategoriaQueryService categoriaQueryService;

    public CategoriaResource(
        CategoriaService categoriaService,
        CategoriaRepository categoriaRepository,
        CategoriaQueryService categoriaQueryService
    ) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
        this.categoriaQueryService = categoriaQueryService;
    }

    /**
     * {@code POST  /categorias} : Create a new categoria.
     *
     * @param categoria the categoria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoria, or with status {@code 400 (Bad Request)} if the categoria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoria);
        if (categoria.getId() != null) {
            throw new BadRequestAlertException("A new categoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categoria result = categoriaService.save(categoria);
        return ResponseEntity
            .created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorias/:id} : Updates an existing categoria.
     *
     * @param id the id of the categoria to save.
     * @param categoria the categoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoria,
     * or with status {@code 400 (Bad Request)} if the categoria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> updateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Categoria categoria
    ) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}, {}", id, categoria);
        if (categoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Categoria result = categoriaService.save(categoria);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoria.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorias/:id} : Partial updates given fields of an existing categoria, field will ignore if it is null
     *
     * @param id the id of the categoria to save.
     * @param categoria the categoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoria,
     * or with status {@code 400 (Bad Request)} if the categoria is not valid,
     * or with status {@code 404 (Not Found)} if the categoria is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Categoria> partialUpdateCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Categoria categoria
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categoria partially : {}, {}", id, categoria);
        if (categoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Categoria> result = categoriaService.partialUpdate(categoria);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoria.getId().toString())
        );
    }

    /**
     * {@code GET  /categorias} : get all the categorias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorias in body.
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getAllCategorias(CategoriaCriteria criteria) {
        log.debug("REST request to get Categorias by criteria: {}", criteria);
        List<Categoria> entityList = categoriaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /categorias/count} : count all the categorias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categorias/count")
    public ResponseEntity<Long> countCategorias(CategoriaCriteria criteria) {
        log.debug("REST request to count Categorias by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoriaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categorias/:id} : get the "id" categoria.
     *
     * @param id the id of the categoria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Optional<Categoria> categoria = categoriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoria);
    }

    /**
     * {@code DELETE  /categorias/:id} : delete the "id" categoria.
     *
     * @param id the id of the categoria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
