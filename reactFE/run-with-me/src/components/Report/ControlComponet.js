import styles from "./ReportDetail.module.css";

const ControlComponet = (props) => {
  return (
    <div className={styles.alarm_submit_button_box}>
      <button onClick={props.alarmSendTarget}>
        신고 당한 사람에게 알림 보내기
      </button>
      <button onClick={props.alarmSendReporter}>신고자에게 알림 보내기</button>
      <button onClick={props.deleteCrewBoardHandler}>
        해당 게시글 삭제하기
      </button>
    </div>
  );
};

export default ControlComponet;
