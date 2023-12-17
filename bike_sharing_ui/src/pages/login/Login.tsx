import React, { useState } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGoogle } from '@fortawesome/free-brands-svg-icons';
import { loginUser } from '../../api/user_service_api/UserApiCaller';
import { saveUserInfo } from '../../token_manager/LocalStorageManager';
import { useNavigate } from 'react-router-dom';
const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const response = await loginUser(email,password);
    const data = response.data;

    if(data && data.token && data.role){
      saveUserInfo(email,data.token,data.role);
      if(response.redirectTo)
        navigate(response.redirectTo); 
    }


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
              href={`${process.env.REACT_APP_USER_SERVICE_URL}/oauth2/authorization/google`}
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
