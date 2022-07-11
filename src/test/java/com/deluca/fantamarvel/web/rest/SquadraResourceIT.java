package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.Squadra;
import com.deluca.fantamarvel.repository.SquadraRepository;
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
 * Integration tests for the {@link SquadraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SquadraResourceIT {

    private static final Integer DEFAULT_GETTONI = 1;
    private static final Integer UPDATED_GETTONI = 2;

    private static final Boolean DEFAULT_IS_IN_LEGA = false;
    private static final Boolean UPDATED_IS_IN_LEGA = true;

    private static final Boolean DEFAULT_IS_SALVATA = false;
    private static final Boolean UPDATED_IS_SALVATA = true;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUNTEGGIO = 1;
    private static final Integer UPDATED_PUNTEGGIO = 2;

    private static final String ENTITY_API_URL = "/api/squadras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SquadraRepository squadraRepository;

    @Mock
    private SquadraRepository squadraRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSquadraMockMvc;

    private Squadra squadra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Squadra createEntity(EntityManager em) {
        Squadra squadra = new Squadra()
            .gettoni(DEFAULT_GETTONI)
            .isInLega(DEFAULT_IS_IN_LEGA)
            .isSalvata(DEFAULT_IS_SALVATA)
            .nome(DEFAULT_NOME)
            .punteggio(DEFAULT_PUNTEGGIO);
        return squadra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Squadra createUpdatedEntity(EntityManager em) {
        Squadra squadra = new Squadra()
            .gettoni(UPDATED_GETTONI)
            .isInLega(UPDATED_IS_IN_LEGA)
            .isSalvata(UPDATED_IS_SALVATA)
            .nome(UPDATED_NOME)
            .punteggio(UPDATED_PUNTEGGIO);
        return squadra;
    }

    @BeforeEach
    public void initTest() {
        squadra = createEntity(em);
    }

    @Test
    @Transactional
    void createSquadra() throws Exception {
        int databaseSizeBeforeCreate = squadraRepository.findAll().size();
        // Create the Squadra
        restSquadraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isCreated());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeCreate + 1);
        Squadra testSquadra = squadraList.get(squadraList.size() - 1);
        assertThat(testSquadra.getGettoni()).isEqualTo(DEFAULT_GETTONI);
        assertThat(testSquadra.getIsInLega()).isEqualTo(DEFAULT_IS_IN_LEGA);
        assertThat(testSquadra.getIsSalvata()).isEqualTo(DEFAULT_IS_SALVATA);
        assertThat(testSquadra.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSquadra.getPunteggio()).isEqualTo(DEFAULT_PUNTEGGIO);
    }

    @Test
    @Transactional
    void createSquadraWithExistingId() throws Exception {
        // Create the Squadra with an existing ID
        squadra.setId(1L);

        int databaseSizeBeforeCreate = squadraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSquadraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isBadRequest());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGettoniIsRequired() throws Exception {
        int databaseSizeBeforeTest = squadraRepository.findAll().size();
        // set the field null
        squadra.setGettoni(null);

        // Create the Squadra, which fails.

        restSquadraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isBadRequest());

        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSalvataIsRequired() throws Exception {
        int databaseSizeBeforeTest = squadraRepository.findAll().size();
        // set the field null
        squadra.setIsSalvata(null);

        // Create the Squadra, which fails.

        restSquadraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isBadRequest());

        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSquadras() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        // Get all the squadraList
        restSquadraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(squadra.getId().intValue())))
            .andExpect(jsonPath("$.[*].gettoni").value(hasItem(DEFAULT_GETTONI)))
            .andExpect(jsonPath("$.[*].isInLega").value(hasItem(DEFAULT_IS_IN_LEGA.booleanValue())))
            .andExpect(jsonPath("$.[*].isSalvata").value(hasItem(DEFAULT_IS_SALVATA.booleanValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].punteggio").value(hasItem(DEFAULT_PUNTEGGIO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSquadrasWithEagerRelationshipsIsEnabled() throws Exception {
        when(squadraRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSquadraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(squadraRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSquadrasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(squadraRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSquadraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(squadraRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSquadra() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        // Get the squadra
        restSquadraMockMvc
            .perform(get(ENTITY_API_URL_ID, squadra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(squadra.getId().intValue()))
            .andExpect(jsonPath("$.gettoni").value(DEFAULT_GETTONI))
            .andExpect(jsonPath("$.isInLega").value(DEFAULT_IS_IN_LEGA.booleanValue()))
            .andExpect(jsonPath("$.isSalvata").value(DEFAULT_IS_SALVATA.booleanValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.punteggio").value(DEFAULT_PUNTEGGIO));
    }

    @Test
    @Transactional
    void getNonExistingSquadra() throws Exception {
        // Get the squadra
        restSquadraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSquadra() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();

        // Update the squadra
        Squadra updatedSquadra = squadraRepository.findById(squadra.getId()).get();
        // Disconnect from session so that the updates on updatedSquadra are not directly saved in db
        em.detach(updatedSquadra);
        updatedSquadra
            .gettoni(UPDATED_GETTONI)
            .isInLega(UPDATED_IS_IN_LEGA)
            .isSalvata(UPDATED_IS_SALVATA)
            .nome(UPDATED_NOME)
            .punteggio(UPDATED_PUNTEGGIO);

        restSquadraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSquadra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSquadra))
            )
            .andExpect(status().isOk());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
        Squadra testSquadra = squadraList.get(squadraList.size() - 1);
        assertThat(testSquadra.getGettoni()).isEqualTo(UPDATED_GETTONI);
        assertThat(testSquadra.getIsInLega()).isEqualTo(UPDATED_IS_IN_LEGA);
        assertThat(testSquadra.getIsSalvata()).isEqualTo(UPDATED_IS_SALVATA);
        assertThat(testSquadra.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSquadra.getPunteggio()).isEqualTo(UPDATED_PUNTEGGIO);
    }

    @Test
    @Transactional
    void putNonExistingSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, squadra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(squadra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(squadra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSquadraWithPatch() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();

        // Update the squadra using partial update
        Squadra partialUpdatedSquadra = new Squadra();
        partialUpdatedSquadra.setId(squadra.getId());

        partialUpdatedSquadra.gettoni(UPDATED_GETTONI).isSalvata(UPDATED_IS_SALVATA).punteggio(UPDATED_PUNTEGGIO);

        restSquadraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSquadra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSquadra))
            )
            .andExpect(status().isOk());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
        Squadra testSquadra = squadraList.get(squadraList.size() - 1);
        assertThat(testSquadra.getGettoni()).isEqualTo(UPDATED_GETTONI);
        assertThat(testSquadra.getIsInLega()).isEqualTo(DEFAULT_IS_IN_LEGA);
        assertThat(testSquadra.getIsSalvata()).isEqualTo(UPDATED_IS_SALVATA);
        assertThat(testSquadra.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSquadra.getPunteggio()).isEqualTo(UPDATED_PUNTEGGIO);
    }

    @Test
    @Transactional
    void fullUpdateSquadraWithPatch() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();

        // Update the squadra using partial update
        Squadra partialUpdatedSquadra = new Squadra();
        partialUpdatedSquadra.setId(squadra.getId());

        partialUpdatedSquadra
            .gettoni(UPDATED_GETTONI)
            .isInLega(UPDATED_IS_IN_LEGA)
            .isSalvata(UPDATED_IS_SALVATA)
            .nome(UPDATED_NOME)
            .punteggio(UPDATED_PUNTEGGIO);

        restSquadraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSquadra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSquadra))
            )
            .andExpect(status().isOk());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
        Squadra testSquadra = squadraList.get(squadraList.size() - 1);
        assertThat(testSquadra.getGettoni()).isEqualTo(UPDATED_GETTONI);
        assertThat(testSquadra.getIsInLega()).isEqualTo(UPDATED_IS_IN_LEGA);
        assertThat(testSquadra.getIsSalvata()).isEqualTo(UPDATED_IS_SALVATA);
        assertThat(testSquadra.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSquadra.getPunteggio()).isEqualTo(UPDATED_PUNTEGGIO);
    }

    @Test
    @Transactional
    void patchNonExistingSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, squadra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(squadra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(squadra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSquadra() throws Exception {
        int databaseSizeBeforeUpdate = squadraRepository.findAll().size();
        squadra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSquadraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(squadra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Squadra in the database
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSquadra() throws Exception {
        // Initialize the database
        squadraRepository.saveAndFlush(squadra);

        int databaseSizeBeforeDelete = squadraRepository.findAll().size();

        // Delete the squadra
        restSquadraMockMvc
            .perform(delete(ENTITY_API_URL_ID, squadra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Squadra> squadraList = squadraRepository.findAll();
        assertThat(squadraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
