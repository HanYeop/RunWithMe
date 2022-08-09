import { createSlice } from "@reduxjs/toolkit";

const initialReportPageState = {
  status: "WAITING",
  pageItemSize: 10,
  currentPage: 1,
  pageNaviSize: 10,
};

//전역 상태를 만든다. 여러개의 상태를 각 슬라이스라고 한다.
const reportPageSlice = createSlice({
  name: "reportPage",
  initialState: initialReportPageState,
  reducers: {
    setPageStatus(state, action) {
      state = action.payload;
    },
    clearPageStatus(state) {
      //로그 아웃 시 상태를 버림
      state = initialReportPageState;
    },
  },
});

export const reportPageActions = reportPageSlice.actions;

export default reportPageSlice.reducer;
