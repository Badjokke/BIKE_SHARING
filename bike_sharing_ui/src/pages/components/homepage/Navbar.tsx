import React from "react";
import { useEffect,useState } from "react";
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useTranslation } from "react-i18next";
import LanguageSwitcher from "./LanguageSwitcher";
import { getUserInfo,deleteUserInfo } from "../../../token_manager/LocalStorageManager";
import { useNavigate } from "react-router-dom";

const Navbar: React.FC = () => {
  const { t } = useTranslation();
  const [email,setEmail] = useState<string|null>();
  const [role,setRole] = useState<string|null>();
  const navigate = useNavigate();
  useEffect(() => {
    // Add event listener to handle click on the collapsed button
    const navbarToggler = document.querySelector('.navbar-toggler');
    const navbarNav = document.querySelector('#navbarNav');


    const handleNavbarToggle = () => {
      navbarNav?.classList.toggle('show');
    };
    const handleUserInfoChange= () =>{
      const {email,role} = getUserInfo();
      setEmail(email);
      setRole(role);
    }
    navbarToggler?.addEventListener('click', handleNavbarToggle);

    document.addEventListener('userinfochange',handleUserInfoChange);




    // Cleanup the event listener when the component is unmounted
    return () => {
      navbarToggler?.removeEventListener('click', handleNavbarToggle);
      document.removeEventListener("userinfochange",handleUserInfoChange);
    };
  }, []);
  

  const handleLogout = () => {
    deleteUserInfo();
    navigate("/");
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <Link className="navbar-brand" to="/">
        {t("Home")}
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
              {t("Login")}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/register">
              {t("Register")}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/ride">
              {t("Start ride")}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/service">
              {t("Service bikes")}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/map">
              {t("Map")}
            </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/rides">
              {t("Past rides")}
            </Link>
          </li>
        </ul>
        
        {/* Display user information when logged in */}
        {email && role && (
          <div className="ml-auto text-right">
            <span className="navbar-text mr-3 text-dark">
              {t("Logged in as")} {email}
            </span>
            <span className="badge badge-primary">
              <span className="text-dark">{t("Role")}: {role}</span>
            </span>
            <button className="btn btn-link" onClick={handleLogout}>
              {t("Logout")}
            </button>
          </div>
        )}
      <LanguageSwitcher/>

      </div>

    </nav>
  );
};

export default Navbar;