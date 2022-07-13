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
import { IBonusMalus } from 'app/shared/model/bonus-malus.model';
import { getEntity, updateEntity, createEntity, reset } from './bonus-malus.reducer';

export const BonusMalusUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const films = useAppSelector(state => state.film.entities);
  const personaggios = useAppSelector(state => state.personaggio.entities);
  const bonusMalusEntity = useAppSelector(state => state.bonusMalus.entity);
  const loading = useAppSelector(state => state.bonusMalus.loading);
  const updating = useAppSelector(state => state.bonusMalus.updating);
  const updateSuccess = useAppSelector(state => state.bonusMalus.updateSuccess);
  const handleClose = () => {
    props.history.push('/bonus-malus');
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
      ...bonusMalusEntity,
      ...values,
      film: films.find(it => it.id.toString() === values.film.toString()),
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
          ...bonusMalusEntity,
          film: bonusMalusEntity?.film?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.bonusMalus.home.createOrEditLabel" data-cy="BonusMalusCreateUpdateHeading">
            Create or edit a BonusMalus
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bonus-malus-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Descrizione"
                id="bonus-malus-descrizione"
                name="descrizione"
                data-cy="descrizione"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 1024, message: 'This field cannot be longer than 1024 characters.' },
                }}
              />
              <ValidatedField
                label="Punti"
                required
                id="bonus-malus-punti"
                name="punti"
                data-cy="punti"
                type="number"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="bonus-malus-film"
                required
                name="film"
                data-cy="film"
                label="Film"
                type="select"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              >
                <option value="" key="0" />
                {films
                  ? films.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.titolo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bonus-malus" replace color="info">
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

export default BonusMalusUpdate;
