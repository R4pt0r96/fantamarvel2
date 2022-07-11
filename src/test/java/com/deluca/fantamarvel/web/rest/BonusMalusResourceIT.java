package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.BonusMalus;
import com.deluca.fantamarvel.repository.BonusMalusRepository;
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
 * Integration tests for the {@link BonusMalusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BonusMalusResourceIT {

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUNTI = 1;
    private static final Integer UPDATED_PUNTI = 2;

    private static final String ENTITY_API_URL = "/api/bonus-maluses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BonusMalusRepository bonusMalusRepository;

    @Mock
    private BonusMalusRepository bonusMalusRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBonusMalusMockMvc;

    private BonusMalus bonusMalus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonusMalus createEntity(EntityManager em) {
        BonusMalus bonusMalus = new BonusMalus().descrizione(DEFAULT_DESCRIZIONE).punti(DEFAULT_PUNTI);
        return bonusMalus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BonusMalus createUpdatedEntity(EntityManager em) {
        BonusMalus bonusMalus = new BonusMalus().descrizione(UPDATED_DESCRIZIONE).punti(UPDATED_PUNTI);
        return bonusMalus;
    }

    @BeforeEach
    public void initTest() {
        bonusMalus = createEntity(em);
    }

    @Test
    @Transactional
    void createBonusMalus() throws Exception {
        int databaseSizeBeforeCreate = bonusMalusRepository.findAll().size();
        // Create the BonusMalus
        restBonusMalusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
            .andExpect(status().isCreated());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeCreate + 1);
        BonusMalus testBonusMalus = bonusMalusList.get(bonusMalusList.size() - 1);
        assertThat(testBonusMalus.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testBonusMalus.getPunti()).isEqualTo(DEFAULT_PUNTI);
    }

    @Test
    @Transactional
    void createBonusMalusWithExistingId() throws Exception {
        // Create the BonusMalus with an existing ID
        bonusMalus.setId(1L);

        int databaseSizeBeforeCreate = bonusMalusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonusMalusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
            .andExpect(status().isBadRequest());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescrizioneIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonusMalusRepository.findAll().size();
        // set the field null
        bonusMalus.setDescrizione(null);

        // Create the BonusMalus, which fails.

        restBonusMalusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
            .andExpect(status().isBadRequest());

        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBonusMaluses() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        // Get all the bonusMalusList
        restBonusMalusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonusMalus.getId().intValue())))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)))
            .andExpect(jsonPath("$.[*].punti").value(hasItem(DEFAULT_PUNTI)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBonusMalusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bonusMalusRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBonusMalusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bonusMalusRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBonusMalusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bonusMalusRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBonusMalusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bonusMalusRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        // Get the bonusMalus
        restBonusMalusMockMvc
            .perform(get(ENTITY_API_URL_ID, bonusMalus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bonusMalus.getId().intValue()))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE))
            .andExpect(jsonPath("$.punti").value(DEFAULT_PUNTI));
    }

    @Test
    @Transactional
    void getNonExistingBonusMalus() throws Exception {
        // Get the bonusMalus
        restBonusMalusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();

        // Update the bonusMalus
        BonusMalus updatedBonusMalus = bonusMalusRepository.findById(bonusMalus.getId()).get();
        // Disconnect from session so that the updates on updatedBonusMalus are not directly saved in db
        em.detach(updatedBonusMalus);
        updatedBonusMalus.descrizione(UPDATED_DESCRIZIONE).punti(UPDATED_PUNTI);

        restBonusMalusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBonusMalus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBonusMalus))
            )
            .andExpect(status().isOk());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
        BonusMalus testBonusMalus = bonusMalusList.get(bonusMalusList.size() - 1);
        assertThat(testBonusMalus.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testBonusMalus.getPunti()).isEqualTo(UPDATED_PUNTI);
    }

    @Test
    @Transactional
    void putNonExistingBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bonusMalus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonusMalus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bonusMalus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bonusMalus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBonusMalusWithPatch() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();

        // Update the bonusMalus using partial update
        BonusMalus partialUpdatedBonusMalus = new BonusMalus();
        partialUpdatedBonusMalus.setId(bonusMalus.getId());

        partialUpdatedBonusMalus.punti(UPDATED_PUNTI);

        restBonusMalusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonusMalus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonusMalus))
            )
            .andExpect(status().isOk());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
        BonusMalus testBonusMalus = bonusMalusList.get(bonusMalusList.size() - 1);
        assertThat(testBonusMalus.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testBonusMalus.getPunti()).isEqualTo(UPDATED_PUNTI);
    }

    @Test
    @Transactional
    void fullUpdateBonusMalusWithPatch() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();

        // Update the bonusMalus using partial update
        BonusMalus partialUpdatedBonusMalus = new BonusMalus();
        partialUpdatedBonusMalus.setId(bonusMalus.getId());

        partialUpdatedBonusMalus.descrizione(UPDATED_DESCRIZIONE).punti(UPDATED_PUNTI);

        restBonusMalusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBonusMalus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBonusMalus))
            )
            .andExpect(status().isOk());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
        BonusMalus testBonusMalus = bonusMalusList.get(bonusMalusList.size() - 1);
        assertThat(testBonusMalus.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testBonusMalus.getPunti()).isEqualTo(UPDATED_PUNTI);
    }

    @Test
    @Transactional
    void patchNonExistingBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bonusMalus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonusMalus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bonusMalus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBonusMalus() throws Exception {
        int databaseSizeBeforeUpdate = bonusMalusRepository.findAll().size();
        bonusMalus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBonusMalusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bonusMalus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BonusMalus in the database
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBonusMalus() throws Exception {
        // Initialize the database
        bonusMalusRepository.saveAndFlush(bonusMalus);

        int databaseSizeBeforeDelete = bonusMalusRepository.findAll().size();

        // Delete the bonusMalus
        restBonusMalusMockMvc
            .perform(delete(ENTITY_API_URL_ID, bonusMalus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BonusMalus> bonusMalusList = bonusMalusRepository.findAll();
        assertThat(bonusMalusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
