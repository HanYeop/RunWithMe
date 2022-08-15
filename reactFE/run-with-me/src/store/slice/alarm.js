import { createSlice } from "@reduxjs/toolkit";

const alarmInitailState = {
  userSeq: "",
  title: "",
  text: "",
};

//전역 상태를 만든다. 여러개의 상태를 각 슬라이스라고 한다.
const alarmSlice = createSlice({
  name: "alarm",
  initialState: alarmInitailState,
  reducers: {
    setUserSeq(state, action) {
      state.userSeq = action.payload;
    },
    setTitle(state, action) {
      state.title = action.payload;
    },
    setText(state, action) {
      state.text = action.payload;
    },
    clear(state) {
      state = alarmInitailState;
    },
  },
});

export const alarmSliceActions = alarmSlice.actions;

export default alarmSlice.reducer;
