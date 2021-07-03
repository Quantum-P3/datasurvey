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
import org.datasurvey.domain.Categoria;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.domain.enumeration.EstadoCategoria;
import org.datasurvey.repository.CategoriaRepository;
import org.datasurvey.service.criteria.CategoriaCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CategoriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final EstadoCategoria DEFAULT_ESTADO = EstadoCategoria.ACTIVE;
    private static final EstadoCategoria UPDATED_ESTADO = EstadoCategoria.INACTIVE;

    private static final String ENTITY_API_URL = "/api/categorias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaMockMvc;

    private Categoria categoria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createEntity(EntityManager em) {
        Categoria categoria = new Categoria().nombre(DEFAULT_NOMBRE).estado(DEFAULT_ESTADO);
        return categoria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categoria createUpdatedEntity(EntityManager em) {
        Categoria categoria = new Categoria().nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);
        return categoria;
    }

    @BeforeEach
    public void initTest() {
        categoria = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoria() throws Exception {
        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();
        // Create the Categoria
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isCreated());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate + 1);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCategoria.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createCategoriaWithExistingId() throws Exception {
        // Create the Categoria with an existing ID
        categoria.setId(1L);

        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaRepository.findAll().size();
        // set the field null
        categoria.setNombre(null);

        // Create the Categoria, which fails.

        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaRepository.findAll().size();
        // set the field null
        categoria.setEstado(null);

        // Create the Categoria, which fails.

        restCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorias() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get the categoria
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, categoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoria.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getCategoriasByIdFiltering() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        Long id = categoria.getId();

        defaultCategoriaShouldBeFound("id.equals=" + id);
        defaultCategoriaShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre equals to DEFAULT_NOMBRE
        defaultCategoriaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the categoriaList where nombre equals to UPDATED_NOMBRE
        defaultCategoriaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre not equals to DEFAULT_NOMBRE
        defaultCategoriaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the categoriaList where nombre not equals to UPDATED_NOMBRE
        defaultCategoriaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultCategoriaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the categoriaList where nombre equals to UPDATED_NOMBRE
        defaultCategoriaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre is not null
        defaultCategoriaShouldBeFound("nombre.specified=true");

        // Get all the categoriaList where nombre is null
        defaultCategoriaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreContainsSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre contains DEFAULT_NOMBRE
        defaultCategoriaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the categoriaList where nombre contains UPDATED_NOMBRE
        defaultCategoriaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCategoriasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where nombre does not contain DEFAULT_NOMBRE
        defaultCategoriaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the categoriaList where nombre does not contain UPDATED_NOMBRE
        defaultCategoriaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCategoriasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where estado equals to DEFAULT_ESTADO
        defaultCategoriaShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the categoriaList where estado equals to UPDATED_ESTADO
        defaultCategoriaShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCategoriasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where estado not equals to DEFAULT_ESTADO
        defaultCategoriaShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the categoriaList where estado not equals to UPDATED_ESTADO
        defaultCategoriaShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCategoriasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultCategoriaShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the categoriaList where estado equals to UPDATED_ESTADO
        defaultCategoriaShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllCategoriasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList where estado is not null
        defaultCategoriaShouldBeFound("estado.specified=true");

        // Get all the categoriaList where estado is null
        defaultCategoriaShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriasByEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);
        Encuesta encuesta = EncuestaResourceIT.createEntity(em);
        em.persist(encuesta);
        em.flush();
        categoria.addEncuesta(encuesta);
        categoriaRepository.saveAndFlush(categoria);
        Long encuestaId = encuesta.getId();

        // Get all the categoriaList where encuesta equals to encuestaId
        defaultCategoriaShouldBeFound("encuestaId.equals=" + encuestaId);

        // Get all the categoriaList where encuesta equals to (encuestaId + 1)
        defaultCategoriaShouldNotBeFound("encuestaId.equals=" + (encuestaId + 1));
    }

    @Test
    @Transactional
    void getAllCategoriasByPlantillaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);
        Plantilla plantilla = PlantillaResourceIT.createEntity(em);
        em.persist(plantilla);
        em.flush();
        categoria.addPlantilla(plantilla);
        categoriaRepository.saveAndFlush(categoria);
        Long plantillaId = plantilla.getId();

        // Get all the categoriaList where plantilla equals to plantillaId
        defaultCategoriaShouldBeFound("plantillaId.equals=" + plantillaId);

        // Get all the categoriaList where plantilla equals to (plantillaId + 1)
        defaultCategoriaShouldNotBeFound("plantillaId.equals=" + (plantillaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaShouldBeFound(String filter) throws Exception {
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaShouldNotBeFound(String filter) throws Exception {
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoria() throws Exception {
        // Get the categoria
        restCategoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria
        Categoria updatedCategoria = categoriaRepository.findById(categoria.getId()).get();
        // Disconnect from session so that the updates on updatedCategoria are not directly saved in db
        em.detach(updatedCategoria);
        updatedCategoria.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);

        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCategoria.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setId(categoria.getId());

        partialUpdatedCategoria.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCategoria.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaWithPatch() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria using partial update
        Categoria partialUpdatedCategoria = new Categoria();
        partialUpdatedCategoria.setId(categoria.getId());

        partialUpdatedCategoria.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);

        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoria))
            )
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCategoria.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();
        categoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        int databaseSizeBeforeDelete = categoriaRepository.findAll().size();

        // Delete the categoria
        restCategoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
