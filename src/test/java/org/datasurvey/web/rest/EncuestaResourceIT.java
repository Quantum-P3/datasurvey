package org.datasurvey.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.datasurvey.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.datasurvey.IntegrationTest;
import org.datasurvey.domain.Categoria;
import org.datasurvey.domain.EPreguntaAbierta;
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.domain.enumeration.AccesoEncuesta;
import org.datasurvey.domain.enumeration.EstadoEncuesta;
import org.datasurvey.repository.EncuestaRepository;
import org.datasurvey.service.criteria.EncuestaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EncuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EncuestaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_CREACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_CREACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_CREACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FECHA_PUBLICACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_PUBLICACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_PUBLICACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FECHA_FINALIZAR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FINALIZAR = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_FINALIZAR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FECHA_FINALIZADA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FINALIZADA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_FINALIZADA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Double DEFAULT_CALIFICACION = 1D;
    private static final Double UPDATED_CALIFICACION = 2D;
    private static final Double SMALLER_CALIFICACION = 1D - 1D;

    private static final AccesoEncuesta DEFAULT_ACCESO = AccesoEncuesta.PUBLIC;
    private static final AccesoEncuesta UPDATED_ACCESO = AccesoEncuesta.PRIVATE;

    private static final String DEFAULT_CONTRASENNA = "AAAAAAAAAA";
    private static final String UPDATED_CONTRASENNA = "BBBBBBBBBB";

    private static final EstadoEncuesta DEFAULT_ESTADO = EstadoEncuesta.DRAFT;
    private static final EstadoEncuesta UPDATED_ESTADO = EstadoEncuesta.ACTIVE;

    private static final String ENTITY_API_URL = "/api/encuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EncuestaRepository encuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEncuestaMockMvc;

    private Encuesta encuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encuesta createEntity(EntityManager em) {
        Encuesta encuesta = new Encuesta()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaPublicacion(DEFAULT_FECHA_PUBLICACION)
            .fechaFinalizar(DEFAULT_FECHA_FINALIZAR)
            .fechaFinalizada(DEFAULT_FECHA_FINALIZADA)
            .calificacion(DEFAULT_CALIFICACION)
            .acceso(DEFAULT_ACCESO)
            .contrasenna(DEFAULT_CONTRASENNA)
            .estado(DEFAULT_ESTADO);
        return encuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encuesta createUpdatedEntity(EntityManager em) {
        Encuesta encuesta = new Encuesta()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .fechaFinalizar(UPDATED_FECHA_FINALIZAR)
            .fechaFinalizada(UPDATED_FECHA_FINALIZADA)
            .calificacion(UPDATED_CALIFICACION)
            .acceso(UPDATED_ACCESO)
            .contrasenna(UPDATED_CONTRASENNA)
            .estado(UPDATED_ESTADO);
        return encuesta;
    }

    @BeforeEach
    public void initTest() {
        encuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createEncuesta() throws Exception {
        int databaseSizeBeforeCreate = encuestaRepository.findAll().size();
        // Create the Encuesta
        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isCreated());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeCreate + 1);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEncuesta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEncuesta.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testEncuesta.getFechaPublicacion()).isEqualTo(DEFAULT_FECHA_PUBLICACION);
        assertThat(testEncuesta.getFechaFinalizar()).isEqualTo(DEFAULT_FECHA_FINALIZAR);
        assertThat(testEncuesta.getFechaFinalizada()).isEqualTo(DEFAULT_FECHA_FINALIZADA);
        assertThat(testEncuesta.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testEncuesta.getAcceso()).isEqualTo(DEFAULT_ACCESO);
        assertThat(testEncuesta.getContrasenna()).isEqualTo(DEFAULT_CONTRASENNA);
        assertThat(testEncuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createEncuestaWithExistingId() throws Exception {
        // Create the Encuesta with an existing ID
        encuesta.setId(1L);

        int databaseSizeBeforeCreate = encuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = encuestaRepository.findAll().size();
        // set the field null
        encuesta.setNombre(null);

        // Create the Encuesta, which fails.

        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaCreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = encuestaRepository.findAll().size();
        // set the field null
        encuesta.setFechaCreacion(null);

        // Create the Encuesta, which fails.

        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = encuestaRepository.findAll().size();
        // set the field null
        encuesta.setCalificacion(null);

        // Create the Encuesta, which fails.

        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccesoIsRequired() throws Exception {
        int databaseSizeBeforeTest = encuestaRepository.findAll().size();
        // set the field null
        encuesta.setAcceso(null);

        // Create the Encuesta, which fails.

        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = encuestaRepository.findAll().size();
        // set the field null
        encuesta.setEstado(null);

        // Create the Encuesta, which fails.

        restEncuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isBadRequest());

        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEncuestas() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(sameInstant(DEFAULT_FECHA_CREACION))))
            .andExpect(jsonPath("$.[*].fechaPublicacion").value(hasItem(sameInstant(DEFAULT_FECHA_PUBLICACION))))
            .andExpect(jsonPath("$.[*].fechaFinalizar").value(hasItem(sameInstant(DEFAULT_FECHA_FINALIZAR))))
            .andExpect(jsonPath("$.[*].fechaFinalizada").value(hasItem(sameInstant(DEFAULT_FECHA_FINALIZADA))))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION.doubleValue())))
            .andExpect(jsonPath("$.[*].acceso").value(hasItem(DEFAULT_ACCESO.toString())))
            .andExpect(jsonPath("$.[*].contrasenna").value(hasItem(DEFAULT_CONTRASENNA)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get the encuesta
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, encuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(encuesta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaCreacion").value(sameInstant(DEFAULT_FECHA_CREACION)))
            .andExpect(jsonPath("$.fechaPublicacion").value(sameInstant(DEFAULT_FECHA_PUBLICACION)))
            .andExpect(jsonPath("$.fechaFinalizar").value(sameInstant(DEFAULT_FECHA_FINALIZAR)))
            .andExpect(jsonPath("$.fechaFinalizada").value(sameInstant(DEFAULT_FECHA_FINALIZADA)))
            .andExpect(jsonPath("$.calificacion").value(DEFAULT_CALIFICACION.doubleValue()))
            .andExpect(jsonPath("$.acceso").value(DEFAULT_ACCESO.toString()))
            .andExpect(jsonPath("$.contrasenna").value(DEFAULT_CONTRASENNA))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getEncuestasByIdFiltering() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        Long id = encuesta.getId();

        defaultEncuestaShouldBeFound("id.equals=" + id);
        defaultEncuestaShouldNotBeFound("id.notEquals=" + id);

        defaultEncuestaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEncuestaShouldNotBeFound("id.greaterThan=" + id);

        defaultEncuestaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEncuestaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre equals to DEFAULT_NOMBRE
        defaultEncuestaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the encuestaList where nombre equals to UPDATED_NOMBRE
        defaultEncuestaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre not equals to DEFAULT_NOMBRE
        defaultEncuestaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the encuestaList where nombre not equals to UPDATED_NOMBRE
        defaultEncuestaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEncuestaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the encuestaList where nombre equals to UPDATED_NOMBRE
        defaultEncuestaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre is not null
        defaultEncuestaShouldBeFound("nombre.specified=true");

        // Get all the encuestaList where nombre is null
        defaultEncuestaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre contains DEFAULT_NOMBRE
        defaultEncuestaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the encuestaList where nombre contains UPDATED_NOMBRE
        defaultEncuestaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEncuestasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nombre does not contain DEFAULT_NOMBRE
        defaultEncuestaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the encuestaList where nombre does not contain UPDATED_NOMBRE
        defaultEncuestaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultEncuestaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the encuestaList where descripcion equals to UPDATED_DESCRIPCION
        defaultEncuestaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultEncuestaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the encuestaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultEncuestaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultEncuestaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the encuestaList where descripcion equals to UPDATED_DESCRIPCION
        defaultEncuestaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion is not null
        defaultEncuestaShouldBeFound("descripcion.specified=true");

        // Get all the encuestaList where descripcion is null
        defaultEncuestaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion contains DEFAULT_DESCRIPCION
        defaultEncuestaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the encuestaList where descripcion contains UPDATED_DESCRIPCION
        defaultEncuestaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEncuestasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultEncuestaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the encuestaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultEncuestaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion not equals to DEFAULT_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.notEquals=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion not equals to UPDATED_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.notEquals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion is not null
        defaultEncuestaShouldBeFound("fechaCreacion.specified=true");

        // Get all the encuestaList where fechaCreacion is null
        defaultEncuestaShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion is greater than or equal to DEFAULT_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.greaterThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion is greater than or equal to UPDATED_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.greaterThanOrEqual=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion is less than or equal to DEFAULT_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.lessThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion is less than or equal to SMALLER_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.lessThanOrEqual=" + SMALLER_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsLessThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion is less than DEFAULT_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.lessThan=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion is less than UPDATED_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.lessThan=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaCreacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaCreacion is greater than DEFAULT_FECHA_CREACION
        defaultEncuestaShouldNotBeFound("fechaCreacion.greaterThan=" + DEFAULT_FECHA_CREACION);

        // Get all the encuestaList where fechaCreacion is greater than SMALLER_FECHA_CREACION
        defaultEncuestaShouldBeFound("fechaCreacion.greaterThan=" + SMALLER_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion equals to DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.equals=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion equals to UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.equals=" + UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion not equals to DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.notEquals=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion not equals to UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.notEquals=" + UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion in DEFAULT_FECHA_PUBLICACION or UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.in=" + DEFAULT_FECHA_PUBLICACION + "," + UPDATED_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion equals to UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.in=" + UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion is not null
        defaultEncuestaShouldBeFound("fechaPublicacion.specified=true");

        // Get all the encuestaList where fechaPublicacion is null
        defaultEncuestaShouldNotBeFound("fechaPublicacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion is greater than or equal to DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.greaterThanOrEqual=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion is greater than or equal to UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.greaterThanOrEqual=" + UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion is less than or equal to DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.lessThanOrEqual=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion is less than or equal to SMALLER_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.lessThanOrEqual=" + SMALLER_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsLessThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion is less than DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.lessThan=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion is less than UPDATED_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.lessThan=" + UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaPublicacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaPublicacion is greater than DEFAULT_FECHA_PUBLICACION
        defaultEncuestaShouldNotBeFound("fechaPublicacion.greaterThan=" + DEFAULT_FECHA_PUBLICACION);

        // Get all the encuestaList where fechaPublicacion is greater than SMALLER_FECHA_PUBLICACION
        defaultEncuestaShouldBeFound("fechaPublicacion.greaterThan=" + SMALLER_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar equals to DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.equals=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar equals to UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.equals=" + UPDATED_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar not equals to DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.notEquals=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar not equals to UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.notEquals=" + UPDATED_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar in DEFAULT_FECHA_FINALIZAR or UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.in=" + DEFAULT_FECHA_FINALIZAR + "," + UPDATED_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar equals to UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.in=" + UPDATED_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar is not null
        defaultEncuestaShouldBeFound("fechaFinalizar.specified=true");

        // Get all the encuestaList where fechaFinalizar is null
        defaultEncuestaShouldNotBeFound("fechaFinalizar.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar is greater than or equal to DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.greaterThanOrEqual=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar is greater than or equal to UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.greaterThanOrEqual=" + UPDATED_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar is less than or equal to DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.lessThanOrEqual=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar is less than or equal to SMALLER_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.lessThanOrEqual=" + SMALLER_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsLessThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar is less than DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.lessThan=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar is less than UPDATED_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.lessThan=" + UPDATED_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizarIsGreaterThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizar is greater than DEFAULT_FECHA_FINALIZAR
        defaultEncuestaShouldNotBeFound("fechaFinalizar.greaterThan=" + DEFAULT_FECHA_FINALIZAR);

        // Get all the encuestaList where fechaFinalizar is greater than SMALLER_FECHA_FINALIZAR
        defaultEncuestaShouldBeFound("fechaFinalizar.greaterThan=" + SMALLER_FECHA_FINALIZAR);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada equals to DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.equals=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada equals to UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.equals=" + UPDATED_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada not equals to DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.notEquals=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada not equals to UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.notEquals=" + UPDATED_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada in DEFAULT_FECHA_FINALIZADA or UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.in=" + DEFAULT_FECHA_FINALIZADA + "," + UPDATED_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada equals to UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.in=" + UPDATED_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada is not null
        defaultEncuestaShouldBeFound("fechaFinalizada.specified=true");

        // Get all the encuestaList where fechaFinalizada is null
        defaultEncuestaShouldNotBeFound("fechaFinalizada.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada is greater than or equal to DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.greaterThanOrEqual=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada is greater than or equal to UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.greaterThanOrEqual=" + UPDATED_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada is less than or equal to DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.lessThanOrEqual=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada is less than or equal to SMALLER_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.lessThanOrEqual=" + SMALLER_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsLessThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada is less than DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.lessThan=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada is less than UPDATED_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.lessThan=" + UPDATED_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaFinalizadaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fechaFinalizada is greater than DEFAULT_FECHA_FINALIZADA
        defaultEncuestaShouldNotBeFound("fechaFinalizada.greaterThan=" + DEFAULT_FECHA_FINALIZADA);

        // Get all the encuestaList where fechaFinalizada is greater than SMALLER_FECHA_FINALIZADA
        defaultEncuestaShouldBeFound("fechaFinalizada.greaterThan=" + SMALLER_FECHA_FINALIZADA);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion equals to DEFAULT_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.equals=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion equals to UPDATED_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.equals=" + UPDATED_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion not equals to DEFAULT_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.notEquals=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion not equals to UPDATED_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.notEquals=" + UPDATED_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion in DEFAULT_CALIFICACION or UPDATED_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.in=" + DEFAULT_CALIFICACION + "," + UPDATED_CALIFICACION);

        // Get all the encuestaList where calificacion equals to UPDATED_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.in=" + UPDATED_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion is not null
        defaultEncuestaShouldBeFound("calificacion.specified=true");

        // Get all the encuestaList where calificacion is null
        defaultEncuestaShouldNotBeFound("calificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion is greater than or equal to DEFAULT_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.greaterThanOrEqual=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion is greater than or equal to UPDATED_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.greaterThanOrEqual=" + UPDATED_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion is less than or equal to DEFAULT_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.lessThanOrEqual=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion is less than or equal to SMALLER_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.lessThanOrEqual=" + SMALLER_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsLessThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion is less than DEFAULT_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.lessThan=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion is less than UPDATED_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.lessThan=" + UPDATED_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalificacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calificacion is greater than DEFAULT_CALIFICACION
        defaultEncuestaShouldNotBeFound("calificacion.greaterThan=" + DEFAULT_CALIFICACION);

        // Get all the encuestaList where calificacion is greater than SMALLER_CALIFICACION
        defaultEncuestaShouldBeFound("calificacion.greaterThan=" + SMALLER_CALIFICACION);
    }

    @Test
    @Transactional
    void getAllEncuestasByAccesoIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where acceso equals to DEFAULT_ACCESO
        defaultEncuestaShouldBeFound("acceso.equals=" + DEFAULT_ACCESO);

        // Get all the encuestaList where acceso equals to UPDATED_ACCESO
        defaultEncuestaShouldNotBeFound("acceso.equals=" + UPDATED_ACCESO);
    }

    @Test
    @Transactional
    void getAllEncuestasByAccesoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where acceso not equals to DEFAULT_ACCESO
        defaultEncuestaShouldNotBeFound("acceso.notEquals=" + DEFAULT_ACCESO);

        // Get all the encuestaList where acceso not equals to UPDATED_ACCESO
        defaultEncuestaShouldBeFound("acceso.notEquals=" + UPDATED_ACCESO);
    }

    @Test
    @Transactional
    void getAllEncuestasByAccesoIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where acceso in DEFAULT_ACCESO or UPDATED_ACCESO
        defaultEncuestaShouldBeFound("acceso.in=" + DEFAULT_ACCESO + "," + UPDATED_ACCESO);

        // Get all the encuestaList where acceso equals to UPDATED_ACCESO
        defaultEncuestaShouldNotBeFound("acceso.in=" + UPDATED_ACCESO);
    }

    @Test
    @Transactional
    void getAllEncuestasByAccesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where acceso is not null
        defaultEncuestaShouldBeFound("acceso.specified=true");

        // Get all the encuestaList where acceso is null
        defaultEncuestaShouldNotBeFound("acceso.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna equals to DEFAULT_CONTRASENNA
        defaultEncuestaShouldBeFound("contrasenna.equals=" + DEFAULT_CONTRASENNA);

        // Get all the encuestaList where contrasenna equals to UPDATED_CONTRASENNA
        defaultEncuestaShouldNotBeFound("contrasenna.equals=" + UPDATED_CONTRASENNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna not equals to DEFAULT_CONTRASENNA
        defaultEncuestaShouldNotBeFound("contrasenna.notEquals=" + DEFAULT_CONTRASENNA);

        // Get all the encuestaList where contrasenna not equals to UPDATED_CONTRASENNA
        defaultEncuestaShouldBeFound("contrasenna.notEquals=" + UPDATED_CONTRASENNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna in DEFAULT_CONTRASENNA or UPDATED_CONTRASENNA
        defaultEncuestaShouldBeFound("contrasenna.in=" + DEFAULT_CONTRASENNA + "," + UPDATED_CONTRASENNA);

        // Get all the encuestaList where contrasenna equals to UPDATED_CONTRASENNA
        defaultEncuestaShouldNotBeFound("contrasenna.in=" + UPDATED_CONTRASENNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna is not null
        defaultEncuestaShouldBeFound("contrasenna.specified=true");

        // Get all the encuestaList where contrasenna is null
        defaultEncuestaShouldNotBeFound("contrasenna.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna contains DEFAULT_CONTRASENNA
        defaultEncuestaShouldBeFound("contrasenna.contains=" + DEFAULT_CONTRASENNA);

        // Get all the encuestaList where contrasenna contains UPDATED_CONTRASENNA
        defaultEncuestaShouldNotBeFound("contrasenna.contains=" + UPDATED_CONTRASENNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByContrasennaNotContainsSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where contrasenna does not contain DEFAULT_CONTRASENNA
        defaultEncuestaShouldNotBeFound("contrasenna.doesNotContain=" + DEFAULT_CONTRASENNA);

        // Get all the encuestaList where contrasenna does not contain UPDATED_CONTRASENNA
        defaultEncuestaShouldBeFound("contrasenna.doesNotContain=" + UPDATED_CONTRASENNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where estado equals to DEFAULT_ESTADO
        defaultEncuestaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the encuestaList where estado equals to UPDATED_ESTADO
        defaultEncuestaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEncuestasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where estado not equals to DEFAULT_ESTADO
        defaultEncuestaShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the encuestaList where estado not equals to UPDATED_ESTADO
        defaultEncuestaShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEncuestasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultEncuestaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the encuestaList where estado equals to UPDATED_ESTADO
        defaultEncuestaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllEncuestasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where estado is not null
        defaultEncuestaShouldBeFound("estado.specified=true");

        // Get all the encuestaList where estado is null
        defaultEncuestaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByUsuarioEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        UsuarioEncuesta usuarioEncuesta = UsuarioEncuestaResourceIT.createEntity(em);
        em.persist(usuarioEncuesta);
        em.flush();
        encuesta.addUsuarioEncuesta(usuarioEncuesta);
        encuestaRepository.saveAndFlush(encuesta);
        Long usuarioEncuestaId = usuarioEncuesta.getId();

        // Get all the encuestaList where usuarioEncuesta equals to usuarioEncuestaId
        defaultEncuestaShouldBeFound("usuarioEncuestaId.equals=" + usuarioEncuestaId);

        // Get all the encuestaList where usuarioEncuesta equals to (usuarioEncuestaId + 1)
        defaultEncuestaShouldNotBeFound("usuarioEncuestaId.equals=" + (usuarioEncuestaId + 1));
    }

    @Test
    @Transactional
    void getAllEncuestasByEPreguntaAbiertaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        EPreguntaAbierta ePreguntaAbierta = EPreguntaAbiertaResourceIT.createEntity(em);
        em.persist(ePreguntaAbierta);
        em.flush();
        encuesta.addEPreguntaAbierta(ePreguntaAbierta);
        encuestaRepository.saveAndFlush(encuesta);
        Long ePreguntaAbiertaId = ePreguntaAbierta.getId();

        // Get all the encuestaList where ePreguntaAbierta equals to ePreguntaAbiertaId
        defaultEncuestaShouldBeFound("ePreguntaAbiertaId.equals=" + ePreguntaAbiertaId);

        // Get all the encuestaList where ePreguntaAbierta equals to (ePreguntaAbiertaId + 1)
        defaultEncuestaShouldNotBeFound("ePreguntaAbiertaId.equals=" + (ePreguntaAbiertaId + 1));
    }

    @Test
    @Transactional
    void getAllEncuestasByEPreguntaCerradaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        EPreguntaCerrada ePreguntaCerrada = EPreguntaCerradaResourceIT.createEntity(em);
        em.persist(ePreguntaCerrada);
        em.flush();
        encuesta.addEPreguntaCerrada(ePreguntaCerrada);
        encuestaRepository.saveAndFlush(encuesta);
        Long ePreguntaCerradaId = ePreguntaCerrada.getId();

        // Get all the encuestaList where ePreguntaCerrada equals to ePreguntaCerradaId
        defaultEncuestaShouldBeFound("ePreguntaCerradaId.equals=" + ePreguntaCerradaId);

        // Get all the encuestaList where ePreguntaCerrada equals to (ePreguntaCerradaId + 1)
        defaultEncuestaShouldNotBeFound("ePreguntaCerradaId.equals=" + (ePreguntaCerradaId + 1));
    }

    @Test
    @Transactional
    void getAllEncuestasByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        Categoria categoria = CategoriaResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        encuesta.setCategoria(categoria);
        encuestaRepository.saveAndFlush(encuesta);
        Long categoriaId = categoria.getId();

        // Get all the encuestaList where categoria equals to categoriaId
        defaultEncuestaShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the encuestaList where categoria equals to (categoriaId + 1)
        defaultEncuestaShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllEncuestasByUsuarioExtraIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        UsuarioExtra usuarioExtra = UsuarioExtraResourceIT.createEntity(em);
        em.persist(usuarioExtra);
        em.flush();
        encuesta.setUsuarioExtra(usuarioExtra);
        encuestaRepository.saveAndFlush(encuesta);
        Long usuarioExtraId = usuarioExtra.getId();

        // Get all the encuestaList where usuarioExtra equals to usuarioExtraId
        defaultEncuestaShouldBeFound("usuarioExtraId.equals=" + usuarioExtraId);

        // Get all the encuestaList where usuarioExtra equals to (usuarioExtraId + 1)
        defaultEncuestaShouldNotBeFound("usuarioExtraId.equals=" + (usuarioExtraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEncuestaShouldBeFound(String filter) throws Exception {
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(sameInstant(DEFAULT_FECHA_CREACION))))
            .andExpect(jsonPath("$.[*].fechaPublicacion").value(hasItem(sameInstant(DEFAULT_FECHA_PUBLICACION))))
            .andExpect(jsonPath("$.[*].fechaFinalizar").value(hasItem(sameInstant(DEFAULT_FECHA_FINALIZAR))))
            .andExpect(jsonPath("$.[*].fechaFinalizada").value(hasItem(sameInstant(DEFAULT_FECHA_FINALIZADA))))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION.doubleValue())))
            .andExpect(jsonPath("$.[*].acceso").value(hasItem(DEFAULT_ACCESO.toString())))
            .andExpect(jsonPath("$.[*].contrasenna").value(hasItem(DEFAULT_CONTRASENNA)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEncuestaShouldNotBeFound(String filter) throws Exception {
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEncuesta() throws Exception {
        // Get the encuesta
        restEncuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta
        Encuesta updatedEncuesta = encuestaRepository.findById(encuesta.getId()).get();
        // Disconnect from session so that the updates on updatedEncuesta are not directly saved in db
        em.detach(updatedEncuesta);
        updatedEncuesta
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .fechaFinalizar(UPDATED_FECHA_FINALIZAR)
            .fechaFinalizada(UPDATED_FECHA_FINALIZADA)
            .calificacion(UPDATED_CALIFICACION)
            .acceso(UPDATED_ACCESO)
            .contrasenna(UPDATED_CONTRASENNA)
            .estado(UPDATED_ESTADO);

        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEncuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEncuesta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEncuesta.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testEncuesta.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
        assertThat(testEncuesta.getFechaFinalizar()).isEqualTo(UPDATED_FECHA_FINALIZAR);
        assertThat(testEncuesta.getFechaFinalizada()).isEqualTo(UPDATED_FECHA_FINALIZADA);
        assertThat(testEncuesta.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testEncuesta.getAcceso()).isEqualTo(UPDATED_ACCESO);
        assertThat(testEncuesta.getContrasenna()).isEqualTo(UPDATED_CONTRASENNA);
        assertThat(testEncuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEncuestaWithPatch() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta using partial update
        Encuesta partialUpdatedEncuesta = new Encuesta();
        partialUpdatedEncuesta.setId(encuesta.getId());

        partialUpdatedEncuesta
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .fechaFinalizar(UPDATED_FECHA_FINALIZAR)
            .fechaFinalizada(UPDATED_FECHA_FINALIZADA);

        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEncuesta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEncuesta.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testEncuesta.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
        assertThat(testEncuesta.getFechaFinalizar()).isEqualTo(UPDATED_FECHA_FINALIZAR);
        assertThat(testEncuesta.getFechaFinalizada()).isEqualTo(UPDATED_FECHA_FINALIZADA);
        assertThat(testEncuesta.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testEncuesta.getAcceso()).isEqualTo(DEFAULT_ACCESO);
        assertThat(testEncuesta.getContrasenna()).isEqualTo(DEFAULT_CONTRASENNA);
        assertThat(testEncuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateEncuestaWithPatch() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta using partial update
        Encuesta partialUpdatedEncuesta = new Encuesta();
        partialUpdatedEncuesta.setId(encuesta.getId());

        partialUpdatedEncuesta
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .fechaFinalizar(UPDATED_FECHA_FINALIZAR)
            .fechaFinalizada(UPDATED_FECHA_FINALIZADA)
            .calificacion(UPDATED_CALIFICACION)
            .acceso(UPDATED_ACCESO)
            .contrasenna(UPDATED_CONTRASENNA)
            .estado(UPDATED_ESTADO);

        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEncuesta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEncuesta.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testEncuesta.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
        assertThat(testEncuesta.getFechaFinalizar()).isEqualTo(UPDATED_FECHA_FINALIZAR);
        assertThat(testEncuesta.getFechaFinalizada()).isEqualTo(UPDATED_FECHA_FINALIZADA);
        assertThat(testEncuesta.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testEncuesta.getAcceso()).isEqualTo(UPDATED_ACCESO);
        assertThat(testEncuesta.getContrasenna()).isEqualTo(UPDATED_CONTRASENNA);
        assertThat(testEncuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, encuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(encuesta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeDelete = encuestaRepository.findAll().size();

        // Delete the encuesta
        restEncuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, encuesta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
