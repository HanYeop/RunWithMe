import React from "react"; //기본
import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google"; //구글 oauth를 사용하기 위함
import axiosClient from "../../api/api.js"; //서버와 api 통신
import { useDispatch } from "react-redux"; //상태변경, 로그인 된 상태로
import jwtDecode from "jwt-decode"; //jwt token claim의 role을 알기 위함
import { authActions } from "../../store/slice/auth";
import { useNavigate } from "react-router-dom";
const clientId = process.env.REACT_APP_GOOGLE_OAUTH_CLIENT_ID;
//나중에 env로 ###
const GoogleOAuth = () => {
  const dispatch = useDispatch();
  const navigation = useNavigate();
  const onSuccessHandler = async (credentialResponse) => {
    console.log(credentialResponse);
    const credential = credentialResponse.credential;
    axiosClient
      .get("/login/oauth2/code/google", {
        params: {
          code: credential,
        },
      })
      .then(({ data }) => {
        console.log("로그인 성공");

        let accessToken = data["JWT-AUTHENTICATION"];
        let userEmail = data.email;
        let isRegistered = data.isRegistered;
        let userSeq = data.userSeq;

        console.log("로그인 데이터 뽑기 성공");

        let payload = jwtDecode(accessToken);
        let role = payload.role;
        if (isRegistered !== true) {
          console.log("없는 아이디 입니다.");
        }

        if (role !== "ROLE_USER") {
          console.log(`관리자 권한이 아닙니다. 사용할 수 없습니다. ${role}`);
          return;
        }
        /*
      state.ACCES_TOKEN = action.ACCES_TOKEN;
      state.userSeq = "asd";
      state.userEmail = "asfd";
*/

        console.log("로그인 정보를 auth slice에 저장합니다.");
        dispatch(authActions.setAuth({ accessToken, userEmail, userSeq }));
        navigation("/");
      });
  };

  return (
    <GoogleOAuthProvider clientId={clientId}>
      <GoogleLogin
        onSuccess={onSuccessHandler}
        onError={() => {
          console.log("Login Failed");
        }}
      />
    </GoogleOAuthProvider>
  );
};
export default GoogleOAuth;
