package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.BonusMalus;
import com.deluca.fantamarvel.domain.Personaggio;
import com.deluca.fantamarvel.domain.Squadra;
import com.deluca.fantamarvel.repository.BonusMalusRepository;
import com.deluca.fantamarvel.repository.PersonaggioRepository;
import com.deluca.fantamarvel.repository.SquadraRepository;
import com.deluca.fantamarvel.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
 * REST controller for managing {@link com.deluca.fantamarvel.domain.Personaggio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonaggioResource {

    private final Logger log = LoggerFactory.getLogger(PersonaggioResource.class);

    private static final String ENTITY_NAME = "personaggio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonaggioRepository personaggioRepository;
    private final SquadraRepository squadraRepository;
    private final BonusMalusRepository bonusMalusRepository;

    public PersonaggioResource(
        PersonaggioRepository personaggioRepository,
        SquadraRepository squadraRepository,
        BonusMalusRepository bonusMalusRepository
    ) {
        this.personaggioRepository = personaggioRepository;
        this.squadraRepository = squadraRepository;
        this.bonusMalusRepository = bonusMalusRepository;
    }

    /**
     * {@code POST  /personaggios} : Create a new personaggio.
     *
     * @param personaggio the personaggio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personaggio, or with status {@code 400 (Bad Request)} if the personaggio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personaggios")
    public ResponseEntity<Personaggio> createPersonaggio(@Valid @RequestBody Personaggio personaggio) throws URISyntaxException {
        log.debug("REST request to save Personaggio : {}", personaggio);
        if (personaggio.getId() != null) {
            throw new BadRequestAlertException("A new personaggio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Personaggio result = personaggioRepository.save(personaggio);
        return ResponseEntity
            .created(new URI("/api/personaggios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personaggios/:id} : Updates an existing personaggio.
     *
     * @param id the id of the personaggio to save.
     * @param personaggio the personaggio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaggio,
     * or with status {@code 400 (Bad Request)} if the personaggio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personaggio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personaggios/{id}")
    public ResponseEntity<Personaggio> updatePersonaggio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Personaggio personaggio
    ) throws URISyntaxException {
        log.debug("REST request to update Personaggio : {}, {}", id, personaggio);
        if (personaggio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaggio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personaggioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Personaggio result = personaggioRepository.save(personaggio);

        //calcolo punti

        List<Squadra> listaSquadre = squadraRepository.findSquadreOfFilmActive();
        listaSquadre =
            listaSquadre.stream().filter(squadra -> squadra.getPersonaggios().contains(personaggio)).collect(Collectors.toList());
        for (Squadra squadra : listaSquadre) {
            if (squadra.getIsSalvata()) {
                squadra.setPunteggio(0);
                List<Personaggio> personaggiSquadra = squadra.getPersonaggios().stream().collect(Collectors.toList());
                for (Personaggio pers : personaggiSquadra) {
                    List<BonusMalus> listaBonusMalus = pers.getBonusmaluses().stream().collect(Collectors.toList());
                    for (BonusMalus bonusMalus : listaBonusMalus) {
                        squadra.setPunteggio(squadra.getPunteggio() + bonusMalus.getPunti());
                    }
                }
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personaggio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personaggios/:id} : Partial updates given fields of an existing personaggio, field will ignore if it is null
     *
     * @param id the id of the personaggio to save.
     * @param personaggio the personaggio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personaggio,
     * or with status {@code 400 (Bad Request)} if the personaggio is not valid,
     * or with status {@code 404 (Not Found)} if the personaggio is not found,
     * or with status {@code 500 (Internal Server Error)} if the personaggio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personaggios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Personaggio> partialUpdatePersonaggio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Personaggio personaggio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Personaggio partially : {}, {}", id, personaggio);
        if (personaggio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personaggio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personaggioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Personaggio> result = personaggioRepository
            .findById(personaggio.getId())
            .map(existingPersonaggio -> {
                if (personaggio.getNome() != null) {
                    existingPersonaggio.setNome(personaggio.getNome());
                }
                if (personaggio.getDescription() != null) {
                    existingPersonaggio.setDescription(personaggio.getDescription());
                }
                if (personaggio.getNote() != null) {
                    existingPersonaggio.setNote(personaggio.getNote());
                }
                if (personaggio.getIsActive() != null) {
                    existingPersonaggio.setIsActive(personaggio.getIsActive());
                }
                if (personaggio.getUrlImg() != null) {
                    existingPersonaggio.setUrlImg(personaggio.getUrlImg());
                }

                return existingPersonaggio;
            })
            .map(personaggioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personaggio.getId().toString())
        );
    }

    /**
     * {@code GET  /personaggios} : get all the personaggios.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personaggios in body.
     */
    @GetMapping("/personaggios")
    public ResponseEntity<List<Personaggio>> getAllPersonaggios(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Personaggios");
        Page<Personaggio> page;
        if (eagerload) {
            page = personaggioRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = personaggioRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personaggios/:id} : get the "id" personaggio.
     *
     * @param id the id of the personaggio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personaggio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personaggios/{id}")
    public ResponseEntity<Personaggio> getPersonaggio(@PathVariable Long id) {
        log.debug("REST request to get Personaggio : {}", id);
        Optional<Personaggio> personaggio = personaggioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(personaggio);
    }

    /**
     * {@code DELETE  /personaggios/:id} : delete the "id" personaggio.
     *
     * @param id the id of the personaggio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personaggios/{id}")
    public ResponseEntity<Void> deletePersonaggio(@PathVariable Long id) {
        log.debug("REST request to delete Personaggio : {}", id);
        personaggioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
