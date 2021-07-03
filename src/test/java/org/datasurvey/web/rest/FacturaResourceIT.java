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
import org.datasurvey.domain.Factura;
import org.datasurvey.repository.FacturaRepository;
import org.datasurvey.service.criteria.FacturaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final String DEFAULT_NOMBRE_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_PLANTILLA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PLANTILLA = "BBBBBBBBBB";

    private static final Double DEFAULT_COSTO = 1D;
    private static final Double UPDATED_COSTO = 2D;
    private static final Double SMALLER_COSTO = 1D - 1D;

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .nombreUsuario(DEFAULT_NOMBRE_USUARIO)
            .nombrePlantilla(DEFAULT_NOMBRE_PLANTILLA)
            .costo(DEFAULT_COSTO)
            .fecha(DEFAULT_FECHA);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .nombreUsuario(UPDATED_NOMBRE_USUARIO)
            .nombrePlantilla(UPDATED_NOMBRE_PLANTILLA)
            .costo(UPDATED_COSTO)
            .fecha(UPDATED_FECHA);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();
        // Create the Factura
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNombreUsuario()).isEqualTo(DEFAULT_NOMBRE_USUARIO);
        assertThat(testFactura.getNombrePlantilla()).isEqualTo(DEFAULT_NOMBRE_PLANTILLA);
        assertThat(testFactura.getCosto()).isEqualTo(DEFAULT_COSTO);
        assertThat(testFactura.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);

        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setNombreUsuario(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombrePlantillaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setNombrePlantilla(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setCosto(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setFecha(null);

        // Create the Factura, which fails.

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreUsuario").value(hasItem(DEFAULT_NOMBRE_USUARIO)))
            .andExpect(jsonPath("$.[*].nombrePlantilla").value(hasItem(DEFAULT_NOMBRE_PLANTILLA)))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.nombreUsuario").value(DEFAULT_NOMBRE_USUARIO))
            .andExpect(jsonPath("$.nombrePlantilla").value(DEFAULT_NOMBRE_PLANTILLA))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO.doubleValue()))
            .andExpect(jsonPath("$.fecha").value(sameInstant(DEFAULT_FECHA)));
    }

    @Test
    @Transactional
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        Long id = factura.getId();

        defaultFacturaShouldBeFound("id.equals=" + id);
        defaultFacturaShouldNotBeFound("id.notEquals=" + id);

        defaultFacturaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacturaShouldNotBeFound("id.greaterThan=" + id);

        defaultFacturaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacturaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario equals to DEFAULT_NOMBRE_USUARIO
        defaultFacturaShouldBeFound("nombreUsuario.equals=" + DEFAULT_NOMBRE_USUARIO);

        // Get all the facturaList where nombreUsuario equals to UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldNotBeFound("nombreUsuario.equals=" + UPDATED_NOMBRE_USUARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario not equals to DEFAULT_NOMBRE_USUARIO
        defaultFacturaShouldNotBeFound("nombreUsuario.notEquals=" + DEFAULT_NOMBRE_USUARIO);

        // Get all the facturaList where nombreUsuario not equals to UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldBeFound("nombreUsuario.notEquals=" + UPDATED_NOMBRE_USUARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario in DEFAULT_NOMBRE_USUARIO or UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldBeFound("nombreUsuario.in=" + DEFAULT_NOMBRE_USUARIO + "," + UPDATED_NOMBRE_USUARIO);

        // Get all the facturaList where nombreUsuario equals to UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldNotBeFound("nombreUsuario.in=" + UPDATED_NOMBRE_USUARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario is not null
        defaultFacturaShouldBeFound("nombreUsuario.specified=true");

        // Get all the facturaList where nombreUsuario is null
        defaultFacturaShouldNotBeFound("nombreUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario contains DEFAULT_NOMBRE_USUARIO
        defaultFacturaShouldBeFound("nombreUsuario.contains=" + DEFAULT_NOMBRE_USUARIO);

        // Get all the facturaList where nombreUsuario contains UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldNotBeFound("nombreUsuario.contains=" + UPDATED_NOMBRE_USUARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByNombreUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombreUsuario does not contain DEFAULT_NOMBRE_USUARIO
        defaultFacturaShouldNotBeFound("nombreUsuario.doesNotContain=" + DEFAULT_NOMBRE_USUARIO);

        // Get all the facturaList where nombreUsuario does not contain UPDATED_NOMBRE_USUARIO
        defaultFacturaShouldBeFound("nombreUsuario.doesNotContain=" + UPDATED_NOMBRE_USUARIO);
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla equals to DEFAULT_NOMBRE_PLANTILLA
        defaultFacturaShouldBeFound("nombrePlantilla.equals=" + DEFAULT_NOMBRE_PLANTILLA);

        // Get all the facturaList where nombrePlantilla equals to UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldNotBeFound("nombrePlantilla.equals=" + UPDATED_NOMBRE_PLANTILLA);
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla not equals to DEFAULT_NOMBRE_PLANTILLA
        defaultFacturaShouldNotBeFound("nombrePlantilla.notEquals=" + DEFAULT_NOMBRE_PLANTILLA);

        // Get all the facturaList where nombrePlantilla not equals to UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldBeFound("nombrePlantilla.notEquals=" + UPDATED_NOMBRE_PLANTILLA);
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla in DEFAULT_NOMBRE_PLANTILLA or UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldBeFound("nombrePlantilla.in=" + DEFAULT_NOMBRE_PLANTILLA + "," + UPDATED_NOMBRE_PLANTILLA);

        // Get all the facturaList where nombrePlantilla equals to UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldNotBeFound("nombrePlantilla.in=" + UPDATED_NOMBRE_PLANTILLA);
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla is not null
        defaultFacturaShouldBeFound("nombrePlantilla.specified=true");

        // Get all the facturaList where nombrePlantilla is null
        defaultFacturaShouldNotBeFound("nombrePlantilla.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla contains DEFAULT_NOMBRE_PLANTILLA
        defaultFacturaShouldBeFound("nombrePlantilla.contains=" + DEFAULT_NOMBRE_PLANTILLA);

        // Get all the facturaList where nombrePlantilla contains UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldNotBeFound("nombrePlantilla.contains=" + UPDATED_NOMBRE_PLANTILLA);
    }

    @Test
    @Transactional
    void getAllFacturasByNombrePlantillaNotContainsSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where nombrePlantilla does not contain DEFAULT_NOMBRE_PLANTILLA
        defaultFacturaShouldNotBeFound("nombrePlantilla.doesNotContain=" + DEFAULT_NOMBRE_PLANTILLA);

        // Get all the facturaList where nombrePlantilla does not contain UPDATED_NOMBRE_PLANTILLA
        defaultFacturaShouldBeFound("nombrePlantilla.doesNotContain=" + UPDATED_NOMBRE_PLANTILLA);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo equals to DEFAULT_COSTO
        defaultFacturaShouldBeFound("costo.equals=" + DEFAULT_COSTO);

        // Get all the facturaList where costo equals to UPDATED_COSTO
        defaultFacturaShouldNotBeFound("costo.equals=" + UPDATED_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo not equals to DEFAULT_COSTO
        defaultFacturaShouldNotBeFound("costo.notEquals=" + DEFAULT_COSTO);

        // Get all the facturaList where costo not equals to UPDATED_COSTO
        defaultFacturaShouldBeFound("costo.notEquals=" + UPDATED_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo in DEFAULT_COSTO or UPDATED_COSTO
        defaultFacturaShouldBeFound("costo.in=" + DEFAULT_COSTO + "," + UPDATED_COSTO);

        // Get all the facturaList where costo equals to UPDATED_COSTO
        defaultFacturaShouldNotBeFound("costo.in=" + UPDATED_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo is not null
        defaultFacturaShouldBeFound("costo.specified=true");

        // Get all the facturaList where costo is null
        defaultFacturaShouldNotBeFound("costo.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo is greater than or equal to DEFAULT_COSTO
        defaultFacturaShouldBeFound("costo.greaterThanOrEqual=" + DEFAULT_COSTO);

        // Get all the facturaList where costo is greater than or equal to UPDATED_COSTO
        defaultFacturaShouldNotBeFound("costo.greaterThanOrEqual=" + UPDATED_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo is less than or equal to DEFAULT_COSTO
        defaultFacturaShouldBeFound("costo.lessThanOrEqual=" + DEFAULT_COSTO);

        // Get all the facturaList where costo is less than or equal to SMALLER_COSTO
        defaultFacturaShouldNotBeFound("costo.lessThanOrEqual=" + SMALLER_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo is less than DEFAULT_COSTO
        defaultFacturaShouldNotBeFound("costo.lessThan=" + DEFAULT_COSTO);

        // Get all the facturaList where costo is less than UPDATED_COSTO
        defaultFacturaShouldBeFound("costo.lessThan=" + UPDATED_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByCostoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where costo is greater than DEFAULT_COSTO
        defaultFacturaShouldNotBeFound("costo.greaterThan=" + DEFAULT_COSTO);

        // Get all the facturaList where costo is greater than SMALLER_COSTO
        defaultFacturaShouldBeFound("costo.greaterThan=" + SMALLER_COSTO);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha equals to DEFAULT_FECHA
        defaultFacturaShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha equals to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha not equals to DEFAULT_FECHA
        defaultFacturaShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha not equals to UPDATED_FECHA
        defaultFacturaShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultFacturaShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the facturaList where fecha equals to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is not null
        defaultFacturaShouldBeFound("fecha.specified=true");

        // Get all the facturaList where fecha is null
        defaultFacturaShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is greater than or equal to DEFAULT_FECHA
        defaultFacturaShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha is greater than or equal to UPDATED_FECHA
        defaultFacturaShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is less than or equal to DEFAULT_FECHA
        defaultFacturaShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha is less than or equal to SMALLER_FECHA
        defaultFacturaShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is less than DEFAULT_FECHA
        defaultFacturaShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha is less than UPDATED_FECHA
        defaultFacturaShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fecha is greater than DEFAULT_FECHA
        defaultFacturaShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the facturaList where fecha is greater than SMALLER_FECHA
        defaultFacturaShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturaShouldBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreUsuario").value(hasItem(DEFAULT_NOMBRE_USUARIO)))
            .andExpect(jsonPath("$.[*].nombrePlantilla").value(hasItem(DEFAULT_NOMBRE_PLANTILLA)))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))));

        // Check, that the count call also returns 1
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturaShouldNotBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .nombreUsuario(UPDATED_NOMBRE_USUARIO)
            .nombrePlantilla(UPDATED_NOMBRE_PLANTILLA)
            .costo(UPDATED_COSTO)
            .fecha(UPDATED_FECHA);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNombreUsuario()).isEqualTo(UPDATED_NOMBRE_USUARIO);
        assertThat(testFactura.getNombrePlantilla()).isEqualTo(UPDATED_NOMBRE_PLANTILLA);
        assertThat(testFactura.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testFactura.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura.costo(UPDATED_COSTO).fecha(UPDATED_FECHA);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNombreUsuario()).isEqualTo(DEFAULT_NOMBRE_USUARIO);
        assertThat(testFactura.getNombrePlantilla()).isEqualTo(DEFAULT_NOMBRE_PLANTILLA);
        assertThat(testFactura.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testFactura.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .nombreUsuario(UPDATED_NOMBRE_USUARIO)
            .nombrePlantilla(UPDATED_NOMBRE_PLANTILLA)
            .costo(UPDATED_COSTO)
            .fecha(UPDATED_FECHA);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getNombreUsuario()).isEqualTo(UPDATED_NOMBRE_USUARIO);
        assertThat(testFactura.getNombrePlantilla()).isEqualTo(UPDATED_NOMBRE_PLANTILLA);
        assertThat(testFactura.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testFactura.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
