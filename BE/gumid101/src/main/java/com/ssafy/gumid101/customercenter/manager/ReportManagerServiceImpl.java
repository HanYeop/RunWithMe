package com.ssafy.gumid101.customercenter.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ssafy.gumid101.crew.activity.CrewActivityBoardRepository;
import com.ssafy.gumid101.customercenter.ReportRepository;
import com.ssafy.gumid101.customercenter.ReportStatus;
import com.ssafy.gumid101.customexception.FCMTokenUnValidException;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.firebase.FcmMessage.Message;
import com.ssafy.gumid101.firebase.FcmMessage;
import com.ssafy.gumid101.firebase.FirebaseMessageUtil;
import com.ssafy.gumid101.req.AlarmReqDto;
import com.ssafy.gumid101.req.ReportSelectReqDto;
import com.ssafy.gumid101.res.CrewBoardFileDto;
import com.ssafy.gumid101.res.CrewBoardRes;
import com.ssafy.gumid101.res.PagingParameter;
import com.ssafy.gumid101.res.ReportResDto;
import com.ssafy.gumid101.res.ReportResDto.ReportResDtoBuilder;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportManagerServiceImpl implements ReportManagerService {

	private final ReportRepository reportRepository;
	private final CrewActivityBoardRepository boardRepo;
	private final FirebaseMessageUtil fcmUtil;
	private final UserRepository userRepo;

	public Map<String, Long> getReportStateCount() {
		List<Map<String, Object>> result = reportRepository.getReportStateCountThoughtGroupBy();

		Map<String, Long> resultMap = new HashMap<>();

		resultMap.put(ReportStatus.COMPLETE.name(), 0L);
		resultMap.put(ReportStatus.PROCESSING.name(), 0L);
		resultMap.put(ReportStatus.WAITING.name(), 0L);

		for (int i = 0; i < result.size(); i++) {
			String key = ((ReportStatus) result.get(i).get("0")).name();
			Long value = (Long) result.get(i).get("1");
			if (!StringUtils.hasLength(key) || value == null) {
				continue;
			}
			resultMap.put(key, value);
		}

		return resultMap;
	}

	@Transactional
	@Override
	public Map<String, Object> selectReportsByParam(ReportSelectReqDto params) {

		List<ReportResDto> reportList = reportRepository.selectReportsByParam(params);
		long count = reportRepository.selectCountReportsByParam(params);

		int pageItemSize = params.getPageItemSize();
		int currentPage = params.getCurrentPage();
		int pageNaviSize = params.getPageNaviSize();

		int lastPageIndex = (int) (((count - 1) / pageItemSize) + 1);

		if (lastPageIndex <= 0) {
			lastPageIndex = 1;
		}
		int startPageIndex = ((currentPage - 1) / pageNaviSize) * pageNaviSize + 1;
		int endPageIndex = startPageIndex + pageNaviSize - 1;
		int nextPageIndex = endPageIndex + 1;
		int prevPageIndex = startPageIndex - 1;

		if (endPageIndex > lastPageIndex) {
			endPageIndex = lastPageIndex;
		}
		if (startPageIndex < 1) {
			startPageIndex = 1;
		}

		nextPageIndex = endPageIndex >= lastPageIndex ? endPageIndex : nextPageIndex;
		prevPageIndex = prevPageIndex <= 1 ? 1 : prevPageIndex;

		PagingParameter pagingParameter = new PagingParameter();

		pagingParameter.setCurrentPageIndex(currentPage);
		pagingParameter.setLastPageIndex(lastPageIndex);

		pagingParameter.setEndPageIndex(endPageIndex);
		pagingParameter.setNextPageIndex(nextPageIndex);
		pagingParameter.setCurrentPageIndex(currentPage);
		pagingParameter.setPageNavSize(pageNaviSize);
		pagingParameter.setPrevPageIndex(prevPageIndex);
		pagingParameter.setStartPageIndex(startPageIndex);

		Map<String, Object> map = new HashMap<>();
		map.put("reports", reportList);
		map.put("pageinfo", pagingParameter);
		return map;

	}

	@Transactional
	@Override
	public int updateReportsStatus(Long reportId, ReportStatus status) {

		Optional<ReportEntity> report = reportRepository.findById(reportId);
		if (!report.isPresent()) {
			return 0;
		}
		report.get().setReportStatus(status);

		return 1;
	}

	@Override
	public int deleteCrewBoard(Long boardSeq) throws Exception {

		try {
			boardRepo.deleteById(boardSeq);
			// 데이터가 없으면 롤백 처리되기에 리턴값이 없다?
			// 롤백 처리에러를 던졌는데 ,나는 익셉션으로 받고 롤백 처리 없게 하였다.
			// 하지만 트랝잭션내에서 에러가 발생했다는 것은 체크됬고
			// ????
		} catch (Exception e) {
			log.debug(e.getMessage());
			return 0;
		}

		return 1;
	}

	@Transactional
	@Override
	public int deleteReport(Long seq) throws Exception {

		try {
			reportRepository.deleteById(seq);
		} catch (Exception e) {

			log.debug("신고글 삭제 에러 : {}", e.getMessage());
			return 0;
		}

		return 1;
	}

	@Transactional
	@Override
	public Map<String, Object> selectReportById(Long reportSeq) {

		ReportEntity report = reportRepository.findById(reportSeq).orElse(null);

		if (report != null) {

			Map<String, Object> result = new HashMap<String, Object>();

			UserEntity reporter = report.getUserReporterEntity();
			UserEntity target = report.getUserTargetEntity();

			ReportResDtoBuilder builder = ReportResDto.builder().crewBoardSeq(report.getReportCrewBoardSeq())
					.regTime(report.getReportRegTime()).reportContent(report.getReportContent())
					.reportSeq(report.getReportSeq()).reportState(report.getReportStatus());

			result.put("reportImgSeq", null);
			result.put("targetImgSeq", null);

			result.put("reporter", null);
			if (reporter != null) {
				builder.reporterNickName(reporter.getNickName()).reporterUserSeq(reporter.getUserSeq());
				result.put("reporter", UserDto.of(reporter));
				result.put("reportImgSeq", reporter.getImageFile().getImgSeq());
			}

			result.put("target", null);
			if (target != null) {
				builder.targetNickName(target.getNickName()).targetUserSeq(target.getUserSeq());
				result.put("target", UserDto.of(target));
				result.put("targetImgSeq", target.getImageFile().getImgSeq());
			}

			ReportResDto reportDto = builder.build();

			result.put("report", reportDto);

			CrewBoardEntity crewboard = boardRepo.findById(reportDto.getCrewBoardSeq()).orElse(null);

			CrewBoardFileDto boardDto = null;
			if (crewboard != null) {
				boardDto = CrewBoardFileDto.builder().crewBoardDto(CrewBoardRes.of(crewboard))
						.imageFileDto(ImageFileDto.of(crewboard.getImgFile())).build();

			}
			result.put("board", boardDto);

			return result;

		} else {
			return null;
		}

	}

	@Transactional
	@Override
	public int sendAlarm(AlarmReqDto requestBody) {

		UserEntity userEntity = userRepo.findById(requestBody.getUserSeq()).orElse(null);
		if (userEntity == null) {
			return 0;
		}
		if (null == userEntity.getFcmToken()) {
			return 0;
		}

		try {
			fcmUtil.sendMessageTo(userEntity.getFcmToken(), requestBody.getTitle(), requestBody.getBody());
		} catch (IOException | FCMTokenUnValidException e) {
			return 0;
		}

		return 1;
	}

	@Transactional
	@Override
	public int sendAlarmTotal(AlarmReqDto requestBody) {
		
		List<UserEntity> users = userRepo.findByALLFcmTokenNotNULL();

		List<FcmMessage.Message> messageList =	users.stream().map(
(user)->{
	return FcmMessage.ofMessage(user.getFcmToken(), "[전체 알림]", requestBody.getBody());
}
				).collect(Collectors.toList());
		  
		  // 
		//  
		try {
			fcmUtil.sendMessageTo(messageList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
}
