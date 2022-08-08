import MainHeadLineComponent from "../components/Layout/MainHeadLineComponent";
import WellComeComponent from "../components/Layout/WellComeComponent";
import ReportMain from "../components/Report/ReportMain";
import SideBar from "../components/Layout/SideBar";

import { useState } from "react";

import styles from "./HomePage.module.css";

const HomeMain = () => {
  const [sideBarVisible, setsideBarVisible] = useState(true);

  const [toggle, setToggle] = useState(false);

  return (
    <div className={styles.home}>
      <div className={styles.side}>{!toggle && <SideBar></SideBar>}</div>
      <div className={styles.main}>
        <div className={styles["main-upside"]}>
          <MainHeadLineComponent />
          <WellComeComponent />
        </div>
        <div>
          <ReportMain />
        </div>
        <div></div>
      </div>
    </div>
  );
};

export default HomeMain;
