package org.datasurvey.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.datasurvey.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.datasurvey.IntegrationTest;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.domain.Plantilla;
import org.datasurvey.domain.User;
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.domain.enumeration.EstadoUsuario;
import org.datasurvey.repository.UsuarioExtraRepository;
import org.datasurvey.service.UsuarioExtraService;
import org.datasurvey.service.criteria.UsuarioExtraCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioExtraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioExtraResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ICONO_PERFIL = "AAAAAAAAAA";
    private static final String UPDATED_ICONO_PERFIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_NACIMIENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_NACIMIENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_NACIMIENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final EstadoUsuario DEFAULT_ESTADO = EstadoUsuario.ACTIVE;
    private static final EstadoUsuario UPDATED_ESTADO = EstadoUsuario.SUSPENDED;

    private static final String ENTITY_API_URL = "/api/usuario-extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioExtraRepository usuarioExtraRepository;

    @Mock
    private UsuarioExtraRepository usuarioExtraRepositoryMock;

    @Mock
    private UsuarioExtraService usuarioExtraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioExtraMockMvc;

    private UsuarioExtra usuarioExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioExtra createEntity(EntityManager em) {
        UsuarioExtra usuarioExtra = new UsuarioExtra()
            .nombre(DEFAULT_NOMBRE)
            .iconoPerfil(DEFAULT_ICONO_PERFIL)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .estado(DEFAULT_ESTADO);
        return usuarioExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioExtra createUpdatedEntity(EntityManager em) {
        UsuarioExtra usuarioExtra = new UsuarioExtra()
            .nombre(UPDATED_NOMBRE)
            .iconoPerfil(UPDATED_ICONO_PERFIL)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estado(UPDATED_ESTADO);
        return usuarioExtra;
    }

    @BeforeEach
    public void initTest() {
        usuarioExtra = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuarioExtra() throws Exception {
        int databaseSizeBeforeCreate = usuarioExtraRepository.findAll().size();
        // Create the UsuarioExtra
        restUsuarioExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioExtra)))
            .andExpect(status().isCreated());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioExtra testUsuarioExtra = usuarioExtraList.get(usuarioExtraList.size() - 1);
        assertThat(testUsuarioExtra.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUsuarioExtra.getIconoPerfil()).isEqualTo(DEFAULT_ICONO_PERFIL);
        assertThat(testUsuarioExtra.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testUsuarioExtra.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createUsuarioExtraWithExistingId() throws Exception {
        // Create the UsuarioExtra with an existing ID
        usuarioExtra.setId(1L);

        int databaseSizeBeforeCreate = usuarioExtraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioExtra)))
            .andExpect(status().isBadRequest());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioExtraRepository.findAll().size();
        // set the field null
        usuarioExtra.setNombre(null);

        // Create the UsuarioExtra, which fails.

        restUsuarioExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioExtra)))
            .andExpect(status().isBadRequest());

        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioExtraRepository.findAll().size();
        // set the field null
        usuarioExtra.setEstado(null);

        // Create the UsuarioExtra, which fails.

        restUsuarioExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioExtra)))
            .andExpect(status().isBadRequest());

        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarioExtras() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].iconoPerfil").value(hasItem(DEFAULT_ICONO_PERFIL)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(sameInstant(DEFAULT_FECHA_NACIMIENTO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioExtrasWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioExtraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioExtraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioExtraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuarioExtrasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioExtraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioExtraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioExtraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUsuarioExtra() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get the usuarioExtra
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioExtra.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.iconoPerfil").value(DEFAULT_ICONO_PERFIL))
            .andExpect(jsonPath("$.fechaNacimiento").value(sameInstant(DEFAULT_FECHA_NACIMIENTO)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getUsuarioExtrasByIdFiltering() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        Long id = usuarioExtra.getId();

        defaultUsuarioExtraShouldBeFound("id.equals=" + id);
        defaultUsuarioExtraShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioExtraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioExtraShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioExtraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioExtraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre equals to DEFAULT_NOMBRE
        defaultUsuarioExtraShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the usuarioExtraList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioExtraShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre not equals to DEFAULT_NOMBRE
        defaultUsuarioExtraShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the usuarioExtraList where nombre not equals to UPDATED_NOMBRE
        defaultUsuarioExtraShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultUsuarioExtraShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the usuarioExtraList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioExtraShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre is not null
        defaultUsuarioExtraShouldBeFound("nombre.specified=true");

        // Get all the usuarioExtraList where nombre is null
        defaultUsuarioExtraShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre contains DEFAULT_NOMBRE
        defaultUsuarioExtraShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the usuarioExtraList where nombre contains UPDATED_NOMBRE
        defaultUsuarioExtraShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where nombre does not contain DEFAULT_NOMBRE
        defaultUsuarioExtraShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the usuarioExtraList where nombre does not contain UPDATED_NOMBRE
        defaultUsuarioExtraShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil equals to DEFAULT_ICONO_PERFIL
        defaultUsuarioExtraShouldBeFound("iconoPerfil.equals=" + DEFAULT_ICONO_PERFIL);

        // Get all the usuarioExtraList where iconoPerfil equals to UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.equals=" + UPDATED_ICONO_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil not equals to DEFAULT_ICONO_PERFIL
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.notEquals=" + DEFAULT_ICONO_PERFIL);

        // Get all the usuarioExtraList where iconoPerfil not equals to UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldBeFound("iconoPerfil.notEquals=" + UPDATED_ICONO_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil in DEFAULT_ICONO_PERFIL or UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldBeFound("iconoPerfil.in=" + DEFAULT_ICONO_PERFIL + "," + UPDATED_ICONO_PERFIL);

        // Get all the usuarioExtraList where iconoPerfil equals to UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.in=" + UPDATED_ICONO_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil is not null
        defaultUsuarioExtraShouldBeFound("iconoPerfil.specified=true");

        // Get all the usuarioExtraList where iconoPerfil is null
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilContainsSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil contains DEFAULT_ICONO_PERFIL
        defaultUsuarioExtraShouldBeFound("iconoPerfil.contains=" + DEFAULT_ICONO_PERFIL);

        // Get all the usuarioExtraList where iconoPerfil contains UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.contains=" + UPDATED_ICONO_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByIconoPerfilNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where iconoPerfil does not contain DEFAULT_ICONO_PERFIL
        defaultUsuarioExtraShouldNotBeFound("iconoPerfil.doesNotContain=" + DEFAULT_ICONO_PERFIL);

        // Get all the usuarioExtraList where iconoPerfil does not contain UPDATED_ICONO_PERFIL
        defaultUsuarioExtraShouldBeFound("iconoPerfil.doesNotContain=" + UPDATED_ICONO_PERFIL);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento not equals to DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.notEquals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento not equals to UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.notEquals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento is not null
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.specified=true");

        // Get all the usuarioExtraList where fechaNacimiento is null
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the usuarioExtraList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultUsuarioExtraShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where estado equals to DEFAULT_ESTADO
        defaultUsuarioExtraShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the usuarioExtraList where estado equals to UPDATED_ESTADO
        defaultUsuarioExtraShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where estado not equals to DEFAULT_ESTADO
        defaultUsuarioExtraShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the usuarioExtraList where estado not equals to UPDATED_ESTADO
        defaultUsuarioExtraShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultUsuarioExtraShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the usuarioExtraList where estado equals to UPDATED_ESTADO
        defaultUsuarioExtraShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        // Get all the usuarioExtraList where estado is not null
        defaultUsuarioExtraShouldBeFound("estado.specified=true");

        // Get all the usuarioExtraList where estado is null
        defaultUsuarioExtraShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        usuarioExtra.setUser(user);
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Long userId = user.getId();

        // Get all the usuarioExtraList where user equals to userId
        defaultUsuarioExtraShouldBeFound("userId.equals=" + userId);

        // Get all the usuarioExtraList where user equals to (userId + 1)
        defaultUsuarioExtraShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Encuesta encuesta = EncuestaResourceIT.createEntity(em);
        em.persist(encuesta);
        em.flush();
        usuarioExtra.addEncuesta(encuesta);
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Long encuestaId = encuesta.getId();

        // Get all the usuarioExtraList where encuesta equals to encuestaId
        defaultUsuarioExtraShouldBeFound("encuestaId.equals=" + encuestaId);

        // Get all the usuarioExtraList where encuesta equals to (encuestaId + 1)
        defaultUsuarioExtraShouldNotBeFound("encuestaId.equals=" + (encuestaId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByUsuarioEncuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        UsuarioEncuesta usuarioEncuesta = UsuarioEncuestaResourceIT.createEntity(em);
        em.persist(usuarioEncuesta);
        em.flush();
        usuarioExtra.addUsuarioEncuesta(usuarioEncuesta);
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Long usuarioEncuestaId = usuarioEncuesta.getId();

        // Get all the usuarioExtraList where usuarioEncuesta equals to usuarioEncuestaId
        defaultUsuarioExtraShouldBeFound("usuarioEncuestaId.equals=" + usuarioEncuestaId);

        // Get all the usuarioExtraList where usuarioEncuesta equals to (usuarioEncuestaId + 1)
        defaultUsuarioExtraShouldNotBeFound("usuarioEncuestaId.equals=" + (usuarioEncuestaId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioExtrasByPlantillaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Plantilla plantilla = PlantillaResourceIT.createEntity(em);
        em.persist(plantilla);
        em.flush();
        usuarioExtra.addPlantilla(plantilla);
        usuarioExtraRepository.saveAndFlush(usuarioExtra);
        Long plantillaId = plantilla.getId();

        // Get all the usuarioExtraList where plantilla equals to plantillaId
        defaultUsuarioExtraShouldBeFound("plantillaId.equals=" + plantillaId);

        // Get all the usuarioExtraList where plantilla equals to (plantillaId + 1)
        defaultUsuarioExtraShouldNotBeFound("plantillaId.equals=" + (plantillaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioExtraShouldBeFound(String filter) throws Exception {
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].iconoPerfil").value(hasItem(DEFAULT_ICONO_PERFIL)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(sameInstant(DEFAULT_FECHA_NACIMIENTO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioExtraShouldNotBeFound(String filter) throws Exception {
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuarioExtra() throws Exception {
        // Get the usuarioExtra
        restUsuarioExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuarioExtra() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();

        // Update the usuarioExtra
        UsuarioExtra updatedUsuarioExtra = usuarioExtraRepository.findById(usuarioExtra.getId()).get();
        // Disconnect from session so that the updates on updatedUsuarioExtra are not directly saved in db
        em.detach(updatedUsuarioExtra);
        updatedUsuarioExtra
            .nombre(UPDATED_NOMBRE)
            .iconoPerfil(UPDATED_ICONO_PERFIL)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estado(UPDATED_ESTADO);

        restUsuarioExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuarioExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuarioExtra))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
        UsuarioExtra testUsuarioExtra = usuarioExtraList.get(usuarioExtraList.size() - 1);
        assertThat(testUsuarioExtra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuarioExtra.getIconoPerfil()).isEqualTo(UPDATED_ICONO_PERFIL);
        assertThat(testUsuarioExtra.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testUsuarioExtra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioExtra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioExtraWithPatch() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();

        // Update the usuarioExtra using partial update
        UsuarioExtra partialUpdatedUsuarioExtra = new UsuarioExtra();
        partialUpdatedUsuarioExtra.setId(usuarioExtra.getId());

        partialUpdatedUsuarioExtra.nombre(UPDATED_NOMBRE).fechaNacimiento(UPDATED_FECHA_NACIMIENTO).estado(UPDATED_ESTADO);

        restUsuarioExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioExtra))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
        UsuarioExtra testUsuarioExtra = usuarioExtraList.get(usuarioExtraList.size() - 1);
        assertThat(testUsuarioExtra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuarioExtra.getIconoPerfil()).isEqualTo(DEFAULT_ICONO_PERFIL);
        assertThat(testUsuarioExtra.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testUsuarioExtra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioExtraWithPatch() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();

        // Update the usuarioExtra using partial update
        UsuarioExtra partialUpdatedUsuarioExtra = new UsuarioExtra();
        partialUpdatedUsuarioExtra.setId(usuarioExtra.getId());

        partialUpdatedUsuarioExtra
            .nombre(UPDATED_NOMBRE)
            .iconoPerfil(UPDATED_ICONO_PERFIL)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estado(UPDATED_ESTADO);

        restUsuarioExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioExtra))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
        UsuarioExtra testUsuarioExtra = usuarioExtraList.get(usuarioExtraList.size() - 1);
        assertThat(testUsuarioExtra.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuarioExtra.getIconoPerfil()).isEqualTo(UPDATED_ICONO_PERFIL);
        assertThat(testUsuarioExtra.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testUsuarioExtra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioExtra))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarioExtra() throws Exception {
        int databaseSizeBeforeUpdate = usuarioExtraRepository.findAll().size();
        usuarioExtra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioExtraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioExtra))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioExtra in the database
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarioExtra() throws Exception {
        // Initialize the database
        usuarioExtraRepository.saveAndFlush(usuarioExtra);

        int databaseSizeBeforeDelete = usuarioExtraRepository.findAll().size();

        // Delete the usuarioExtra
        restUsuarioExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioExtra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UsuarioExtra> usuarioExtraList = usuarioExtraRepository.findAll();
        assertThat(usuarioExtraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
