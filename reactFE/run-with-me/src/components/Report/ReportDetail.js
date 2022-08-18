import { useNavigate, useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { reportPageActions } from "../../store/slice/reportPaging";
import apiClient from "../../api/api";
import styles from "./ReportDetail.module.css";
import { useEffect, useState } from "react";
import Image from "react-bootstrap/Image";
import default_Img from "../../assets/molu.jpg";
import UserCard from "./UserCard";
import BoardComponent from "./BoardComponent";
import ReportDescComponent from "./ReportDescComponent";
import ControlComponet from "./ControlComponet";
import Form from "react-bootstrap/Form";

const ReportDetail = () => {
  const navigate = useNavigate();
  const params = useParams();
  const dispatch = useDispatch();
  const [alarmActive, setAlarmActive] = useState(false);
  const auth = useSelector((state) => {
    return state.auth;
  });
  const selectedReport = useSelector((state) => {
    return state.reportPage.selectedReport;
  });

  const reportDetail = useSelector((state) => {
    return state.reportPage.selectedReportDetail;
  });

  const { reportSeq } = params;

  useEffect(() => {
    apiClient
      .get(`/customer-center/manager/reports/${reportSeq}`, {
        headers: {
          "JWT-AUTHENTICATION": auth.accessToken,
        },
      })
      .then(({ data }) => {
        console.log(data);
        /* 
        const
         {
        board,
        report,
        reportImgSeq,
        reporter,
        target,
        targetImgSeq,
        } 
        */
        const reportDetail = data.data;
        //const {crewBoardDto,imageFileDto} = board;

        /* 
        console.log("board");
        console.log(board);
        console.log(crewBoardDto);
        console.log(imageFileDto);
        console.log("report");
        console.log(report);
        console.log("reportImgSeq");
        console.log(reportImgSeq);
        console.log("reporter");
        console.log(reporter);
*/
        console.log(reportDetail);
        dispatch(reportPageActions.setSelectedReports(reportDetail));
      })
      .catch((error) => {
        console.log(error);
      });
  }, [selectedReport]);

  const returnReporPage = () => {
    navigate("/report");
  };

  const alarmSendTarget = (e) => {
    if (reportDetail.report.targetUserSeq != null) {
      navigate(`/alarm/${reportDetail.report.targetUserSeq}`);
    } else {
    }
  };

  const alarmSendReporter = (e) => {
    // apiClient.post
    if (reportDetail.report.reporterUserSeq != null) {
      navigate(`/alarm/${reportDetail.report.reporterUserSeq}`);
    } else {
    }
  };

  const deleteCrewBoardHandler = (e) => {
    apiClient
      .delete(
        `/customer-center/manager/crew-boards/${reportDetail.report.crewBoardSeq}`,
        {
          headers: {
            "JWT-AUTHENTICATION": auth.accessToken,
          },
        },
      )
      .then(({ data }) => {
        alert(data.msg);
      });
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.reporter_box}>
          <div className={styles.role}>신고자</div>
          <div className={styles.user_desc}>
            <UserCard
              user={reportDetail?.reporter}
              imgSeq={reportDetail?.reportImgSeq}
            />
          </div>
        </div>
        <div className={styles.targeter_box}>
          <div className={styles.role}>신고 당한 사람</div>
          <div className={styles.user_desc}>
            <UserCard
              user={reportDetail.target}
              imgSeq={reportDetail.targetImgSeq}
            />
          </div>
        </div>

        <div className={styles.reported_board}>
          <BoardComponent board={reportDetail.board} />
          <div></div> <ReportDescComponent report={reportDetail.report} />
        </div>
        <div className={styles.contorller_box}>
          <ControlComponet
            alarmSendTarget={alarmSendTarget}
            alarmSendReporter={alarmSendReporter}
            deleteCrewBoardHandler={deleteCrewBoardHandler}
          />
        </div>
      </div>
    </>
  );
};

export default ReportDetail;
