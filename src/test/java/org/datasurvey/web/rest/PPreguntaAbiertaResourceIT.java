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
import org.datasurvey.domain.PPreguntaAbierta;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.repository.PPreguntaAbiertaRepository;
import org.datasurvey.service.criteria.PPreguntaAbiertaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PPreguntaAbiertaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PPreguntaAbiertaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPCIONAL = false;
    private static final Boolean UPDATED_OPCIONAL = true;

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String ENTITY_API_URL = "/api/p-pregunta-abiertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PPreguntaAbiertaRepository pPreguntaAbiertaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPPreguntaAbiertaMockMvc;

    private PPreguntaAbierta pPreguntaAbierta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaAbierta createEntity(EntityManager em) {
        PPreguntaAbierta pPreguntaAbierta = new PPreguntaAbierta().nombre(DEFAULT_NOMBRE).opcional(DEFAULT_OPCIONAL).orden(DEFAULT_ORDEN);
        return pPreguntaAbierta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaAbierta createUpdatedEntity(EntityManager em) {
        PPreguntaAbierta pPreguntaAbierta = new PPreguntaAbierta().nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);
        return pPreguntaAbierta;
    }

    @BeforeEach
    public void initTest() {
        pPreguntaAbierta = createEntity(em);
    }

    @Test
    @Transactional
    void createPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeCreate = pPreguntaAbiertaRepository.findAll().size();
        // Create the PPreguntaAbierta
        restPPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isCreated());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeCreate + 1);
        PPreguntaAbierta testPPreguntaAbierta = pPreguntaAbiertaList.get(pPreguntaAbiertaList.size() - 1);
        assertThat(testPPreguntaAbierta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPPreguntaAbierta.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testPPreguntaAbierta.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void createPPreguntaAbiertaWithExistingId() throws Exception {
        // Create the PPreguntaAbierta with an existing ID
        pPreguntaAbierta.setId(1L);

        int databaseSizeBeforeCreate = pPreguntaAbiertaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaAbiertaRepository.findAll().size();
        // set the field null
        pPreguntaAbierta.setNombre(null);

        // Create the PPreguntaAbierta, which fails.

        restPPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpcionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaAbiertaRepository.findAll().size();
        // set the field null
        pPreguntaAbierta.setOpcional(null);

        // Create the PPreguntaAbierta, which fails.

        restPPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaAbiertaRepository.findAll().size();
        // set the field null
        pPreguntaAbierta.setOrden(null);

        // Create the PPreguntaAbierta, which fails.

        restPPreguntaAbiertaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertas() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaAbierta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    void getPPreguntaAbierta() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get the pPreguntaAbierta
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL_ID, pPreguntaAbierta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pPreguntaAbierta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.opcional").value(DEFAULT_OPCIONAL.booleanValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    void getPPreguntaAbiertasByIdFiltering() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        Long id = pPreguntaAbierta.getId();

        defaultPPreguntaAbiertaShouldBeFound("id.equals=" + id);
        defaultPPreguntaAbiertaShouldNotBeFound("id.notEquals=" + id);

        defaultPPreguntaAbiertaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPPreguntaAbiertaShouldNotBeFound("id.greaterThan=" + id);

        defaultPPreguntaAbiertaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPPreguntaAbiertaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre equals to DEFAULT_NOMBRE
        defaultPPreguntaAbiertaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaAbiertaList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre not equals to DEFAULT_NOMBRE
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaAbiertaList where nombre not equals to UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the pPreguntaAbiertaList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre is not null
        defaultPPreguntaAbiertaShouldBeFound("nombre.specified=true");

        // Get all the pPreguntaAbiertaList where nombre is null
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre contains DEFAULT_NOMBRE
        defaultPPreguntaAbiertaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaAbiertaList where nombre contains UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where nombre does not contain DEFAULT_NOMBRE
        defaultPPreguntaAbiertaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaAbiertaList where nombre does not contain UPDATED_NOMBRE
        defaultPPreguntaAbiertaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOpcionalIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where opcional equals to DEFAULT_OPCIONAL
        defaultPPreguntaAbiertaShouldBeFound("opcional.equals=" + DEFAULT_OPCIONAL);

        // Get all the pPreguntaAbiertaList where opcional equals to UPDATED_OPCIONAL
        defaultPPreguntaAbiertaShouldNotBeFound("opcional.equals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOpcionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where opcional not equals to DEFAULT_OPCIONAL
        defaultPPreguntaAbiertaShouldNotBeFound("opcional.notEquals=" + DEFAULT_OPCIONAL);

        // Get all the pPreguntaAbiertaList where opcional not equals to UPDATED_OPCIONAL
        defaultPPreguntaAbiertaShouldBeFound("opcional.notEquals=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOpcionalIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where opcional in DEFAULT_OPCIONAL or UPDATED_OPCIONAL
        defaultPPreguntaAbiertaShouldBeFound("opcional.in=" + DEFAULT_OPCIONAL + "," + UPDATED_OPCIONAL);

        // Get all the pPreguntaAbiertaList where opcional equals to UPDATED_OPCIONAL
        defaultPPreguntaAbiertaShouldNotBeFound("opcional.in=" + UPDATED_OPCIONAL);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOpcionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where opcional is not null
        defaultPPreguntaAbiertaShouldBeFound("opcional.specified=true");

        // Get all the pPreguntaAbiertaList where opcional is null
        defaultPPreguntaAbiertaShouldNotBeFound("opcional.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden equals to DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden equals to UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden not equals to DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden not equals to UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the pPreguntaAbiertaList where orden equals to UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden is not null
        defaultPPreguntaAbiertaShouldBeFound("orden.specified=true");

        // Get all the pPreguntaAbiertaList where orden is null
        defaultPPreguntaAbiertaShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden is greater than or equal to DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden is greater than or equal to UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden is less than or equal to DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden is less than or equal to SMALLER_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden is less than DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden is less than UPDATED_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        // Get all the pPreguntaAbiertaList where orden is greater than DEFAULT_ORDEN
        defaultPPreguntaAbiertaShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaAbiertaList where orden is greater than SMALLER_ORDEN
        defaultPPreguntaAbiertaShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaAbiertasByPlantillaIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);
        Plantilla plantilla = PlantillaResourceIT.createEntity(em);
        em.persist(plantilla);
        em.flush();
        pPreguntaAbierta.setPlantilla(plantilla);
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);
        Long plantillaId = plantilla.getId();

        // Get all the pPreguntaAbiertaList where plantilla equals to plantillaId
        defaultPPreguntaAbiertaShouldBeFound("plantillaId.equals=" + plantillaId);

        // Get all the pPreguntaAbiertaList where plantilla equals to (plantillaId + 1)
        defaultPPreguntaAbiertaShouldNotBeFound("plantillaId.equals=" + (plantillaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPPreguntaAbiertaShouldBeFound(String filter) throws Exception {
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaAbierta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].opcional").value(hasItem(DEFAULT_OPCIONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));

        // Check, that the count call also returns 1
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPPreguntaAbiertaShouldNotBeFound(String filter) throws Exception {
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPPreguntaAbiertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPPreguntaAbierta() throws Exception {
        // Get the pPreguntaAbierta
        restPPreguntaAbiertaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPPreguntaAbierta() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();

        // Update the pPreguntaAbierta
        PPreguntaAbierta updatedPPreguntaAbierta = pPreguntaAbiertaRepository.findById(pPreguntaAbierta.getId()).get();
        // Disconnect from session so that the updates on updatedPPreguntaAbierta are not directly saved in db
        em.detach(updatedPPreguntaAbierta);
        updatedPPreguntaAbierta.nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restPPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPPreguntaAbierta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaAbierta testPPreguntaAbierta = pPreguntaAbiertaList.get(pPreguntaAbiertaList.size() - 1);
        assertThat(testPPreguntaAbierta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaAbierta.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testPPreguntaAbierta.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void putNonExistingPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pPreguntaAbierta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePPreguntaAbiertaWithPatch() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();

        // Update the pPreguntaAbierta using partial update
        PPreguntaAbierta partialUpdatedPPreguntaAbierta = new PPreguntaAbierta();
        partialUpdatedPPreguntaAbierta.setId(pPreguntaAbierta.getId());

        restPPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaAbierta testPPreguntaAbierta = pPreguntaAbiertaList.get(pPreguntaAbiertaList.size() - 1);
        assertThat(testPPreguntaAbierta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPPreguntaAbierta.getOpcional()).isEqualTo(DEFAULT_OPCIONAL);
        assertThat(testPPreguntaAbierta.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void fullUpdatePPreguntaAbiertaWithPatch() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();

        // Update the pPreguntaAbierta using partial update
        PPreguntaAbierta partialUpdatedPPreguntaAbierta = new PPreguntaAbierta();
        partialUpdatedPPreguntaAbierta.setId(pPreguntaAbierta.getId());

        partialUpdatedPPreguntaAbierta.nombre(UPDATED_NOMBRE).opcional(UPDATED_OPCIONAL).orden(UPDATED_ORDEN);

        restPPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaAbierta))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaAbierta testPPreguntaAbierta = pPreguntaAbiertaList.get(pPreguntaAbiertaList.size() - 1);
        assertThat(testPPreguntaAbierta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaAbierta.getOpcional()).isEqualTo(UPDATED_OPCIONAL);
        assertThat(testPPreguntaAbierta.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void patchNonExistingPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pPreguntaAbierta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPPreguntaAbierta() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaAbiertaRepository.findAll().size();
        pPreguntaAbierta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaAbiertaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaAbierta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaAbierta in the database
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePPreguntaAbierta() throws Exception {
        // Initialize the database
        pPreguntaAbiertaRepository.saveAndFlush(pPreguntaAbierta);

        int databaseSizeBeforeDelete = pPreguntaAbiertaRepository.findAll().size();

        // Delete the pPreguntaAbierta
        restPPreguntaAbiertaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pPreguntaAbierta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PPreguntaAbierta> pPreguntaAbiertaList = pPreguntaAbiertaRepository.findAll();
        assertThat(pPreguntaAbiertaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
