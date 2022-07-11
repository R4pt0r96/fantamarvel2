import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bonus-malus.reducer';

export const BonusMalusDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const bonusMalusEntity = useAppSelector(state => state.bonusMalus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bonusMalusDetailsHeading">BonusMalus</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bonusMalusEntity.id}</dd>
          <dt>
            <span id="descrizione">Descrizione</span>
          </dt>
          <dd>{bonusMalusEntity.descrizione}</dd>
          <dt>
            <span id="punti">Punti</span>
          </dt>
          <dd>{bonusMalusEntity.punti}</dd>
          <dt>Film</dt>
          <dd>{bonusMalusEntity.film ? bonusMalusEntity.film.titolo : ''}</dd>
        </dl>
        <Button tag={Link} to="/bonus-malus" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bonus-malus/${bonusMalusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BonusMalusDetail;
