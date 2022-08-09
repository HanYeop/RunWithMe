package com.ssafy.gumid101.customercenter.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.gumid101.crew.activity.CrewActivityBoardRepository;
import com.ssafy.gumid101.customercenter.ReportRepository;
import com.ssafy.gumid101.customercenter.ReportStatus;
import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.req.ReportSelectReqDto;
import com.ssafy.gumid101.res.PagingParameter;
import com.ssafy.gumid101.res.ReportResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportManagerServiceImpl implements ReportManagerService {

	private final ReportRepository reportRepository;

	private final CrewActivityBoardRepository boardRepo;
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
		if(!report.isPresent()) {
			return 0;
		}
		report.get().setReportStatus(status);
		return 1;
	}
	
	@Transactional
	@Override
	public int deleteCrewBoard(Long boardSeq) throws Exception {

		try {
			boardRepo.deleteById(boardSeq);
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
}
