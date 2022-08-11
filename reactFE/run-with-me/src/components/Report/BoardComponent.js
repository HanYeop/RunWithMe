import Image from "react-bootstrap/esm/Image";
const BoardComponent = (props) => {
  console.log(props.board);

  const board = props.board?.crewBoardDto;
  const imageFileDto = props.board?.imageFileDto;
  return (
    <>
      {board ? (
        <div>
          <div>크루 : {board.crewName}</div>
          {imageFileDto != null && imageFileDto.imgSeq != 0 && (
            <Image
              src={`http://localhost:8080/api/images/${imageFileDto.imgSeq}`}
            />
          )}
          <div>내용 : {board.crewBoardContent}</div>
          <div>작성일 : {board.crewBoardRegTime}</div>
        </div>
      ) : (
        <div>이미 존재하지 않는 글입니다.</div>
      )}
    </>
  );
};

export default BoardComponent;
