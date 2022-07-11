package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.Personaggio;
import com.deluca.fantamarvel.repository.PersonaggioRepository;
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
 * Integration tests for the {@link PersonaggioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonaggioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_URL_IMG = "AAAAAAAAAA";
    private static final String UPDATED_URL_IMG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personaggios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonaggioRepository personaggioRepository;

    @Mock
    private PersonaggioRepository personaggioRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonaggioMockMvc;

    private Personaggio personaggio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personaggio createEntity(EntityManager em) {
        Personaggio personaggio = new Personaggio()
            .nome(DEFAULT_NOME)
            .description(DEFAULT_DESCRIPTION)
            .note(DEFAULT_NOTE)
            .isActive(DEFAULT_IS_ACTIVE)
            .urlImg(DEFAULT_URL_IMG);
        return personaggio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personaggio createUpdatedEntity(EntityManager em) {
        Personaggio personaggio = new Personaggio()
            .nome(UPDATED_NOME)
            .description(UPDATED_DESCRIPTION)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG);
        return personaggio;
    }

    @BeforeEach
    public void initTest() {
        personaggio = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonaggio() throws Exception {
        int databaseSizeBeforeCreate = personaggioRepository.findAll().size();
        // Create the Personaggio
        restPersonaggioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaggio)))
            .andExpect(status().isCreated());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeCreate + 1);
        Personaggio testPersonaggio = personaggioList.get(personaggioList.size() - 1);
        assertThat(testPersonaggio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPersonaggio.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPersonaggio.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPersonaggio.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPersonaggio.getUrlImg()).isEqualTo(DEFAULT_URL_IMG);
    }

    @Test
    @Transactional
    void createPersonaggioWithExistingId() throws Exception {
        // Create the Personaggio with an existing ID
        personaggio.setId(1L);

        int databaseSizeBeforeCreate = personaggioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonaggioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaggio)))
            .andExpect(status().isBadRequest());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaggioRepository.findAll().size();
        // set the field null
        personaggio.setNome(null);

        // Create the Personaggio, which fails.

        restPersonaggioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaggio)))
            .andExpect(status().isBadRequest());

        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonaggios() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        // Get all the personaggioList
        restPersonaggioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personaggio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].urlImg").value(hasItem(DEFAULT_URL_IMG)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonaggiosWithEagerRelationshipsIsEnabled() throws Exception {
        when(personaggioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonaggioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personaggioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonaggiosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personaggioRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonaggioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personaggioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPersonaggio() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        // Get the personaggio
        restPersonaggioMockMvc
            .perform(get(ENTITY_API_URL_ID, personaggio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personaggio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.urlImg").value(DEFAULT_URL_IMG));
    }

    @Test
    @Transactional
    void getNonExistingPersonaggio() throws Exception {
        // Get the personaggio
        restPersonaggioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonaggio() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();

        // Update the personaggio
        Personaggio updatedPersonaggio = personaggioRepository.findById(personaggio.getId()).get();
        // Disconnect from session so that the updates on updatedPersonaggio are not directly saved in db
        em.detach(updatedPersonaggio);
        updatedPersonaggio
            .nome(UPDATED_NOME)
            .description(UPDATED_DESCRIPTION)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG);

        restPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonaggio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
        Personaggio testPersonaggio = personaggioList.get(personaggioList.size() - 1);
        assertThat(testPersonaggio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPersonaggio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPersonaggio.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPersonaggio.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPersonaggio.getUrlImg()).isEqualTo(UPDATED_URL_IMG);
    }

    @Test
    @Transactional
    void putNonExistingPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaggio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personaggio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonaggioWithPatch() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();

        // Update the personaggio using partial update
        Personaggio partialUpdatedPersonaggio = new Personaggio();
        partialUpdatedPersonaggio.setId(personaggio.getId());

        partialUpdatedPersonaggio.nome(UPDATED_NOME).description(UPDATED_DESCRIPTION).note(UPDATED_NOTE);

        restPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
        Personaggio testPersonaggio = personaggioList.get(personaggioList.size() - 1);
        assertThat(testPersonaggio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPersonaggio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPersonaggio.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPersonaggio.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testPersonaggio.getUrlImg()).isEqualTo(DEFAULT_URL_IMG);
    }

    @Test
    @Transactional
    void fullUpdatePersonaggioWithPatch() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();

        // Update the personaggio using partial update
        Personaggio partialUpdatedPersonaggio = new Personaggio();
        partialUpdatedPersonaggio.setId(personaggio.getId());

        partialUpdatedPersonaggio
            .nome(UPDATED_NOME)
            .description(UPDATED_DESCRIPTION)
            .note(UPDATED_NOTE)
            .isActive(UPDATED_IS_ACTIVE)
            .urlImg(UPDATED_URL_IMG);

        restPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonaggio))
            )
            .andExpect(status().isOk());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
        Personaggio testPersonaggio = personaggioList.get(personaggioList.size() - 1);
        assertThat(testPersonaggio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPersonaggio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPersonaggio.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPersonaggio.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testPersonaggio.getUrlImg()).isEqualTo(UPDATED_URL_IMG);
    }

    @Test
    @Transactional
    void patchNonExistingPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personaggio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personaggio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonaggio() throws Exception {
        int databaseSizeBeforeUpdate = personaggioRepository.findAll().size();
        personaggio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaggioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personaggio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personaggio in the database
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonaggio() throws Exception {
        // Initialize the database
        personaggioRepository.saveAndFlush(personaggio);

        int databaseSizeBeforeDelete = personaggioRepository.findAll().size();

        // Delete the personaggio
        restPersonaggioMockMvc
            .perform(delete(ENTITY_API_URL_ID, personaggio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personaggio> personaggioList = personaggioRepository.findAll();
        assertThat(personaggioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
