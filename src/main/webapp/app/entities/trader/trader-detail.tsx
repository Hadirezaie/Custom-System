import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trader.reducer';

export const TraderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const traderEntity = useAppSelector(state => state.trader.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="traderDetailsHeading">Trader</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{traderEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{traderEntity.name}</dd>
          <dt>
            <span id="nidNumber">Nid Number</span>
          </dt>
          <dd>{traderEntity.nidNumber}</dd>
          <dt>
            <span id="tIN">T IN</span>
          </dt>
          <dd>{traderEntity.tIN}</dd>
          <dt>
            <span id="licenseNumber">License Number</span>
          </dt>
          <dd>{traderEntity.licenseNumber}</dd>
        </dl>
        <Button tag={Link} to="/trader" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trader/${traderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TraderDetail;
