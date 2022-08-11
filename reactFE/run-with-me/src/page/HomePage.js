import MainHeadLineComponent from "../components/Layout/MainHeadLineComponent";
import WellComeComponent from "../components/Layout/WellComeComponent";
import ReportMain from "../components/Report/ReportMain";
import SideBar from "../components/Layout/SideBar";
import ReportDetail from "../components/Report/ReportDetail";
import { useEffect, useState } from "react";

import styles from "./HomePage.module.css";
import { useSelector } from "react-redux";
import { Route, Routes, useNavigate } from "react-router-dom";
import AlarmPage from "./AlarmPage";
import HomeIndex from "./HomeIndex";
const HomeMain = () => {
  //const [sideBarVisible, setsideBarVisible] = useState(true);
  const [toggle] = useState(false);

  const auth = useSelector((state) => state.auth);

  const navigate = useNavigate();

  useEffect(() => {
    if (auth.accessToken === null || auth.accessToken === "") {
      navigate("/login");
    }
  }, [auth, navigate]); //auth에 변화가 있을 떄

  return (
    <div className={styles.home}>
      <div className={styles.side}>{!toggle && <SideBar></SideBar>}</div>
      <div className={styles.main}>
        <div className={styles["main-upside"]}>
          <MainHeadLineComponent />
          <WellComeComponent />
        </div>

        <div className={styles["main-article"]}>
          <Routes>
            <Route index element={<HomeIndex />} />
            <Route path="report" element={<ReportMain />} />
            <Route path="report/:reportSeq" element={<ReportDetail />} />
            <Route path="/alarm/:userSeq" element={<AlarmPage />} />
            <Route path="other" element={<h1>다른 페이지</h1>} />
          </Routes>
        </div>
        <div></div>
      </div>
    </div>
  );
};

export default HomeMain;
