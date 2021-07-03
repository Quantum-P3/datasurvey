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
import org.datasurvey.domain.PPreguntaAbierta;
import org.datasurvey.domain.PPreguntaCerrada;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.domain.enumeration.EstadoPlantilla;
import org.datasurvey.repository.PlantillaRepository;
import org.datasurvey.service.criteria.PlantillaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlantillaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlantillaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_CREACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_CREACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_CREACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_FECHA_PUBLICACION_TIENDA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_PUBLICACION_TIENDA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_PUBLICACION_TIENDA = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final EstadoPlantilla DEFAULT_ESTADO = EstadoPlantilla.DRAFT;
    private static final EstadoPlantilla UPDATED_ESTADO = EstadoPlantilla.ACTIVE;

    private static final Double DEFAULT_PRECIO = 1D;
    private static final Double UPDATED_PRECIO = 2D;
    private static final Double SMALLER_PRECIO = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/plantillas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantillaMockMvc;

    private Plantilla plantilla;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plantilla createEntity(EntityManager em) {
        Plantilla plantilla = new Plantilla()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaPublicacionTienda(DEFAULT_FECHA_PUBLICACION_TIENDA)
            .estado(DEFAULT_ESTADO)
            .precio(DEFAULT_PRECIO);
        return plantilla;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plantilla createUpdatedEntity(EntityManager em) {
        Plantilla plantilla = new Plantilla()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacionTienda(UPDATED_FECHA_PUBLICACION_TIENDA)
            .estado(UPDATED_ESTADO)
            .precio(UPDATED_PRECIO);
        return plantilla;
    }

    @BeforeEach
    public void initTest() {
        plantilla = createEntity(em);
    }

    @Test
    @Transactional
    void createPlantilla() throws Exception {
        int databaseSizeBeforeCreate = plantillaRepository.findAll().size();
        // Create the Plantilla
        restPlantillaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isCreated());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeCreate + 1);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPlantilla.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testPlantilla.getFechaPublicacionTienda()).isEqualTo(DEFAULT_FECHA_PUBLICACION_TIENDA);
        assertThat(testPlantilla.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPlantilla.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void createPlantillaWithExistingId() throws Exception {
        // Create the Plantilla with an existing ID
        plantilla.setId(1L);

        int databaseSizeBeforeCreate = plantillaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantillaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaCreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantillaRepository.findAll().size();
        // set the field null
        plantilla.setFechaCreacion(null);

        // Create the Plantilla, which fails.

        restPlantillaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isBadRequest());

        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantillaRepository.findAll().size();
        // set the field null
        plantilla.setEstado(null);

        // Create the Plantilla, which fails.

        restPlantillaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isBadRequest());

        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantillaRepository.findAll().size();
        // set the field null
        plantilla.setPrecio(null);

        // Create the Plantilla, which fails.

        restPlantillaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isBadRequest());

        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlantillas() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(sameInstant(DEFAULT_FECHA_CREACION))))
            .andExpect(jsonPath("$.[*].fechaPublicacionTienda").value(hasItem(sameInstant(DEFAULT_FECHA_PUBLICACION_TIENDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    void getPlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get the plantilla
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL_ID, plantilla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plantilla.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaCreacion").value(sameInstant(DEFAULT_FECHA_CREACION)))
            .andExpect(jsonPath("$.fechaPublicacionTienda").value(sameInstant(DEFAULT_FECHA_PUBLICACION_TIENDA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    void getPlantillasByIdFiltering() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        Long id = plantilla.getId();

        defaultPlantillaShouldBeFound("id.equals=" + id);
        defaultPlantillaShouldNotBeFound("id.notEquals=" + id);

        defaultPlantillaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlantillaShouldNotBeFound("id.greaterThan=" + id);

        defaultPlantillaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlantillaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre equals to DEFAULT_NOMBRE
        defaultPlantillaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the plantillaList where nombre equals to UPDATED_NOMBRE
        defaultPlantillaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre not equals to DEFAULT_NOMBRE
        defaultPlantillaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the plantillaList where nombre not equals to UPDATED_NOMBRE
        defaultPlantillaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPlantillaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the plantillaList where nombre equals to UPDATED_NOMBRE
        defaultPlantillaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre is not null
        defaultPlantillaShouldBeFound("nombre.specified=true");

        // Get all the plantillaList where nombre is null
        defaultPlantillaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreContainsSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre contains DEFAULT_NOMBRE
        defaultPlantillaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the plantillaList where nombre contains UPDATED_NOMBRE
        defaultPlantillaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPlantillasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where nombre does not contain DEFAULT_NOMBRE
        defaultPlantillaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the plantillaList where nombre does not contain UPDATED_NOMBRE
        defaultPlantillaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultPlantillaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the plantillaList where descripcion equals to UPDATED_DESCRIPCION
        defaultPlantillaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultPlantillaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the plantillaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultPlantillaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultPlantillaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the plantillaList where descripcion equals to UPDATED_DESCRIPCION
        defaultPlantillaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion is not null
        defaultPlantillaShouldBeFound("descripcion.specified=true");

        // Get all the plantillaList where descripcion is null
        defaultPlantillaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion contains DEFAULT_DESCRIPCION
        defaultPlantillaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the plantillaList where descripcion contains UPDATED_DESCRIPCION
        defaultPlantillaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPlantillasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultPlantillaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the plantillaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultPlantillaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion equals to DEFAULT_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion not equals to DEFAULT_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.notEquals=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion not equals to UPDATED_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.notEquals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion in DEFAULT_FECHA_CREACION or UPDATED_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion equals to UPDATED_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.in=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion is not null
        defaultPlantillaShouldBeFound("fechaCreacion.specified=true");

        // Get all the plantillaList where fechaCreacion is null
        defaultPlantillaShouldNotBeFound("fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion is greater than or equal to DEFAULT_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.greaterThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion is greater than or equal to UPDATED_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.greaterThanOrEqual=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion is less than or equal to DEFAULT_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.lessThanOrEqual=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion is less than or equal to SMALLER_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.lessThanOrEqual=" + SMALLER_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsLessThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion is less than DEFAULT_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.lessThan=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion is less than UPDATED_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.lessThan=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaCreacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaCreacion is greater than DEFAULT_FECHA_CREACION
        defaultPlantillaShouldNotBeFound("fechaCreacion.greaterThan=" + DEFAULT_FECHA_CREACION);

        // Get all the plantillaList where fechaCreacion is greater than SMALLER_FECHA_CREACION
        defaultPlantillaShouldBeFound("fechaCreacion.greaterThan=" + SMALLER_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda equals to DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.equals=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda equals to UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.equals=" + UPDATED_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda not equals to DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.notEquals=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda not equals to UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.notEquals=" + UPDATED_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda in DEFAULT_FECHA_PUBLICACION_TIENDA or UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound(
            "fechaPublicacionTienda.in=" + DEFAULT_FECHA_PUBLICACION_TIENDA + "," + UPDATED_FECHA_PUBLICACION_TIENDA
        );

        // Get all the plantillaList where fechaPublicacionTienda equals to UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.in=" + UPDATED_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda is not null
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.specified=true");

        // Get all the plantillaList where fechaPublicacionTienda is null
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda is greater than or equal to DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.greaterThanOrEqual=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda is greater than or equal to UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.greaterThanOrEqual=" + UPDATED_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda is less than or equal to DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.lessThanOrEqual=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda is less than or equal to SMALLER_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.lessThanOrEqual=" + SMALLER_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsLessThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda is less than DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.lessThan=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda is less than UPDATED_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.lessThan=" + UPDATED_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByFechaPublicacionTiendaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where fechaPublicacionTienda is greater than DEFAULT_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldNotBeFound("fechaPublicacionTienda.greaterThan=" + DEFAULT_FECHA_PUBLICACION_TIENDA);

        // Get all the plantillaList where fechaPublicacionTienda is greater than SMALLER_FECHA_PUBLICACION_TIENDA
        defaultPlantillaShouldBeFound("fechaPublicacionTienda.greaterThan=" + SMALLER_FECHA_PUBLICACION_TIENDA);
    }

    @Test
    @Transactional
    void getAllPlantillasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where estado equals to DEFAULT_ESTADO
        defaultPlantillaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the plantillaList where estado equals to UPDATED_ESTADO
        defaultPlantillaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPlantillasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where estado not equals to DEFAULT_ESTADO
        defaultPlantillaShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the plantillaList where estado not equals to UPDATED_ESTADO
        defaultPlantillaShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPlantillasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultPlantillaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the plantillaList where estado equals to UPDATED_ESTADO
        defaultPlantillaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPlantillasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where estado is not null
        defaultPlantillaShouldBeFound("estado.specified=true");

        // Get all the plantillaList where estado is null
        defaultPlantillaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio equals to DEFAULT_PRECIO
        defaultPlantillaShouldBeFound("precio.equals=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio equals to UPDATED_PRECIO
        defaultPlantillaShouldNotBeFound("precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio not equals to DEFAULT_PRECIO
        defaultPlantillaShouldNotBeFound("precio.notEquals=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio not equals to UPDATED_PRECIO
        defaultPlantillaShouldBeFound("precio.notEquals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio in DEFAULT_PRECIO or UPDATED_PRECIO
        defaultPlantillaShouldBeFound("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO);

        // Get all the plantillaList where precio equals to UPDATED_PRECIO
        defaultPlantillaShouldNotBeFound("precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio is not null
        defaultPlantillaShouldBeFound("precio.specified=true");

        // Get all the plantillaList where precio is null
        defaultPlantillaShouldNotBeFound("precio.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio is greater than or equal to DEFAULT_PRECIO
        defaultPlantillaShouldBeFound("precio.greaterThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio is greater than or equal to UPDATED_PRECIO
        defaultPlantillaShouldNotBeFound("precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio is less than or equal to DEFAULT_PRECIO
        defaultPlantillaShouldBeFound("precio.lessThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio is less than or equal to SMALLER_PRECIO
        defaultPlantillaShouldNotBeFound("precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio is less than DEFAULT_PRECIO
        defaultPlantillaShouldNotBeFound("precio.lessThan=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio is less than UPDATED_PRECIO
        defaultPlantillaShouldBeFound("precio.lessThan=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList where precio is greater than DEFAULT_PRECIO
        defaultPlantillaShouldNotBeFound("precio.greaterThan=" + DEFAULT_PRECIO);

        // Get all the plantillaList where precio is greater than SMALLER_PRECIO
        defaultPlantillaShouldBeFound("precio.greaterThan=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllPlantillasByPPreguntaCerradaIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);
        PPreguntaCerrada pPreguntaCerrada = PPreguntaCerradaResourceIT.createEntity(em);
        em.persist(pPreguntaCerrada);
        em.flush();
        plantilla.addPPreguntaCerrada(pPreguntaCerrada);
        plantillaRepository.saveAndFlush(plantilla);
        Long pPreguntaCerradaId = pPreguntaCerrada.getId();

        // Get all the plantillaList where pPreguntaCerrada equals to pPreguntaCerradaId
        defaultPlantillaShouldBeFound("pPreguntaCerradaId.equals=" + pPreguntaCerradaId);

        // Get all the plantillaList where pPreguntaCerrada equals to (pPreguntaCerradaId + 1)
        defaultPlantillaShouldNotBeFound("pPreguntaCerradaId.equals=" + (pPreguntaCerradaId + 1));
    }

    @Test
    @Transactional
    void getAllPlantillasByPPreguntaAbiertaIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);
        PPreguntaAbierta pPreguntaAbierta = PPreguntaAbiertaResourceIT.createEntity(em);
        em.persist(pPreguntaAbierta);
        em.flush();
        plantilla.addPPreguntaAbierta(pPreguntaAbierta);
        plantillaRepository.saveAndFlush(plantilla);
        Long pPreguntaAbiertaId = pPreguntaAbierta.getId();

        // Get all the plantillaList where pPreguntaAbierta equals to pPreguntaAbiertaId
        defaultPlantillaShouldBeFound("pPreguntaAbiertaId.equals=" + pPreguntaAbiertaId);

        // Get all the plantillaList where pPreguntaAbierta equals to (pPreguntaAbiertaId + 1)
        defaultPlantillaShouldNotBeFound("pPreguntaAbiertaId.equals=" + (pPreguntaAbiertaId + 1));
    }

    @Test
    @Transactional
    void getAllPlantillasByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);
        Categoria categoria = CategoriaResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        plantilla.setCategoria(categoria);
        plantillaRepository.saveAndFlush(plantilla);
        Long categoriaId = categoria.getId();

        // Get all the plantillaList where categoria equals to categoriaId
        defaultPlantillaShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the plantillaList where categoria equals to (categoriaId + 1)
        defaultPlantillaShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllPlantillasByUsuarioExtraIsEqualToSomething() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);
        UsuarioExtra usuarioExtra = UsuarioExtraResourceIT.createEntity(em);
        em.persist(usuarioExtra);
        em.flush();
        plantilla.addUsuarioExtra(usuarioExtra);
        plantillaRepository.saveAndFlush(plantilla);
        Long usuarioExtraId = usuarioExtra.getId();

        // Get all the plantillaList where usuarioExtra equals to usuarioExtraId
        defaultPlantillaShouldBeFound("usuarioExtraId.equals=" + usuarioExtraId);

        // Get all the plantillaList where usuarioExtra equals to (usuarioExtraId + 1)
        defaultPlantillaShouldNotBeFound("usuarioExtraId.equals=" + (usuarioExtraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlantillaShouldBeFound(String filter) throws Exception {
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(sameInstant(DEFAULT_FECHA_CREACION))))
            .andExpect(jsonPath("$.[*].fechaPublicacionTienda").value(hasItem(sameInstant(DEFAULT_FECHA_PUBLICACION_TIENDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));

        // Check, that the count call also returns 1
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlantillaShouldNotBeFound(String filter) throws Exception {
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlantillaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlantilla() throws Exception {
        // Get the plantilla
        restPlantillaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();

        // Update the plantilla
        Plantilla updatedPlantilla = plantillaRepository.findById(plantilla.getId()).get();
        // Disconnect from session so that the updates on updatedPlantilla are not directly saved in db
        em.detach(updatedPlantilla);
        updatedPlantilla
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacionTienda(UPDATED_FECHA_PUBLICACION_TIENDA)
            .estado(UPDATED_ESTADO)
            .precio(UPDATED_PRECIO);

        restPlantillaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlantilla.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlantilla))
            )
            .andExpect(status().isOk());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPlantilla.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testPlantilla.getFechaPublicacionTienda()).isEqualTo(UPDATED_FECHA_PUBLICACION_TIENDA);
        assertThat(testPlantilla.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPlantilla.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void putNonExistingPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantilla.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantilla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantilla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantilla)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantillaWithPatch() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();

        // Update the plantilla using partial update
        Plantilla partialUpdatedPlantilla = new Plantilla();
        partialUpdatedPlantilla.setId(plantilla.getId());

        partialUpdatedPlantilla.fechaCreacion(UPDATED_FECHA_CREACION).fechaPublicacionTienda(UPDATED_FECHA_PUBLICACION_TIENDA);

        restPlantillaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantilla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantilla))
            )
            .andExpect(status().isOk());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPlantilla.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testPlantilla.getFechaPublicacionTienda()).isEqualTo(UPDATED_FECHA_PUBLICACION_TIENDA);
        assertThat(testPlantilla.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPlantilla.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void fullUpdatePlantillaWithPatch() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();

        // Update the plantilla using partial update
        Plantilla partialUpdatedPlantilla = new Plantilla();
        partialUpdatedPlantilla.setId(plantilla.getId());

        partialUpdatedPlantilla
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaPublicacionTienda(UPDATED_FECHA_PUBLICACION_TIENDA)
            .estado(UPDATED_ESTADO)
            .precio(UPDATED_PRECIO);

        restPlantillaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantilla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantilla))
            )
            .andExpect(status().isOk());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPlantilla.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testPlantilla.getFechaPublicacionTienda()).isEqualTo(UPDATED_FECHA_PUBLICACION_TIENDA);
        assertThat(testPlantilla.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPlantilla.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void patchNonExistingPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plantilla.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantilla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantilla))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();
        plantilla.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantillaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plantilla))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeDelete = plantillaRepository.findAll().size();

        // Delete the plantilla
        restPlantillaMockMvc
            .perform(delete(ENTITY_API_URL_ID, plantilla.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
