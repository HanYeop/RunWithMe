package com.ssafy.gumid101.req;

import com.ssafy.gumid101.customercenter.ReportStatus;

import lombok.Data;

@Data
public class ReportSelectReqDto extends PagingParameter {

	ReportStatus status;
}
