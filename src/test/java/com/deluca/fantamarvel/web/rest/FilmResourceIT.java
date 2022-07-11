package com.deluca.fantamarvel.web.rest;

import static com.deluca.fantamarvel.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.Film;
import com.deluca.fantamarvel.repository.FilmRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FilmResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FilmResourceIT {

    private static final String DEFAULT_TITOLO = "AAAAAAAAAA";
    private static final String UPDATED_TITOLO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_USCITA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_USCITA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATA_FINE_ISCRIZIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_FINE_ISCRIZIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_URL_IMG = "AAAAAAAAAA";
    private static final String UPDATED_URL_IMG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/films";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilmRepository filmRepository;

    @Mock
    private FilmRepository filmRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilmMockMvc;

    private Film film;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createEntity(EntityManager em) {
        Film film = new Film()
            .titolo(DEFAULT_TITOLO)
            .dataUscita(DEFAULT_DATA_USCITA)
            .dataFineIscrizione(DEFAULT_DATA_FINE_ISCRIZIONE)
            .isActive(DEFAULT_IS_ACTIVE)
            .urlImg(DEFAULT_URL_IMG)
            .descrizione(DEFAULT_DESCRIZIONE);
        return film;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createUpdatedEntity(EntityManager em) {
        Film film = new Film()
            .titolo(UPDATED_TITOLO)
            .dataUscita(UPDATED_DATA_USCITA)
            .dataFineIscrizione(UPDATED_DATA_FINE_ISCRIZIONE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG)
            .descrizione(UPDATED_DESCRIZIONE);
        return film;
    }

    @BeforeEach
    public void initTest() {
        film = createEntity(em);
    }

    @Test
    @Transactional
    void createFilm() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();
        // Create the Film
        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isCreated());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate + 1);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitolo()).isEqualTo(DEFAULT_TITOLO);
        assertThat(testFilm.getDataUscita()).isEqualTo(DEFAULT_DATA_USCITA);
        assertThat(testFilm.getDataFineIscrizione()).isEqualTo(DEFAULT_DATA_FINE_ISCRIZIONE);
        assertThat(testFilm.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testFilm.getUrlImg()).isEqualTo(DEFAULT_URL_IMG);
        assertThat(testFilm.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
    }

    @Test
    @Transactional
    void createFilmWithExistingId() throws Exception {
        // Create the Film with an existing ID
        film.setId(1L);

        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitoloIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setTitolo(null);

        // Create the Film, which fails.

        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isBadRequest());

        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFilms() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList
        restFilmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
            .andExpect(jsonPath("$.[*].titolo").value(hasItem(DEFAULT_TITOLO)))
            .andExpect(jsonPath("$.[*].dataUscita").value(hasItem(sameInstant(DEFAULT_DATA_USCITA))))
            .andExpect(jsonPath("$.[*].dataFineIscrizione").value(hasItem(sameInstant(DEFAULT_DATA_FINE_ISCRIZIONE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].urlImg").value(hasItem(DEFAULT_URL_IMG)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmsWithEagerRelationshipsIsEnabled() throws Exception {
        when(filmRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filmRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filmRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filmRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get the film
        restFilmMockMvc
            .perform(get(ENTITY_API_URL_ID, film.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(film.getId().intValue()))
            .andExpect(jsonPath("$.titolo").value(DEFAULT_TITOLO))
            .andExpect(jsonPath("$.dataUscita").value(sameInstant(DEFAULT_DATA_USCITA)))
            .andExpect(jsonPath("$.dataFineIscrizione").value(sameInstant(DEFAULT_DATA_FINE_ISCRIZIONE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.urlImg").value(DEFAULT_URL_IMG))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFilm() throws Exception {
        // Get the film
        restFilmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film
        Film updatedFilm = filmRepository.findById(film.getId()).get();
        // Disconnect from session so that the updates on updatedFilm are not directly saved in db
        em.detach(updatedFilm);
        updatedFilm
            .titolo(UPDATED_TITOLO)
            .dataUscita(UPDATED_DATA_USCITA)
            .dataFineIscrizione(UPDATED_DATA_FINE_ISCRIZIONE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG)
            .descrizione(UPDATED_DESCRIZIONE);

        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFilm))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitolo()).isEqualTo(UPDATED_TITOLO);
        assertThat(testFilm.getDataUscita()).isEqualTo(UPDATED_DATA_USCITA);
        assertThat(testFilm.getDataFineIscrizione()).isEqualTo(UPDATED_DATA_FINE_ISCRIZIONE);
        assertThat(testFilm.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testFilm.getUrlImg()).isEqualTo(UPDATED_URL_IMG);
        assertThat(testFilm.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
    }

    @Test
    @Transactional
    void putNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, film.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(film))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(film))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilmWithPatch() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film using partial update
        Film partialUpdatedFilm = new Film();
        partialUpdatedFilm.setId(film.getId());

        partialUpdatedFilm.dataFineIscrizione(UPDATED_DATA_FINE_ISCRIZIONE).urlImg(UPDATED_URL_IMG).descrizione(UPDATED_DESCRIZIONE);

        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilm))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitolo()).isEqualTo(DEFAULT_TITOLO);
        assertThat(testFilm.getDataUscita()).isEqualTo(DEFAULT_DATA_USCITA);
        assertThat(testFilm.getDataFineIscrizione()).isEqualTo(UPDATED_DATA_FINE_ISCRIZIONE);
        assertThat(testFilm.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testFilm.getUrlImg()).isEqualTo(UPDATED_URL_IMG);
        assertThat(testFilm.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
    }

    @Test
    @Transactional
    void fullUpdateFilmWithPatch() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film using partial update
        Film partialUpdatedFilm = new Film();
        partialUpdatedFilm.setId(film.getId());

        partialUpdatedFilm
            .titolo(UPDATED_TITOLO)
            .dataUscita(UPDATED_DATA_USCITA)
            .dataFineIscrizione(UPDATED_DATA_FINE_ISCRIZIONE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG)
            .descrizione(UPDATED_DESCRIZIONE);

        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilm))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getTitolo()).isEqualTo(UPDATED_TITOLO);
        assertThat(testFilm.getDataUscita()).isEqualTo(UPDATED_DATA_USCITA);
        assertThat(testFilm.getDataFineIscrizione()).isEqualTo(UPDATED_DATA_FINE_ISCRIZIONE);
        assertThat(testFilm.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testFilm.getUrlImg()).isEqualTo(UPDATED_URL_IMG);
        assertThat(testFilm.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
    }

    @Test
    @Transactional
    void patchNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, film.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(film))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(film))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeDelete = filmRepository.findAll().size();

        // Delete the film
        restFilmMockMvc
            .perform(delete(ENTITY_API_URL_ID, film.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
