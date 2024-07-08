import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tarif.reducer';

export const TarifDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tarifEntity = useAppSelector(state => state.tarif.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tarifDetailsHeading">Tarif</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tarifEntity.id}</dd>
          <dt>
            <span id="amount">Amount</span>
          </dt>
          <dd>{tarifEntity.amount}</dd>
          <dt>
            <span id="paidDate">Paid Date</span>
          </dt>
          <dd>{tarifEntity.paidDate ? <TextFormat value={tarifEntity.paidDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="paid">Paid</span>
          </dt>
          <dd>{tarifEntity.paid ? 'true' : 'false'}</dd>
          <dt>Device</dt>
          <dd>{tarifEntity.device ? tarifEntity.device.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tarif" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tarif/${tarifEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TarifDetail;
