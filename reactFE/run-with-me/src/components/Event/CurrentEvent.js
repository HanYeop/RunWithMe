import styles from "./CurrentEvent.module.css";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/esm/Image";
const CurrentEvent = (props) => {
  const competiton = props.event?.competitionDto;
  return (
    <Container fluid="md">
      <div className={styles.current_event_frame}>
        <div className={styles.head_title}>현재 진행 중인 이벤트</div>
        {competiton != null ? (
          <div className={styles.current_event}>
            <div className={styles.current_event_img_box}>
              <Image
                src={`http://i7d101.p.ssafy.io/api/images/${props.event?.competitionImageFileDto?.imgSeq}`}
                width={600}
              />
            </div>
            <table className={styles.table_desc}>
              <tr>
                <td>Title</td>
                <td>
                  <div className={styles.current_event_title}>
                    {competiton.competitionName}
                  </div>
                </td>
              </tr>
              <tr>
                <td>내용</td>
                <td>
                  <div className={styles.current_event_conent}>
                    {competiton.competitionContent}
                  </div>
                </td>
              </tr>
              <tr>
                <td>시작일</td>
                <td>
                  <div className={styles.current_event_date}>
                    {competiton.competitionDateStart}
                  </div>
                </td>
              </tr>
              <tr>
                <td>종료일</td>
                <td>
                  <div className={styles.current_event_date}>
                    {competiton.competitionDateEnd}
                  </div>
                </td>
              </tr>
            </table>
          </div>
        ) : (
          <div>현재 진행중인 이벤트가 없습니다.</div>
        )}
      </div>
    </Container>
  );
};

export default CurrentEvent;
