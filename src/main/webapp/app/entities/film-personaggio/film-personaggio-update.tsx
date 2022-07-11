import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFilm } from 'app/shared/model/film.model';
import { getEntities as getFilms } from 'app/entities/film/film.reducer';
import { IPersonaggio } from 'app/shared/model/personaggio.model';
import { getEntities as getPersonaggios } from 'app/entities/personaggio/personaggio.reducer';
import { IFilmPersonaggio } from 'app/shared/model/film-personaggio.model';
import { getEntity, updateEntity, createEntity, reset } from './film-personaggio.reducer';

export const FilmPersonaggioUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const films = useAppSelector(state => state.film.entities);
  const personaggios = useAppSelector(state => state.personaggio.entities);
  const filmPersonaggioEntity = useAppSelector(state => state.filmPersonaggio.entity);
  const loading = useAppSelector(state => state.filmPersonaggio.loading);
  const updating = useAppSelector(state => state.filmPersonaggio.updating);
  const updateSuccess = useAppSelector(state => state.filmPersonaggio.updateSuccess);
  const handleClose = () => {
    props.history.push('/film-personaggio');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getFilms({}));
    dispatch(getPersonaggios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...filmPersonaggioEntity,
      ...values,
      film: films.find(it => it.id.toString() === values.film.toString()),
      personaggio: personaggios.find(it => it.id.toString() === values.personaggio.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...filmPersonaggioEntity,
          film: filmPersonaggioEntity?.film?.id,
          personaggio: filmPersonaggioEntity?.personaggio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.filmPersonaggio.home.createOrEditLabel" data-cy="FilmPersonaggioCreateUpdateHeading">
            Create or edit a FilmPersonaggio
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="film-personaggio-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Costo"
                id="film-personaggio-costo"
                name="costo"
                data-cy="costo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Is Active" id="film-personaggio-isActive" name="isActive" data-cy="isActive" check type="checkbox" />
              <ValidatedField id="film-personaggio-film" name="film" data-cy="film" label="Film" type="select">
                <option value="" key="0" />
                {films
                  ? films.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.titolo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="film-personaggio-personaggio" name="personaggio" data-cy="personaggio" label="Personaggio" type="select">
                <option value="" key="0" />
                {personaggios
                  ? personaggios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/film-personaggio" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FilmPersonaggioUpdate;
