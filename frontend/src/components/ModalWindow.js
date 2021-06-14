import React, {useState} from "react";
import { Button, Modal } from 'react-bootstrap';
import {useHistory} from "react-router-dom";


function ModalWindow(props) {
    const [show, setShow] = useState(false);
    const history = useHistory();

    const handleClose = () => {
        history.push('/')
        props.subscribe.unsubscribe()
    };

    return (
        <>
            <Modal
                show={props.show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>{props.value.header}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {props.value.message}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalWindow;