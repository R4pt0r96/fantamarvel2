package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.FilmPersonaggio;
import com.deluca.fantamarvel.repository.FilmPersonaggioRepository;
import com.deluca.fantamarvel.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.deluca.fantamarvel.domain.FilmPersonaggio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FilmPersonaggioResource {

    private final Logger log = LoggerFactory.getLogger(FilmPersonaggioResource.class);

    private static final String ENTITY_NAME = "filmPersonaggio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilmPersonaggioRepository filmPersonaggioRepository;

    public FilmPersonaggioResource(FilmPersonaggioRepository filmPersonaggioRepository) {
        this.filmPersonaggioRepository = filmPersonaggioRepository;
    }

    /**
     * {@code POST  /film-personaggios} : Create a new filmPersonaggio.
     *
     * @param filmPersonaggio the filmPersonaggio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filmPersonaggio, or with status {@code 400 (Bad Request)} if the filmPersonaggio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/film-personaggios")
    public ResponseEntity<FilmPersonaggio> createFilmPersonaggio(@Valid @RequestBody FilmPersonaggio filmPersonaggio)
        throws URISyntaxException {
        log.debug("REST request to save FilmPersonaggio : {}", filmPersonaggio);
        if (filmPersonaggio.getId() != null) {
            throw new BadRequestAlertException("A new filmPersonaggio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilmPersonaggio result = filmPersonaggioRepository.save(filmPersonaggio);
        return ResponseEntity
            .created(new URI("/api/film-personaggios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /film-personaggios/:id} : Updates an existing filmPersonaggio.
     *
     * @param id the id of the filmPersonaggio to save.
     * @param filmPersonaggio the filmPersonaggio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filmPersonaggio,
     * or with status {@code 400 (Bad Request)} if the filmPersonaggio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filmPersonaggio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/film-personaggios/{id}")
    public ResponseEntity<FilmPersonaggio> updateFilmPersonaggio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FilmPersonaggio filmPersonaggio
    ) throws URISyntaxException {
        log.debug("REST request to update FilmPersonaggio : {}, {}", id, filmPersonaggio);
        if (filmPersonaggio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filmPersonaggio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filmPersonaggioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilmPersonaggio result = filmPersonaggioRepository.save(filmPersonaggio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filmPersonaggio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /film-personaggios/:id} : Partial updates given fields of an existing filmPersonaggio, field will ignore if it is null
     *
     * @param id the id of the filmPersonaggio to save.
     * @param filmPersonaggio the filmPersonaggio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filmPersonaggio,
     * or with status {@code 400 (Bad Request)} if the filmPersonaggio is not valid,
     * or with status {@code 404 (Not Found)} if the filmPersonaggio is not found,
     * or with status {@code 500 (Internal Server Error)} if the filmPersonaggio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/film-personaggios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilmPersonaggio> partialUpdateFilmPersonaggio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FilmPersonaggio filmPersonaggio
    ) throws URISyntaxException {
        log.debug("REST request to partial update FilmPersonaggio partially : {}, {}", id, filmPersonaggio);
        if (filmPersonaggio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filmPersonaggio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filmPersonaggioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilmPersonaggio> result = filmPersonaggioRepository
            .findById(filmPersonaggio.getId())
            .map(existingFilmPersonaggio -> {
                if (filmPersonaggio.getCosto() != null) {
                    existingFilmPersonaggio.setCosto(filmPersonaggio.getCosto());
                }
                if (filmPersonaggio.getIsActive() != null) {
                    existingFilmPersonaggio.setIsActive(filmPersonaggio.getIsActive());
                }

                return existingFilmPersonaggio;
            })
            .map(filmPersonaggioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filmPersonaggio.getId().toString())
        );
    }

    /**
     * {@code GET  /film-personaggios} : get all the filmPersonaggios.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filmPersonaggios in body.
     */
    @GetMapping("/film-personaggios")
    public ResponseEntity<List<FilmPersonaggio>> getAllFilmPersonaggios(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of FilmPersonaggios");
        Page<FilmPersonaggio> page;
        if (eagerload) {
            page = filmPersonaggioRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = filmPersonaggioRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /film-personaggios/:id} : get the "id" filmPersonaggio.
     *
     * @param id the id of the filmPersonaggio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filmPersonaggio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/film-personaggios/{id}")
    public ResponseEntity<FilmPersonaggio> getFilmPersonaggio(@PathVariable Long id) {
        log.debug("REST request to get FilmPersonaggio : {}", id);
        Optional<FilmPersonaggio> filmPersonaggio = filmPersonaggioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(filmPersonaggio);
    }

    /**
     * {@code DELETE  /film-personaggios/:id} : delete the "id" filmPersonaggio.
     *
     * @param id the id of the filmPersonaggio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/film-personaggios/{id}")
    public ResponseEntity<Void> deleteFilmPersonaggio(@PathVariable Long id) {
        log.debug("REST request to delete FilmPersonaggio : {}", id);
        filmPersonaggioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
