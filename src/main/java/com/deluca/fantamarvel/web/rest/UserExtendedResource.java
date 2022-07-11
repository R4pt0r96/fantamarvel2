package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.UserExtended;
import com.deluca.fantamarvel.repository.UserExtendedRepository;
import com.deluca.fantamarvel.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.deluca.fantamarvel.domain.UserExtended}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserExtendedResource {

    private final Logger log = LoggerFactory.getLogger(UserExtendedResource.class);

    private static final String ENTITY_NAME = "userExtended";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserExtendedRepository userExtendedRepository;

    public UserExtendedResource(UserExtendedRepository userExtendedRepository) {
        this.userExtendedRepository = userExtendedRepository;
    }

    /**
     * {@code POST  /user-extendeds} : Create a new userExtended.
     *
     * @param userExtended the userExtended to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userExtended, or with status {@code 400 (Bad Request)} if the userExtended has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-extendeds")
    public ResponseEntity<UserExtended> createUserExtended(@RequestBody UserExtended userExtended) throws URISyntaxException {
        log.debug("REST request to save UserExtended : {}", userExtended);
        if (userExtended.getId() != null) {
            throw new BadRequestAlertException("A new userExtended cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserExtended result = userExtendedRepository.save(userExtended);
        return ResponseEntity
            .created(new URI("/api/user-extendeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-extendeds/:id} : Updates an existing userExtended.
     *
     * @param id the id of the userExtended to save.
     * @param userExtended the userExtended to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userExtended,
     * or with status {@code 400 (Bad Request)} if the userExtended is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userExtended couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-extendeds/{id}")
    public ResponseEntity<UserExtended> updateUserExtended(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserExtended userExtended
    ) throws URISyntaxException {
        log.debug("REST request to update UserExtended : {}, {}", id, userExtended);
        if (userExtended.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userExtended.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userExtendedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserExtended result = userExtendedRepository.save(userExtended);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userExtended.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-extendeds/:id} : Partial updates given fields of an existing userExtended, field will ignore if it is null
     *
     * @param id the id of the userExtended to save.
     * @param userExtended the userExtended to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userExtended,
     * or with status {@code 400 (Bad Request)} if the userExtended is not valid,
     * or with status {@code 404 (Not Found)} if the userExtended is not found,
     * or with status {@code 500 (Internal Server Error)} if the userExtended couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-extendeds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserExtended> partialUpdateUserExtended(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserExtended userExtended
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserExtended partially : {}, {}", id, userExtended);
        if (userExtended.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userExtended.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userExtendedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserExtended> result = userExtendedRepository
            .findById(userExtended.getId())
            .map(existingUserExtended -> {
                if (userExtended.getUsername() != null) {
                    existingUserExtended.setUsername(userExtended.getUsername());
                }
                if (userExtended.getProvincia() != null) {
                    existingUserExtended.setProvincia(userExtended.getProvincia());
                }
                if (userExtended.getNote1() != null) {
                    existingUserExtended.setNote1(userExtended.getNote1());
                }
                if (userExtended.getNote2() != null) {
                    existingUserExtended.setNote2(userExtended.getNote2());
                }
                if (userExtended.getNote3() != null) {
                    existingUserExtended.setNote3(userExtended.getNote3());
                }

                return existingUserExtended;
            })
            .map(userExtendedRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userExtended.getId().toString())
        );
    }

    /**
     * {@code GET  /user-extendeds} : get all the userExtendeds.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userExtendeds in body.
     */
    @GetMapping("/user-extendeds")
    public ResponseEntity<List<UserExtended>> getAllUserExtendeds(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserExtendeds");
        Page<UserExtended> page;
        if (eagerload) {
            page = userExtendedRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = userExtendedRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-extendeds/:id} : get the "id" userExtended.
     *
     * @param id the id of the userExtended to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userExtended, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-extendeds/{id}")
    public ResponseEntity<UserExtended> getUserExtended(@PathVariable Long id) {
        log.debug("REST request to get UserExtended : {}", id);
        Optional<UserExtended> userExtended = userExtendedRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(userExtended);
    }

    /**
     * {@code DELETE  /user-extendeds/:id} : delete the "id" userExtended.
     *
     * @param id the id of the userExtended to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-extendeds/{id}")
    public ResponseEntity<Void> deleteUserExtended(@PathVariable Long id) {
        log.debug("REST request to delete UserExtended : {}", id);
        userExtendedRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
