import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import styles from "./EventCreate.module.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { useState } from "react";
import apiClient from "../../api/api";
import { useSelector } from "react-redux/es/hooks/useSelector";
import axios from "axios";
import { useNavigate } from "react-router-dom";
const EventCreate = () => {
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [imgFile, setImgFile] = useState(null);
  const navigate = useNavigate();
  const auth = useSelector((state) => {
    return state.auth;
  });

  const buttonClickHandler = (e) => {
    e.preventDefault();

    console.log(startDate);
    console.log(endDate);
    if (startDate >= endDate) {
      alert("시작일은 종료일 이전이여야 합니다.");
      return;
    }
    if (!title) {
      alert("title을 입력하셔야 합니다.");
      return;
    }
    if (!content) {
      alert("내용을 입력하셔야합니다.");
      return;
    }
    //yyyy-MM-dd 00:00:00
    let startDateTrans = `${startDate.getFullYear()}-${
      startDate.getMonth() < 10
        ? "0" + startDate.getMonth()
        : startDate.getMonth()
    }-${
      startDate.getDay() < 10 ? "0" + startDate.getDay() : startDate.getDay()
    } 00:00:00`;
    //yyyy-MM-dd 23:59:59
    let endDateTrans = `${endDate.getFullYear()}-${
      endDate.getMonth() < 10 ? "0" + endDate.getMonth() : endDate.getMonth()
    }-${
      startDate.getDay() < 10 ? "0" + endDate.getDay() : endDate.getDay()
    } 23:59:59`;
    console.log(startDateTrans);
    console.log(endDateTrans);

    //apiClient

    const formData = new FormData();
    formData.append(
      "competitionDto",
      JSON.stringify({
        competitionName: title,
        competitionContent: content,
        competitionDateStart: startDateTrans,
        competitionDateEnd: endDateTrans,
      }),
    );

    formData.append("competitionImageFile", setImgFile);
    console.log(setImgFile);
    apiClient({
      method: "post",
      url: "/competition",
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data",
        "JWT-AUTHENTICATION": auth.accessToken,
      },
    })
      .then(({ data }) => {
        console.log(data);

        if (data.success) {
          alert(data.msg);
          navigate(-1);
        } else {
          alert(data.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fileInputChageHadler = (e) => {
    console.log(e.target.value);
    console.log(e.target.files[0]);
    if (e.target.files.length > 1) {
      alert("파일은 한개만 선택할 수 있습니다.");
      return;
    }
    setImgFile(e.target.files[0]);
  };
  return (
    <div className={styles.form}>
      <h2 className={styles.title}>이벤트 생성</h2>
      <Form>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>이벤트 이름</Form.Label>
          <Form.Control
            type="text"
            value={title}
            onChange={(e) => {
              setTitle(e.target.value);
            }}
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>이벤트 내용</Form.Label>
          <Form.Control
            cols={10}
            as="textarea"
            aria-label="With textarea"
            value={content}
            onChange={(e) => {
              setContent(e.target.value);
            }}
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="startDate">
          <Form.Label>이벤트 시작일</Form.Label>
          <DatePicker
            selected={startDate}
            onChange={(date) => setStartDate(date)}
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="endDate">
          <Form.Label>이벤트 종료일</Form.Label>
          <DatePicker
            selected={endDate}
            onChange={(date) => setEndDate(date)}
          />
        </Form.Group>
        <Form.Group controlId="formFile" className="mb-3">
          <Form.Label>상단에 노출될 파일택선택</Form.Label>
          <Form.Control
            multiple={false}
            type="file"
            onChange={fileInputChageHadler}
          />
        </Form.Group>

        <Button variant="primary" type="submit" onClick={buttonClickHandler}>
          생성
        </Button>
      </Form>
    </div>
  );
};

export default EventCreate;
