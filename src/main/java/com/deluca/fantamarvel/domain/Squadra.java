package com.deluca.fantamarvel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Squadra.
 */
@Entity
@Table(name = "squadra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Squadra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "gettoni", nullable = false)
    private Integer gettoni;

    @Column(name = "is_in_lega")
    private Boolean isInLega;

    @NotNull
    @Column(name = "is_salvata", nullable = false)
    private Boolean isSalvata;

    @Column(name = "nome")
    private String nome;

    @Column(name = "punteggio")
    private Integer punteggio;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userExtendeds" }, allowSetters = true)
    private Film film;

    @ManyToOne
    private Lega lega;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "films" }, allowSetters = true)
    private UserExtended userExtended;

    @ManyToMany
    @JoinTable(
        name = "rel_squadra__personaggio",
        joinColumns = @JoinColumn(name = "squadra_id"),
        inverseJoinColumns = @JoinColumn(name = "personaggio_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bonusmaluses", "teams" }, allowSetters = true)
    private Set<Personaggio> personaggios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Squadra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGettoni() {
        return this.gettoni;
    }

    public Squadra gettoni(Integer gettoni) {
        this.setGettoni(gettoni);
        return this;
    }

    public void setGettoni(Integer gettoni) {
        this.gettoni = gettoni;
    }

    public Boolean getIsInLega() {
        return this.isInLega;
    }

    public Squadra isInLega(Boolean isInLega) {
        this.setIsInLega(isInLega);
        return this;
    }

    public void setIsInLega(Boolean isInLega) {
        this.isInLega = isInLega;
    }

    public Boolean getIsSalvata() {
        return this.isSalvata;
    }

    public Squadra isSalvata(Boolean isSalvata) {
        this.setIsSalvata(isSalvata);
        return this;
    }

    public void setIsSalvata(Boolean isSalvata) {
        this.isSalvata = isSalvata;
    }

    public String getNome() {
        return this.nome;
    }

    public Squadra nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPunteggio() {
        return this.punteggio;
    }

    public Squadra punteggio(Integer punteggio) {
        this.setPunteggio(punteggio);
        return this;
    }

    public void setPunteggio(Integer punteggio) {
        this.punteggio = punteggio;
    }

    public Film getFilm() {
        return this.film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Squadra film(Film film) {
        this.setFilm(film);
        return this;
    }

    public Lega getLega() {
        return this.lega;
    }

    public void setLega(Lega lega) {
        this.lega = lega;
    }

    public Squadra lega(Lega lega) {
        this.setLega(lega);
        return this;
    }

    public UserExtended getUserExtended() {
        return this.userExtended;
    }

    public void setUserExtended(UserExtended userExtended) {
        this.userExtended = userExtended;
    }

    public Squadra userExtended(UserExtended userExtended) {
        this.setUserExtended(userExtended);
        return this;
    }

    public Set<Personaggio> getPersonaggios() {
        return this.personaggios;
    }

    public void setPersonaggios(Set<Personaggio> personaggios) {
        this.personaggios = personaggios;
    }

    public Squadra personaggios(Set<Personaggio> personaggios) {
        this.setPersonaggios(personaggios);
        return this;
    }

    public Squadra addPersonaggio(Personaggio personaggio) {
        this.personaggios.add(personaggio);
        personaggio.getTeams().add(this);
        return this;
    }

    public Squadra removePersonaggio(Personaggio personaggio) {
        this.personaggios.remove(personaggio);
        personaggio.getTeams().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Squadra)) {
            return false;
        }
        return id != null && id.equals(((Squadra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Squadra{" +
            "id=" + getId() +
            ", gettoni=" + getGettoni() +
            ", isInLega='" + getIsInLega() + "'" +
            ", isSalvata='" + getIsSalvata() + "'" +
            ", nome='" + getNome() + "'" +
            ", punteggio=" + getPunteggio() +
            "}";
    }
}
