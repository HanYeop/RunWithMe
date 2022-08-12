import styles from "./ReportDetail.module.css";

const ReportDescComponent = (props) => {
  return (
    <div className={styles.report_reason}>
      신고사유 : {props.report?.reportContent}
    </div>
  );
};

export default ReportDescComponent;
