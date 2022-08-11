import styles from "./Report.module.css";
const ReportCardItem = (props) => {
  const title = props.title;
  const value = props.value;
  return (
    <div className={styles["card-frame"]}>
      <div className={styles["card-left-image"]}>{props.children}</div>
      <div className={styles["card-right-desc"]}>
        <div className={styles["card-title"]}>{title}</div>
        <div className={styles["card-value"]}>{value}</div>
      </div>
    </div>
  );
};

export default ReportCardItem;
