package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.FilmPersonaggio;
import com.deluca.fantamarvel.repository.FilmPersonaggioRepository;
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

/**
 * Integration tests for the {@link FilmPersonaggioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FilmPersonaggioResourceIT {

    private static final Integer DEFAULT_COSTO = 1;
    private static final Integer UPDATED_COSTO = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/film-personaggios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilmPersonaggioRepository filmPersonaggioRepository;

    @Mock
    private FilmPersonaggioRepository filmPersonaggioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilmPersonaggioMockMvc;

    private FilmPersonaggio filmPersonaggio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilmPersonaggio createEntity(EntityManager em) {
        FilmPersonaggio filmPersonaggio = new FilmPersonaggio().costo(DEFAULT_COSTO).isActive(DEFAULT_IS_ACTIVE);
        return filmPersonaggio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilmPersonaggio createUpdatedEntity(EntityManager em) {
        FilmPersonaggio filmPersonaggio = new FilmPersonaggio().costo(UPDATED_COSTO).isActive(UPDATED_IS_ACTIVE);
        return filmPersonaggio;
    }

    @BeforeEach
    public void initTest() {
        filmPersonaggio = createEntity(em);
    }

    @Test
    @Transactional
    void createFilmPersonaggio() throws Exception {
        int databaseSizeBeforeCreate = filmPersonaggioRepository.findAll().size();
        // Create the FilmPersonaggio
        restFilmPersonaggioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isCreated());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeCreate + 1);
        FilmPersonaggio testFilmPersonaggio = filmPersonaggioList.get(filmPersonaggioList.size() - 1);
        assertThat(testFilmPersonaggio.getCosto()).isEqualTo(DEFAULT_COSTO);
        assertThat(testFilmPersonaggio.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createFilmPersonaggioWithExistingId() throws Exception {
        // Create the FilmPersonaggio with an existing ID
        filmPersonaggio.setId(1L);

        int databaseSizeBeforeCreate = filmPersonaggioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmPersonaggioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCostoIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmPersonaggioRepository.findAll().size();
        // set the field null
        filmPersonaggio.setCosto(null);

        // Create the FilmPersonaggio, which fails.

        restFilmPersonaggioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFilmPersonaggios() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        // Get all the filmPersonaggioList
        restFilmPersonaggioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filmPersonaggio.getId().intValue())))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmPersonaggiosWithEagerRelationshipsIsEnabled() throws Exception {
        when(filmPersonaggioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmPersonaggioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filmPersonaggioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmPersonaggiosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filmPersonaggioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmPersonaggioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filmPersonaggioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFilmPersonaggio() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        // Get the filmPersonaggio
        restFilmPersonaggioMockMvc
            .perform(get(ENTITY_API_URL_ID, filmPersonaggio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filmPersonaggio.getId().intValue()))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFilmPersonaggio() throws Exception {
        // Get the filmPersonaggio
        restFilmPersonaggioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilmPersonaggio() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();

        // Update the filmPersonaggio
        FilmPersonaggio updatedFilmPersonaggio = filmPersonaggioRepository.findById(filmPersonaggio.getId()).get();
        // Disconnect from session so that the updates on updatedFilmPersonaggio are not directly saved in db
        em.detach(updatedFilmPersonaggio);
        updatedFilmPersonaggio.costo(UPDATED_COSTO).isActive(UPDATED_IS_ACTIVE);

        restFilmPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilmPersonaggio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFilmPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
        FilmPersonaggio testFilmPersonaggio = filmPersonaggioList.get(filmPersonaggioList.size() - 1);
        assertThat(testFilmPersonaggio.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testFilmPersonaggio.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filmPersonaggio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilmPersonaggioWithPatch() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();

        // Update the filmPersonaggio using partial update
        FilmPersonaggio partialUpdatedFilmPersonaggio = new FilmPersonaggio();
        partialUpdatedFilmPersonaggio.setId(filmPersonaggio.getId());

        restFilmPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmPersonaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilmPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
        FilmPersonaggio testFilmPersonaggio = filmPersonaggioList.get(filmPersonaggioList.size() - 1);
        assertThat(testFilmPersonaggio.getCosto()).isEqualTo(DEFAULT_COSTO);
        assertThat(testFilmPersonaggio.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateFilmPersonaggioWithPatch() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();

        // Update the filmPersonaggio using partial update
        FilmPersonaggio partialUpdatedFilmPersonaggio = new FilmPersonaggio();
        partialUpdatedFilmPersonaggio.setId(filmPersonaggio.getId());

        partialUpdatedFilmPersonaggio.costo(UPDATED_COSTO).isActive(UPDATED_IS_ACTIVE);

        restFilmPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmPersonaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilmPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
        FilmPersonaggio testFilmPersonaggio = filmPersonaggioList.get(filmPersonaggioList.size() - 1);
        assertThat(testFilmPersonaggio.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testFilmPersonaggio.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filmPersonaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilmPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = filmPersonaggioRepository.findAll().size();
        filmPersonaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmPersonaggio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilmPersonaggio in the database
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilmPersonaggio() throws Exception {
        // Initialize the database
        filmPersonaggioRepository.saveAndFlush(filmPersonaggio);

        int databaseSizeBeforeDelete = filmPersonaggioRepository.findAll().size();

        // Delete the filmPersonaggio
        restFilmPersonaggioMockMvc
            .perform(delete(ENTITY_API_URL_ID, filmPersonaggio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FilmPersonaggio> filmPersonaggioList = filmPersonaggioRepository.findAll();
        assertThat(filmPersonaggioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
