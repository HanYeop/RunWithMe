import styles from "./LoginLayout.module.css";
const LoginLayout = (props) => {
  return (
    <div className={styles.bg}>
      <div className={styles.center}>
        <div className={styles.login_title}>관리자 로그인</div>
        {props.children}
      </div>
    </div>
  );
};

export default LoginLayout;
