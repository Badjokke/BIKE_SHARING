// src/components/Homepage.tsx
import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { useTranslation } from "react-i18next";
import { createConnection } from '../../socket/SocketClient';
import { SocketConnectionParams } from '../../socket/SocketClient';
import Stomp from 'stompjs';
const Homepage: React.FC = () => {
  const { t } = useTranslation();
  /*
  const subscribeCallback = (message: Stomp.Message) =>{
      console.log(message.body);
  }

  const errorCallback = (error:string | Stomp.Frame)=>{
    console.log(error);
  }
  const connectCallback = function(frame: Stomp.Frame | undefined) {
    // called back after the client is connected and authenticated to the STOMP server
    console.log("hello");
    const bikeId = 0;
    client.subscribe("/bike_ride/location/"+bikeId,subscribeCallback);
    client.publish({
      destination:"/bike_sharing/bike",
      body:JSON.stringify({bikeId})
    });
    /*setInterval(()=>{
      client.push("/bike_sharing/bike",{},JSON.stringify({bikeId:bikeId}));
  
    },2500);
  };*/

    return (
        <div>
          <div className="bg-primary text-white text-center p-5">
            <h1>{t("Welcome to BikeShare")}</h1>
            <p>{t("Your go-to platform for convenient and eco-friendly bike sharing!")}</p>
            <a href="/login" className="btn btn-light">{t("Get Started")}</a>
          </div>
    
          <Container className="mt-4">
            <Row>
              <Col>
                <h2>{t("How it works")}</h2>
                <p>
                  {t("BikeShare makes it easy for you to find and rent bikes. Simply sign up, locate a bike near you, unlock it with our app, and start your ride!")}
                </p>
              </Col>
              <Col>
                <h2>{t("Benefits")}</h2>
                <ul>
                  <li>{t("Convenient and accessible bike rental.")}</li>
                  <li>{t("Reduce your carbon footprint.")}</li>
                  <li>{t("Stay active and healthy.")}</li>
                  <li>{t("Explore your city in a new way.")}</li>
                </ul>
              </Col>
            </Row>
          </Container>
    
          <Container fluid className="bg-light mt-4 p-5 text-center">
            <h2>{t("Join BikeShare Today!")}</h2>
            <p>{t("Sign up now to start your biking adventure.")}</p>
            <a href="/register" className="btn btn-primary">{t("Sign Up")}</a>
          </Container>
        </div>
      );
};

export default Homepage;
