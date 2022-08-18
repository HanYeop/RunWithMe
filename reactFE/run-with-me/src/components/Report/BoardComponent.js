import Image from "react-bootstrap/esm/Image";
import styles from "./ReportDetail.module.css";

const BoardComponent = (props) => {
  console.log(props.board);

  const board = props.board?.crewBoardDto;
  const imageFileDto = props.board?.imageFileDto;
  return (
    <>
      {board ? (
        <div className={styles.board_fraem}>
          <div className={styles.board_crew_box}>크루 : {board.crewName}</div>
          <div className={styles.board_reg_box}>
            작성일 : {board.crewBoardRegTime}
          </div>
          {imageFileDto != null && imageFileDto.imgSeq != 0 && (
            <Image
              width={200}
              src={`http://i7d101.p.ssafy.io/api/images/${imageFileDto.imgSeq}`}
            />
          )}
          <div className={styles.board_content_box}>
            내용 : {board.crewBoardContent}
          </div>
        </div>
      ) : (
        <div>이미 존재하지 않는 글입니다.</div>
      )}
    </>
  );
};

export default BoardComponent;
