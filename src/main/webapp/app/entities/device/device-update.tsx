import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrader } from 'app/shared/model/trader.model';
import { getEntities as getTraders } from 'app/entities/trader/trader.reducer';
import { IDevice } from 'app/shared/model/device.model';
import { getEntity, updateEntity, createEntity, reset } from './device.reducer';

export const DeviceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const traders = useAppSelector(state => state.trader.entities);
  const deviceEntity = useAppSelector(state => state.device.entity);
  const loading = useAppSelector(state => state.device.loading);
  const updating = useAppSelector(state => state.device.updating);
  const updateSuccess = useAppSelector(state => state.device.updateSuccess);

  const handleClose = () => {
    navigate('/device' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTraders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...deviceEntity,
      ...values,
      trader: traders.find(it => it.id.toString() === values.trader.toString()),
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
          ...deviceEntity,
          trader: deviceEntity?.trader?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="customSystemApp.device.home.createOrEditLabel" data-cy="DeviceCreateUpdateHeading">
            Create or edit a Device
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="device-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Serial Number"
                id="device-serialNumber"
                name="serialNumber"
                data-cy="serialNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Model Number"
                id="device-modelNumber"
                name="modelNumber"
                data-cy="modelNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="device-trader" name="trader" data-cy="trader" label="Trader" type="select">
                <option value="" key="0" />
                {traders
                  ? traders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/device" replace color="info">
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

export default DeviceUpdate;
