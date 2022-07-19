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
  const [bonusMalusChecked, setBonusMalusChecked] = useState([]);

  const filmDropdown: React.MutableRefObject<any> = useRef();
  const isMounted = useRef(false);

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
  const PersonaggioBonusMalusId = personaggioEntity?.bonusmaluses?.map(e => e.id);

  const bonusMalusCheck = () => {
    let x = [];
    for (let i = 0; i < bonusMaluses.length; i++) {
      if (PersonaggioBonusMalusId.includes(bonusMaluses[i].id)) {
        x.push({ bonusmalus: bonusMaluses[i], check: true });
      } else {
        x.push({ bonusmalus: bonusMaluses[i], check: false });
      }
    }
    setBonusMalusChecked(x);
  };

  const changeFilmHandler = () => {
    setFilmSelectedId(filmDropdown.current.value);
  };

  const checkBonusMalusHandler = e => {
    if (e.target.checked) {
      setBonusMalusChecked(
        bonusMalusChecked.map(obj => {
          if (obj.bonusmalus.id == e.target.value) {
            return { bonusmalus: obj.bonusmalus, check: true };
          } else {
            return { bonusmalus: obj.bonusmalus, check: obj.check };
          }
        })
      );
    } else {
      setBonusMalusChecked(
        bonusMalusChecked.map(obj => {
          if (obj.bonusmalus.id == e.target.value) {
            return { bonusmalus: obj.bonusmalus, check: false };
          } else {
            return { bonusmalus: obj.bonusmalus, check: obj.check };
          }
        })
      );
    }
  };

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(getFilms({}));
    dispatch(getSquadras({}));
    dispatch(getBonusMaluses({}));
  }, []);

  useEffect(() => {
    if (isMounted.current) {
      bonusMalusCheck();
    } else {
      isMounted.current = true;
    }
  }, [filmSelectedId]);

  useEffect(() => {}, [bonusMalusChecked]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    let x = [];
    bonusMalusChecked.forEach(obj => {
      if (obj.check) {
        x.push(obj.bonusmalus.id);
      }
    });
    values.preventDefault();
    console.log(bonusMalusChecked);
    const entity = {
      ...personaggioEntity,
      bonusmaluses: mapIdList(x),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
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
            <>
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
              <form onSubmit={saveEntity}>
                <FormGroup>
                  {filmSelectedId != 0 ? (
                    <FormGroup check>
                      <Label>Bonus e Malus</Label>
                      <div>
                        {bonusMalusChecked ? (
                          bonusMalusChecked.map(obj => {
                            if (obj.bonusmalus?.film?.id == filmSelectedId) {
                              return (
                                <Row key={obj.bonusmalus.id}>
                                  <label htmlFor={'' + obj.bonusmalus.id}>
                                    <Input
                                      type="checkbox"
                                      name="bonusmaluses"
                                      id={'' + obj.bonusmalus.id}
                                      value={'' + obj.bonusmalus.id}
                                      onChange={checkBonusMalusHandler}
                                      defaultChecked={obj.check}
                                    />
                                    {' ' + obj.bonusmalus.descrizione}
                                  </label>
                                </Row>
                              );
                            }
                          })
                        ) : (
                          <p>err</p>
                        )}
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
                </FormGroup>
              </form>
            </>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PersonaggioBonusMalus;
