import styles from "./WellComeComponent.module.css";
import { useSelector, useDispatch } from "react-redux/es/exports";
import { useNavigate } from "react-router-dom";
const WellComeComponent = (props) => {
  const email = useSelector((state) => state.auth.userEmail);
  const navigate = useNavigate();

  const logoutHandler = async (e) => {
    console.log(e);
    navigate("/");
  };

  return (
    <div>
      {email} <button onClick={logoutHandler}>[로그아웃]</button>
    </div>
  );
};

export default WellComeComponent;
