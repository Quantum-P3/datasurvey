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
import org.datasurvey.domain.PPreguntaCerrada;
import org.datasurvey.domain.PPreguntaCerradaOpcion;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.domain.enumeration.PreguntaCerradaTipo;
import org.datasurvey.repository.PPreguntaCerradaRepository;
import org.datasurvey.service.criteria.PPreguntaCerradaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PPreguntaCerradaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PPreguntaCerradaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final PreguntaCerradaTipo DEFAULT_TIPO = PreguntaCerradaTipo.SINGLE;
    private static final PreguntaCerradaTipo UPDATED_TIPO = PreguntaCerradaTipo.MULTIPLE;

    private static final Boolean DEFAULT_OPCIONAL = false;
    private static final Boolean UPDATED_OPCIONAL = true;

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String ENTITY_API_URL = "/api/p-pregunta-cerradas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PPreguntaCerradaRepository pPreguntaCerradaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPPreguntaCerradaMockMvc;

    private PPreguntaCerrada pPreguntaCerrada;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaCerrada createEntity(EntityManager em) {
        PPreguntaCerrada pPreguntaCerrada = new PPreguntaCerrada()
            .nombre(DEFAULT_NOMBRE)
            .tipo(DEFAULT_TIPO)
            .opcional(DEFAULT_OPCIONAL)
            .orden(DEFAULT_ORDEN);
        return pPreguntaCerrada;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaCerrada createUpdatedEntity(EntityManager em) {
        PPreguntaCerrada pPreguntaCerrada = new PPreguntaCerrada()
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .opcional(UPDATED_OPCIONAL)
            .orden(UPDATED_ORDEN);
        return pPreguntaCerrada;
    }

    @BeforeEach
    public void initTest() {
        pPreguntaCerrada = createEntity(em);
    }

    @Test
    @Transactional
    void createPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeCreate = pPreguntaCerradaRepository.findAll().size();
        // Create the PPreguntaCerrada
        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isCreated());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeCreate + 1);
        PPreguntaCerrada testPPreguntaCerrada = pPreguntaCerradaList.get(pPreguntaCerradaList.size() - 1);
        assertThat(testPPreguntaCerrada.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPPreguntaCerrada.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testPPreguntaCerrada.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testPPreguntaCerrada.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void createPPreguntaCerradaWithExistingId() throws Exception {
        // Create the PPreguntaCerrada with an existing ID
        pPreguntaCerrada.setId(1L);

        int databaseSizeBeforeCreate = pPreguntaCerradaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaRepository.findAll().size();
        // set the field null
        pPreguntaCerrada.setNombre(null);

        // Create the PPreguntaCerrada, which fails.

        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaRepository.findAll().size();
        // set the field null
        pPreguntaCerrada.setTipo(null);

        // Create the PPreguntaCerrada, which fails.

        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpcionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaRepository.findAll().size();
        // set the field null
        pPreguntaCerrada.setOpcional(null);

        // Create the PPreguntaCerrada, which fails.

        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaRepository.findAll().size();
        // set the field null
        pPreguntaCerrada.setOrden(null);

        // Create the PPreguntaCerrada, which fails.

        restPPreguntaCerradaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradas() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaCerrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    void getPPreguntaCerrada() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get the pPreguntaCerrada
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL_ID, pPreguntaCerrada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pPreguntaCerrada.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.opcional").value(DEFAULT_OPCIONAL.booleanValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    void getPPreguntaCerradasByIdFiltering() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        Long id = pPreguntaCerrada.getId();

        defaultPPreguntaCerradaShouldBeFound("id.equals=" + id);
        defaultPPreguntaCerradaShouldNotBeFound("id.notEquals=" + id);

        defaultPPreguntaCerradaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPPreguntaCerradaShouldNotBeFound("id.greaterThan=" + id);

        defaultPPreguntaCerradaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPPreguntaCerradaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre equals to DEFAULT_NOMBRE
        defaultPPreguntaCerradaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre not equals to DEFAULT_NOMBRE
        defaultPPreguntaCerradaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaList where nombre not equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the pPreguntaCerradaList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre is not null
        defaultPPreguntaCerradaShouldBeFound("nombre.specified=true");

        // Get all the pPreguntaCerradaList where nombre is null
        defaultPPreguntaCerradaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre contains DEFAULT_NOMBRE
        defaultPPreguntaCerradaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaList where nombre contains UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where nombre does not contain DEFAULT_NOMBRE
        defaultPPreguntaCerradaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaList where nombre does not contain UPDATED_NOMBRE
        defaultPPreguntaCerradaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where tipo equals to DEFAULT_TIPO
        defaultPPreguntaCerradaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the pPreguntaCerradaList where tipo equals to UPDATED_TIPO
        defaultPPreguntaCerradaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where tipo not equals to DEFAULT_TIPO
        defaultPPreguntaCerradaShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the pPreguntaCerradaList where tipo not equals to UPDATED_TIPO
        defaultPPreguntaCerradaShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultPPreguntaCerradaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the pPreguntaCerradaList where tipo equals to UPDATED_TIPO
        defaultPPreguntaCerradaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where tipo is not null
        defaultPPreguntaCerradaShouldBeFound("tipo.specified=true");

        // Get all the pPreguntaCerradaList where tipo is null
        defaultPPreguntaCerradaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOpcionalIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where opcional equals to DEFAULT_OPCIONAL
        defaultPPreguntaCerradaShouldBeFound("opcional.equals=" + DEFAULT_OPCIONAL);

        // Get all the pPreguntaCerradaList where opcional equals to UPDATED_OPCIONAL
        defaultPPreguntaCerradaShouldNotBeFound("opcional.equals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOpcionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where opcional not equals to DEFAULT_OPCIONAL
        defaultPPreguntaCerradaShouldNotBeFound("opcional.notEquals=" + DEFAULT_OPCIONAL);

        // Get all the pPreguntaCerradaList where opcional not equals to UPDATED_OPCIONAL
        defaultPPreguntaCerradaShouldBeFound("opcional.notEquals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOpcionalIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where opcional in DEFAULT_OPCIONAL or UPDATED_OPCIONAL
        defaultPPreguntaCerradaShouldBeFound("opcional.in=" + DEFAULT_OPCIONAL + "," + UPDATED_OPCIONAL);

        // Get all the pPreguntaCerradaList where opcional equals to UPDATED_OPCIONAL
        defaultPPreguntaCerradaShouldNotBeFound("opcional.in=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOpcionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where opcional is not null
        defaultPPreguntaCerradaShouldBeFound("opcional.specified=true");

        // Get all the pPreguntaCerradaList where opcional is null
        defaultPPreguntaCerradaShouldNotBeFound("opcional.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden equals to DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden equals to UPDATED_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden not equals to DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden not equals to UPDATED_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the pPreguntaCerradaList where orden equals to UPDATED_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden is not null
        defaultPPreguntaCerradaShouldBeFound("orden.specified=true");

        // Get all the pPreguntaCerradaList where orden is null
        defaultPPreguntaCerradaShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden is greater than or equal to DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden is greater than or equal to UPDATED_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden is less than or equal to DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden is less than or equal to SMALLER_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden is less than DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden is less than UPDATED_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        // Get all the pPreguntaCerradaList where orden is greater than DEFAULT_ORDEN
        defaultPPreguntaCerradaShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaList where orden is greater than SMALLER_ORDEN
        defaultPPreguntaCerradaShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByPPreguntaCerradaOpcionIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);
        PPreguntaCerradaOpcion pPreguntaCerradaOpcion = PPreguntaCerradaOpcionResourceIT.createEntity(em);
        em.persist(pPreguntaCerradaOpcion);
        em.flush();
        pPreguntaCerrada.addPPreguntaCerradaOpcion(pPreguntaCerradaOpcion);
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);
        Long pPreguntaCerradaOpcionId = pPreguntaCerradaOpcion.getId();

        // Get all the pPreguntaCerradaList where pPreguntaCerradaOpcion equals to pPreguntaCerradaOpcionId
        defaultPPreguntaCerradaShouldBeFound("pPreguntaCerradaOpcionId.equals=" + pPreguntaCerradaOpcionId);

        // Get all the pPreguntaCerradaList where pPreguntaCerradaOpcion equals to (pPreguntaCerradaOpcionId + 1)
        defaultPPreguntaCerradaShouldNotBeFound("pPreguntaCerradaOpcionId.equals=" + (pPreguntaCerradaOpcionId + 1));
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradasByPlantillaIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);
        Plantilla plantilla = PlantillaResourceIT.createEntity(em);
        em.persist(plantilla);
        em.flush();
        pPreguntaCerrada.setPlantilla(plantilla);
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);
        Long plantillaId = plantilla.getId();

        // Get all the pPreguntaCerradaList where plantilla equals to plantillaId
        defaultPPreguntaCerradaShouldBeFound("plantillaId.equals=" + plantillaId);

        // Get all the pPreguntaCerradaList where plantilla equals to (plantillaId + 1)
        defaultPPreguntaCerradaShouldNotBeFound("plantillaId.equals=" + (plantillaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPPreguntaCerradaShouldBeFound(String filter) throws Exception {
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaCerrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));

        // Check, that the count call also returns 1
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPPreguntaCerradaShouldNotBeFound(String filter) throws Exception {
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPPreguntaCerradaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPPreguntaCerrada() throws Exception {
        // Get the pPreguntaCerrada
        restPPreguntaCerradaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPPreguntaCerrada() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();

        // Update the pPreguntaCerrada
        PPreguntaCerrada updatedPPreguntaCerrada = pPreguntaCerradaRepository.findById(pPreguntaCerrada.getId()).get();
        // Disconnect from session so that the updates on updatedPPreguntaCerrada are not directly saved in db
        em.detach(updatedPPreguntaCerrada);
        updatedPPreguntaCerrada.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restPPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPPreguntaCerrada.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerrada testPPreguntaCerrada = pPreguntaCerradaList.get(pPreguntaCerradaList.size() - 1);
        assertThat(testPPreguntaCerrada.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPPreguntaCerrada.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testPPreguntaCerrada.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void putNonExistingPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pPreguntaCerrada.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePPreguntaCerradaWithPatch() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();

        // Update the pPreguntaCerrada using partial update
        PPreguntaCerrada partialUpdatedPPreguntaCerrada = new PPreguntaCerrada();
        partialUpdatedPPreguntaCerrada.setId(pPreguntaCerrada.getId());

        partialUpdatedPPreguntaCerrada.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO);

        restPPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerrada testPPreguntaCerrada = pPreguntaCerradaList.get(pPreguntaCerradaList.size() - 1);
        assertThat(testPPreguntaCerrada.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPPreguntaCerrada.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testPPreguntaCerrada.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void fullUpdatePPreguntaCerradaWithPatch() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();

        // Update the pPreguntaCerrada using partial update
        PPreguntaCerrada partialUpdatedPPreguntaCerrada = new PPreguntaCerrada();
        partialUpdatedPPreguntaCerrada.setId(pPreguntaCerrada.getId());

        partialUpdatedPPreguntaCerrada.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restPPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaCerrada))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerrada testPPreguntaCerrada = pPreguntaCerradaList.get(pPreguntaCerradaList.size() - 1);
        assertThat(testPPreguntaCerrada.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerrada.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPPreguntaCerrada.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testPPreguntaCerrada.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void patchNonExistingPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pPreguntaCerrada.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPPreguntaCerrada() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaRepository.findAll().size();
        pPreguntaCerrada.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerrada))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaCerrada in the database
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePPreguntaCerrada() throws Exception {
        // Initialize the database
        pPreguntaCerradaRepository.saveAndFlush(pPreguntaCerrada);

        int databaseSizeBeforeDelete = pPreguntaCerradaRepository.findAll().size();

        // Delete the pPreguntaCerrada
        restPPreguntaCerradaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pPreguntaCerrada.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PPreguntaCerrada> pPreguntaCerradaList = pPreguntaCerradaRepository.findAll();
        assertThat(pPreguntaCerradaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
