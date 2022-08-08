import { createSlice } from "@reduxjs/toolkit";

const initialSiteState = {
  pageName: "WelCome",
};

const siteSlice = createSlice({
  name: "site",
  initialState: initialSiteState,
  reducers: {
    setPathName(state, action) {
      state.pageName = action.payload.pageName;
    },
    addPathName(state, action) {
      state.pageName = state.pageName + action.payload.pageName;
    },
  },
});

export const siteActions = siteSlice.actions;
export default siteSlice.reducer;
