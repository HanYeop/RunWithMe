import styles from "./Report.module.css";
import ReportCardItem from "./ReportCardItem";
import ReportList from "./ReportList";
import {
  IoCheckmarkCircleOutline,
  IoStopCircleOutline,
  IoPlayCircleOutline,
} from "react-icons/io5";
const ReportMain = () => {
  //WAITING,PROCESSING,COMPLETE

  return (
    <>
      <div className={styles["report-main"]}>
        <ReportCardItem image="이미지" title="Waitting Report" value={1}>
          <IoStopCircleOutline color="orange" />
        </ReportCardItem>

        <ReportCardItem image="이미지" title="Processing Report" value={1}>
          <IoPlayCircleOutline color="grey" />
        </ReportCardItem>
        <ReportCardItem image="이미지" title="Complete Report" value={1}>
          <IoCheckmarkCircleOutline color="green" />
        </ReportCardItem>
        <div className={styles["list-frame"]}>
          <ReportList></ReportList>
        </div>
      </div>
    </>
  );
};

export default ReportMain;
