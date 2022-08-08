import { createSlice } from "@reduxjs/toolkit";

const initialAuthState = {
  accessToken: null,
  userSeq: null,
  userEmail: null,
};

//전역 상태를 만든다. 여러개의 상태를 각 슬라이스라고 한다.
const authSlice = createSlice({
  name: "auth",
  initialState: initialAuthState,
  reducers: {
    setAuth(state, action) {
      console.log("auth slice - setAuth 실행 : ");
      console.log(action.payload);

      //로그인 성공 시 , 값 셋팅
      //원래 상태는 불변이라 리턴으로하지만 여기서는 알아서 해줌
      state.accessToken = action.payload.accessToken;
      state.userSeq = action.payload.userSeq;
      state.userEmail = action.payload.userEmail;
    },
    clearAuth(state) {
      //로그 아웃 시 상태를 버림
      state.ACCES_TOKEN = null;
      state.userSeq = null;
      state.userEmail = null;
    },
  },
});

export const authActions = authSlice.actions;

export default authSlice.reducer;
