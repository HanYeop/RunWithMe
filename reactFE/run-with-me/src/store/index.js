import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./slice/auth";
import siteReducer from "./slice/siteMap";

const store = configureStore({
  reducer: { auth: authReducer, site: siteReducer },
});

export default store;
