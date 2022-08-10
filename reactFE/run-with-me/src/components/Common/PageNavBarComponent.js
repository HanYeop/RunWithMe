import Pagination from "react-bootstrap/Pagination";

const PageNavBarComponent = (props) => {
  let pageList = [];

  let {
    currentPageIndex,
    endPageIndex,
    lastPageIndex,
    nextPageIndex,
    pageNavSize,
    prevPageIndex,
    startPageIndex,
  } = props.pageinfo;

  console.log(props);
  for (let i = startPageIndex; i <= endPageIndex; i++) {
    pageList.push(i);
  }

  return (
    <div>
      <Pagination onClick={props.pageNaviClickHandler}>
        <Pagination.First index={1} />
        <Pagination.Prev index={prevPageIndex} />
        {pageList.map((item) => {
          if (item == currentPageIndex) {
            return (
              <Pagination.Item index={item} active>
                {item}
              </Pagination.Item>
            );
          }
          return <Pagination.Item index={item}>{item}</Pagination.Item>;
        })}
        <Pagination.Next index={nextPageIndex} />
        <Pagination.Last index={lastPageIndex} />
      </Pagination>
    </div>
  );
};
export default PageNavBarComponent;
