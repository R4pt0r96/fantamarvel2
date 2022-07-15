import React, { useState, useEffect, useRef } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, FormGroup, Input, Label } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBonusMalus } from 'app/shared/model/bonus-malus.model';
import { getEntities as getBonusMaluses } from 'app/entities/bonus-malus/bonus-malus.reducer';
import { ISquadra } from 'app/shared/model/squadra.model';
import { getEntities as getSquadras } from 'app/entities/squadra/squadra.reducer';
import { IPersonaggio } from 'app/shared/model/personaggio.model';
import { getEntity, updateEntity, createEntity, reset } from './personaggio.reducer';
import { getEntities as getFilms } from '../film/film.reducer';

export const PersonaggioBonusMalus = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);
  const [filmSelectedId, setFilmSelectedId] = useState(0);

  const filmDropdown: React.MutableRefObject<any> = useRef();

  const films = useAppSelector(state => state.film.entities);
  const bonusMaluses = useAppSelector(state => state.bonusMalus.entities);
  const squadras = useAppSelector(state => state.squadra.entities);
  const personaggioEntity = useAppSelector(state => state.personaggio.entity);
  const loading = useAppSelector(state => state.personaggio.loading);
  const updating = useAppSelector(state => state.personaggio.updating);
  const updateSuccess = useAppSelector(state => state.personaggio.updateSuccess);
  const handleClose = () => {
    props.history.push('/personaggio');
  };

  const bonusMalusFilterByFilm: IBonusMalus[] = bonusMaluses.filter(obj => obj.film.id == filmSelectedId);

  const changeFilmHandler = () => {
    setFilmSelectedId(filmDropdown.current.value);
  };

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(getFilms({}));
    dispatch(getSquadras({}));
    dispatch(getBonusMaluses({}));
  }, []);

  useEffect(() => {
    dispatch(getBonusMaluses({}));
  }, [filmSelectedId]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    delete values.film;
    const entity = {
      ...personaggioEntity,
      ...values,
      bonusmaluses: mapIdList(values.bonusmaluses),
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
          ...personaggioEntity,
          bonusmaluses: personaggioEntity?.bonusmaluses?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.personaggio.home.createOrEditLabel" data-cy="PersonaggioCreateUpdateHeading">
            Create or edit a Personaggio
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="personaggio-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                readOnly
                label="Nome"
                id="personaggio-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                readOnly
                hidden
                label="Description"
                id="personaggio-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                readOnly
                hidden
                label="Note"
                id="personaggio-note"
                name="note"
                data-cy="note"
                type="text"
                validate={{
                  maxLength: { value: 1024, message: 'This field cannot be longer than 1024 characters.' },
                }}
              />
              <ValidatedField
                readOnly
                hidden
                label="Is Active"
                id="personaggio-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                readOnly
                hidden
                label="Url Img"
                id="personaggio-urlImg"
                name="urlImg"
                data-cy="urlImg"
                type="text"
                validate={{
                  maxLength: { value: 1024, message: 'This field cannot be longer than 1024 characters.' },
                }}
              />
              <ValidatedField
                id="bonus-malus-film"
                name="film"
                data-cy="film"
                label="Film"
                type="select"
                onChange={changeFilmHandler}
                innerRef={filmDropdown}
              >
                <option value="0" key="0" />
                {films
                  ? films.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.titolo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {filmSelectedId != 0 ? (
                <FormGroup>
                  <Label>Bonus e Malus</Label>
                  <div>
                    {bonusMalusFilterByFilm
                      ? bonusMalusFilterByFilm.map(obj => {
                          return (
                            <Row key={obj.id}>
                              <label htmlFor={'' + obj.id}>
                                <Input type="checkbox" name="bonusmaluses" id={'' + obj.id} value={obj.id} />
                                {' ' + obj.descrizione}
                              </label>
                            </Row>
                          );
                        })
                      : null}
                  </div>
                </FormGroup>
              ) : null}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personaggio" replace color="info">
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

export default PersonaggioBonusMalus;
