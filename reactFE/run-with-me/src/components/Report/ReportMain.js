import styles from "./Report.module.css";
import ReportCardItem from "./ReportCardItem";
import ReportList from "./ReportList";
import PageNavBarComponent from "../Common/PageNavBarComponent";

import apiClient from "../../api/api";
import {
  IoCheckmarkCircleOutline,
  IoStopCircleOutline,
  IoPlayCircleOutline,
} from "react-icons/io5";
import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { reportPageActions } from "../../store/slice/reportPaging";

const ReportMain = () => {

  const [pageInfo, setPageInfo] = useState(null);
  const [forceReRender, setForceReRender] = useState(1);
const auth = useSelector((state)=>state.auth);
const statusCount = useSelector((state)=>state.reportPage.reportStatus);

  const reportPage = useSelector((state) => {
    return state.reportPage;
  });

  const dispatch = useDispatch();
  useEffect(
()=>{

  console.log("상태별 갯수 가져오기");
  apiClient.get("/customer-center/manager/reports-state-count",
  {
    headers:{
      "JWT-AUTHENTICATION": auth.accessToken,
    }
  }).then(({data})=>{
    console.log(data);
    const statusCount = data.data;
    dispatch(reportPageActions.setStatusCount(statusCount));
  }).catch((error)=>{
    console.log(error);
  })

},[auth.accessToken,forceReRender]

  )





  //WAITING,PROCESSING,COMPLETE
  const setPageInfoHandler = (pageinfo) => {
    console.log("report frame setPAgeInfoHandler" + pageinfo);
    setPageInfo(pageinfo);
  };

  const pageNaviClickHandler = (e) => {
    let clickedComponent = e.target;
    let index = clickedComponent.getAttribute("index");
    if (index == null) {
      return;
    }
    console.log(index);
    let { status, pageItemSize, pageNaviSize } =
      reportPage.pageMeta;
    let temp = {
      status,
      pageItemSize,
      currentPage: index,
      pageNaviSize,
    };
    dispatch(reportPageActions.setPageStatus(temp));
  };
  return (
    <>
      <div className={styles["report-main"]}>
        <ReportCardItem image="이미지" title="Waitting Report" value={statusCount.WAITING}>
          <IoStopCircleOutline color="orange" />
        </ReportCardItem>

        <ReportCardItem image="이미지" title="Processing Report" value={statusCount.PROCESSING}>
          <IoPlayCircleOutline color="grey" />
        </ReportCardItem>
        <ReportCardItem image="이미지" title="Complete Report" value={statusCount.COMPLETE}>
          <IoCheckmarkCircleOutline color="green" />
        </ReportCardItem>
        <div className={styles["list-frame"]}>
          <ReportList
            forceReRender={forceReRender}
            setForceReRender={setForceReRender}
            setPageInfoHandler={setPageInfoHandler}
            pageMeta={reportPage.pageMeta}
            reports={reportPage.reports}
          ></ReportList>
        </div>
        <div className={styles["list-page-navigation"]}>
          {pageInfo && (
            <PageNavBarComponent
              pageinfo={pageInfo}
              pageNaviClickHandler={pageNaviClickHandler}
            />
          )}
        </div>
      </div>
    </>
  );
};

export default ReportMain;
