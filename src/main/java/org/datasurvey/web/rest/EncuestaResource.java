package org.datasurvey.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.datasurvey.domain.*;
import org.datasurvey.domain.enumeration.AccesoEncuesta;
import org.datasurvey.repository.EncuestaRepository;
import org.datasurvey.service.*;
import org.datasurvey.service.EncuestaQueryService;
import org.datasurvey.service.EncuestaService;
import org.datasurvey.service.MailService;
import org.datasurvey.service.criteria.EncuestaCriteria;
import org.datasurvey.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.datasurvey.domain.Encuesta}.
 */
@RestController
@RequestMapping("/api")
public class EncuestaResource {

    private final Logger log = LoggerFactory.getLogger(EncuestaResource.class);

    private static final String ENTITY_NAME = "encuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EncuestaService encuestaService;

    private final EncuestaRepository encuestaRepository;

    private final EncuestaQueryService encuestaQueryService;

    private final MailService mailService;

    private final EPreguntaCerradaService ePreguntaCerradaService;

    private final EPreguntaAbiertaService ePreguntaAbiertaService;

    private final EPreguntaCerradaOpcionService ePreguntaCerradaOpcionService;

    private final PlantillaService plantillaService;

    private final PPreguntaCerradaService pPreguntaCerradaService;

    private final PPreguntaAbiertaService pPreguntaAbiertaService;

    private final PPreguntaCerradaOpcionService pPreguntaCerradaOpcionService;

    public EncuestaResource(
        EncuestaService encuestaService,
        EncuestaRepository encuestaRepository,
        EncuestaQueryService encuestaQueryService,
        MailService mailService,
        EPreguntaCerradaService ePreguntaCerradaService,
        EPreguntaAbiertaService ePreguntaAbiertaService,
        EPreguntaCerradaOpcionService ePreguntaCerradaOpcionService,
        PlantillaService plantillaService,
        PPreguntaCerradaService pPreguntaCerradaService,
        PPreguntaAbiertaService pPreguntaAbiertaService,
        PPreguntaCerradaOpcionService pPreguntaCerradaOpcionService
    ) {
        this.encuestaService = encuestaService;
        this.encuestaRepository = encuestaRepository;
        this.encuestaQueryService = encuestaQueryService;
        this.mailService = mailService;
        this.ePreguntaCerradaService = ePreguntaCerradaService;
        this.ePreguntaAbiertaService = ePreguntaAbiertaService;
        this.ePreguntaCerradaOpcionService = ePreguntaCerradaOpcionService;
        this.plantillaService = plantillaService;
        this.pPreguntaCerradaService = pPreguntaCerradaService;
        this.pPreguntaAbiertaService = pPreguntaAbiertaService;
        this.pPreguntaCerradaOpcionService = pPreguntaCerradaOpcionService;
    }

    /**
     * {@code POST  /encuestas} : Create a new encuesta.
     *
     * @param encuesta the encuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new encuesta, or with status {@code 400 (Bad Request)} if the encuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/encuestas")
    public ResponseEntity<Encuesta> createEncuesta(@Valid @RequestBody Encuesta encuesta) throws URISyntaxException {
        log.debug("REST request to save Encuesta : {}", encuesta);
        if (encuesta.getId() != null) {
            throw new BadRequestAlertException("A new encuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Encuesta result = encuestaService.save(encuesta);
        return ResponseEntity
            .created(new URI("/api/encuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/encuestas/{plantillaId}")
    public ResponseEntity<Encuesta> createEncuestaFromTemplate(
        @Valid @RequestBody Encuesta encuesta,
        @PathVariable(value = "plantillaId", required = false) final Long plantillaId
    ) throws URISyntaxException {
        log.debug("REST request to save Encuesta : {}", encuesta);
        if (encuesta.getId() != null) {
            throw new BadRequestAlertException("A new encuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Copy from survey template to survey
        Optional<Plantilla> plantilla = plantillaService.findOne(plantillaId);

        if (plantilla.isPresent()) {
            encuesta.setNombre(plantilla.get().getNombre());
            encuesta.setDescripcion(plantilla.get().getDescripcion());
            encuesta.setCategoria(plantilla.get().getCategoria());

            Encuesta encuestaCreated = encuestaService.save(encuesta);

            // Preguntas cerradas
            List<PPreguntaCerrada> preguntasCerradas = pPreguntaCerradaService.findAll();
            for (PPreguntaCerrada pPreguntaCerrada : preguntasCerradas) {
                if (pPreguntaCerrada.getPlantilla().getId().equals(plantillaId)) {
                    EPreguntaCerrada newEPreguntaCerrada = new EPreguntaCerrada();
                    newEPreguntaCerrada.setNombre(pPreguntaCerrada.getNombre());
                    newEPreguntaCerrada.setTipo(pPreguntaCerrada.getTipo());
                    newEPreguntaCerrada.setOpcional(pPreguntaCerrada.getOpcional());
                    newEPreguntaCerrada.setOrden(pPreguntaCerrada.getOrden());
                    newEPreguntaCerrada.setEncuesta(encuestaCreated);

                    ePreguntaCerradaService.save(newEPreguntaCerrada);

                    // Opciones de preguntas cerradas
                    List<PPreguntaCerradaOpcion> opciones = pPreguntaCerradaOpcionService.findAll();
                    for (PPreguntaCerradaOpcion pPreguntaCerradaOpcion : opciones) {
                        if (pPreguntaCerradaOpcion.getPPreguntaCerrada().getId().equals(pPreguntaCerrada.getId())) {
                            EPreguntaCerradaOpcion newEPreguntaCerradaOpcion = new EPreguntaCerradaOpcion();
                            newEPreguntaCerradaOpcion.setNombre(pPreguntaCerradaOpcion.getNombre());
                            newEPreguntaCerradaOpcion.setOrden(pPreguntaCerradaOpcion.getOrden());
                            newEPreguntaCerradaOpcion.setCantidad(0);
                            newEPreguntaCerradaOpcion.setEPreguntaCerrada(newEPreguntaCerrada);

                            ePreguntaCerradaOpcionService.save(newEPreguntaCerradaOpcion);
                        }
                    }
                }
            }

            // Preguntas abiertas
            List<PPreguntaAbierta> preguntasAbiertas = pPreguntaAbiertaService.findAll();
            for (PPreguntaAbierta pPreguntaAbierta : preguntasAbiertas) {
                if (pPreguntaAbierta.getPlantilla().getId().equals(plantillaId)) {
                    EPreguntaAbierta newEPreguntaAbierta = new EPreguntaAbierta();
                    newEPreguntaAbierta.setNombre(pPreguntaAbierta.getNombre());
                    newEPreguntaAbierta.setOpcional(pPreguntaAbierta.getOpcional());
                    newEPreguntaAbierta.setOrden(pPreguntaAbierta.getOrden());
                    newEPreguntaAbierta.setEncuesta(encuestaCreated);

                    ePreguntaAbiertaService.save(newEPreguntaAbierta);
                }
            }

            return ResponseEntity
                .created(new URI("/api/encuestas/" + encuestaCreated.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, encuestaCreated.getId().toString()))
                .body(encuestaCreated);
        }

        return ResponseEntity.ok().body(null);
    }

    /**
     * {@code PUT  /encuestas/:id} : Updates an existing encuesta.
     *
     * @param id       the id of the encuesta to save.
     * @param encuesta the encuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encuesta,
     * or with status {@code 400 (Bad Request)} if the encuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the encuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/encuestas/{id}")
    public ResponseEntity<Encuesta> updateEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Encuesta : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Encuesta result = encuestaService.save(encuesta);

        if (encuesta.getUsuarioExtra().getUser() != null) {
            mailService.sendEncuestaDeleted(encuesta.getUsuarioExtra());
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString()))
            .body(result);
    }

    @PutMapping("/encuestas/update/{id}")
    public ResponseEntity<Encuesta> updateEncuestaReal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Encuesta : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Encuesta result = encuestaService.save(encuesta);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString()))
            .body(result);
    }

    @PutMapping("/encuestas/publish/{id}")
    public ResponseEntity<Encuesta> publishEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Encuesta : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Encuesta result = encuestaService.save(encuesta);

        if (result.getAcceso().equals(AccesoEncuesta.PRIVATE)) {
            mailService.sendPublishedPrivateMail(result.getUsuarioExtra(), result.getContrasenna());
        } else {
            mailService.sendPublishedPublicMail(result.getUsuarioExtra());
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /encuestas/:id} : Partial updates given fields of an existing encuesta, field will ignore if it is null
     *
     * @param id       the id of the encuesta to save.
     * @param encuesta the encuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encuesta,
     * or with status {@code 400 (Bad Request)} if the encuesta is not valid,
     * or with status {@code 404 (Not Found)} if the encuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the encuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/encuestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Encuesta> partialUpdateEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Encuesta encuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Encuesta partially : {}, {}", id, encuesta);
        if (encuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, encuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!encuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Encuesta> result = encuestaService.partialUpdate(encuesta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, encuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /encuestas} : get all the encuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of encuestas in body.
     */
    @GetMapping("/encuestas")
    public ResponseEntity<List<Encuesta>> getAllEncuestas(EncuestaCriteria criteria) {
        log.debug("REST request to get Encuestas by criteria: {}", criteria);
        List<Encuesta> entityList = encuestaQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/encuestas/preguntas/{id}")
    public ResponseEntity<List<Object>> getPreguntasByIdEncuesta(@PathVariable Long id) {
        List<EPreguntaCerrada> preguntasCerradas = ePreguntaCerradaService.findAll();
        List<EPreguntaAbierta> preguntasAbiertas = ePreguntaAbiertaService.findAll();
        List<Object> preguntas = Stream.concat(preguntasCerradas.stream(), preguntasAbiertas.stream()).collect(Collectors.toList());
        List<Object> preguntasFiltered = new ArrayList<>();

        for (Object obj : preguntas) {
            if (obj.getClass() == EPreguntaCerrada.class) {
                if (((EPreguntaCerrada) obj).getEncuesta() != null) {
                    if (((EPreguntaCerrada) obj).getEncuesta().getId().equals(id)) {
                        preguntasFiltered.add(obj);
                    }
                }
            } else if (obj.getClass() == EPreguntaAbierta.class) {
                if (((EPreguntaAbierta) obj).getEncuesta() != null) {
                    if (((EPreguntaAbierta) obj).getEncuesta().getId().equals(id)) {
                        preguntasFiltered.add(obj);
                    }
                }
            }
        }
        return ResponseEntity.ok().body(preguntasFiltered);
    }

    @GetMapping("/encuestas/preguntas-opciones/{id}")
    public ResponseEntity<List<List<EPreguntaCerradaOpcion>>> getPreguntaCerradaOpcionByIdEncuesta(@PathVariable Long id) {
        List<List<EPreguntaCerradaOpcion>> res = new ArrayList<>();
        List<EPreguntaCerrada> preguntasCerradas = ePreguntaCerradaService.findAll();
        List<EPreguntaCerrada> preguntasCerradasFiltered = preguntasCerradas
            .stream()
            .filter(p -> Objects.nonNull(p.getEncuesta()))
            .filter(p -> p.getEncuesta().getId().equals(id))
            .collect(Collectors.toList());
        List<EPreguntaCerradaOpcion> opciones = ePreguntaCerradaOpcionService.findAll();

        for (EPreguntaCerrada ePreguntaCerrada : preguntasCerradasFiltered) {
            long preguntaCerradaId = ePreguntaCerrada.getId();
            List<EPreguntaCerradaOpcion> opcionesFiltered = opciones
                .stream()
                .filter(o -> Objects.nonNull(o.getEPreguntaCerrada()))
                .filter(o -> o.getEPreguntaCerrada().getId().equals(preguntaCerradaId))
                .collect(Collectors.toList());
            res.add(opcionesFiltered);
        }

        return ResponseEntity.ok().body(res);
    }

    /**
     * {@code GET  /encuestas/count} : count all the encuestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/encuestas/count")
    public ResponseEntity<Long> countEncuestas(EncuestaCriteria criteria) {
        log.debug("REST request to count Encuestas by criteria: {}", criteria);
        return ResponseEntity.ok().body(encuestaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /encuestas/:id} : get the "id" encuesta.
     *
     * @param id the id of the encuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the encuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/encuestas/{id}")
    public ResponseEntity<Encuesta> getEncuesta(@PathVariable Long id) {
        log.debug("REST request to get Encuesta : {}", id);
        Optional<Encuesta> encuesta = encuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(encuesta);
    }

    /**
     * {@code DELETE  /encuestas/:id} : delete the "id" encuesta.
     *
     * @param id the id of the encuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/encuestas/{id}")
    public ResponseEntity<Void> deleteEncuesta(@PathVariable Long id) {
        log.debug("REST request to delete Encuesta : {}", id);
        encuestaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @DeleteMapping("encuestas/notify/{id}")
    public ResponseEntity<Void> notifyEncuestaDeleted(@PathVariable Long id, @Valid @RequestBody Encuesta encuesta) {
        log.debug("REST request to notify {} of deleted Encuesta", encuesta.getUsuarioExtra().getUser().getEmail());
        mailService.sendEncuestaDeleted(encuesta.getUsuarioExtra());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/encuestas/duplicate/{id}")
    public ResponseEntity<Encuesta> getAllEncuestas(@PathVariable Long id) {
        Optional<Encuesta> encuesta = encuestaService.findOne(id);
        Encuesta newEncuesta = new Encuesta();

        if (encuesta.isPresent()) {
            // Encuesta
            newEncuesta.setNombre(encuesta.get().getNombre());
            newEncuesta.setDescripcion(encuesta.get().getDescripcion());
            newEncuesta.setFechaCreacion(ZonedDateTime.now());
            newEncuesta.setCalificacion(5d);
            newEncuesta.setAcceso(encuesta.get().getAcceso());
            newEncuesta.setEstado(encuesta.get().getEstado());
            newEncuesta.setCategoria(encuesta.get().getCategoria());
            newEncuesta.setUsuarioExtra(encuesta.get().getUsuarioExtra());

            Encuesta encuestaCreated = encuestaService.save(newEncuesta);

            // Preguntas cerradas
            List<EPreguntaCerrada> preguntasCerradas = ePreguntaCerradaService.findAll();
            for (EPreguntaCerrada ePreguntaCerrada : preguntasCerradas) {
                if (ePreguntaCerrada.getEncuesta().getId().equals(id)) {
                    EPreguntaCerrada newEPreguntaCerrada = new EPreguntaCerrada();
                    newEPreguntaCerrada.setNombre(ePreguntaCerrada.getNombre());
                    newEPreguntaCerrada.setTipo(ePreguntaCerrada.getTipo());
                    newEPreguntaCerrada.setOpcional(ePreguntaCerrada.getOpcional());
                    newEPreguntaCerrada.setOrden(ePreguntaCerrada.getOrden());
                    newEPreguntaCerrada.setEncuesta(encuestaCreated);

                    ePreguntaCerradaService.save(newEPreguntaCerrada);

                    // Opciones de preguntas cerradas
                    List<EPreguntaCerradaOpcion> opciones = ePreguntaCerradaOpcionService.findAll();
                    for (EPreguntaCerradaOpcion ePreguntaCerradaOpcion : opciones) {
                        if (ePreguntaCerradaOpcion.getEPreguntaCerrada().getId().equals(ePreguntaCerrada.getId())) {
                            EPreguntaCerradaOpcion newEPreguntaCerradaOpcion = new EPreguntaCerradaOpcion();
                            newEPreguntaCerradaOpcion.setNombre(ePreguntaCerradaOpcion.getNombre());
                            newEPreguntaCerradaOpcion.setOrden(ePreguntaCerradaOpcion.getOrden());
                            newEPreguntaCerradaOpcion.setCantidad(0);
                            newEPreguntaCerradaOpcion.setEPreguntaCerrada(newEPreguntaCerrada);

                            ePreguntaCerradaOpcionService.save(newEPreguntaCerradaOpcion);
                        }
                    }
                }
            }

            // Preguntas abiertas
            List<EPreguntaAbierta> preguntasAbiertas = ePreguntaAbiertaService.findAll();
            for (EPreguntaAbierta ePreguntaAbierta : preguntasAbiertas) {
                if (ePreguntaAbierta.getEncuesta().getId().equals(id)) {
                    EPreguntaAbierta newEPreguntaAbierta = new EPreguntaAbierta();
                    newEPreguntaAbierta.setNombre(ePreguntaAbierta.getNombre());
                    newEPreguntaAbierta.setOpcional(ePreguntaAbierta.getOpcional());
                    newEPreguntaAbierta.setOrden(ePreguntaAbierta.getOrden());
                    newEPreguntaAbierta.setEncuesta(encuestaCreated);

                    ePreguntaAbiertaService.save(newEPreguntaAbierta);
                }
            }

            return ResponseEntity.ok().body(encuestaCreated);
        }

        return ResponseEntity.ok().body(null);
    }
}
