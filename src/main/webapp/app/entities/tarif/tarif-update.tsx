import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITarif } from 'app/shared/model/tarif.model';
import { getEntity, updateEntity, createEntity, reset } from './tarif.reducer';

export const TarifUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tarifEntity = useAppSelector(state => state.tarif.entity);
  const loading = useAppSelector(state => state.tarif.loading);
  const updating = useAppSelector(state => state.tarif.updating);
  const updateSuccess = useAppSelector(state => state.tarif.updateSuccess);

  const handleClose = () => {
    navigate('/tarif' + location.search);
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
      ...tarifEntity,
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
          ...tarifEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="customSystemApp.tarif.home.createOrEditLabel" data-cy="TarifCreateUpdateHeading">
            Create or edit a Tarif
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="tarif-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Amount" id="tarif-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField label="Paid Date" id="tarif-paidDate" name="paidDate" data-cy="paidDate" type="date" />
              <ValidatedField label="Paid" id="tarif-paid" name="paid" data-cy="paid" check type="checkbox" />
              <ValidatedField
                label="Number Of Device"
                id="tarif-numberOfDevice"
                name="numberOfDevice"
                data-cy="numberOfDevice"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tarif" replace color="info">
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

export default TarifUpdate;
