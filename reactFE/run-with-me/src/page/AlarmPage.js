import { useSelector, useDispatch } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import apiClient from "../api/api";
import { alarmSliceActions } from "../store/slice/alarm";
import Form from "react-bootstrap/Form";
import styles from "./AlarmPage.module.css";

const AlarmPage = (props) => {
  const params = useParams();
  const auth = useSelector((state) => state.auth);
  const alarmState = useSelector((state) => state.alarm);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  if (params.userSeq == "null") {
    console.log(params.userSeq);
    alert("더이상 유저가 존재하지 않습니다.");
    navigate(-1);
  }

  const alarmSendHandler = (e) => {
    console.log("알람보내기");
    console.log(alarmState.title);
    apiClient
      .post(
        "/customer-center/manager/alarm",
        {
          userSeq: params.userSeq,
          title: alarmState.title,
          body: alarmState.text,
        },
        {
          headers: {
            "JWT-AUTHENTICATION": auth.accessToken,
          },
        },
      )
      .then((response) => {
        console.log(response);
        alert(response.data.msg);
      });
  };

  const titleChageHandler = (event) => {
    dispatch(alarmSliceActions.setTitle(event.target.value));
  };

  const textChageHandler = (event) => {
    dispatch(alarmSliceActions.setText(event.target.value));
  };

  return (
    <div className={styles.alarm_form_frame}>
      <div>알림 송신 유저SEQ : {params.userSeq}</div>
      <Form>
        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
          <Form.Label>제목</Form.Label>
          <Form.Control type="text" onChange={titleChageHandler} />
        </Form.Group>
        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
          <Form.Label>내용</Form.Label>
          <Form.Control as="textarea" rows={3} onChange={textChageHandler} />
        </Form.Group>
      </Form>
      <button onClick={alarmSendHandler}>전송</button>
      <button
        onClick={() => {
          navigate(-1);
        }}
      >
        돌아가기
      </button>
    </div>
  );
};

export default AlarmPage;
