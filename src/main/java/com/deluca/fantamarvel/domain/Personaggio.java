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
 * A Personaggio.
 */
@Entity
@Table(name = "personaggio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personaggio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "description")
    private String description;

    @Size(max = 1024)
    @Column(name = "note", length = 1024)
    private String note;

    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 1024)
    @Column(name = "url_img", length = 1024)
    private String urlImg;

    @ManyToMany
    @JoinTable(
        name = "rel_personaggio__bonusmalus",
        joinColumns = @JoinColumn(name = "personaggio_id"),
        inverseJoinColumns = @JoinColumn(name = "bonusmalus_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "film", "personaggios" }, allowSetters = true)
    private Set<BonusMalus> bonusmaluses = new HashSet<>();

    @ManyToMany(mappedBy = "personaggios")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "film", "lega", "userExtended", "personaggios" }, allowSetters = true)
    private Set<Squadra> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personaggio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Personaggio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescription() {
        return this.description;
    }

    public Personaggio description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return this.note;
    }

    public Personaggio note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Personaggio isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUrlImg() {
        return this.urlImg;
    }

    public Personaggio urlImg(String urlImg) {
        this.setUrlImg(urlImg);
        return this;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Set<BonusMalus> getBonusmaluses() {
        return this.bonusmaluses;
    }

    public void setBonusmaluses(Set<BonusMalus> bonusMaluses) {
        this.bonusmaluses = bonusMaluses;
    }

    public Personaggio bonusmaluses(Set<BonusMalus> bonusMaluses) {
        this.setBonusmaluses(bonusMaluses);
        return this;
    }

    public Personaggio addBonusmalus(BonusMalus bonusMalus) {
        this.bonusmaluses.add(bonusMalus);
        bonusMalus.getPersonaggios().add(this);
        return this;
    }

    public Personaggio removeBonusmalus(BonusMalus bonusMalus) {
        this.bonusmaluses.remove(bonusMalus);
        bonusMalus.getPersonaggios().remove(this);
        return this;
    }

    public Set<Squadra> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<Squadra> squadras) {
        if (this.teams != null) {
            this.teams.forEach(i -> i.removePersonaggio(this));
        }
        if (squadras != null) {
            squadras.forEach(i -> i.addPersonaggio(this));
        }
        this.teams = squadras;
    }

    public Personaggio teams(Set<Squadra> squadras) {
        this.setTeams(squadras);
        return this;
    }

    public Personaggio addTeam(Squadra squadra) {
        this.teams.add(squadra);
        squadra.getPersonaggios().add(this);
        return this;
    }

    public Personaggio removeTeam(Squadra squadra) {
        this.teams.remove(squadra);
        squadra.getPersonaggios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personaggio)) {
            return false;
        }
        return id != null && id.equals(((Personaggio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personaggio{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", description='" + getDescription() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", urlImg='" + getUrlImg() + "'" +
            "}";
    }
}
