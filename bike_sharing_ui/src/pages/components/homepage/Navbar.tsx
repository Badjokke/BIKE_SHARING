import React from "react";

import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

const Navbar: React.FC = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <Link className="navbar-brand" to="/">
        Home
      </Link>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav">
          <li className="nav-item">
            <Link className="nav-link" to="/login">
              Login
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/register">
              Register
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/ride">
              Start ride
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/service">
              Service bikes
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/map">
              Map
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/rides">
              Past rides
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;