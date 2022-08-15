import styles from "./HomeIndex.module.css";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/esm/Button";
import { useEffect, useState } from "react";
import apiClient from "../api/api";
import { useSelector } from "react-redux";

const HomeIndex = () => {
  const [alarmText, setAlarmText] = useState("");
  const [mailText, setMailText] = useState("");
  const auth = useSelector((state) => state.auth);

  const alarmClickHandler = () => {
    console.log(alarmText);
    apiClient
      .post(
        "/customer-center/manager/alarm-total",
        {
          body: alarmText,
        },
        {
          headers: {
            "JWT-AUTHENTICATION": auth.accessToken,
          },
        },
      )
      .then(({ data }) => {
        alert(data.msg);
        setAlarmText("");
      })
      .catch((response) => {
        console.log(response);
      });
  };

  const mailClickHandler = () => {
    console.log(mailText);
    console.log(alarmText);
    apiClient
      .post(
        "/customer-center/manager/mail-total",
        {
          body: setMailText,
        },
        {
          headers: {
            "JWT-AUTHENTICATION": auth.accessToken,
          },
        },
      )
      .then(({ data }) => {
        alert(data.msg);
        setMailText("");
      })
      .catch((response) => {
        console.log(response);
      });
  };
  return (
    <>
      <div className={styles.home_index_frame}>
        <div className={styles.main_function_card}>
          <Form.Label>전체 알림 보내기</Form.Label>
          <div>
            <Form.Control
              type="text"
              value={alarmText}
              onChange={(e) => {
                setAlarmText(e.target.value);
              }}
            />
            <div className={styles.button_box}>
              <Button type="submit" onClick={alarmClickHandler}>
                Submit
              </Button>
            </div>
          </div>
        </div>
        <div className={styles.main_function_card}>
          <Form.Label>전체 메일 보내기</Form.Label>
          <div>
            <Form.Control
              type="text"
              value={mailText}
              onChange={(e) => {
                setMailText(e.target.value);
              }}
            />
            <div className={styles.button_box}>
              <Button type="submit" onClick={mailClickHandler}>
                Submit
              </Button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default HomeIndex;
