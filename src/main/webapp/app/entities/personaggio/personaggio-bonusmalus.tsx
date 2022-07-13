import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
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

export const PersonaggioBonusMalus = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const bonusMaluses = useAppSelector(state => state.bonusMalus.entities);
  const squadras = useAppSelector(state => state.squadra.entities);
  const personaggioEntity = useAppSelector(state => state.personaggio.entity);
  const loading = useAppSelector(state => state.personaggio.loading);
  const updating = useAppSelector(state => state.personaggio.updating);
  const updateSuccess = useAppSelector(state => state.personaggio.updateSuccess);
  const handleClose = () => {
    props.history.push('/personaggio');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBonusMaluses({}));
    dispatch(getSquadras({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
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
                label="Bonusmalus"
                id="personaggio-bonusmalus"
                data-cy="bonusmalus"
                type="select"
                multiple
                name="bonusmaluses"
              >
                <option value="" key="0" />
                {bonusMaluses
                  ? bonusMaluses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.descrizione}
                      </option>
                    ))
                  : null}
              </ValidatedField>
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
