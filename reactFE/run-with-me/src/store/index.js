import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./slice/auth";
import siteReducer from "./slice/siteMap";
import reportPageReducer from "./slice/reportPaging";
import alarmReducer from "./slice/alarm";

const store = configureStore({
  reducer: {
    auth: authReducer,
    site: siteReducer,
    reportPage: reportPageReducer,
    alarm: alarmReducer,
  },
});

export default store;
