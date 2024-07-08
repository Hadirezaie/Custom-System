import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrader } from 'app/shared/model/trader.model';
import { getEntity, updateEntity, createEntity, reset } from './trader.reducer';

export const TraderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const traderEntity = useAppSelector(state => state.trader.entity);
  const loading = useAppSelector(state => state.trader.loading);
  const updating = useAppSelector(state => state.trader.updating);
  const updateSuccess = useAppSelector(state => state.trader.updateSuccess);

  const handleClose = () => {
    navigate('/trader' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...traderEntity,
      ...values,
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
          ...traderEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="customSystemApp.trader.home.createOrEditLabel" data-cy="TraderCreateUpdateHeading">
            Create or edit a Trader
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="trader-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="trader-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Nid Number" id="trader-nidNumber" name="nidNumber" data-cy="nidNumber" type="text" />
              <ValidatedField label="T IN" id="trader-tIN" name="tIN" data-cy="tIN" type="text" />
              <ValidatedField label="License Number" id="trader-licenseNumber" name="licenseNumber" data-cy="licenseNumber" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trader" replace color="info">
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

export default TraderUpdate;
