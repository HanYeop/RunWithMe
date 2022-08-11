import React from "react";
import { useSelector } from "react-redux/es/exports";

const MainHeadLineComponent = () => {
  const pageName = useSelector((state) => state.site.pageName);
  return (
    <div>
      <h3>{pageName}</h3>
    </div>
  );
};
export default MainHeadLineComponent;
