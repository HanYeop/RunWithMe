import { useEffect } from "react";
import { useSelector } from "react-redux";
import apiClient from "../../api/api";

//	ReportStatus status;
/*

	@Min(value = 0,message = "페이지 아이템 갯수는 1이상이어야 합니다.")
	private int pageItemSize; //페이안에 있는 아이템 갯수
	@Min(value = 0,message ="요청 페이지는 1이상 이어야합니다.")
	private int currentPage;  //현재 요청페이지
	@Min(value = 0,message = "페이지 네비게이션 사이즈는 1이상이어야 합니다.")
	private int pageNaviSize;

*/
const ReportList = () => {
  const auth = useSelector((state) => {
    return state.auth;
  });
  const reportPageState = useSelector((state) => {
    return state.reportPage;
  });
  useEffect(() => {
    console.log("신고글 조회 api 실행");
    console.log(reportPageState);
    console.log(auth.accessToken);
    apiClient
      .get("/customer-center/manager/reports", {
        params: reportPageState,
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
          withCredential: true,
        },
      })
      .then((reponse) => {
        console.log(reponse);
      });
  }, [reportPageState]);
  return <div>report list</div>;
};

export default ReportList;
