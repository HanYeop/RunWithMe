import styles from "./ReportDetail.module.css";
import Image from "react-bootstrap/esm/Image";

import default_Img from "../../assets/molu.jpg";
const UserCard = (props) => {
  return (
    <>
      <div className={styles.user_desc}>
        <div className={styles.user_img}>
          {props.imgSeq ? (
            <Image
              roundedCircle
              width={80}
              height={80}
              src={`http://localhost:8080/api/images/${props.imgSeq}`}
            />
          ) : (
            <Image roundedCircle width={80} height={80} src={default_Img} />
          )}
        </div>
        <div>
          <table>
            <tr>
              <td>Name</td>
              <td>{props.user?.nickName ? props.user?.nickName : "미표기"}</td>
            </tr>
            <tr>
              <td>region</td>
              <td>{props.user?.region ? props.user?.region : "미표기"}</td>
            </tr>
            <tr>
              <td>gender</td>
              <td>{props.user?.gender ? props.user?.gender : "미표기"}</td>
            </tr>
            <tr>
              <td>birthYear</td>
              <td>
                {props.user?.birthYear ? props.user?.birthYear : "미표기"}
              </td>
            </tr>
          </table>
        </div>
      </div>
    </>
  );
};

export default UserCard;
