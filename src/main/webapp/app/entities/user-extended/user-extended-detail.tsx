import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-extended.reducer';

export const UserExtendedDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userExtendedEntity = useAppSelector(state => state.userExtended.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userExtendedDetailsHeading">UserExtended</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userExtendedEntity.id}</dd>
          <dt>
            <span id="username">Username</span>
          </dt>
          <dd>{userExtendedEntity.username}</dd>
          <dt>
            <span id="provincia">Provincia</span>
          </dt>
          <dd>{userExtendedEntity.provincia}</dd>
          <dt>
            <span id="note1">Note 1</span>
          </dt>
          <dd>{userExtendedEntity.note1}</dd>
          <dt>
            <span id="note2">Note 2</span>
          </dt>
          <dd>{userExtendedEntity.note2}</dd>
          <dt>
            <span id="note3">Note 3</span>
          </dt>
          <dd>{userExtendedEntity.note3}</dd>
          <dt>User</dt>
          <dd>{userExtendedEntity.user ? userExtendedEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-extended" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-extended/${userExtendedEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserExtendedDetail;
