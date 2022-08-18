import Badge from "react-bootstrap/Badge";
import styles from "./Report.module.css";
import { useNavigate } from "react-router-dom";
const ReportListItem = (props) => {
  const {
    crewBoardSeq,
    regTime,
    reportContent,
    reportSeq,
    reportState,
    reporterNickName,
    reporterUserSeq,
    targetNickName,
    targetUserSeq,
  } = props.report;
  const navigate = useNavigate();
  //<Badge bg="success">Success</Badge> <Badge bg="danger">Danger</Badge>{' '}
  //<Badge bg="warning" text="dark"></Badge>

  const reportListClickHandler = () => {
    navigate(`/report/${reportSeq}`);
  };

  const waitToBanner = (reportSeq, reportState, reportStateChangeHandler) => {
    if (reportState === "WAITING") {
      return (
        <Badge
          bg="warning"
          reportSeq={reportSeq}
          reportState={reportState}
          className={styles["none-visible-button"]}
          onClick={reportStateChangeHandler}
        >
          {reportState}
        </Badge>
      );
    } else if (reportState === "PROCESSING") {
      return (
        <Badge
          bg="danger"
          reportSeq={reportSeq}
          reportState={reportState}
          className={styles["none-visible-button"]}
          onClick={reportStateChangeHandler}
        >
          {reportState}
        </Badge>
      );
    } else if (reportState === "COMPLETE") {
      return (
        <Badge
          bg="success"
          reportSeq={reportSeq}
          reportState={reportState}
          className={styles["none-visible-button"]}
          text="dark"
          onClick={reportStateChangeHandler}
        >
          {reportState}
        </Badge>
      );
    }
  };
  return (
    <tr>
      <td onClick={reportListClickHandler}>{reportSeq}</td>
      <td>{targetNickName ? targetNickName : "Undefined"}</td>
      <td>{crewBoardSeq}</td>
      <td>{reportContent}</td>
      <td>{reporterNickName}</td>
      <td>{regTime}</td>
      <td>
        {waitToBanner(reportSeq, reportState, props.reportStateChangeHandler)}
      </td>
    </tr>
  );
};
export default ReportListItem;
