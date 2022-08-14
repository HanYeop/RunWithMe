import { createSlice } from "@reduxjs/toolkit";

const initialEventState = {
  prevEvent: [
    {
      competitionDto: {
        competitionSeq: null,
        competitionName: "",
        competitionContent: "",
        competitionDateStart: "",
        competitionDateEnd: "",
        competitionStatus: null,
      },
      competitionImageFileDto: {
        imgSeq: 0,
        imgOriginalName: "",
        imgSavedName: "",
        imgSavedPath: "",
      },
    },
  ],
  endEvent: [
    {
      competitionDto: {
        competitionSeq: null,
        competitionName: "",
        competitionContent: "",
        competitionDateStart: "",
        competitionDateEnd: "",
        competitionStatus: null,
      },
      competitionImageFileDto: {
        imgSeq: 0,
        imgOriginalName: "",
        imgSavedName: "",
        imgSavedPath: "",
      },
    },
  ],
  currentEvent: {
    competitionDto: null,
    competitionImageFileDto: null,
  },
};

//전역 상태를 만든다. 여러개의 상태를 각 슬라이스라고 한다.
const eventSlice = createSlice({
  name: "event",
  initialState: initialEventState,
  reducers: {
    setCurrentEvent(state, action) {
      state.currentEvent = action.payload;
    },
    setEndEvent(state, action) {
      state.endEvent = action.payload;
    },
    setPrevEvent(state, action) {
      state.prevEvent = action.payload;
    },
  },
});

export const evnetActions = eventSlice.actions;

export default eventSlice.reducer;
