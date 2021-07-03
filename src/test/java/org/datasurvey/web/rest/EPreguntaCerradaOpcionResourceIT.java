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
import org.datasurvey.repository.EPreguntaCerradaOpcionRepository;
import org.datasurvey.service.criteria.EPreguntaCerradaOpcionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EPreguntaCerradaOpcionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EPreguntaCerradaOpcionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String ENTITY_API_URL = "/api/e-pregunta-cerrada-opcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEPreguntaCerradaOpcionMockMvc;

    private EPreguntaCerradaOpcion ePreguntaCerradaOpcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaCerradaOpcion createEntity(EntityManager em) {
        EPreguntaCerradaOpcion ePreguntaCerradaOpcion = new EPreguntaCerradaOpcion()
            .nombre(DEFAULT_NOMBRE)
            .orden(DEFAULT_ORDEN)
            .cantidad(DEFAULT_CANTIDAD);
        return ePreguntaCerradaOpcion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaCerradaOpcion createUpdatedEntity(EntityManager em) {
        EPreguntaCerradaOpcion ePreguntaCerradaOpcion = new EPreguntaCerradaOpcion()
            .nombre(UPDATED_NOMBRE)
            .orden(UPDATED_ORDEN)
            .cantidad(UPDATED_CANTIDAD);
        return ePreguntaCerradaOpcion;
    }

    @BeforeEach
    public void initTest() {
        ePreguntaCerradaOpcion = createEntity(em);
    }

    @Test
    @Transactional
    void createEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeCreate = ePreguntaCerradaOpcionRepository.findAll().size();
        // Create the EPreguntaCerradaOpcion
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isCreated());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeCreate + 1);
        EPreguntaCerradaOpcion testEPreguntaCerradaOpcion = ePreguntaCerradaOpcionList.get(ePreguntaCerradaOpcionList.size() - 1);
        assertThat(testEPreguntaCerradaOpcion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEPreguntaCerradaOpcion.getOrden()).isEqualTo(DEFAULT_ORDEN);
        assertThat(testEPreguntaCerradaOpcion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void createEPreguntaCerradaOpcionWithExistingId() throws Exception {
        // Create the EPreguntaCerradaOpcion with an existing ID
        ePreguntaCerradaOpcion.setId(1L);

        int databaseSizeBeforeCreate = ePreguntaCerradaOpcionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaOpcionRepository.findAll().size();
        // set the field null
        ePreguntaCerradaOpcion.setNombre(null);

        // Create the EPreguntaCerradaOpcion, which fails.

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaOpcionRepository.findAll().size();
        // set the field null
        ePreguntaCerradaOpcion.setOrden(null);

        // Create the EPreguntaCerradaOpcion, which fails.

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaCerradaOpcionRepository.findAll().size();
        // set the field null
        ePreguntaCerradaOpcion.setCantidad(null);

        // Create the EPreguntaCerradaOpcion, which fails.

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcions() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaCerradaOpcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));
    }

    @Test
    @Transactional
    void getEPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get the ePreguntaCerradaOpcion
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL_ID, ePreguntaCerradaOpcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ePreguntaCerradaOpcion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD));
    }

    @Test
    @Transactional
    void getEPreguntaCerradaOpcionsByIdFiltering() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        Long id = ePreguntaCerradaOpcion.getId();

        defaultEPreguntaCerradaOpcionShouldBeFound("id.equals=" + id);
        defaultEPreguntaCerradaOpcionShouldNotBeFound("id.notEquals=" + id);

        defaultEPreguntaCerradaOpcionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEPreguntaCerradaOpcionShouldNotBeFound("id.greaterThan=" + id);

        defaultEPreguntaCerradaOpcionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEPreguntaCerradaOpcionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre equals to DEFAULT_NOMBRE
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaOpcionList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre not equals to DEFAULT_NOMBRE
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaOpcionList where nombre not equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the ePreguntaCerradaOpcionList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre is not null
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.specified=true");

        // Get all the ePreguntaCerradaOpcionList where nombre is null
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre contains DEFAULT_NOMBRE
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaOpcionList where nombre contains UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where nombre does not contain DEFAULT_NOMBRE
        defaultEPreguntaCerradaOpcionShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaCerradaOpcionList where nombre does not contain UPDATED_NOMBRE
        defaultEPreguntaCerradaOpcionShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden equals to DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden equals to UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden not equals to DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden not equals to UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden equals to UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden is not null
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.specified=true");

        // Get all the ePreguntaCerradaOpcionList where orden is null
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden is greater than or equal to DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden is greater than or equal to UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden is less than or equal to DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden is less than or equal to SMALLER_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden is less than DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden is less than UPDATED_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where orden is greater than DEFAULT_ORDEN
        defaultEPreguntaCerradaOpcionShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaCerradaOpcionList where orden is greater than SMALLER_ORDEN
        defaultEPreguntaCerradaOpcionShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad equals to DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad equals to UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad not equals to DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad not equals to UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad equals to UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad is not null
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.specified=true");

        // Get all the ePreguntaCerradaOpcionList where cantidad is null
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad is less than DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad is less than UPDATED_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        // Get all the ePreguntaCerradaOpcionList where cantidad is greater than DEFAULT_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the ePreguntaCerradaOpcionList where cantidad is greater than SMALLER_CANTIDAD
        defaultEPreguntaCerradaOpcionShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllEPreguntaCerradaOpcionsByEPreguntaCerradaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);
        EPreguntaCerrada ePreguntaCerrada = EPreguntaCerradaResourceIT.createEntity(em);
        em.persist(ePreguntaCerrada);
        em.flush();
        ePreguntaCerradaOpcion.setEPreguntaCerrada(ePreguntaCerrada);
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);
        Long ePreguntaCerradaId = ePreguntaCerrada.getId();

        // Get all the ePreguntaCerradaOpcionList where ePreguntaCerrada equals to ePreguntaCerradaId
        defaultEPreguntaCerradaOpcionShouldBeFound("ePreguntaCerradaId.equals=" + ePreguntaCerradaId);

        // Get all the ePreguntaCerradaOpcionList where ePreguntaCerrada equals to (ePreguntaCerradaId + 1)
        defaultEPreguntaCerradaOpcionShouldNotBeFound("ePreguntaCerradaId.equals=" + (ePreguntaCerradaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEPreguntaCerradaOpcionShouldBeFound(String filter) throws Exception {
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaCerradaOpcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));

        // Check, that the count call also returns 1
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEPreguntaCerradaOpcionShouldNotBeFound(String filter) throws Exception {
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEPreguntaCerradaOpcion() throws Exception {
        // Get the ePreguntaCerradaOpcion
        restEPreguntaCerradaOpcionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();

        // Update the ePreguntaCerradaOpcion
        EPreguntaCerradaOpcion updatedEPreguntaCerradaOpcion = ePreguntaCerradaOpcionRepository
            .findById(ePreguntaCerradaOpcion.getId())
            .get();
        // Disconnect from session so that the updates on updatedEPreguntaCerradaOpcion are not directly saved in db
        em.detach(updatedEPreguntaCerradaOpcion);
        updatedEPreguntaCerradaOpcion.nombre(UPDATED_NOMBRE).orden(UPDATED_ORDEN).cantidad(UPDATED_CANTIDAD);

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEPreguntaCerradaOpcion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerradaOpcion testEPreguntaCerradaOpcion = ePreguntaCerradaOpcionList.get(ePreguntaCerradaOpcionList.size() - 1);
        assertThat(testEPreguntaCerradaOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaCerradaOpcion.getOrden()).isEqualTo(UPDATED_ORDEN);
        assertThat(testEPreguntaCerradaOpcion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void putNonExistingEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ePreguntaCerradaOpcion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEPreguntaCerradaOpcionWithPatch() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();

        // Update the ePreguntaCerradaOpcion using partial update
        EPreguntaCerradaOpcion partialUpdatedEPreguntaCerradaOpcion = new EPreguntaCerradaOpcion();
        partialUpdatedEPreguntaCerradaOpcion.setId(ePreguntaCerradaOpcion.getId());

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerradaOpcion testEPreguntaCerradaOpcion = ePreguntaCerradaOpcionList.get(ePreguntaCerradaOpcionList.size() - 1);
        assertThat(testEPreguntaCerradaOpcion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEPreguntaCerradaOpcion.getOrden()).isEqualTo(DEFAULT_ORDEN);
        assertThat(testEPreguntaCerradaOpcion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void fullUpdateEPreguntaCerradaOpcionWithPatch() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();

        // Update the ePreguntaCerradaOpcion using partial update
        EPreguntaCerradaOpcion partialUpdatedEPreguntaCerradaOpcion = new EPreguntaCerradaOpcion();
        partialUpdatedEPreguntaCerradaOpcion.setId(ePreguntaCerradaOpcion.getId());

        partialUpdatedEPreguntaCerradaOpcion.nombre(UPDATED_NOMBRE).orden(UPDATED_ORDEN).cantidad(UPDATED_CANTIDAD);

        restEPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaCerradaOpcion testEPreguntaCerradaOpcion = ePreguntaCerradaOpcionList.get(ePreguntaCerradaOpcionList.size() - 1);
        assertThat(testEPreguntaCerradaOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaCerradaOpcion.getOrden()).isEqualTo(UPDATED_ORDEN);
        assertThat(testEPreguntaCerradaOpcion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ePreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaCerradaOpcionRepository.findAll().size();
        ePreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaCerradaOpcion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaCerradaOpcion in the database
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        ePreguntaCerradaOpcionRepository.saveAndFlush(ePreguntaCerradaOpcion);

        int databaseSizeBeforeDelete = ePreguntaCerradaOpcionRepository.findAll().size();

        // Delete the ePreguntaCerradaOpcion
        restEPreguntaCerradaOpcionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ePreguntaCerradaOpcion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EPreguntaCerradaOpcion> ePreguntaCerradaOpcionList = ePreguntaCerradaOpcionRepository.findAll();
        assertThat(ePreguntaCerradaOpcionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
