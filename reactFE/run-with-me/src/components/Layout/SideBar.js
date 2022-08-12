import { useState } from "react";

import {
  ProSidebar,
  Menu,
  MenuItem,
  SubMenu,
  SidebarHeader,
  SidebarContent,
  SidebarFooter,
} from "react-pro-sidebar";
import "react-pro-sidebar/dist/css/styles.css";
import styles from "./SideBar.module.css";
import Molu from "../../assets/molu.jpg";
import { Link } from "react-router-dom";
import runWithMeBG from "../../assets/runwithme.png";

const SideBar = (props) => {
  const [toggled, setToggled] = useState(false);

  const handleToggleSidebar = () => {
    setToggled(!toggled);
  };

  return (
    <ProSidebar
      image={runWithMeBG}
      breakPoint="md"
      onToggle={handleToggleSidebar}
    >
      <SidebarHeader>
        <div className={styles["side-bar-header"]}>
          <h2>Run With Me</h2>
          <img src={Molu} alt="로고"></img>
        </div>
      </SidebarHeader>
      <SidebarContent>
        <Menu iconShape="square">
          <MenuItem>
            <Link to="/">
              <h2>Alarm</h2>
            </Link>
          </MenuItem>
          <MenuItem>
            <Link to="/report">
              <h2>Report</h2>
            </Link>
          </MenuItem>
          <SubMenu title="Components">
            <MenuItem>Component 1</MenuItem>
            <MenuItem>Component 2</MenuItem>
          </SubMenu>
        </Menu>
      </SidebarContent>

      <SidebarFooter>
        <div className={styles.footer}>start_so@naver.com</div>
      </SidebarFooter>
    </ProSidebar>
  );
};

export default SideBar;
