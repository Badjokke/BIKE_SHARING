// src/components/Homepage.tsx
import React from 'react';
import { Container, Row, Col,Button } from 'react-bootstrap';

const Homepage: React.FC = () => {
    return (
        <div>
          <div className="bg-primary text-white text-center p-5">
            <h1>Welcome to BikeShare</h1>
            <p>Your go-to platform for convenient and eco-friendly bike sharing!</p>
            <a href="/login" className="btn btn-light">Get Started</a>
          </div>
    
          <Container className="mt-4">
            <Row>
              <Col>
                <h2>How it Works</h2>
                <p>
                  BikeShare makes it easy for you to find and rent bikes. Simply sign up, locate a bike near you, unlock it
                  with our app, and start your ride!
                </p>
              </Col>
              <Col>
                <h2>Benefits</h2>
                <ul>
                  <li>Convenient and accessible bike rental.</li>
                  <li>Reduce your carbon footprint.</li>
                  <li>Stay active and healthy.</li>
                  <li>Explore your city in a new way.</li>
                </ul>
              </Col>
            </Row>
          </Container>
    
          <Container fluid className="bg-light mt-4 p-5 text-center">
            <h2>Join BikeShare Today!</h2>
            <p>Sign up now to start your biking adventure.</p>
            <a href="/register" className="btn btn-primary">Sign Up</a>
          </Container>
        </div>
      );
};

export default Homepage;
