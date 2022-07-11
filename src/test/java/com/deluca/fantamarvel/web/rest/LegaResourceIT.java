package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.Lega;
import com.deluca.fantamarvel.repository.LegaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link LegaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LegaResourceIT {

    private static final String DEFAULT_CODICE = "AAAAAAAAAA";
    private static final String UPDATED_CODICE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/legas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LegaRepository legaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLegaMockMvc;

    private Lega lega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lega createEntity(EntityManager em) {
        Lega lega = new Lega().codice(DEFAULT_CODICE).descrizione(DEFAULT_DESCRIZIONE).isPrivate(DEFAULT_IS_PRIVATE).nome(DEFAULT_NOME);
        return lega;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lega createUpdatedEntity(EntityManager em) {
        Lega lega = new Lega().codice(UPDATED_CODICE).descrizione(UPDATED_DESCRIZIONE).isPrivate(UPDATED_IS_PRIVATE).nome(UPDATED_NOME);
        return lega;
    }

    @BeforeEach
    public void initTest() {
        lega = createEntity(em);
    }

    @Test
    @Transactional
    void createLega() throws Exception {
        int databaseSizeBeforeCreate = legaRepository.findAll().size();
        // Create the Lega
        restLegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isCreated());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeCreate + 1);
        Lega testLega = legaList.get(legaList.size() - 1);
        assertThat(testLega.getCodice()).isEqualTo(DEFAULT_CODICE);
        assertThat(testLega.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testLega.getIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
        assertThat(testLega.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createLegaWithExistingId() throws Exception {
        // Create the Lega with an existing ID
        lega.setId(1L);

        int databaseSizeBeforeCreate = legaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isBadRequest());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = legaRepository.findAll().size();
        // set the field null
        lega.setCodice(null);

        // Create the Lega, which fails.

        restLegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isBadRequest());

        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = legaRepository.findAll().size();
        // set the field null
        lega.setNome(null);

        // Create the Lega, which fails.

        restLegaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isBadRequest());

        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLegas() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        // Get all the legaList
        restLegaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lega.getId().intValue())))
            .andExpect(jsonPath("$.[*].codice").value(hasItem(DEFAULT_CODICE)))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE.toString())))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getLega() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        // Get the lega
        restLegaMockMvc
            .perform(get(ENTITY_API_URL_ID, lega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lega.getId().intValue()))
            .andExpect(jsonPath("$.codice").value(DEFAULT_CODICE))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE.toString()))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingLega() throws Exception {
        // Get the lega
        restLegaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLega() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        int databaseSizeBeforeUpdate = legaRepository.findAll().size();

        // Update the lega
        Lega updatedLega = legaRepository.findById(lega.getId()).get();
        // Disconnect from session so that the updates on updatedLega are not directly saved in db
        em.detach(updatedLega);
        updatedLega.codice(UPDATED_CODICE).descrizione(UPDATED_DESCRIZIONE).isPrivate(UPDATED_IS_PRIVATE).nome(UPDATED_NOME);

        restLegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLega))
            )
            .andExpect(status().isOk());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
        Lega testLega = legaList.get(legaList.size() - 1);
        assertThat(testLega.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testLega.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testLega.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testLega.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lega.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLegaWithPatch() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        int databaseSizeBeforeUpdate = legaRepository.findAll().size();

        // Update the lega using partial update
        Lega partialUpdatedLega = new Lega();
        partialUpdatedLega.setId(lega.getId());

        partialUpdatedLega.codice(UPDATED_CODICE).descrizione(UPDATED_DESCRIZIONE).isPrivate(UPDATED_IS_PRIVATE).nome(UPDATED_NOME);

        restLegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLega))
            )
            .andExpect(status().isOk());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
        Lega testLega = legaList.get(legaList.size() - 1);
        assertThat(testLega.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testLega.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testLega.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testLega.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateLegaWithPatch() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        int databaseSizeBeforeUpdate = legaRepository.findAll().size();

        // Update the lega using partial update
        Lega partialUpdatedLega = new Lega();
        partialUpdatedLega.setId(lega.getId());

        partialUpdatedLega.codice(UPDATED_CODICE).descrizione(UPDATED_DESCRIZIONE).isPrivate(UPDATED_IS_PRIVATE).nome(UPDATED_NOME);

        restLegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLega))
            )
            .andExpect(status().isOk());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
        Lega testLega = legaList.get(legaList.size() - 1);
        assertThat(testLega.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testLega.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testLega.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testLega.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lega.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lega))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLega() throws Exception {
        int databaseSizeBeforeUpdate = legaRepository.findAll().size();
        lega.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLegaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lega)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lega in the database
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLega() throws Exception {
        // Initialize the database
        legaRepository.saveAndFlush(lega);

        int databaseSizeBeforeDelete = legaRepository.findAll().size();

        // Delete the lega
        restLegaMockMvc
            .perform(delete(ENTITY_API_URL_ID, lega.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lega> legaList = legaRepository.findAll();
        assertThat(legaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
