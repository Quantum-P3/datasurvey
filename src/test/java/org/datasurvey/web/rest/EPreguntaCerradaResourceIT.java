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
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.domain.EPreguntaCerradaOpcion;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.enumeration.PreguntaCerradaTipo;
import org.datasurvey.repository.EPreguntaCerradaRepository;
import org.datasurvey.service.criteria.EPreguntaCerradaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EPreguntaCerradaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EPreguntaCerradaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final PreguntaCerradaTipo DEFAULT_TIPO = PreguntaCerradaTipo.SINGLE;
    private static final PreguntaCerradaTipo UPDATED_TIPO = PreguntaCerradaTipo.MULTIPLE;

    private static final Boolean DEFAULT_OPCIONAL = false;
    private static final Boolean UPDATED_OPCIONAL = true;

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String ENTITY_API_URL = "/api/e-pregunta-cerradas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EPreguntaCerradaRepository ePreguntaCerradaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEPreguntaCerradaMockMvc;

    private EPreguntaCerrada ePreguntaCerrada;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaCerrada createEntity(EntityManager em) {
        EPreguntaCerrada ePreguntaCerrada = new EPreguntaCerrada()
            .nombre(DEFAULT_NOMBRE)
            .tipo(DEFAULT_TIPO)
            .opcional(DEFAULT_OPCIONAL)
            .orden(DEFAULT_ORDEN);
        return ePreguntaCerrada;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaCerrada createUpdatedEntity(EntityManager em) {
        EPreguntaCerrada ePreguntaCerrada = new EPreguntaCerrada()
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .opcional(UPDATED_OPCIONAL)
            .orden(UPDATED_ORDEN);
        return ePreguntaCerrada;
    }

    @BeforeEach
    public void initTest() {
        ePreguntaCerrada = createEntity(em);
    }

    @Test
    @Transactional
    void createEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeCreate = ePreguntaCerradaRepository.findAll().size();
        // Create the EPreguntaCerrada
        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isCreated());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeCreate + 1);
        EPreguntaCerrada testEPreguntaCerrada = ePreguntaCerradaList.get(ePreguntaCerradaList.size() - 1);
        assertThat(testEPreguntaCerrada.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEPreguntaCerrada.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testEPreguntaCerrada.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testEPreguntaCerrada.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void createEPreguntaCerradaWithExistingId() throws Exception {
        // Create the EPreguntaCerrada with an existing ID
        ePreguntaCerrada.setId(1L);

        int databaseSizeBeforeCreate = ePreguntaCerradaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaRepository.findAll().size();
        // set the field null
        ePreguntaCerrada.setNombre(null);

        // Create the EPreguntaCerrada, which fails.

        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaRepository.findAll().size();
        // set the field null
        ePreguntaCerrada.setTipo(null);

        // Create the EPreguntaCerrada, which fails.

        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpcionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaRepository.findAll().size();
        // set the field null
        ePreguntaCerrada.setOpcional(null);

        // Create the EPreguntaCerrada, which fails.

        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaRepository.findAll().size();
        // set the field null
        ePreguntaCerrada.setOrden(null);

        // Create the EPreguntaCerrada, which fails.

        restEPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradas() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaCerrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    void getEPreguntaCerrada() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get the ePreguntaCerrada
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL_ID, ePreguntaCerrada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ePreguntaCerrada.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.opcional").value(DEFAULT_OPCIONAL.booleanValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    void getEPreguntaCerradasByIdFiltering() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        Long id = ePreguntaCerrada.getId();

        defaultEPreguntaCerradaShouldBeFound("id.equals=" + id);
        defaultEPreguntaCerradaShouldNotBeFound("id.notEquals=" + id);

        defaultEPreguntaCerradaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEPreguntaCerradaShouldNotBeFound("id.greaterThan=" + id);

        defaultEPreguntaCerradaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEPreguntaCerradaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre equals to DEFAULT_NOMBRE
        defaultEPreguntaCerradaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre not equals to DEFAULT_NOMBRE
        defaultEPreguntaCerradaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaList where nombre not equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the ePreguntaCerradaList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre is not null
        defaultEPreguntaCerradaShouldBeFound("nombre.specified=true");

        // Get all the ePreguntaCerradaList where nombre is null
        defaultEPreguntaCerradaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre contains DEFAULT_NOMBRE
        defaultEPreguntaCerradaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaList where nombre contains UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where nombre does not contain DEFAULT_NOMBRE
        defaultEPreguntaCerradaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaList where nombre does not contain UPDATED_NOMBRE
        defaultEPreguntaCerradaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where tipo equals to DEFAULT_TIPO
        defaultEPreguntaCerradaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the ePreguntaCerradaList where tipo equals to UPDATED_TIPO
        defaultEPreguntaCerradaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where tipo not equals to DEFAULT_TIPO
        defaultEPreguntaCerradaShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the ePreguntaCerradaList where tipo not equals to UPDATED_TIPO
        defaultEPreguntaCerradaShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultEPreguntaCerradaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the ePreguntaCerradaList where tipo equals to UPDATED_TIPO
        defaultEPreguntaCerradaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where tipo is not null
        defaultEPreguntaCerradaShouldBeFound("tipo.specified=true");

        // Get all the ePreguntaCerradaList where tipo is null
        defaultEPreguntaCerradaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOpcionalIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where opcional equals to DEFAULT_OPCIONAL
        defaultEPreguntaCerradaShouldBeFound("opcional.equals=" + DEFAULT_OPCIONAL);

        // Get all the ePreguntaCerradaList where opcional equals to UPDATED_OPCIONAL
        defaultEPreguntaCerradaShouldNotBeFound("opcional.equals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOpcionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where opcional not equals to DEFAULT_OPCIONAL
        defaultEPreguntaCerradaShouldNotBeFound("opcional.notEquals=" + DEFAULT_OPCIONAL);

        // Get all the ePreguntaCerradaList where opcional not equals to UPDATED_OPCIONAL
        defaultEPreguntaCerradaShouldBeFound("opcional.notEquals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOpcionalIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where opcional in DEFAULT_OPCIONAL or UPDATED_OPCIONAL
        defaultEPreguntaCerradaShouldBeFound("opcional.in=" + DEFAULT_OPCIONAL + "," + UPDATED_OPCIONAL);

        // Get all the ePreguntaCerradaList where opcional equals to UPDATED_OPCIONAL
        defaultEPreguntaCerradaShouldNotBeFound("opcional.in=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOpcionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where opcional is not null
        defaultEPreguntaCerradaShouldBeFound("opcional.specified=true");

        // Get all the ePreguntaCerradaList where opcional is null
        defaultEPreguntaCerradaShouldNotBeFound("opcional.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden equals to DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden equals to UPDATED_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden not equals to DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden not equals to UPDATED_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the ePreguntaCerradaList where orden equals to UPDATED_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden is not null
        defaultEPreguntaCerradaShouldBeFound("orden.specified=true");

        // Get all the ePreguntaCerradaList where orden is null
        defaultEPreguntaCerradaShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden is greater than or equal to DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden is greater than or equal to UPDATED_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden is less than or equal to DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden is less than or equal to SMALLER_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden is less than DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden is less than UPDATED_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        // Get all the ePreguntaCerradaList where orden is greater than DEFAULT_ORDEN
        defaultEPreguntaCerradaShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaList where orden is greater than SMALLER_ORDEN
        defaultEPreguntaCerradaShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByEPreguntaCerradaOpcionIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);
        EPreguntaCerradaOpcion ePreguntaCerradaOpcion = EPreguntaCerradaOpcionResourceIT.createEntity(em);
        em.persist(ePreguntaCerradaOpcion);
        em.flush();
        ePreguntaCerrada.addEPreguntaCerradaOpcion(ePreguntaCerradaOpcion);
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);
        Long ePreguntaCerradaOpcionId = ePreguntaCerradaOpcion.getId();

        // Get all the ePreguntaCerradaList where ePreguntaCerradaOpcion equals to ePreguntaCerradaOpcionId
        defaultEPreguntaCerradaShouldBeFound("ePreguntaCerradaOpcionId.equals=" + ePreguntaCerradaOpcionId);

        // Get all the ePreguntaCerradaList where ePreguntaCerradaOpcion equals to (ePreguntaCerradaOpcionId + 1)
        defaultEPreguntaCerradaShouldNotBeFound("ePreguntaCerradaOpcionId.equals=" + (ePreguntaCerradaOpcionId + 1));
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradasByEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);
        Encuesta encuesta = EncuestaResourceIT.createEntity(em);
        em.persist(encuesta);
        em.flush();
        ePreguntaCerrada.setEncuesta(encuesta);
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);
        Long encuestaId = encuesta.getId();

        // Get all the ePreguntaCerradaList where encuesta equals to encuestaId
        defaultEPreguntaCerradaShouldBeFound("encuestaId.equals=" + encuestaId);

        // Get all the ePreguntaCerradaList where encuesta equals to (encuestaId + 1)
        defaultEPreguntaCerradaShouldNotBeFound("encuestaId.equals=" + (encuestaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEPreguntaCerradaShouldBeFound(String filter) throws Exception {
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaCerrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));

        // Check, that the count call also returns 1
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEPreguntaCerradaShouldNotBeFound(String filter) throws Exception {
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEPreguntaCerrada() throws Exception {
        // Get the ePreguntaCerrada
        restEPreguntaCerradaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEPreguntaCerrada() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();

        // Update the ePreguntaCerrada
        EPreguntaCerrada updatedEPreguntaCerrada = ePreguntaCerradaRepository.findById(ePreguntaCerrada.getId()).get();
        // Disconnect from session so that the updates on updatedEPreguntaCerrada are not directly saved in db
        em.detach(updatedEPreguntaCerrada);
        updatedEPreguntaCerrada.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restEPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEPreguntaCerrada.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerrada testEPreguntaCerrada = ePreguntaCerradaList.get(ePreguntaCerradaList.size() - 1);
        assertThat(testEPreguntaCerrada.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEPreguntaCerrada.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testEPreguntaCerrada.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void putNonExistingEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ePreguntaCerrada.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEPreguntaCerradaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();

        // Update the ePreguntaCerrada using partial update
        EPreguntaCerrada partialUpdatedEPreguntaCerrada = new EPreguntaCerrada();
        partialUpdatedEPreguntaCerrada.setId(ePreguntaCerrada.getId());

        partialUpdatedEPreguntaCerrada.tipo(UPDATED_TIPO);

        restEPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerrada testEPreguntaCerrada = ePreguntaCerradaList.get(ePreguntaCerradaList.size() - 1);
        assertThat(testEPreguntaCerrada.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEPreguntaCerrada.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testEPreguntaCerrada.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void fullUpdateEPreguntaCerradaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();

        // Update the ePreguntaCerrada using partial update
        EPreguntaCerrada partialUpdatedEPreguntaCerrada = new EPreguntaCerrada();
        partialUpdatedEPreguntaCerrada.setId(ePreguntaCerrada.getId());

        partialUpdatedEPreguntaCerrada.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restEPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerrada testEPreguntaCerrada = ePreguntaCerradaList.get(ePreguntaCerradaList.size() - 1);
        assertThat(testEPreguntaCerrada.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testEPreguntaCerrada.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testEPreguntaCerrada.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void patchNonExistingEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ePreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaRepository.findAll().size();
        ePreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerrada))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaCerrada in the database
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEPreguntaCerrada() throws Exception {
        // Initialize the database
        ePreguntaCerradaRepository.saveAndFlush(ePreguntaCerrada);

        int databaseSizeBeforeDelete = ePreguntaCerradaRepository.findAll().size();

        // Delete the ePreguntaCerrada
        restEPreguntaCerradaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ePreguntaCerrada.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EPreguntaCerrada> ePreguntaCerradaList = ePreguntaCerradaRepository.findAll();
        assertThat(ePreguntaCerradaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
