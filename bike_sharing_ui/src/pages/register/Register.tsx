// src/components/RegisterForm.tsx
import React, { useState } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { registerUser } from '../../api/user_service_api/UserApiCaller';
import { saveUserInfo } from '../../token_manager/LocalStorageManager';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
const Register: React.FC = () => {
  const {t} = useTranslation();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [userType, setUserType] = useState('');
  const navigate = useNavigate();


  const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleUserTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setUserType(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const response = await registerUser(username,email,password,userType);
    const responseMessage = response.message;
    const data = response.data;
    if(data && data.token && data.role){
      saveUserInfo(email,data.token,data.role);
      navigate("/");
      //toast.success(responseMessage,{autoClose:1500})
    }
    else{
      //toast.error(responseMessage,{autoClose:1500});
    }
  };

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col md={6}>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formUsername">
              <Form.Label>{t("Username")}</Form.Label>
              <Form.Control
                type="text"
                placeholder={t("Enter your username")}
                value={username}
                onChange={handleUsernameChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formEmail">
              <Form.Label>{t("Email")}</Form.Label>
              <Form.Control
                type="email"
                placeholder={t("Enter your email")}
                value={email}
                onChange={handleEmailChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formPassword">
              <Form.Label>{t("Password")}</Form.Label>
              <Form.Control
                type="password"
                placeholder={t("Enter your password")}
                value={password}
                onChange={handlePasswordChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formUserType">
              <Form.Label>{t("User Type")}</Form.Label>
              <Form.Select value={userType} onChange={handleUserTypeChange} required>
                <option value="" disabled>
                  {t("Select User Type")}
                </option>
                <option value="serviceman">{t("Serviceman")}</option>
                <option value="regular">{t("User")}</option>
              </Form.Select>
            </Form.Group>

            <Button variant="primary" type="submit">
              {t("Register")}
            </Button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default Register;
