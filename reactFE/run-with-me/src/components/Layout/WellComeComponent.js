//import styles from "./WellComeComponent.module.css";
import { useSelector, useDispatch } from "react-redux/es/exports";
//import { useNavigate } from "react-router-dom";
import { authActions } from "../../store/slice/auth";
import Button from "react-bootstrap/Button";
const WellComeComponent = (props) => {
  const email = useSelector((state) => state.auth.userEmail);
  //const navigate = useNavigate();
  const dispatch = useDispatch();

  const logoutHandler = async (e) => {
    dispatch(authActions.clearAuth());
    console.log(e);

    // navigate("/login");
  };

  return (
    <div>
      {email} 님 환영합니다.
      <Button variant="primary" onClick={logoutHandler}>
        로그아웃
      </Button>
    </div>
  );
};

export default WellComeComponent;
