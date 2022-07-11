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
import { ILega } from 'app/shared/model/lega.model';
import { getEntities as getLegas } from 'app/entities/lega/lega.reducer';
import { IUserExtended } from 'app/shared/model/user-extended.model';
import { getEntities as getUserExtendeds } from 'app/entities/user-extended/user-extended.reducer';
import { IPersonaggio } from 'app/shared/model/personaggio.model';
import { getEntities as getPersonaggios } from 'app/entities/personaggio/personaggio.reducer';
import { ISquadra } from 'app/shared/model/squadra.model';
import { getEntity, updateEntity, createEntity, reset } from './squadra.reducer';

export const SquadraUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const films = useAppSelector(state => state.film.entities);
  const legas = useAppSelector(state => state.lega.entities);
  const userExtendeds = useAppSelector(state => state.userExtended.entities);
  const personaggios = useAppSelector(state => state.personaggio.entities);
  const squadraEntity = useAppSelector(state => state.squadra.entity);
  const loading = useAppSelector(state => state.squadra.loading);
  const updating = useAppSelector(state => state.squadra.updating);
  const updateSuccess = useAppSelector(state => state.squadra.updateSuccess);
  const handleClose = () => {
    props.history.push('/squadra' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getFilms({}));
    dispatch(getLegas({}));
    dispatch(getUserExtendeds({}));
    dispatch(getPersonaggios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...squadraEntity,
      ...values,
      personaggios: mapIdList(values.personaggios),
      film: films.find(it => it.id.toString() === values.film.toString()),
      lega: legas.find(it => it.id.toString() === values.lega.toString()),
      userExtended: userExtendeds.find(it => it.id.toString() === values.userExtended.toString()),
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
          ...squadraEntity,
          film: squadraEntity?.film?.id,
          lega: squadraEntity?.lega?.id,
          userExtended: squadraEntity?.userExtended?.id,
          personaggios: squadraEntity?.personaggios?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.squadra.home.createOrEditLabel" data-cy="SquadraCreateUpdateHeading">
            Create or edit a Squadra
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="squadra-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Gettoni"
                id="squadra-gettoni"
                name="gettoni"
                data-cy="gettoni"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Is In Lega" id="squadra-isInLega" name="isInLega" data-cy="isInLega" check type="checkbox" />
              <ValidatedField label="Is Salvata" id="squadra-isSalvata" name="isSalvata" data-cy="isSalvata" check type="checkbox" />
              <ValidatedField label="Nome" id="squadra-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField label="Punteggio" id="squadra-punteggio" name="punteggio" data-cy="punteggio" type="text" />
              <ValidatedField id="squadra-film" name="film" data-cy="film" label="Film" type="select">
                <option value="" key="0" />
                {films
                  ? films.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.titolo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="squadra-lega" name="lega" data-cy="lega" label="Lega" type="select">
                <option value="" key="0" />
                {legas
                  ? legas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="squadra-userExtended" name="userExtended" data-cy="userExtended" label="User Extended" type="select">
                <option value="" key="0" />
                {userExtendeds
                  ? userExtendeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.username}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Personaggio" id="squadra-personaggio" data-cy="personaggio" type="select" multiple name="personaggios">
                <option value="" key="0" />
                {personaggios
                  ? personaggios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/squadra" replace color="info">
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

export default SquadraUpdate;
