package org.datasurvey.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.datasurvey.IntegrationTest;
import org.datasurvey.domain.ParametroAplicacion;
import org.datasurvey.repository.ParametroAplicacionRepository;
import org.datasurvey.service.criteria.ParametroAplicacionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParametroAplicacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParametroAplicacionResourceIT {

    private static final Integer DEFAULT_MAX_DIAS_ENCUESTA = 1;
    private static final Integer UPDATED_MAX_DIAS_ENCUESTA = 2;
    private static final Integer SMALLER_MAX_DIAS_ENCUESTA = 1 - 1;

    private static final Integer DEFAULT_MIN_DIAS_ENCUESTA = 1;
    private static final Integer UPDATED_MIN_DIAS_ENCUESTA = 2;
    private static final Integer SMALLER_MIN_DIAS_ENCUESTA = 1 - 1;

    private static final Integer DEFAULT_MAX_CANTIDAD_PREGUNTAS = 1;
    private static final Integer UPDATED_MAX_CANTIDAD_PREGUNTAS = 2;
    private static final Integer SMALLER_MAX_CANTIDAD_PREGUNTAS = 1 - 1;

    private static final Integer DEFAULT_MIN_CANTIDAD_PREGUNTAS = 1;
    private static final Integer UPDATED_MIN_CANTIDAD_PREGUNTAS = 2;
    private static final Integer SMALLER_MIN_CANTIDAD_PREGUNTAS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/parametro-aplicacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParametroAplicacionRepository parametroAplicacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametroAplicacionMockMvc;

    private ParametroAplicacion parametroAplicacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametroAplicacion createEntity(EntityManager em) {
        ParametroAplicacion parametroAplicacion = new ParametroAplicacion()
            .maxDiasEncuesta(DEFAULT_MAX_DIAS_ENCUESTA)
            .minDiasEncuesta(DEFAULT_MIN_DIAS_ENCUESTA)
            .maxCantidadPreguntas(DEFAULT_MAX_CANTIDAD_PREGUNTAS)
            .minCantidadPreguntas(DEFAULT_MIN_CANTIDAD_PREGUNTAS);
        return parametroAplicacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParametroAplicacion createUpdatedEntity(EntityManager em) {
        ParametroAplicacion parametroAplicacion = new ParametroAplicacion()
            .maxDiasEncuesta(UPDATED_MAX_DIAS_ENCUESTA)
            .minDiasEncuesta(UPDATED_MIN_DIAS_ENCUESTA)
            .maxCantidadPreguntas(UPDATED_MAX_CANTIDAD_PREGUNTAS)
            .minCantidadPreguntas(UPDATED_MIN_CANTIDAD_PREGUNTAS);
        return parametroAplicacion;
    }

    @BeforeEach
    public void initTest() {
        parametroAplicacion = createEntity(em);
    }

    @Test
    @Transactional
    void createParametroAplicacion() throws Exception {
        int databaseSizeBeforeCreate = parametroAplicacionRepository.findAll().size();
        // Create the ParametroAplicacion
        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isCreated());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeCreate + 1);
        ParametroAplicacion testParametroAplicacion = parametroAplicacionList.get(parametroAplicacionList.size() - 1);
        assertThat(testParametroAplicacion.getMaxDiasEncuesta()).isEqualTo(DEFAULT_MAX_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMinDiasEncuesta()).isEqualTo(DEFAULT_MIN_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMaxCantidadPreguntas()).isEqualTo(DEFAULT_MAX_CANTIDAD_PREGUNTAS);
        assertThat(testParametroAplicacion.getMinCantidadPreguntas()).isEqualTo(DEFAULT_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void createParametroAplicacionWithExistingId() throws Exception {
        // Create the ParametroAplicacion with an existing ID
        parametroAplicacion.setId(1L);

        int databaseSizeBeforeCreate = parametroAplicacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMaxDiasEncuestaIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroAplicacionRepository.findAll().size();
        // set the field null
        parametroAplicacion.setMaxDiasEncuesta(null);

        // Create the ParametroAplicacion, which fails.

        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinDiasEncuestaIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroAplicacionRepository.findAll().size();
        // set the field null
        parametroAplicacion.setMinDiasEncuesta(null);

        // Create the ParametroAplicacion, which fails.

        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxCantidadPreguntasIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroAplicacionRepository.findAll().size();
        // set the field null
        parametroAplicacion.setMaxCantidadPreguntas(null);

        // Create the ParametroAplicacion, which fails.

        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinCantidadPreguntasIsRequired() throws Exception {
        int databaseSizeBeforeTest = parametroAplicacionRepository.findAll().size();
        // set the field null
        parametroAplicacion.setMinCantidadPreguntas(null);

        // Create the ParametroAplicacion, which fails.

        restParametroAplicacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParametroAplicacions() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametroAplicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxDiasEncuesta").value(hasItem(DEFAULT_MAX_DIAS_ENCUESTA)))
            .andExpect(jsonPath("$.[*].minDiasEncuesta").value(hasItem(DEFAULT_MIN_DIAS_ENCUESTA)))
            .andExpect(jsonPath("$.[*].maxCantidadPreguntas").value(hasItem(DEFAULT_MAX_CANTIDAD_PREGUNTAS)))
            .andExpect(jsonPath("$.[*].minCantidadPreguntas").value(hasItem(DEFAULT_MIN_CANTIDAD_PREGUNTAS)));
    }

    @Test
    @Transactional
    void getParametroAplicacion() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get the parametroAplicacion
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL_ID, parametroAplicacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametroAplicacion.getId().intValue()))
            .andExpect(jsonPath("$.maxDiasEncuesta").value(DEFAULT_MAX_DIAS_ENCUESTA))
            .andExpect(jsonPath("$.minDiasEncuesta").value(DEFAULT_MIN_DIAS_ENCUESTA))
            .andExpect(jsonPath("$.maxCantidadPreguntas").value(DEFAULT_MAX_CANTIDAD_PREGUNTAS))
            .andExpect(jsonPath("$.minCantidadPreguntas").value(DEFAULT_MIN_CANTIDAD_PREGUNTAS));
    }

    @Test
    @Transactional
    void getParametroAplicacionsByIdFiltering() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        Long id = parametroAplicacion.getId();

        defaultParametroAplicacionShouldBeFound("id.equals=" + id);
        defaultParametroAplicacionShouldNotBeFound("id.notEquals=" + id);

        defaultParametroAplicacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParametroAplicacionShouldNotBeFound("id.greaterThan=" + id);

        defaultParametroAplicacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParametroAplicacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta equals to DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.equals=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta equals to UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.equals=" + UPDATED_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta not equals to DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.notEquals=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta not equals to UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.notEquals=" + UPDATED_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsInShouldWork() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta in DEFAULT_MAX_DIAS_ENCUESTA or UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.in=" + DEFAULT_MAX_DIAS_ENCUESTA + "," + UPDATED_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta equals to UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.in=" + UPDATED_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta is not null
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.specified=true");

        // Get all the parametroAplicacionList where maxDiasEncuesta is null
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.specified=false");
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta is greater than or equal to DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.greaterThanOrEqual=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta is greater than or equal to UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.greaterThanOrEqual=" + UPDATED_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta is less than or equal to DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.lessThanOrEqual=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta is less than or equal to SMALLER_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.lessThanOrEqual=" + SMALLER_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsLessThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta is less than DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.lessThan=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta is less than UPDATED_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.lessThan=" + UPDATED_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxDiasEncuestaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxDiasEncuesta is greater than DEFAULT_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("maxDiasEncuesta.greaterThan=" + DEFAULT_MAX_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where maxDiasEncuesta is greater than SMALLER_MAX_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("maxDiasEncuesta.greaterThan=" + SMALLER_MAX_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta equals to DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.equals=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta equals to UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.equals=" + UPDATED_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta not equals to DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.notEquals=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta not equals to UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.notEquals=" + UPDATED_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsInShouldWork() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta in DEFAULT_MIN_DIAS_ENCUESTA or UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.in=" + DEFAULT_MIN_DIAS_ENCUESTA + "," + UPDATED_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta equals to UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.in=" + UPDATED_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta is not null
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.specified=true");

        // Get all the parametroAplicacionList where minDiasEncuesta is null
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.specified=false");
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta is greater than or equal to DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.greaterThanOrEqual=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta is greater than or equal to UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.greaterThanOrEqual=" + UPDATED_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta is less than or equal to DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.lessThanOrEqual=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta is less than or equal to SMALLER_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.lessThanOrEqual=" + SMALLER_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsLessThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta is less than DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.lessThan=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta is less than UPDATED_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.lessThan=" + UPDATED_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinDiasEncuestaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minDiasEncuesta is greater than DEFAULT_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldNotBeFound("minDiasEncuesta.greaterThan=" + DEFAULT_MIN_DIAS_ENCUESTA);

        // Get all the parametroAplicacionList where minDiasEncuesta is greater than SMALLER_MIN_DIAS_ENCUESTA
        defaultParametroAplicacionShouldBeFound("minDiasEncuesta.greaterThan=" + SMALLER_MIN_DIAS_ENCUESTA);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas equals to DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.equals=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas equals to UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.equals=" + UPDATED_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas not equals to DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.notEquals=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas not equals to UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.notEquals=" + UPDATED_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsInShouldWork() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas in DEFAULT_MAX_CANTIDAD_PREGUNTAS or UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound(
            "maxCantidadPreguntas.in=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS + "," + UPDATED_MAX_CANTIDAD_PREGUNTAS
        );

        // Get all the parametroAplicacionList where maxCantidadPreguntas equals to UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.in=" + UPDATED_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is not null
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.specified=true");

        // Get all the parametroAplicacionList where maxCantidadPreguntas is null
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.specified=false");
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is greater than or equal to DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.greaterThanOrEqual=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is greater than or equal to UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.greaterThanOrEqual=" + UPDATED_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is less than or equal to DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.lessThanOrEqual=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is less than or equal to SMALLER_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.lessThanOrEqual=" + SMALLER_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsLessThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is less than DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.lessThan=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is less than UPDATED_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.lessThan=" + UPDATED_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMaxCantidadPreguntasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is greater than DEFAULT_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("maxCantidadPreguntas.greaterThan=" + DEFAULT_MAX_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where maxCantidadPreguntas is greater than SMALLER_MAX_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("maxCantidadPreguntas.greaterThan=" + SMALLER_MAX_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas equals to DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.equals=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas equals to UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.equals=" + UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas not equals to DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.notEquals=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas not equals to UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.notEquals=" + UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsInShouldWork() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas in DEFAULT_MIN_CANTIDAD_PREGUNTAS or UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound(
            "minCantidadPreguntas.in=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS + "," + UPDATED_MIN_CANTIDAD_PREGUNTAS
        );

        // Get all the parametroAplicacionList where minCantidadPreguntas equals to UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.in=" + UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsNullOrNotNull() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas is not null
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.specified=true");

        // Get all the parametroAplicacionList where minCantidadPreguntas is null
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.specified=false");
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas is greater than or equal to DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.greaterThanOrEqual=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas is greater than or equal to UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.greaterThanOrEqual=" + UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas is less than or equal to DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.lessThanOrEqual=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas is less than or equal to SMALLER_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.lessThanOrEqual=" + SMALLER_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsLessThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas is less than DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.lessThan=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas is less than UPDATED_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.lessThan=" + UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void getAllParametroAplicacionsByMinCantidadPreguntasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        // Get all the parametroAplicacionList where minCantidadPreguntas is greater than DEFAULT_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldNotBeFound("minCantidadPreguntas.greaterThan=" + DEFAULT_MIN_CANTIDAD_PREGUNTAS);

        // Get all the parametroAplicacionList where minCantidadPreguntas is greater than SMALLER_MIN_CANTIDAD_PREGUNTAS
        defaultParametroAplicacionShouldBeFound("minCantidadPreguntas.greaterThan=" + SMALLER_MIN_CANTIDAD_PREGUNTAS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParametroAplicacionShouldBeFound(String filter) throws Exception {
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametroAplicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxDiasEncuesta").value(hasItem(DEFAULT_MAX_DIAS_ENCUESTA)))
            .andExpect(jsonPath("$.[*].minDiasEncuesta").value(hasItem(DEFAULT_MIN_DIAS_ENCUESTA)))
            .andExpect(jsonPath("$.[*].maxCantidadPreguntas").value(hasItem(DEFAULT_MAX_CANTIDAD_PREGUNTAS)))
            .andExpect(jsonPath("$.[*].minCantidadPreguntas").value(hasItem(DEFAULT_MIN_CANTIDAD_PREGUNTAS)));

        // Check, that the count call also returns 1
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParametroAplicacionShouldNotBeFound(String filter) throws Exception {
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParametroAplicacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParametroAplicacion() throws Exception {
        // Get the parametroAplicacion
        restParametroAplicacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParametroAplicacion() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();

        // Update the parametroAplicacion
        ParametroAplicacion updatedParametroAplicacion = parametroAplicacionRepository.findById(parametroAplicacion.getId()).get();
        // Disconnect from session so that the updates on updatedParametroAplicacion are not directly saved in db
        em.detach(updatedParametroAplicacion);
        updatedParametroAplicacion
            .maxDiasEncuesta(UPDATED_MAX_DIAS_ENCUESTA)
            .minDiasEncuesta(UPDATED_MIN_DIAS_ENCUESTA)
            .maxCantidadPreguntas(UPDATED_MAX_CANTIDAD_PREGUNTAS)
            .minCantidadPreguntas(UPDATED_MIN_CANTIDAD_PREGUNTAS);

        restParametroAplicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParametroAplicacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParametroAplicacion))
            )
            .andExpect(status().isOk());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
        ParametroAplicacion testParametroAplicacion = parametroAplicacionList.get(parametroAplicacionList.size() - 1);
        assertThat(testParametroAplicacion.getMaxDiasEncuesta()).isEqualTo(UPDATED_MAX_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMinDiasEncuesta()).isEqualTo(UPDATED_MIN_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMaxCantidadPreguntas()).isEqualTo(UPDATED_MAX_CANTIDAD_PREGUNTAS);
        assertThat(testParametroAplicacion.getMinCantidadPreguntas()).isEqualTo(UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void putNonExistingParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametroAplicacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametroAplicacionWithPatch() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();

        // Update the parametroAplicacion using partial update
        ParametroAplicacion partialUpdatedParametroAplicacion = new ParametroAplicacion();
        partialUpdatedParametroAplicacion.setId(parametroAplicacion.getId());

        partialUpdatedParametroAplicacion.maxDiasEncuesta(UPDATED_MAX_DIAS_ENCUESTA).maxCantidadPreguntas(UPDATED_MAX_CANTIDAD_PREGUNTAS);

        restParametroAplicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametroAplicacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametroAplicacion))
            )
            .andExpect(status().isOk());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
        ParametroAplicacion testParametroAplicacion = parametroAplicacionList.get(parametroAplicacionList.size() - 1);
        assertThat(testParametroAplicacion.getMaxDiasEncuesta()).isEqualTo(UPDATED_MAX_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMinDiasEncuesta()).isEqualTo(DEFAULT_MIN_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMaxCantidadPreguntas()).isEqualTo(UPDATED_MAX_CANTIDAD_PREGUNTAS);
        assertThat(testParametroAplicacion.getMinCantidadPreguntas()).isEqualTo(DEFAULT_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void fullUpdateParametroAplicacionWithPatch() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();

        // Update the parametroAplicacion using partial update
        ParametroAplicacion partialUpdatedParametroAplicacion = new ParametroAplicacion();
        partialUpdatedParametroAplicacion.setId(parametroAplicacion.getId());

        partialUpdatedParametroAplicacion
            .maxDiasEncuesta(UPDATED_MAX_DIAS_ENCUESTA)
            .minDiasEncuesta(UPDATED_MIN_DIAS_ENCUESTA)
            .maxCantidadPreguntas(UPDATED_MAX_CANTIDAD_PREGUNTAS)
            .minCantidadPreguntas(UPDATED_MIN_CANTIDAD_PREGUNTAS);

        restParametroAplicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametroAplicacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParametroAplicacion))
            )
            .andExpect(status().isOk());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
        ParametroAplicacion testParametroAplicacion = parametroAplicacionList.get(parametroAplicacionList.size() - 1);
        assertThat(testParametroAplicacion.getMaxDiasEncuesta()).isEqualTo(UPDATED_MAX_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMinDiasEncuesta()).isEqualTo(UPDATED_MIN_DIAS_ENCUESTA);
        assertThat(testParametroAplicacion.getMaxCantidadPreguntas()).isEqualTo(UPDATED_MAX_CANTIDAD_PREGUNTAS);
        assertThat(testParametroAplicacion.getMinCantidadPreguntas()).isEqualTo(UPDATED_MIN_CANTIDAD_PREGUNTAS);
    }

    @Test
    @Transactional
    void patchNonExistingParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametroAplicacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametroAplicacion() throws Exception {
        int databaseSizeBeforeUpdate = parametroAplicacionRepository.findAll().size();
        parametroAplicacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametroAplicacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parametroAplicacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParametroAplicacion in the database
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametroAplicacion() throws Exception {
        // Initialize the database
        parametroAplicacionRepository.saveAndFlush(parametroAplicacion);

        int databaseSizeBeforeDelete = parametroAplicacionRepository.findAll().size();

        // Delete the parametroAplicacion
        restParametroAplicacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametroAplicacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParametroAplicacion> parametroAplicacionList = parametroAplicacionRepository.findAll();
        assertThat(parametroAplicacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
