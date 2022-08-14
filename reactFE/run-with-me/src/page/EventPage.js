import ListGroup from "react-bootstrap/ListGroup";
import EventList from "../components/Event/EventList";
import CurrentEvent from "../components/Event/CurrentEvent";
import Button from "react-bootstrap/esm/Button";
import styles from "./EventPage.module.css";
import { useSelector, useDispatch } from "react-redux/es/exports";
import { Route, Routes, useNavigate } from "react-router-dom";
import EventCreate from "../components/Event/EventCreate";
import { useEffect } from "react";
import { evnetActions } from "../store/slice/event";
import apiClient from "../api/api";

const EventPage = () => {
  const navigate = useNavigate();
  const auth = useSelector((state) => state.auth);
  const event = useSelector((state) => state.event);
  const dispatch = useDispatch();
  const currentEvent = event.currentEvent;
  const prevEvent = event.prevEvent;
  const endEvent = event.endEvent;

  useEffect(() => {
    apiClient
      .get("/competition/inprogress", {
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
        },
      })
      .then(({ data }) => {
        console.log("현재 진행중 이벤트");
        console.log(data);
        dispatch(evnetActions.setCurrentEvent(data.data));
      })
      .catch((error) => {
        console.log(error);
      });

    apiClient
      .get("/competition/beforestart", {
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
        },
      })
      .then(({ data }) => {
        console.log("시작전 이벤트");
        console.log(data);
        dispatch(evnetActions.setPrevEvent(data.data));
      })
      .catch((error) => {
        console.log(error);
      });

    apiClient
      .get("/competition/afterend", {
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
        },
      })
      .then(({ data }) => {
        console.log("끝난 이벤트");
        console.log(data);
        dispatch(evnetActions.setEndEvent(data.data));
      })
      .catch((error) => {
        console.log(error);
      });
  }, [navigate]);

  const eventAddButtonClickHandler = () => {
    navigate("/event/create");
  };

  return (
    <Routes>
      <Route path="/create" element={<EventCreate />} />
      <Route
        index
        element={
          <div>
            <div>
              <CurrentEvent event={currentEvent} />
            </div>
            <div className={styles.control}>
              <Button variant="primary" onClick={eventAddButtonClickHandler}>
                이벤트 추가하기
              </Button>
            </div>
            <div className={styles.horizontal_area}>
              <div>
                <h3>시작전 이벤트</h3>
                <EventList eventList={prevEvent}></EventList>
              </div>
              <div>
                <h3>종료된 이벤트</h3>
                <EventList eventList={endEvent}></EventList>
              </div>
            </div>
          </div>
        }
      ></Route>
    </Routes>
  );
};

export default EventPage;
