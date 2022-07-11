import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserExtended } from 'app/shared/model/user-extended.model';
import { getEntities as getUserExtendeds } from 'app/entities/user-extended/user-extended.reducer';
import { IFilm } from 'app/shared/model/film.model';
import { getEntity, updateEntity, createEntity, reset } from './film.reducer';

export const FilmUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const userExtendeds = useAppSelector(state => state.userExtended.entities);
  const filmEntity = useAppSelector(state => state.film.entity);
  const loading = useAppSelector(state => state.film.loading);
  const updating = useAppSelector(state => state.film.updating);
  const updateSuccess = useAppSelector(state => state.film.updateSuccess);
  const handleClose = () => {
    props.history.push('/film' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUserExtendeds({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dataUscita = convertDateTimeToServer(values.dataUscita);
    values.dataFineIscrizione = convertDateTimeToServer(values.dataFineIscrizione);

    const entity = {
      ...filmEntity,
      ...values,
      userExtendeds: mapIdList(values.userExtendeds),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dataUscita: displayDefaultDateTime(),
          dataFineIscrizione: displayDefaultDateTime(),
        }
      : {
          ...filmEntity,
          dataUscita: convertDateTimeFromServer(filmEntity.dataUscita),
          dataFineIscrizione: convertDateTimeFromServer(filmEntity.dataFineIscrizione),
          userExtendeds: filmEntity?.userExtendeds?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.film.home.createOrEditLabel" data-cy="FilmCreateUpdateHeading">
            Create or edit a Film
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="film-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Titolo"
                id="film-titolo"
                name="titolo"
                data-cy="titolo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Data Uscita"
                id="film-dataUscita"
                name="dataUscita"
                data-cy="dataUscita"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Data Fine Iscrizione"
                id="film-dataFineIscrizione"
                name="dataFineIscrizione"
                data-cy="dataFineIscrizione"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Is Active" id="film-isActive" name="isActive" data-cy="isActive" check type="checkbox" />
              <ValidatedField
                label="Url Img"
                id="film-urlImg"
                name="urlImg"
                data-cy="urlImg"
                type="text"
                validate={{
                  maxLength: { value: 1024, message: 'This field cannot be longer than 1024 characters.' },
                }}
              />
              <ValidatedField label="Descrizione" id="film-descrizione" name="descrizione" data-cy="descrizione" type="textarea" />
              <ValidatedField
                label="User Extended"
                id="film-userExtended"
                data-cy="userExtended"
                type="select"
                multiple
                name="userExtendeds"
              >
                <option value="" key="0" />
                {userExtendeds
                  ? userExtendeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.username}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/film" replace color="info">
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

export default FilmUpdate;
