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
import org.datasurvey.repository.EPreguntaAbiertaRespuestaRepository;
import org.datasurvey.service.criteria.EPreguntaAbiertaRespuestaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EPreguntaAbiertaRespuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EPreguntaAbiertaRespuestaResourceIT {

    private static final String DEFAULT_RESPUESTA = "AAAAAAAAAA";
    private static final String UPDATED_RESPUESTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/e-pregunta-abierta-respuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEPreguntaAbiertaRespuestaMockMvc;

    private EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaAbiertaRespuesta createEntity(EntityManager em) {
        EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta = new EPreguntaAbiertaRespuesta().respuesta(DEFAULT_RESPUESTA);
        return ePreguntaAbiertaRespuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EPreguntaAbiertaRespuesta createUpdatedEntity(EntityManager em) {
        EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta = new EPreguntaAbiertaRespuesta().respuesta(UPDATED_RESPUESTA);
        return ePreguntaAbiertaRespuesta;
    }

    @BeforeEach
    public void initTest() {
        ePreguntaAbiertaRespuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeCreate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        // Create the EPreguntaAbiertaRespuesta
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isCreated());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeCreate + 1);
        EPreguntaAbiertaRespuesta testEPreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaList.get(
            ePreguntaAbiertaRespuestaList.size() - 1
        );
        assertThat(testEPreguntaAbiertaRespuesta.getRespuesta()).isEqualTo(DEFAULT_RESPUESTA);
    }

    @Test
    @Transactional
    void createEPreguntaAbiertaRespuestaWithExistingId() throws Exception {
        // Create the EPreguntaAbiertaRespuesta with an existing ID
        ePreguntaAbiertaRespuesta.setId(1L);

        int databaseSizeBeforeCreate = ePreguntaAbiertaRespuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRespuestaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ePreguntaAbiertaRespuestaRepository.findAll().size();
        // set the field null
        ePreguntaAbiertaRespuesta.setRespuesta(null);

        // Create the EPreguntaAbiertaRespuesta, which fails.

        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestas() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaAbiertaRespuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].respuesta").value(hasItem(DEFAULT_RESPUESTA)));
    }

    @Test
    @Transactional
    void getEPreguntaAbiertaRespuesta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get the ePreguntaAbiertaRespuesta
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, ePreguntaAbiertaRespuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ePreguntaAbiertaRespuesta.getId().intValue()))
            .andExpect(jsonPath("$.respuesta").value(DEFAULT_RESPUESTA));
    }

    @Test
    @Transactional
    void getEPreguntaAbiertaRespuestasByIdFiltering() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        Long id = ePreguntaAbiertaRespuesta.getId();

        defaultEPreguntaAbiertaRespuestaShouldBeFound("id.equals=" + id);
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("id.notEquals=" + id);

        defaultEPreguntaAbiertaRespuestaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("id.greaterThan=" + id);

        defaultEPreguntaAbiertaRespuestaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta equals to DEFAULT_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.equals=" + DEFAULT_RESPUESTA);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta equals to UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.equals=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta not equals to DEFAULT_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.notEquals=" + DEFAULT_RESPUESTA);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta not equals to UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.notEquals=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaIsInShouldWork() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta in DEFAULT_RESPUESTA or UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.in=" + DEFAULT_RESPUESTA + "," + UPDATED_RESPUESTA);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta equals to UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.in=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaIsNullOrNotNull() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta is not null
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.specified=true");

        // Get all the ePreguntaAbiertaRespuestaList where respuesta is null
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.specified=false");
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta contains DEFAULT_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.contains=" + DEFAULT_RESPUESTA);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta contains UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.contains=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByRespuestaNotContainsSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta does not contain DEFAULT_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("respuesta.doesNotContain=" + DEFAULT_RESPUESTA);

        // Get all the ePreguntaAbiertaRespuestaList where respuesta does not contain UPDATED_RESPUESTA
        defaultEPreguntaAbiertaRespuestaShouldBeFound("respuesta.doesNotContain=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllEPreguntaAbiertaRespuestasByEPreguntaAbiertaIsEqualToSomething() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);
        EPreguntaAbierta ePreguntaAbierta = EPreguntaAbiertaResourceIT.createEntity(em);
        em.persist(ePreguntaAbierta);
        em.flush();
        ePreguntaAbiertaRespuesta.setEPreguntaAbierta(ePreguntaAbierta);
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);
        Long ePreguntaAbiertaId = ePreguntaAbierta.getId();

        // Get all the ePreguntaAbiertaRespuestaList where ePreguntaAbierta equals to ePreguntaAbiertaId
        defaultEPreguntaAbiertaRespuestaShouldBeFound("ePreguntaAbiertaId.equals=" + ePreguntaAbiertaId);

        // Get all the ePreguntaAbiertaRespuestaList where ePreguntaAbierta equals to (ePreguntaAbiertaId + 1)
        defaultEPreguntaAbiertaRespuestaShouldNotBeFound("ePreguntaAbiertaId.equals=" + (ePreguntaAbiertaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEPreguntaAbiertaRespuestaShouldBeFound(String filter) throws Exception {
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ePreguntaAbiertaRespuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].respuesta").value(hasItem(DEFAULT_RESPUESTA)));

        // Check, that the count call also returns 1
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEPreguntaAbiertaRespuestaShouldNotBeFound(String filter) throws Exception {
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEPreguntaAbiertaRespuesta() throws Exception {
        // Get the ePreguntaAbiertaRespuesta
        restEPreguntaAbiertaRespuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEPreguntaAbiertaRespuesta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();

        // Update the ePreguntaAbiertaRespuesta
        EPreguntaAbiertaRespuesta updatedEPreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaRepository
            .findById(ePreguntaAbiertaRespuesta.getId())
            .get();
        // Disconnect from session so that the updates on updatedEPreguntaAbiertaRespuesta are not directly saved in db
        em.detach(updatedEPreguntaAbiertaRespuesta);
        updatedEPreguntaAbiertaRespuesta.respuesta(UPDATED_RESPUESTA);

        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEPreguntaAbiertaRespuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEPreguntaAbiertaRespuesta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbiertaRespuesta testEPreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaList.get(
            ePreguntaAbiertaRespuestaList.size() - 1
        );
        assertThat(testEPreguntaAbiertaRespuesta.getRespuesta()).isEqualTo(UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void putNonExistingEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ePreguntaAbiertaRespuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEPreguntaAbiertaRespuestaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();

        // Update the ePreguntaAbiertaRespuesta using partial update
        EPreguntaAbiertaRespuesta partialUpdatedEPreguntaAbiertaRespuesta = new EPreguntaAbiertaRespuesta();
        partialUpdatedEPreguntaAbiertaRespuesta.setId(ePreguntaAbiertaRespuesta.getId());

        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaAbiertaRespuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaAbiertaRespuesta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbiertaRespuesta testEPreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaList.get(
            ePreguntaAbiertaRespuestaList.size() - 1
        );
        assertThat(testEPreguntaAbiertaRespuesta.getRespuesta()).isEqualTo(DEFAULT_RESPUESTA);
    }

    @Test
    @Transactional
    void fullUpdateEPreguntaAbiertaRespuestaWithPatch() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();

        // Update the ePreguntaAbiertaRespuesta using partial update
        EPreguntaAbiertaRespuesta partialUpdatedEPreguntaAbiertaRespuesta = new EPreguntaAbiertaRespuesta();
        partialUpdatedEPreguntaAbiertaRespuesta.setId(ePreguntaAbiertaRespuesta.getId());

        partialUpdatedEPreguntaAbiertaRespuesta.respuesta(UPDATED_RESPUESTA);

        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEPreguntaAbiertaRespuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEPreguntaAbiertaRespuesta))
            )
            .andExpect(status().isOk());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
        EPreguntaAbiertaRespuesta testEPreguntaAbiertaRespuesta = ePreguntaAbiertaRespuestaList.get(
            ePreguntaAbiertaRespuestaList.size() - 1
        );
        assertThat(testEPreguntaAbiertaRespuesta.getRespuesta()).isEqualTo(UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void patchNonExistingEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ePreguntaAbiertaRespuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEPreguntaAbiertaRespuesta() throws Exception {
        int databaseSizeBeforeUpdate = ePreguntaAbiertaRespuestaRepository.findAll().size();
        ePreguntaAbiertaRespuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ePreguntaAbiertaRespuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EPreguntaAbiertaRespuesta in the database
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEPreguntaAbiertaRespuesta() throws Exception {
        // Initialize the database
        ePreguntaAbiertaRespuestaRepository.saveAndFlush(ePreguntaAbiertaRespuesta);

        int databaseSizeBeforeDelete = ePreguntaAbiertaRespuestaRepository.findAll().size();

        // Delete the ePreguntaAbiertaRespuesta
        restEPreguntaAbiertaRespuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ePreguntaAbiertaRespuesta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestaList = ePreguntaAbiertaRespuestaRepository.findAll();
        assertThat(ePreguntaAbiertaRespuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
