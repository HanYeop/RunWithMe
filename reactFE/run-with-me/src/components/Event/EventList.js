import Table from "react-bootstrap/esm/Table";
const EventList = (props) => {
  const eventList = props.eventList;
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>title</th>
          <th>content</th>
          <th>시작일</th>
          <th>종료일</th>
        </tr>
      </thead>
      <tbody>
        {eventList == null || eventList.length == 0 ? (
          <tr>
            <td colSpan={4}>비어있습니다.</td>
          </tr>
        ) : (
          <>
            {eventList.map((item) => {
              return (
                <tr key={item.competitionDto.competitionSeq}>
                  <td>{item.competitionDto.competitionName}</td>
                  <td>{item.competitionDto.competitionContent}</td>
                  <td>
                    {item.competitionDto.competitionDateStart.substring(0, 11)}
                  </td>
                  <td>
                    {item.competitionDto.competitionDateEnd.substring(0, 11)}
                  </td>
                </tr>
              );
            })}
          </>
        )}
      </tbody>
    </Table>
  );
};

export default EventList;
