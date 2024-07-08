import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICustomCharges } from 'app/shared/model/custom-charges.model';
import { getEntity, updateEntity, createEntity, reset } from './custom-charges.reducer';

export const CustomChargesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customChargesEntity = useAppSelector(state => state.customCharges.entity);
  const loading = useAppSelector(state => state.customCharges.loading);
  const updating = useAppSelector(state => state.customCharges.updating);
  const updateSuccess = useAppSelector(state => state.customCharges.updateSuccess);

  const handleClose = () => {
    navigate('/custom-charges' + location.search);
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
      ...customChargesEntity,
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
          ...customChargesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="customSystemApp.customCharges.home.createOrEditLabel" data-cy="CustomChargesCreateUpdateHeading">
            Create or edit a Custom Charges
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="custom-charges-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Device Model" id="custom-charges-deviceModel" name="deviceModel" data-cy="deviceModel" type="text" />
              <ValidatedField label="Custom Fee" id="custom-charges-customFee" name="customFee" data-cy="customFee" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/custom-charges" replace color="info">
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

export default CustomChargesUpdate;
