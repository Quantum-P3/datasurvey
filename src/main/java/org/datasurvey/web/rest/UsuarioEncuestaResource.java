package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.repository.UsuarioEncuestaRepository;
import org.datasurvey.service.*;
import org.datasurvey.service.criteria.UsuarioEncuestaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.UsuarioEncuesta}.
 */
@RestController
@RequestMapping("/api")
public class UsuarioEncuestaResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioEncuestaResource.class);

    private static final String ENTITY_NAME = "usuarioEncuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioEncuestaService usuarioEncuestaService;
    private final UsuarioExtraService usuarioExtraService;
    private final EncuestaService encuestaService;

    private final UsuarioEncuestaRepository usuarioEncuestaRepository;

    private final UsuarioEncuestaQueryService usuarioEncuestaQueryService;

    private final MailService mailService;

    public UsuarioEncuestaResource(
        UsuarioEncuestaService usuarioEncuestaService,
        UsuarioEncuestaRepository usuarioEncuestaRepository,
        UsuarioEncuestaQueryService usuarioEncuestaQueryService,
        UsuarioExtraService usuarioExtraService,
        EncuestaService encuestaService,
        MailService mailService
    ) {
        this.usuarioEncuestaService = usuarioEncuestaService;
        this.usuarioEncuestaRepository = usuarioEncuestaRepository;
        this.usuarioEncuestaQueryService = usuarioEncuestaQueryService;
        this.usuarioExtraService = usuarioExtraService;
        this.encuestaService = encuestaService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /usuario-encuestas} : Create a new usuarioEncuesta.
     *
     * @param usuarioEncuesta the usuarioEncuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioEncuesta, or with status {@code 400 (Bad Request)} if the usuarioEncuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-encuestas")
    public ResponseEntity<UsuarioEncuesta> createUsuarioEncuesta(@Valid @RequestBody UsuarioEncuesta usuarioEncuesta)
        throws URISyntaxException {
        log.debug("REST request to save UsuarioEncuesta : {}", usuarioEncuesta);
        if (usuarioEncuesta.getId() != null) {
            throw new BadRequestAlertException("A new usuarioEncuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsuarioEncuesta result = usuarioEncuestaService.save(usuarioEncuesta);
        if (result.getId() != null) {
            mailService.sendInvitationColaborator(usuarioEncuesta);
        }
        return ResponseEntity
            .created(new URI("/api/usuario-encuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /usuario-encuestas/:id} : Updates an existing usuarioEncuesta.
     *
     * @param id the id of the usuarioEncuesta to save.
     * @param usuarioEncuesta the usuarioEncuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioEncuesta,
     * or with status {@code 400 (Bad Request)} if the usuarioEncuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioEncuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-encuestas/{id}")
    public ResponseEntity<UsuarioEncuesta> updateUsuarioEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioEncuesta usuarioEncuesta
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioEncuesta : {}, {}", id, usuarioEncuesta);
        if (usuarioEncuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioEncuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioEncuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UsuarioEncuesta result = usuarioEncuestaService.save(usuarioEncuesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioEncuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /usuario-encuestas/:id} : Partial updates given fields of an existing usuarioEncuesta, field will ignore if it is null
     *
     * @param id the id of the usuarioEncuesta to save.
     * @param usuarioEncuesta the usuarioEncuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioEncuesta,
     * or with status {@code 400 (Bad Request)} if the usuarioEncuesta is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioEncuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioEncuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-encuestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UsuarioEncuesta> partialUpdateUsuarioEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioEncuesta usuarioEncuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioEncuesta partially : {}, {}", id, usuarioEncuesta);
        if (usuarioEncuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioEncuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioEncuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioEncuesta> result = usuarioEncuestaService.partialUpdate(usuarioEncuesta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioEncuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /usuario-encuestas} : get all the usuarioEncuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioEncuestas in body.
     */
    @GetMapping("/usuario-encuestas")
    public ResponseEntity<List<UsuarioEncuesta>> getAllUsuarioEncuestas(UsuarioEncuestaCriteria criteria) {
        log.debug("REST request to get UsuarioEncuestas by criteria: {}", criteria);
        List<UsuarioEncuesta> entityList = usuarioEncuestaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /usuario-encuestas/count} : count all the usuarioEncuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/usuario-encuestas/count")
    public ResponseEntity<Long> countUsuarioEncuestas(UsuarioEncuestaCriteria criteria) {
        log.debug("REST request to count UsuarioEncuestas by criteria: {}", criteria);
        return ResponseEntity.ok().body(usuarioEncuestaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /usuario-encuestas/:id} : get the "id" usuarioEncuesta.
     *
     * @param id the id of the usuarioEncuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioEncuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-encuestas/{id}")
    public ResponseEntity<UsuarioEncuesta> getUsuarioEncuesta(@PathVariable Long id) {
        log.debug("REST request to get UsuarioEncuesta : {}", id);
        Optional<UsuarioEncuesta> usuarioEncuesta = usuarioEncuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioEncuesta);
    }

    /**
     * {@code DELETE  /usuario-encuestas/:id} : delete the "id" usuarioEncuesta.
     *
     * @param id the id of the usuarioEncuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-encuestas/{id}")
    public ResponseEntity<Void> deleteUsuarioEncuesta(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioEncuesta : {}", id);
        Optional<UsuarioEncuesta> usuarioEncuesta = usuarioEncuestaService.findOne(id);
        usuarioEncuestaService.delete(id);
        if (usuarioEncuesta != null) {
            mailService.sendNotifyDeleteColaborator(usuarioEncuesta.get());
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/usuario-encuestas/encuesta/{id}")
    public ResponseEntity<List<UsuarioEncuesta>> getColaboradores(@PathVariable Long id) {
        List<UsuarioExtra> usuariosExtras = usuarioExtraService.findAll();
        List<UsuarioEncuesta> usuariosEncuestas = usuarioEncuestaService
            .findAll()
            .stream()
            .filter(uE -> Objects.nonNull(uE.getEncuesta()))
            .filter(uE -> uE.getEncuesta().getId().equals(id))
            .collect(Collectors.toList());

        for (UsuarioEncuesta usuarioEncuesta : usuariosEncuestas) {
            long usuarioExtraId = usuarioEncuesta.getUsuarioExtra().getId();
            UsuarioExtra usuarioExtra = usuariosExtras.stream().filter(u -> u.getId() == usuarioExtraId).findFirst().get();
            usuarioEncuesta.getUsuarioExtra().setNombre(usuarioExtra.getNombre());
            usuarioEncuesta.getUsuarioExtra().setIconoPerfil(usuarioExtra.getIconoPerfil());
        }
        return ResponseEntity.ok().body(usuariosEncuestas);
    }

    @PostMapping("/usuario-encuestas/notify/{id}")
    public ResponseEntity<Void> notifyInvitationColaborator(@PathVariable Long id, @Valid @RequestBody UsuarioEncuesta usuarioEncuesta) {
        log.debug("REST request to notify {} of invitation to Encuesta", usuarioEncuesta.getUsuarioExtra().getUser().getEmail());
        mailService.sendInvitationColaborator(usuarioEncuesta);
        return ResponseEntity.noContent().build();
    }
}
