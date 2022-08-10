import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import isEmpty from 'is-empty';
import { Alert } from 'react-bootstrap';

function CreateChargeEventl({ vehicles, handleNewChargeEvent }) {
    const [formErrors, setFormErrors] = useState({})
    const [error, setError] = useState(null)
    const [show, setShow] = useState(false);
    const [startHour, setStartHour] = useState("")
    const [endHour, setEndHour] = useState("")
    const [selectedVehicleId, setSelectedVehicleId] = useState("")
    const [chargeSaveStatus, setChargeSaveStatus] = useState(false)
    const [validated, setValidated] = useState(false)
    const [isSuccess, setIsSuccess] = useState(false)

    const handleClose = () => {
        setShow(false)
        setStartHour("")
        setEndHour("")
        setSelectedVehicleId("")
        setChargeSaveStatus(false)
        setIsSuccess(false)
        setValidated(false)
    };

    const handleShow = () => setShow(true);

    const formSubmit = (event) => {
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault()
            event.stopPropagation()
            const allErrors = {}
            if (startHour === "" && endHour === "") {
                allErrors.startAndEndNotDefined = "Please specify value for Start or End hour. Both Can't be empty."
            }
            if (selectedVehicleId === "") {
                allErrors.selectedVehicleId = "Please select a vehicle";
            }
            if (isEmpty(allErrors)) {
                setValidated(true)
            } else {
                setValidated(false)
            }
            setFormErrors(allErrors)
        } else {
            event.preventDefault()
            const allErrors = {}
            if (startHour === "" && endHour !== "") {
                allErrors.startComesBeforeEnd = "Start year needs to specified before end year"
            }
            if (endHour <= startHour) {
                allErrors.endMoreThanStart = "End hour cannot be less than start hour"
            }
            if (startHour < 0 || startHour > 23) {
                allErrors.startHourOutOfRange = "Start hour begins at 0 and has to be less than or equal 23"
            }
            if (endHour < 0 || endHour > 23) {
                allErrors.endHourOutOfRange = "End hour begins at 0 and has to be less than or equal 23"
            }
            if (isEmpty(allErrors)) {
                setFormErrors({})
                setValidated(true)
                setChargeSaveStatus(true)
            } else {
                setFormErrors(allErrors)
                setValidated(false)
                setChargeSaveStatus(false)
                event.stopPropagation()
                return;
            }
            axios({
                method: 'post',
                url: process.env.REACT_APP_BACKEND_API + '/charge',
                data: {
                    vehicleId: selectedVehicleId,
                    startTime: startHour,
                    endTime: endHour
                }
            }).then(response => {
                setChargeSaveStatus(false)
                handleNewChargeEvent(Math.random())
                setIsSuccess(true)
            }).catch(error => {
                console.error(error)
                setError("An unexpected error has occured")
                setIsSuccess(false)
                setChargeSaveStatus(false)
            })
        }
    }

    return (
        <>
            <Button variant="primary" onClick={handleShow}>
                Charge Vehicle
            </Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Charge Vehicle</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form id='charge-vehicle-form' onSubmit={formSubmit} noValidate validated={validated}>
                        <Form.Group className="mb-3">
                            <Form.Label htmlFor='vehicles'>Vehicles</Form.Label>
                            <Form.Control
                                isInvalid={!!formErrors.selectedVehicleId}
                                id="vehicles"
                                as="select"
                                type="select"
                                onChange={(e) => setSelectedVehicleId(e.target.value)}
                                defaultValue={""}
                                required>
                                <option disabled value={""} key={-1}>Please select a vehicle</option>
                                {vehicles && vehicles.map(vehicle =>
                                    <option key={vehicle.id} value={vehicle.id}>{vehicle.make} - {vehicle.model}</option>
                                )}
                            </Form.Control>
                            {formErrors.selectedVehicleId &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.selectedVehicleId}
                                </Form.Control.Feedback>
                            }
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label htmlFor='startTime'>Start Hour</Form.Label>
                            <Form.Control isInvalid={!!formErrors.startAndEndNotDefined || !!formErrors.startHourOutOfRange || !!formErrors.startComesBeforeEnd}
                                placeholder="Enter Start Hour"
                                id="startTime"
                                defaultValue={startHour}
                                onChange={(e) => setStartHour(e.target.value)}
                                type="number"
                                required={!!formErrors.startAndEndNotDefined || !!formErrors.startHourOutOfRange || !!formErrors.startComesBeforeEnd} />
                            <Form.Text className="text-muted">
                                Please enter hour of the day in 24 hour format i.e 0-23
                            </Form.Text>
                            {formErrors.startAndEndNotDefined &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startAndEndNotDefined}
                                </Form.Control.Feedback>
                            }
                            {formErrors.startHourOutOfRange &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startHourOutOfRange}
                                </Form.Control.Feedback>
                            }
                            {formErrors.startComesBeforeEnd &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startComesBeforeEnd}
                                </Form.Control.Feedback>
                            }
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label htmlFor='endHour'>End Hour</Form.Label>
                            <Form.Control placeholder="Enter End Hour"
                                isInvalid={!!formErrors.startAndEndNotDefined || !!formErrors.endHourOutOfRange || !!formErrors.endMoreThanStart}
                                id='endHour'
                                defaultValue={endHour}
                                onChange={(e) => setEndHour(e.target.value)}
                                type="number"
                                required={!!formErrors.startAndEndNotDefined || !!formErrors.endHourOutOfRange || !!formErrors.endMoreThanStart} />
                            <Form.Text className="text-muted">
                                Please enter hour of the day in 24 hour format i.e 0-23
                            </Form.Text>
                            {formErrors.startAndEndNotDefined &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.startAndEndNotDefined}
                                </Form.Control.Feedback>
                            }
                            {formErrors.endHourOutOfRange &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.endHourOutOfRange}
                                </Form.Control.Feedback>
                            }
                            {formErrors.endMoreThanStart &&
                                <Form.Control.Feedback type="invalid">
                                    {formErrors.endMoreThanStart}
                                </Form.Control.Feedback>
                            }
                        </Form.Group>
                    </Form>
                    {error && <Alert variant="danger" >
                        <Alert.Heading>Charge Event failed.</Alert.Heading>
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
                    <Button variant="primary" type='submit' form='charge-vehicle-form' disabled={chargeSaveStatus}>
                        Save
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
export default CreateChargeEventl;

