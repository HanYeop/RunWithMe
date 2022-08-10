import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { reportPageActions } from "../../store/slice/reportPaging";
import Table from "react-bootstrap/Table";
import apiClient from "../../api/api";
import ReportListItem from "./ReportListItem";
//	ReportStatus status;
/*

	@Min(value = 0,message = "페이지 아이템 갯수는 1이상이어야 합니다.")
	private int pageItemSize; //페이안에 있는 아이템 갯수
	@Min(value = 0,message ="요청 페이지는 1이상 이어야합니다.")
	private int currentPage;  //현재 요청페이지
	@Min(value = 0,message = "페이지 네비게이션 사이즈는 1이상이어야 합니다.")
	private int pageNaviSize;

*/
const ReportList = (props) => {
  const auth = useSelector((state) => {
    return state.auth;
  });

  const pageMeta = props.pageMeta;
  const reportsList = props.reports;

  const dispatch = useDispatch();
  useEffect(() => {
    console.log("신고글 조회 api 실행");
    console.log(pageMeta);

    apiClient
      .get("/customer-center/manager/reports", {
        params: { ...pageMeta },
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
        },
      })
      .then(({ data }) => {
        console.log(data);
        let success = data.success;
        if (success === true) {
          let reports = data.data.reports;
          console.log(reports);
          dispatch(reportPageActions.setReports(reports));
          props.setPageInfoHandler(data.data.pageinfo);
        } else {
          alert(data.msg);
        }
      });
  }, [pageMeta, props.forceReRender]);

  const reportStateChangeHandler = (e) => {
    const reportSeq = e.target.getAttribute("reportSeq");
    const reportState = e.target.getAttribute("reportState");
    console.log(reportSeq + reportState);
    let transitionState = reportState;
    if (reportState === "WAITING") {
      transitionState = "PROCESSING";
    } else if (reportState === "PROCESSING") {
      transitionState = "COMPLETE";
    } else if (reportState === "COMPLETE") {
      transitionState = "WAITING";
    }

    apiClient
      .put(
        `/customer-center/manager/reports/${reportSeq}`,
        {
          reportStatus: transitionState,
        },
        {
          headers: {
            "JWT-AUTHENTICATION": auth.accessToken,
          },
        },
      )
      .then((response) => {
        console.log("리스트 프레임 폴스 렌더");
        props.setForceReRender(props.forceReRender + 1);
      });
  };

  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Seq</th>
            <th>신고당한 사람</th>
            <th>신고당한 게시글 Seq</th>
            <th>신고 사유</th>
            <th>신고자</th>
            <th>신고 시간</th>
            <th>처리 상태</th>
          </tr>
        </thead>
        <tbody>
          {reportsList.length > 0 &&
            reportsList.map((item) => {
              return (
                <ReportListItem key={item.reportSeq}
                  report={item}
                  reportStateChangeHandler={reportStateChangeHandler}
                />
              );
            })}
        </tbody>
      </Table>
    </>
  );
};

export default ReportList;
