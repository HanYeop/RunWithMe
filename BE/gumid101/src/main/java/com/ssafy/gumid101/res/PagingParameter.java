package com.ssafy.gumid101.res;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PagingParameter {

	private int prevPageIndex;
	private int startPageIndex;
	private int currentPageIndex;
	private int endPageIndex;
	private int nextPageIndex;
	private int pageNavSize;
	private int lastPageIndex;
}
