import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lega.reducer';

export const LegaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const legaEntity = useAppSelector(state => state.lega.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="legaDetailsHeading">Lega</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{legaEntity.id}</dd>
          <dt>
            <span id="codice">Codice</span>
          </dt>
          <dd>{legaEntity.codice}</dd>
          <dt>
            <span id="descrizione">Descrizione</span>
          </dt>
          <dd>{legaEntity.descrizione}</dd>
          <dt>
            <span id="isPrivate">Is Private</span>
          </dt>
          <dd>{legaEntity.isPrivate ? 'true' : 'false'}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{legaEntity.nome}</dd>
        </dl>
        <Button tag={Link} to="/lega" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lega/${legaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LegaDetail;
