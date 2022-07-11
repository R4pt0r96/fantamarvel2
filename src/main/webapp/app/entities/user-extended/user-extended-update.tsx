import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IFilm } from 'app/shared/model/film.model';
import { getEntities as getFilms } from 'app/entities/film/film.reducer';
import { IUserExtended } from 'app/shared/model/user-extended.model';
import { getEntity, updateEntity, createEntity, reset } from './user-extended.reducer';

export const UserExtendedUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const films = useAppSelector(state => state.film.entities);
  const userExtendedEntity = useAppSelector(state => state.userExtended.entity);
  const loading = useAppSelector(state => state.userExtended.loading);
  const updating = useAppSelector(state => state.userExtended.updating);
  const updateSuccess = useAppSelector(state => state.userExtended.updateSuccess);
  const handleClose = () => {
    props.history.push('/user-extended' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getFilms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...userExtendedEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          ...userExtendedEntity,
          user: userExtendedEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="fantamarvel2App.userExtended.home.createOrEditLabel" data-cy="UserExtendedCreateUpdateHeading">
            Create or edit a UserExtended
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
                <ValidatedField name="id" required readOnly id="user-extended-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Username" id="user-extended-username" name="username" data-cy="username" type="text" />
              <ValidatedField label="Provincia" id="user-extended-provincia" name="provincia" data-cy="provincia" type="text" />
              <ValidatedField label="Note 1" id="user-extended-note1" name="note1" data-cy="note1" type="text" />
              <ValidatedField label="Note 2" id="user-extended-note2" name="note2" data-cy="note2" type="text" />
              <ValidatedField label="Note 3" id="user-extended-note3" name="note3" data-cy="note3" type="text" />
              <ValidatedField id="user-extended-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-extended" replace color="info">
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

export default UserExtendedUpdate;
