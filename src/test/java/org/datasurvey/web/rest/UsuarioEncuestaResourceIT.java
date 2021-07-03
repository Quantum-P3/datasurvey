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
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.domain.enumeration.EstadoColaborador;
import org.datasurvey.domain.enumeration.RolColaborador;
import org.datasurvey.repository.UsuarioEncuestaRepository;
import org.datasurvey.service.criteria.UsuarioEncuestaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioEncuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioEncuestaResourceIT {

    private static final RolColaborador DEFAULT_ROL = RolColaborador.READ;
    private static final RolColaborador UPDATED_ROL = RolColaborador.WRITE;

    private static final EstadoColaborador DEFAULT_ESTADO = EstadoColaborador.PENDING;
    private static final EstadoColaborador UPDATED_ESTADO = EstadoColaborador.ACTIVE;

    private static final ZonedDateTime DEFAULT_FECHA_AGREGADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_AGREGADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_AGREGADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/usuario-encuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioEncuestaRepository usuarioEncuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioEncuestaMockMvc;

    private UsuarioEncuesta usuarioEncuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioEncuesta createEntity(EntityManager em) {
        UsuarioEncuesta usuarioEncuesta = new UsuarioEncuesta()
            .rol(DEFAULT_ROL)
            .estado(DEFAULT_ESTADO)
            .fechaAgregado(DEFAULT_FECHA_AGREGADO);
        return usuarioEncuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioEncuesta createUpdatedEntity(EntityManager em) {
        UsuarioEncuesta usuarioEncuesta = new UsuarioEncuesta()
            .rol(UPDATED_ROL)
            .estado(UPDATED_ESTADO)
            .fechaAgregado(UPDATED_FECHA_AGREGADO);
        return usuarioEncuesta;
    }

    @BeforeEach
    public void initTest() {
        usuarioEncuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeCreate = usuarioEncuestaRepository.findAll().size();
        // Create the UsuarioEncuesta
        restUsuarioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isCreated());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioEncuesta testUsuarioEncuesta = usuarioEncuestaList.get(usuarioEncuestaList.size() - 1);
        assertThat(testUsuarioEncuesta.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testUsuarioEncuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testUsuarioEncuesta.getFechaAgregado()).isEqualTo(DEFAULT_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void createUsuarioEncuestaWithExistingId() throws Exception {
        // Create the UsuarioEncuesta with an existing ID
        usuarioEncuesta.setId(1L);

        int databaseSizeBeforeCreate = usuarioEncuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRolIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioEncuestaRepository.findAll().size();
        // set the field null
        usuarioEncuesta.setRol(null);

        // Create the UsuarioEncuesta, which fails.

        restUsuarioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioEncuestaRepository.findAll().size();
        // set the field null
        usuarioEncuesta.setEstado(null);

        // Create the UsuarioEncuesta, which fails.

        restUsuarioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAgregadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioEncuestaRepository.findAll().size();
        // set the field null
        usuarioEncuesta.setFechaAgregado(null);

        // Create the UsuarioEncuesta, which fails.

        restUsuarioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestas() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioEncuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].fechaAgregado").value(hasItem(sameInstant(DEFAULT_FECHA_AGREGADO))));
    }

    @Test
    @Transactional
    void getUsuarioEncuesta() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get the usuarioEncuesta
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioEncuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioEncuesta.getId().intValue()))
            .andExpect(jsonPath("$.rol").value(DEFAULT_ROL.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.fechaAgregado").value(sameInstant(DEFAULT_FECHA_AGREGADO)));
    }

    @Test
    @Transactional
    void getUsuarioEncuestasByIdFiltering() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        Long id = usuarioEncuesta.getId();

        defaultUsuarioEncuestaShouldBeFound("id.equals=" + id);
        defaultUsuarioEncuestaShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioEncuestaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioEncuestaShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioEncuestaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioEncuestaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByRolIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where rol equals to DEFAULT_ROL
        defaultUsuarioEncuestaShouldBeFound("rol.equals=" + DEFAULT_ROL);

        // Get all the usuarioEncuestaList where rol equals to UPDATED_ROL
        defaultUsuarioEncuestaShouldNotBeFound("rol.equals=" + UPDATED_ROL);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByRolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where rol not equals to DEFAULT_ROL
        defaultUsuarioEncuestaShouldNotBeFound("rol.notEquals=" + DEFAULT_ROL);

        // Get all the usuarioEncuestaList where rol not equals to UPDATED_ROL
        defaultUsuarioEncuestaShouldBeFound("rol.notEquals=" + UPDATED_ROL);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByRolIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where rol in DEFAULT_ROL or UPDATED_ROL
        defaultUsuarioEncuestaShouldBeFound("rol.in=" + DEFAULT_ROL + "," + UPDATED_ROL);

        // Get all the usuarioEncuestaList where rol equals to UPDATED_ROL
        defaultUsuarioEncuestaShouldNotBeFound("rol.in=" + UPDATED_ROL);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByRolIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where rol is not null
        defaultUsuarioEncuestaShouldBeFound("rol.specified=true");

        // Get all the usuarioEncuestaList where rol is null
        defaultUsuarioEncuestaShouldNotBeFound("rol.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where estado equals to DEFAULT_ESTADO
        defaultUsuarioEncuestaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the usuarioEncuestaList where estado equals to UPDATED_ESTADO
        defaultUsuarioEncuestaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where estado not equals to DEFAULT_ESTADO
        defaultUsuarioEncuestaShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the usuarioEncuestaList where estado not equals to UPDATED_ESTADO
        defaultUsuarioEncuestaShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultUsuarioEncuestaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the usuarioEncuestaList where estado equals to UPDATED_ESTADO
        defaultUsuarioEncuestaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where estado is not null
        defaultUsuarioEncuestaShouldBeFound("estado.specified=true");

        // Get all the usuarioEncuestaList where estado is null
        defaultUsuarioEncuestaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado equals to DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.equals=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado equals to UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.equals=" + UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado not equals to DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.notEquals=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado not equals to UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.notEquals=" + UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado in DEFAULT_FECHA_AGREGADO or UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.in=" + DEFAULT_FECHA_AGREGADO + "," + UPDATED_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado equals to UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.in=" + UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado is not null
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.specified=true");

        // Get all the usuarioEncuestaList where fechaAgregado is null
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado is greater than or equal to DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.greaterThanOrEqual=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado is greater than or equal to UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.greaterThanOrEqual=" + UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado is less than or equal to DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.lessThanOrEqual=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado is less than or equal to SMALLER_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.lessThanOrEqual=" + SMALLER_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado is less than DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.lessThan=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado is less than UPDATED_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.lessThan=" + UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByFechaAgregadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        // Get all the usuarioEncuestaList where fechaAgregado is greater than DEFAULT_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldNotBeFound("fechaAgregado.greaterThan=" + DEFAULT_FECHA_AGREGADO);

        // Get all the usuarioEncuestaList where fechaAgregado is greater than SMALLER_FECHA_AGREGADO
        defaultUsuarioEncuestaShouldBeFound("fechaAgregado.greaterThan=" + SMALLER_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByUsuarioExtraIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);
        UsuarioExtra usuarioExtra = UsuarioExtraResourceIT.createEntity(em);
        em.persist(usuarioExtra);
        em.flush();
        usuarioEncuesta.setUsuarioExtra(usuarioExtra);
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);
        Long usuarioExtraId = usuarioExtra.getId();

        // Get all the usuarioEncuestaList where usuarioExtra equals to usuarioExtraId
        defaultUsuarioEncuestaShouldBeFound("usuarioExtraId.equals=" + usuarioExtraId);

        // Get all the usuarioEncuestaList where usuarioExtra equals to (usuarioExtraId + 1)
        defaultUsuarioEncuestaShouldNotBeFound("usuarioExtraId.equals=" + (usuarioExtraId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioEncuestasByEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);
        Encuesta encuesta = EncuestaResourceIT.createEntity(em);
        em.persist(encuesta);
        em.flush();
        usuarioEncuesta.setEncuesta(encuesta);
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);
        Long encuestaId = encuesta.getId();

        // Get all the usuarioEncuestaList where encuesta equals to encuestaId
        defaultUsuarioEncuestaShouldBeFound("encuestaId.equals=" + encuestaId);

        // Get all the usuarioEncuestaList where encuesta equals to (encuestaId + 1)
        defaultUsuarioEncuestaShouldNotBeFound("encuestaId.equals=" + (encuestaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioEncuestaShouldBeFound(String filter) throws Exception {
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioEncuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].fechaAgregado").value(hasItem(sameInstant(DEFAULT_FECHA_AGREGADO))));

        // Check, that the count call also returns 1
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioEncuestaShouldNotBeFound(String filter) throws Exception {
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuarioEncuesta() throws Exception {
        // Get the usuarioEncuesta
        restUsuarioEncuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuarioEncuesta() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();

        // Update the usuarioEncuesta
        UsuarioEncuesta updatedUsuarioEncuesta = usuarioEncuestaRepository.findById(usuarioEncuesta.getId()).get();
        // Disconnect from session so that the updates on updatedUsuarioEncuesta are not directly saved in db
        em.detach(updatedUsuarioEncuesta);
        updatedUsuarioEncuesta.rol(UPDATED_ROL).estado(UPDATED_ESTADO).fechaAgregado(UPDATED_FECHA_AGREGADO);

        restUsuarioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuarioEncuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuarioEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        UsuarioEncuesta testUsuarioEncuesta = usuarioEncuestaList.get(usuarioEncuestaList.size() - 1);
        assertThat(testUsuarioEncuesta.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuarioEncuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testUsuarioEncuesta.getFechaAgregado()).isEqualTo(UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void putNonExistingUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioEncuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioEncuestaWithPatch() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();

        // Update the usuarioEncuesta using partial update
        UsuarioEncuesta partialUpdatedUsuarioEncuesta = new UsuarioEncuesta();
        partialUpdatedUsuarioEncuesta.setId(usuarioEncuesta.getId());

        partialUpdatedUsuarioEncuesta.rol(UPDATED_ROL).fechaAgregado(UPDATED_FECHA_AGREGADO);

        restUsuarioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioEncuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        UsuarioEncuesta testUsuarioEncuesta = usuarioEncuestaList.get(usuarioEncuestaList.size() - 1);
        assertThat(testUsuarioEncuesta.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuarioEncuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testUsuarioEncuesta.getFechaAgregado()).isEqualTo(UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioEncuestaWithPatch() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();

        // Update the usuarioEncuesta using partial update
        UsuarioEncuesta partialUpdatedUsuarioEncuesta = new UsuarioEncuesta();
        partialUpdatedUsuarioEncuesta.setId(usuarioEncuesta.getId());

        partialUpdatedUsuarioEncuesta.rol(UPDATED_ROL).estado(UPDATED_ESTADO).fechaAgregado(UPDATED_FECHA_AGREGADO);

        restUsuarioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioEncuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        UsuarioEncuesta testUsuarioEncuesta = usuarioEncuestaList.get(usuarioEncuestaList.size() - 1);
        assertThat(testUsuarioEncuesta.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testUsuarioEncuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testUsuarioEncuesta.getFechaAgregado()).isEqualTo(UPDATED_FECHA_AGREGADO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioEncuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = usuarioEncuestaRepository.findAll().size();
        usuarioEncuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioEncuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioEncuesta in the database
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarioEncuesta() throws Exception {
        // Initialize the database
        usuarioEncuestaRepository.saveAndFlush(usuarioEncuesta);

        int databaseSizeBeforeDelete = usuarioEncuestaRepository.findAll().size();

        // Delete the usuarioEncuesta
        restUsuarioEncuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioEncuesta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UsuarioEncuesta> usuarioEncuestaList = usuarioEncuestaRepository.findAll();
        assertThat(usuarioEncuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
