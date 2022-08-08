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

const SideBar = (props) => {
  const [toggled, setToggled] = useState(false);

  const handleToggleSidebar = () => {
    setToggled(!toggled);
  };

  return (
    <ProSidebar
      image={true ? "leftArrow" : false}
      breakPoint="md"
      onToggle={handleToggleSidebar}
    >
      <SidebarHeader>
        <div className={styles["side-bar-header"]}>
          <h2>Run With Me</h2>
          <img src={Molu}></img>
        </div>
      </SidebarHeader>
      <SidebarContent>
        <Menu iconShape="square">
          <MenuItem>
            <h2>신고글</h2>
          </MenuItem>
          <MenuItem>Dashboard</MenuItem>
          <MenuItem>Dashboard</MenuItem>
          <MenuItem>Dashboard</MenuItem>
          <SubMenu title="Components">
            <MenuItem>Component 1</MenuItem>
            <MenuItem>Component 2</MenuItem>
          </SubMenu>
        </Menu>
      </SidebarContent>

      <SidebarFooter>
        <div className={styles.footer}>qwe</div>
      </SidebarFooter>
    </ProSidebar>
  );
};

export default SideBar;
