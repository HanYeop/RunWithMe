import Badge from "react-bootstrap/Badge";
import styles from "./Report.module.css";
const ReportListItem = (props) => {
  let {
    crewBoardSeq,
    regTime,
    reportContent,
    reportSeq,
    reportState,
    reporterNickName,
    reporterUserSeq,
    targetNincName,
    targetUserSeq,
  } = props.report;
  //<Badge bg="success">Success</Badge> <Badge bg="danger">Danger</Badge>{' '}
  //<Badge bg="warning" text="dark"></Badge>
  const waitToBanner = (reportSeq, reportState, reportStateChangeHandler) => {
    if (reportState == "WAITING") {
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
    } else if (reportState == "PROCESSING") {
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
    } else if (reportState == "COMPLETE") {
      return (
        <Badge
          bg="secondary"
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
      <td>{reportSeq}</td>
      <td>{targetNincName ? targetNincName : "Undefined"}</td>
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
