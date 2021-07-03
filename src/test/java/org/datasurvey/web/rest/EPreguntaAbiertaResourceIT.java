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
import org.datasurvey.domain.EPreguntaAbierta;
import org.datasurvey.domain.EPreguntaAbiertaRespuesta;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.repository.EPreguntaAbiertaRepository;
import org.datasurvey.service.criteria.EPreguntaAbiertaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EPreguntaAbiertaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EPreguntaAbiertaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPCIONAL = false;
    private static final Boolean UPDATED_OPCIONAL = true;

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String ENTITY_API_URL = "/api/e-pregunta-abiertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EPreguntaAbiertaRepository ePreguntaAbiertaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEPreguntaAbiertaMockMvc;

    private EPreguntaAbierta ePreguntaAbierta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaAbierta createEntity(EntityManager em) {
        EPreguntaAbierta ePreguntaAbierta = new EPreguntaAbierta().nombre(DEFAULT_NOMBRE).opcional(DEFAULT_OPCIONAL).orden(DEFAULT_ORDEN);
        return ePreguntaAbierta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaAbierta createUpdatedEntity(EntityManager em) {
        EPreguntaAbierta ePreguntaAbierta = new EPreguntaAbierta().nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);
        return ePreguntaAbierta;
    }

    @BeforeEach
    public void initTest() {
        ePreguntaAbierta = createEntity(em);
    }

    @Test
    @Transactional
    void createEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeCreate = ePreguntaAbiertaRepository.findAll().size();
        // Create the EPreguntaAbierta
        restEPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isCreated());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeCreate + 1);
        EPreguntaAbierta testEPreguntaAbierta = ePreguntaAbiertaList.get(ePreguntaAbiertaList.size() - 1);
        assertThat(testEPreguntaAbierta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEPreguntaAbierta.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testEPreguntaAbierta.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void createEPreguntaAbiertaWithExistingId() throws Exception {
        // Create the EPreguntaAbierta with an existing ID
        ePreguntaAbierta.setId(1L);

        int databaseSizeBeforeCreate = ePreguntaAbiertaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaAbiertaRepository.findAll().size();
        // set the field null
        ePreguntaAbierta.setNombre(null);

        // Create the EPreguntaAbierta, which fails.

        restEPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpcionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaAbiertaRepository.findAll().size();
        // set the field null
        ePreguntaAbierta.setOpcional(null);

        // Create the EPreguntaAbierta, which fails.

        restEPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaAbiertaRepository.findAll().size();
        // set the field null
        ePreguntaAbierta.setOrden(null);

        // Create the EPreguntaAbierta, which fails.

        restEPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertas() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaAbierta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    void getEPreguntaAbierta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get the ePreguntaAbierta
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL_ID, ePreguntaAbierta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ePreguntaAbierta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.opcional").value(DEFAULT_OPCIONAL.booleanValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    void getEPreguntaAbiertasByIdFiltering() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        Long id = ePreguntaAbierta.getId();

        defaultEPreguntaAbiertaShouldBeFound("id.equals=" + id);
        defaultEPreguntaAbiertaShouldNotBeFound("id.notEquals=" + id);

        defaultEPreguntaAbiertaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEPreguntaAbiertaShouldNotBeFound("id.greaterThan=" + id);

        defaultEPreguntaAbiertaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEPreguntaAbiertaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre equals to DEFAULT_NOMBRE
        defaultEPreguntaAbiertaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaAbiertaList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre not equals to DEFAULT_NOMBRE
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaAbiertaList where nombre not equals to UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the ePreguntaAbiertaList where nombre equals to UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre is not null
        defaultEPreguntaAbiertaShouldBeFound("nombre.specified=true");

        // Get all the ePreguntaAbiertaList where nombre is null
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre contains DEFAULT_NOMBRE
        defaultEPreguntaAbiertaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaAbiertaList where nombre contains UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where nombre does not contain DEFAULT_NOMBRE
        defaultEPreguntaAbiertaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the ePreguntaAbiertaList where nombre does not contain UPDATED_NOMBRE
        defaultEPreguntaAbiertaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOpcionalIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where opcional equals to DEFAULT_OPCIONAL
        defaultEPreguntaAbiertaShouldBeFound("opcional.equals=" + DEFAULT_OPCIONAL);

        // Get all the ePreguntaAbiertaList where opcional equals to UPDATED_OPCIONAL
        defaultEPreguntaAbiertaShouldNotBeFound("opcional.equals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOpcionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where opcional not equals to DEFAULT_OPCIONAL
        defaultEPreguntaAbiertaShouldNotBeFound("opcional.notEquals=" + DEFAULT_OPCIONAL);

        // Get all the ePreguntaAbiertaList where opcional not equals to UPDATED_OPCIONAL
        defaultEPreguntaAbiertaShouldBeFound("opcional.notEquals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOpcionalIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where opcional in DEFAULT_OPCIONAL or UPDATED_OPCIONAL
        defaultEPreguntaAbiertaShouldBeFound("opcional.in=" + DEFAULT_OPCIONAL + "," + UPDATED_OPCIONAL);

        // Get all the ePreguntaAbiertaList where opcional equals to UPDATED_OPCIONAL
        defaultEPreguntaAbiertaShouldNotBeFound("opcional.in=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOpcionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where opcional is not null
        defaultEPreguntaAbiertaShouldBeFound("opcional.specified=true");

        // Get all the ePreguntaAbiertaList where opcional is null
        defaultEPreguntaAbiertaShouldNotBeFound("opcional.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden equals to DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden equals to UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden not equals to DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden not equals to UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the ePreguntaAbiertaList where orden equals to UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden is not null
        defaultEPreguntaAbiertaShouldBeFound("orden.specified=true");

        // Get all the ePreguntaAbiertaList where orden is null
        defaultEPreguntaAbiertaShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden is greater than or equal to DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden is greater than or equal to UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden is less than or equal to DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden is less than or equal to SMALLER_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden is less than DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden is less than UPDATED_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        // Get all the ePreguntaAbiertaList where orden is greater than DEFAULT_ORDEN
        defaultEPreguntaAbiertaShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the ePreguntaAbiertaList where orden is greater than SMALLER_ORDEN
        defaultEPreguntaAbiertaShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByEPreguntaAbiertaRespuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);
        EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta = EPreguntaAbiertaRespuestaResourceIT.createEntity(em);
        em.persist(ePreguntaAbiertaRespuesta);
        em.flush();
        ePreguntaAbierta.addEPreguntaAbiertaRespuesta(ePreguntaAbiertaRespuesta);
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);
        Long ePreguntaAbiertaRespuestaId = ePreguntaAbiertaRespuesta.getId();

        // Get all the ePreguntaAbiertaList where ePreguntaAbiertaRespuesta equals to ePreguntaAbiertaRespuestaId
        defaultEPreguntaAbiertaShouldBeFound("ePreguntaAbiertaRespuestaId.equals=" + ePreguntaAbiertaRespuestaId);

        // Get all the ePreguntaAbiertaList where ePreguntaAbiertaRespuesta equals to (ePreguntaAbiertaRespuestaId + 1)
        defaultEPreguntaAbiertaShouldNotBeFound("ePreguntaAbiertaRespuestaId.equals=" + (ePreguntaAbiertaRespuestaId + 1));
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertasByEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);
        Encuesta encuesta = EncuestaResourceIT.createEntity(em);
        em.persist(encuesta);
        em.flush();
        ePreguntaAbierta.setEncuesta(encuesta);
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);
        Long encuestaId = encuesta.getId();

        // Get all the ePreguntaAbiertaList where encuesta equals to encuestaId
        defaultEPreguntaAbiertaShouldBeFound("encuestaId.equals=" + encuestaId);

        // Get all the ePreguntaAbiertaList where encuesta equals to (encuestaId + 1)
        defaultEPreguntaAbiertaShouldNotBeFound("encuestaId.equals=" + (encuestaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEPreguntaAbiertaShouldBeFound(String filter) throws Exception {
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaAbierta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));

        // Check, that the count call also returns 1
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEPreguntaAbiertaShouldNotBeFound(String filter) throws Exception {
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEPreguntaAbierta() throws Exception {
        // Get the ePreguntaAbierta
        restEPreguntaAbiertaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEPreguntaAbierta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();

        // Update the ePreguntaAbierta
        EPreguntaAbierta updatedEPreguntaAbierta = ePreguntaAbiertaRepository.findById(ePreguntaAbierta.getId()).get();
        // Disconnect from session so that the updates on updatedEPreguntaAbierta are not directly saved in db
        em.detach(updatedEPreguntaAbierta);
        updatedEPreguntaAbierta.nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restEPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEPreguntaAbierta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbierta testEPreguntaAbierta = ePreguntaAbiertaList.get(ePreguntaAbiertaList.size() - 1);
        assertThat(testEPreguntaAbierta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaAbierta.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testEPreguntaAbierta.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void putNonExistingEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ePreguntaAbierta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEPreguntaAbiertaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();

        // Update the ePreguntaAbierta using partial update
        EPreguntaAbierta partialUpdatedEPreguntaAbierta = new EPreguntaAbierta();
        partialUpdatedEPreguntaAbierta.setId(ePreguntaAbierta.getId());

        partialUpdatedEPreguntaAbierta.nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restEPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbierta testEPreguntaAbierta = ePreguntaAbiertaList.get(ePreguntaAbiertaList.size() - 1);
        assertThat(testEPreguntaAbierta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaAbierta.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testEPreguntaAbierta.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void fullUpdateEPreguntaAbiertaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();

        // Update the ePreguntaAbierta using partial update
        EPreguntaAbierta partialUpdatedEPreguntaAbierta = new EPreguntaAbierta();
        partialUpdatedEPreguntaAbierta.setId(ePreguntaAbierta.getId());

        partialUpdatedEPreguntaAbierta.nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restEPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbierta testEPreguntaAbierta = ePreguntaAbiertaList.get(ePreguntaAbiertaList.size() - 1);
        assertThat(testEPreguntaAbierta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEPreguntaAbierta.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testEPreguntaAbierta.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void patchNonExistingEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ePreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRepository.findAll().size();
        ePreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbierta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaAbierta in the database
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEPreguntaAbierta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRepository.saveAndFlush(ePreguntaAbierta);

        int databaseSizeBeforeDelete = ePreguntaAbiertaRepository.findAll().size();

        // Delete the ePreguntaAbierta
        restEPreguntaAbiertaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ePreguntaAbierta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EPreguntaAbierta> ePreguntaAbiertaList = ePreguntaAbiertaRepository.findAll();
        assertThat(ePreguntaAbiertaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
