import React, { useState } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGoogle } from '@fortawesome/free-brands-svg-icons';
const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // You can add your authentication logic here
    console.log('Email:', email, 'Password:', password);
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col md={6}>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={handleEmailChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={handlePasswordChange}
                required
              />
            </Form.Group>

            <Button variant="primary" type="submit">
              Login
            </Button>
            <a
              href="http://localhost:8080/oauth2/authorization/google"
              className="btn btn-google text-white float-right"
              style={{ backgroundColor: '#4285F4' }}
            >
              <FontAwesomeIcon icon={faGoogle} className="mr-2" />
              Login with Google
            </a>
          </Form>
        </Col>
      </Row>
    </Container>
  );
};
export default Login;
