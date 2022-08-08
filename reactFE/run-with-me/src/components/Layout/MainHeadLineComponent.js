import React from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { useSelector } from "react-redux/es/exports";
import styles from "./MainHeadLineComponent.module.css";

const MainHeadLineComponent = () => {
  const pageName = useSelector((state) => state.site.pageName);
  return (
    <div>
      <h3>{pageName}</h3>
    </div>
  );
};
export default MainHeadLineComponent;
