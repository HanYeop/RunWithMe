import { createSlice } from "@reduxjs/toolkit";

const initialReportPageState = {
  pageMeta: {
    status: "",
    pageItemSize: 10,
    currentPage: 1,
    pageNaviSize: 10,
  },
  reports: [],
};

//전역 상태를 만든다. 여러개의 상태를 각 슬라이스라고 한다.
const reportPageSlice = createSlice({
  name: "reportPage",
  initialState: initialReportPageState,
  reducers: {
    setPageStatus(state, action) {
      console.log("셋 페이지 스테이터스 액션 : ", action.payload);
      state.pageMeta = action.payload;
    },
    clearPageStatus(state) {
      //로그 아웃 시 상태를 버림
      state = initialReportPageState;
    },
    setReports(state, action) {
      state.reports = action.payload;
    },
  },
});

export const reportPageActions = reportPageSlice.actions;

export default reportPageSlice.reducer;
