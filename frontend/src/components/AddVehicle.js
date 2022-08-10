import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import isEmpty from 'is-empty';
import { Alert } from 'react-bootstrap';
import { capitalizeFirst } from '../Utils';

function AddVehicle({ handleNewVehicle }) {
    const [show, setShow] = useState(false);
    const [make, setMake] = useState("")
    const [model, setModel] = useState("")
    const [create, setCreateStatus] = useState(false)
    const [formErrors, setFormErrors] = useState({})
    const [error, setError] = useState(null)
    const [validated, setValidated] = useState(false)
    const [isSuccess, setIsSuccess] = useState(false)

    const handleClose = () => {
        setShow(false)
        setMake(null)
        setModel(null)
        setCreateStatus(false)
        setValidated(false)
        setFormErrors({})
        setError(null)
        setIsSuccess(false)
    }

    const handleShow = () => setShow(true);

    const formSubmit = (event) => {
        const form = event.currentTarget;
        if(form.checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
            const allErrors = {}
            if(make === "") {
                allErrors.make = "Make is required"
            }
            if(model === "") {
                allErrors.model = "Model is required"
            }
            if(isEmpty(allErrors)) {
                setValidated(true)
            } else {
                setValidated(false)
            }
            setFormErrors(allErrors)
        } else {
            event.preventDefault()
            setFormErrors({})
            setCreateStatus(true)
            axios({
                method: 'post',
                url: process.env.REACT_APP_BACKEND_API+'/vehicle',
                data: {
                    make: make,
                    model: model
                }
            }).then(response => {
                setIsSuccess(true)
                setCreateStatus(false)
                handleNewVehicle(Math.random())
            }).catch(error => {
                console.error(error)
                setError("An unexpected error has occured")
                setIsSuccess(false)
                setCreateStatus(false)
            });
        }
    }

    return (
        <>
            <Button variant="primary" onClick={handleShow}>
                Add Vehicle
            </Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Vehicle</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form id='add-vehicle-form' onSubmit={formSubmit} noValidate validated={validated}>
                        <Form.Group className="mb-3">
                            <Form.Label htmlFor='vehicleMake'>Make</Form.Label>
                            <Form.Control
                                isInvalid={!!formErrors.make}
                                placeholder="Make"
                                id="vehicleMake" 
                                value={make}
                                onChange={
                                    event => {
                                        let value = capitalizeFirst(event.target.value)
                                        setMake(_ => value)
                                    }
                                }
                                required />
                            {formErrors.make && <Form.Control.Feedback type="invalid">
                                {formErrors.make}
                            </Form.Control.Feedback>}
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label htmlFor='vehicleModel'>Model</Form.Label>
                            <Form.Control
                                isInvalid={!!formErrors.model}
                                placeholder="Model"
                                id='vehicleModel'
                                value={model}
                                onChange={
                                    event => {
                                        let value = capitalizeFirst(event.target.value)
                                        setModel(_ => value)
                                    }
                                }
                                required />
                            {formErrors.model && <Form.Control.Feedback type="invalid">
                                {formErrors.model}
                            </Form.Control.Feedback>}
                        </Form.Group>
                    </Form>
                    {error && <Alert variant="danger" >
                        <Alert.Heading>Failed to add a vehicle.</Alert.Heading>
                        <p>{error}</p>
                    </Alert>}
                    {isSuccess && <Alert variant="success" >
                        <Alert.Heading>Success!</Alert.Heading>
                    </Alert>}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" type='submit' form='add-vehicle-form' disabled={create}>
                        Save
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
export default AddVehicle;

