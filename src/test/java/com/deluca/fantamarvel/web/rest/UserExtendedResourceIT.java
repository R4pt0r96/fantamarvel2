package com.deluca.fantamarvel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deluca.fantamarvel.IntegrationTest;
import com.deluca.fantamarvel.domain.UserExtended;
import com.deluca.fantamarvel.repository.UserExtendedRepository;
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
 * Integration tests for the {@link UserExtendedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserExtendedResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_1 = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_2 = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_3 = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_3 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-extendeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserExtendedRepository userExtendedRepository;

    @Mock
    private UserExtendedRepository userExtendedRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserExtendedMockMvc;

    private UserExtended userExtended;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtended createEntity(EntityManager em) {
        UserExtended userExtended = new UserExtended()
            .username(DEFAULT_USERNAME)
            .provincia(DEFAULT_PROVINCIA)
            .note1(DEFAULT_NOTE_1)
            .note2(DEFAULT_NOTE_2)
            .note3(DEFAULT_NOTE_3);
        return userExtended;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtended createUpdatedEntity(EntityManager em) {
        UserExtended userExtended = new UserExtended()
            .username(UPDATED_USERNAME)
            .provincia(UPDATED_PROVINCIA)
            .note1(UPDATED_NOTE_1)
            .note2(UPDATED_NOTE_2)
            .note3(UPDATED_NOTE_3);
        return userExtended;
    }

    @BeforeEach
    public void initTest() {
        userExtended = createEntity(em);
    }

    @Test
    @Transactional
    void createUserExtended() throws Exception {
        int databaseSizeBeforeCreate = userExtendedRepository.findAll().size();
        // Create the UserExtended
        restUserExtendedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtended)))
            .andExpect(status().isCreated());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeCreate + 1);
        UserExtended testUserExtended = userExtendedList.get(userExtendedList.size() - 1);
        assertThat(testUserExtended.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserExtended.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testUserExtended.getNote1()).isEqualTo(DEFAULT_NOTE_1);
        assertThat(testUserExtended.getNote2()).isEqualTo(DEFAULT_NOTE_2);
        assertThat(testUserExtended.getNote3()).isEqualTo(DEFAULT_NOTE_3);
    }

    @Test
    @Transactional
    void createUserExtendedWithExistingId() throws Exception {
        // Create the UserExtended with an existing ID
        userExtended.setId(1L);

        int databaseSizeBeforeCreate = userExtendedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtendedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtended)))
            .andExpect(status().isBadRequest());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserExtendeds() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        // Get all the userExtendedList
        restUserExtendedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtended.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].note1").value(hasItem(DEFAULT_NOTE_1)))
            .andExpect(jsonPath("$.[*].note2").value(hasItem(DEFAULT_NOTE_2)))
            .andExpect(jsonPath("$.[*].note3").value(hasItem(DEFAULT_NOTE_3)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserExtendedsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userExtendedRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserExtendedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userExtendedRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserExtendedsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userExtendedRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserExtendedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userExtendedRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUserExtended() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        // Get the userExtended
        restUserExtendedMockMvc
            .perform(get(ENTITY_API_URL_ID, userExtended.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userExtended.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA))
            .andExpect(jsonPath("$.note1").value(DEFAULT_NOTE_1))
            .andExpect(jsonPath("$.note2").value(DEFAULT_NOTE_2))
            .andExpect(jsonPath("$.note3").value(DEFAULT_NOTE_3));
    }

    @Test
    @Transactional
    void getNonExistingUserExtended() throws Exception {
        // Get the userExtended
        restUserExtendedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserExtended() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();

        // Update the userExtended
        UserExtended updatedUserExtended = userExtendedRepository.findById(userExtended.getId()).get();
        // Disconnect from session so that the updates on updatedUserExtended are not directly saved in db
        em.detach(updatedUserExtended);
        updatedUserExtended
            .username(UPDATED_USERNAME)
            .provincia(UPDATED_PROVINCIA)
            .note1(UPDATED_NOTE_1)
            .note2(UPDATED_NOTE_2)
            .note3(UPDATED_NOTE_3);

        restUserExtendedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserExtended.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserExtended))
            )
            .andExpect(status().isOk());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
        UserExtended testUserExtended = userExtendedList.get(userExtendedList.size() - 1);
        assertThat(testUserExtended.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserExtended.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testUserExtended.getNote1()).isEqualTo(UPDATED_NOTE_1);
        assertThat(testUserExtended.getNote2()).isEqualTo(UPDATED_NOTE_2);
        assertThat(testUserExtended.getNote3()).isEqualTo(UPDATED_NOTE_3);
    }

    @Test
    @Transactional
    void putNonExistingUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userExtended.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userExtended))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userExtended))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtended)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserExtendedWithPatch() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();

        // Update the userExtended using partial update
        UserExtended partialUpdatedUserExtended = new UserExtended();
        partialUpdatedUserExtended.setId(userExtended.getId());

        partialUpdatedUserExtended.note1(UPDATED_NOTE_1);

        restUserExtendedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtended.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtended))
            )
            .andExpect(status().isOk());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
        UserExtended testUserExtended = userExtendedList.get(userExtendedList.size() - 1);
        assertThat(testUserExtended.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserExtended.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testUserExtended.getNote1()).isEqualTo(UPDATED_NOTE_1);
        assertThat(testUserExtended.getNote2()).isEqualTo(DEFAULT_NOTE_2);
        assertThat(testUserExtended.getNote3()).isEqualTo(DEFAULT_NOTE_3);
    }

    @Test
    @Transactional
    void fullUpdateUserExtendedWithPatch() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();

        // Update the userExtended using partial update
        UserExtended partialUpdatedUserExtended = new UserExtended();
        partialUpdatedUserExtended.setId(userExtended.getId());

        partialUpdatedUserExtended
            .username(UPDATED_USERNAME)
            .provincia(UPDATED_PROVINCIA)
            .note1(UPDATED_NOTE_1)
            .note2(UPDATED_NOTE_2)
            .note3(UPDATED_NOTE_3);

        restUserExtendedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtended.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtended))
            )
            .andExpect(status().isOk());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
        UserExtended testUserExtended = userExtendedList.get(userExtendedList.size() - 1);
        assertThat(testUserExtended.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserExtended.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testUserExtended.getNote1()).isEqualTo(UPDATED_NOTE_1);
        assertThat(testUserExtended.getNote2()).isEqualTo(UPDATED_NOTE_2);
        assertThat(testUserExtended.getNote3()).isEqualTo(UPDATED_NOTE_3);
    }

    @Test
    @Transactional
    void patchNonExistingUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userExtended.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userExtended))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userExtended))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserExtended() throws Exception {
        int databaseSizeBeforeUpdate = userExtendedRepository.findAll().size();
        userExtended.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtendedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userExtended))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtended in the database
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserExtended() throws Exception {
        // Initialize the database
        userExtendedRepository.saveAndFlush(userExtended);

        int databaseSizeBeforeDelete = userExtendedRepository.findAll().size();

        // Delete the userExtended
        restUserExtendedMockMvc
            .perform(delete(ENTITY_API_URL_ID, userExtended.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserExtended> userExtendedList = userExtendedRepository.findAll();
        assertThat(userExtendedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
