import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./slice/auth";
import siteReducer from "./slice/siteMap";
import reportPageReducer from "./slice/reportPaging";
const store = configureStore({
  reducer: {
    auth: authReducer,
    site: siteReducer,
    reportPage: reportPageReducer,
  },
});

export default store;
