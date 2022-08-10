import {useNavigate,useParams} from "react-router-dom" 
import { useSelector,useDispatch } from "react-redux";
import { reportPageActions } from "../../store/slice/reportPaging";
import apiClient from "../../api/api";
const ReportDetail =()=>{

    const navigate = useNavigate();
    const params = useParams();
    const dispatch = useDispatch();
    const auth = useSelector((state)=>{return state.auth});

    const reportDetail = useSelector((state)=>{return state.reportPage.selectedReportDetail});

    const {reportSeq} = params;

    
    apiClient.get(`/customer-center/manager/reports/${reportSeq}`,{
        headers:{
            "JWT-AUTHENTICATION":auth.accessToken,
        }
    })
    .then(({data})=>{

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
        dispatch(reportPageActions.setSelectedReports(reportDetail));
    }).catch(error=>{
        console.log(error);
    });


    const returnReporPage = ()=>{
        navigate("/report");
    }

    return (<>
    <h1>리포트 디테일</h1>
    </>)
}

 export default ReportDetail;