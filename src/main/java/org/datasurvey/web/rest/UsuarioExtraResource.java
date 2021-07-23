package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.repository.UsuarioExtraRepository;
import org.datasurvey.service.MailService;
import org.datasurvey.service.UserService;
import org.datasurvey.service.UsuarioExtraQueryService;
import org.datasurvey.service.UsuarioExtraService;
import org.datasurvey.service.criteria.UsuarioExtraCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.UsuarioExtra}.
 */
@RestController
@RequestMapping("/api")
public class UsuarioExtraResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioExtraResource.class);

    private static final String ENTITY_NAME = "usuarioExtra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioExtraService usuarioExtraService;

    private final UsuarioExtraRepository usuarioExtraRepository;

    private final UsuarioExtraQueryService usuarioExtraQueryService;

    private final MailService mailService;

    private final UserService userService;

    public UsuarioExtraResource(
        UsuarioExtraService usuarioExtraService,
        UsuarioExtraRepository usuarioExtraRepository,
        UsuarioExtraQueryService usuarioExtraQueryService,
        MailService mailService,
        UserService userService
    ) {
        this.usuarioExtraService = usuarioExtraService;
        this.usuarioExtraRepository = usuarioExtraRepository;
        this.usuarioExtraQueryService = usuarioExtraQueryService;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * {@code POST  /usuario-extras} : Create a new usuarioExtra.
     *
     * @param usuarioExtra the usuarioExtra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioExtra, or with status {@code 400 (Bad Request)} if the usuarioExtra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-extras")
    public ResponseEntity<UsuarioExtra> createUsuarioExtra(@Valid @RequestBody UsuarioExtra usuarioExtra) throws URISyntaxException {
        log.debug("REST request to save UsuarioExtra : {}", usuarioExtra);
        if (usuarioExtra.getId() != null) {
            throw new BadRequestAlertException("A new usuarioExtra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsuarioExtra result = usuarioExtraService.save(usuarioExtra);
        return ResponseEntity
            .created(new URI("/api/usuario-extras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /usuario-extras/:id} : Updates an existing usuarioExtra.
     *
     * @param id the id of the usuarioExtra to save.
     * @param usuarioExtra the usuarioExtra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioExtra,
     * or with status {@code 400 (Bad Request)} if the usuarioExtra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioExtra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-extras/{id}")
    public ResponseEntity<UsuarioExtra> updateUsuarioExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioExtra usuarioExtra
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioExtra : {}, {}", id, usuarioExtra);
        if (usuarioExtra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioExtra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UsuarioExtra result = usuarioExtraService.save(usuarioExtra);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioExtra.getId().toString()))
            .body(result);
    }

    @PutMapping("/usuario-extras-estado/{id}")
    public ResponseEntity<UsuarioExtra> updateUsuarioExtraEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioExtra usuarioExtra
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioExtra : {}, {}", id, usuarioExtra);
        if (usuarioExtra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioExtra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UsuarioExtra result = usuarioExtraService.save(usuarioExtra);

        if (usuarioExtra.getEstado().name().equals("SUSPENDED")) {
            this.userService.modifyStatus(usuarioExtra.getUser().getLogin(), false);
            mailService.sendSuspendedAccountMail(usuarioExtra); //se manda el correo de la suspecion
        } else {
            this.userService.modifyStatus(usuarioExtra.getUser().getLogin(), true);
            mailService.sendActivatedAccountMail(usuarioExtra); //se manda el correo de reactivacion
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioExtra.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /usuario-extras/:id} : Partial updates given fields of an existing usuarioExtra, field will ignore if it is null
     *
     * @param id the id of the usuarioExtra to save.
     * @param usuarioExtra the usuarioExtra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioExtra,
     * or with status {@code 400 (Bad Request)} if the usuarioExtra is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioExtra is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioExtra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-extras/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UsuarioExtra> partialUpdateUsuarioExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioExtra usuarioExtra
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioExtra partially : {}, {}", id, usuarioExtra);
        if (usuarioExtra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioExtra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioExtra> result = usuarioExtraService.partialUpdate(usuarioExtra);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioExtra.getId().toString())
        );
    }

    /**
     * {@code GET  /usuario-extras} : get all the usuarioExtras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioExtras in body.
     */
    @GetMapping("/usuario-extras")
    public ResponseEntity<List<UsuarioExtra>> getAllUsuarioExtras(UsuarioExtraCriteria criteria) {
        log.debug("REST request to get UsuarioExtras by criteria: {}", criteria);
        List<UsuarioExtra> entityList = usuarioExtraQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /usuario-extras/count} : count all the usuarioExtras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/usuario-extras/count")
    public ResponseEntity<Long> countUsuarioExtras(UsuarioExtraCriteria criteria) {
        log.debug("REST request to count UsuarioExtras by criteria: {}", criteria);
        return ResponseEntity.ok().body(usuarioExtraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /usuario-extras/:id} : get the "id" usuarioExtra.
     *
     * @param id the id of the usuarioExtra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioExtra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-extras/{id}")
    public ResponseEntity<UsuarioExtra> getUsuarioExtra(@PathVariable Long id) {
        log.debug("REST request to get UsuarioExtra : {}", id);
        Optional<UsuarioExtra> usuarioExtra = usuarioExtraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioExtra);
    }

    /**
     * {@code DELETE  /usuario-extras/:id} : delete the "id" usuarioExtra.
     *
     * @param id the id of the usuarioExtra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-extras/{id}")
    public ResponseEntity<Void> deleteUsuarioExtra(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioExtra : {}", id);
        usuarioExtraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
