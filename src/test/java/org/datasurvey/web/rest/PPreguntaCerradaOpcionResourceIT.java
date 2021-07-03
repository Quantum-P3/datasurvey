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
import org.datasurvey.repository.PPreguntaCerradaOpcionRepository;
import org.datasurvey.service.criteria.PPreguntaCerradaOpcionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PPreguntaCerradaOpcionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PPreguntaCerradaOpcionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String ENTITY_API_URL = "/api/p-pregunta-cerrada-opcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPPreguntaCerradaOpcionMockMvc;

    private PPreguntaCerradaOpcion pPreguntaCerradaOpcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaCerradaOpcion createEntity(EntityManager em) {
        PPreguntaCerradaOpcion pPreguntaCerradaOpcion = new PPreguntaCerradaOpcion().nombre(DEFAULT_NOMBRE).orden(DEFAULT_ORDEN);
        return pPreguntaCerradaOpcion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PPreguntaCerradaOpcion createUpdatedEntity(EntityManager em) {
        PPreguntaCerradaOpcion pPreguntaCerradaOpcion = new PPreguntaCerradaOpcion().nombre(UPDATED_NOMBRE).orden(UPDATED_ORDEN);
        return pPreguntaCerradaOpcion;
    }

    @BeforeEach
    public void initTest() {
        pPreguntaCerradaOpcion = createEntity(em);
    }

    @Test
    @Transactional
    void createPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeCreate = pPreguntaCerradaOpcionRepository.findAll().size();
        // Create the PPreguntaCerradaOpcion
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isCreated());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeCreate + 1);
        PPreguntaCerradaOpcion testPPreguntaCerradaOpcion = pPreguntaCerradaOpcionList.get(pPreguntaCerradaOpcionList.size() - 1);
        assertThat(testPPreguntaCerradaOpcion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPPreguntaCerradaOpcion.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void createPPreguntaCerradaOpcionWithExistingId() throws Exception {
        // Create the PPreguntaCerradaOpcion with an existing ID
        pPreguntaCerradaOpcion.setId(1L);

        int databaseSizeBeforeCreate = pPreguntaCerradaOpcionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaOpcionRepository.findAll().size();
        // set the field null
        pPreguntaCerradaOpcion.setNombre(null);

        // Create the PPreguntaCerradaOpcion, which fails.

        restPPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        int databaseSizeBeforeTest = pPreguntaCerradaOpcionRepository.findAll().size();
        // set the field null
        pPreguntaCerradaOpcion.setOrden(null);

        // Create the PPreguntaCerradaOpcion, which fails.

        restPPreguntaCerradaOpcionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcions() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaCerradaOpcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    void getPPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get the pPreguntaCerradaOpcion
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL_ID, pPreguntaCerradaOpcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pPreguntaCerradaOpcion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    void getPPreguntaCerradaOpcionsByIdFiltering() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        Long id = pPreguntaCerradaOpcion.getId();

        defaultPPreguntaCerradaOpcionShouldBeFound("id.equals=" + id);
        defaultPPreguntaCerradaOpcionShouldNotBeFound("id.notEquals=" + id);

        defaultPPreguntaCerradaOpcionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPPreguntaCerradaOpcionShouldNotBeFound("id.greaterThan=" + id);

        defaultPPreguntaCerradaOpcionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPPreguntaCerradaOpcionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre equals to DEFAULT_NOMBRE
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaOpcionList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre not equals to DEFAULT_NOMBRE
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaOpcionList where nombre not equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the pPreguntaCerradaOpcionList where nombre equals to UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre is not null
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.specified=true");

        // Get all the pPreguntaCerradaOpcionList where nombre is null
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre contains DEFAULT_NOMBRE
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaOpcionList where nombre contains UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where nombre does not contain DEFAULT_NOMBRE
        defaultPPreguntaCerradaOpcionShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the pPreguntaCerradaOpcionList where nombre does not contain UPDATED_NOMBRE
        defaultPPreguntaCerradaOpcionShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden equals to DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden equals to UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden not equals to DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.notEquals=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden not equals to UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.notEquals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden equals to UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden is not null
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.specified=true");

        // Get all the pPreguntaCerradaOpcionList where orden is null
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden is greater than or equal to DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden is greater than or equal to UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden is less than or equal to DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden is less than or equal to SMALLER_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden is less than DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden is less than UPDATED_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        // Get all the pPreguntaCerradaOpcionList where orden is greater than DEFAULT_ORDEN
        defaultPPreguntaCerradaOpcionShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the pPreguntaCerradaOpcionList where orden is greater than SMALLER_ORDEN
        defaultPPreguntaCerradaOpcionShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPPreguntaCerradaOpcionsByPPreguntaCerradaIsEqualToSomething() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);
        PPreguntaCerrada pPreguntaCerrada = PPreguntaCerradaResourceIT.createEntity(em);
        em.persist(pPreguntaCerrada);
        em.flush();
        pPreguntaCerradaOpcion.setPPreguntaCerrada(pPreguntaCerrada);
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);
        Long pPreguntaCerradaId = pPreguntaCerrada.getId();

        // Get all the pPreguntaCerradaOpcionList where pPreguntaCerrada equals to pPreguntaCerradaId
        defaultPPreguntaCerradaOpcionShouldBeFound("pPreguntaCerradaId.equals=" + pPreguntaCerradaId);

        // Get all the pPreguntaCerradaOpcionList where pPreguntaCerrada equals to (pPreguntaCerradaId + 1)
        defaultPPreguntaCerradaOpcionShouldNotBeFound("pPreguntaCerradaId.equals=" + (pPreguntaCerradaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPPreguntaCerradaOpcionShouldBeFound(String filter) throws Exception {
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pPreguntaCerradaOpcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));

        // Check, that the count call also returns 1
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPPreguntaCerradaOpcionShouldNotBeFound(String filter) throws Exception {
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPPreguntaCerradaOpcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPPreguntaCerradaOpcion() throws Exception {
        // Get the pPreguntaCerradaOpcion
        restPPreguntaCerradaOpcionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();

        // Update the pPreguntaCerradaOpcion
        PPreguntaCerradaOpcion updatedPPreguntaCerradaOpcion = pPreguntaCerradaOpcionRepository
            .findById(pPreguntaCerradaOpcion.getId())
            .get();
        // Disconnect from session so that the updates on updatedPPreguntaCerradaOpcion are not directly saved in db
        em.detach(updatedPPreguntaCerradaOpcion);
        updatedPPreguntaCerradaOpcion.nombre(UPDATED_NOMBRE).orden(UPDATED_ORDEN);

        restPPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPPreguntaCerradaOpcion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerradaOpcion testPPreguntaCerradaOpcion = pPreguntaCerradaOpcionList.get(pPreguntaCerradaOpcionList.size() - 1);
        assertThat(testPPreguntaCerradaOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerradaOpcion.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void putNonExistingPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pPreguntaCerradaOpcion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePPreguntaCerradaOpcionWithPatch() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();

        // Update the pPreguntaCerradaOpcion using partial update
        PPreguntaCerradaOpcion partialUpdatedPPreguntaCerradaOpcion = new PPreguntaCerradaOpcion();
        partialUpdatedPPreguntaCerradaOpcion.setId(pPreguntaCerradaOpcion.getId());

        partialUpdatedPPreguntaCerradaOpcion.nombre(UPDATED_NOMBRE);

        restPPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerradaOpcion testPPreguntaCerradaOpcion = pPreguntaCerradaOpcionList.get(pPreguntaCerradaOpcionList.size() - 1);
        assertThat(testPPreguntaCerradaOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerradaOpcion.getOrden()).isEqualTo(DEFAULT_ORDEN);
    }

    @Test
    @Transactional
    void fullUpdatePPreguntaCerradaOpcionWithPatch() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();

        // Update the pPreguntaCerradaOpcion using partial update
        PPreguntaCerradaOpcion partialUpdatedPPreguntaCerradaOpcion = new PPreguntaCerradaOpcion();
        partialUpdatedPPreguntaCerradaOpcion.setId(pPreguntaCerradaOpcion.getId());

        partialUpdatedPPreguntaCerradaOpcion.nombre(UPDATED_NOMBRE).orden(UPDATED_ORDEN);

        restPPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPPreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPPreguntaCerradaOpcion))
            )
            .andExpect(status().isOk());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
        PPreguntaCerradaOpcion testPPreguntaCerradaOpcion = pPreguntaCerradaOpcionList.get(pPreguntaCerradaOpcionList.size() - 1);
        assertThat(testPPreguntaCerradaOpcion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPPreguntaCerradaOpcion.getOrden()).isEqualTo(UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void patchNonExistingPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pPreguntaCerradaOpcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPPreguntaCerradaOpcion() throws Exception {
        int databaseSizeBeforeUpdate = pPreguntaCerradaOpcionRepository.findAll().size();
        pPreguntaCerradaOpcion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPPreguntaCerradaOpcionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pPreguntaCerradaOpcion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PPreguntaCerradaOpcion in the database
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePPreguntaCerradaOpcion() throws Exception {
        // Initialize the database
        pPreguntaCerradaOpcionRepository.saveAndFlush(pPreguntaCerradaOpcion);

        int databaseSizeBeforeDelete = pPreguntaCerradaOpcionRepository.findAll().size();

        // Delete the pPreguntaCerradaOpcion
        restPPreguntaCerradaOpcionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pPreguntaCerradaOpcion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PPreguntaCerradaOpcion> pPreguntaCerradaOpcionList = pPreguntaCerradaOpcionRepository.findAll();
        assertThat(pPreguntaCerradaOpcionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
