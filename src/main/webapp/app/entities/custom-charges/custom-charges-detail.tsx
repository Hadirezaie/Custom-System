import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './custom-charges.reducer';

export const CustomChargesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customChargesEntity = useAppSelector(state => state.customCharges.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customChargesDetailsHeading">Custom Charges</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{customChargesEntity.id}</dd>
          <dt>
            <span id="deviceModel">Device Model</span>
          </dt>
          <dd>{customChargesEntity.deviceModel}</dd>
          <dt>
            <span id="customFee">Custom Fee</span>
          </dt>
          <dd>{customChargesEntity.customFee}</dd>
        </dl>
        <Button tag={Link} to="/custom-charges" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/custom-charges/${customChargesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomChargesDetail;
