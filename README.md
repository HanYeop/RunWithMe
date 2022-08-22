<div align="left">
<img width="200" src="https://lab.ssafy.com/s07-webmobile4-sub2/S07P12D101/uploads/0a9e04f048a227e4d11bb069518f5ad4/rwm_logo_white.jpg">
</div>
<br/>

## 🏃🏻‍♂️ RunWithMe?

<font size="5" color="orange">**RunWithMe**</font> 프로젝트는 Android 모바일로 제공되는 비대면 러닝 챌린지 크루 어플리케이션입니다. 주요 기획 의도는 <strong>개발진의 역량 향상</strong>과 <strong>기존의 러닝 앱 서비스에 대한 불편점을 개선하고 보완한 새로운 러닝앱 개발</strong>이였습니다.
<br/>
평소 러닝을 즐기는 팀원들이 모여 직접 러닝 앱을 오랫동안 사용하면서 느낀 불편점을 개선하기 위해 적극적으로 기획하였으며 모두가 프로젝트의 주인의식을 가지고 직접 사용할 수 있는 러닝 앱을 제작하였습니다. 기존 서비스를 분석하며 최대한 사용자 편의성을 생각하고 코로나 시대에 증가하는 비대면 러닝 크루 기능에 대한 사용자들의 요구와 달리 기존 러닝앱들은 개인 러닝에만 초점이 맞춰져 있는 점을 고려하여 러닝 크루 기능에 초점을 맞춘 새로운 러닝 앱 서비스를 제공하고자 하였습니다.

<h2> 📱 Android </h2>
<h5>러닝 진행 화면(15min)</h5>
<img  width="500px" src="https://lab.ssafy.com/s07-webmobile4-sub2/S07P12D101/uploads/099d90795ac3c4eeb5cf3ac438f9d1b9/달리기_1분_압축.gif">
<br/>

<h5>스크랩 러닝 화면(15min)</h5>
<img  width="500px" src="https://lab.ssafy.com/s07-webmobile4-sub2/S07P12D101/uploads/e5210fe416ad0ac5d9a6adc2c3261ced/스크랩러닝.gif">
<br/>

<h5>비정상적인 움직임 감지 화면(빠름))</h5>
<img  width="500px" src="https://lab.ssafy.com/s07-webmobile4-sub2/S07P12D101/uploads/eea93de3ccb63d986aafb6e38587271b/KakaoTalk_20220822_213003025.gif">
<br/>


### 🎨 사용한 라이브러리

| Name             | Description                                  |
| ---------------- | -------------------------------------------- |
| Data Layer API   | Wear OS 네트워크 통신 라이브러리             |
| Navigation       | 프래그먼트 전환 라이브러리                   |
| Glide            | 이미지 로딩 라이브러리                       |
| ViewModel        | 수명주기 고려 데이터를 저장, 관리 라이브러리 |
| Coroutine        | 비동기 처리 라이브러리                       |
| Coroutine Flow   | 비동기 데이터 스트림                         |
| TedPermission    | 안드로이드 권한 라이브러리                   |
| Retrofit         | HTTP 통신 라이브러리                         |
| SimpleRatingBar  | RatingBar 커스텀 라이브러리                  |
| Dagger Hilt      | 의존성 주입 라이브러리                       |
| Circle ImageView | 이미지뷰 라이브러리                          |
| Google Auth      | 구글 소셜 로그인                             |
| Naver            | 네이버 소셜 로그인                           |
| KaKao            | 카카오 소셜 로그인                           |
| Lottie           | 안드로이드 애니메이션 라이브러리             |
| GoogleMap        | 구글 지도 라이브러리                         |
| FCM              | 파이어베이스 푸시 알림                       |
| Paging 3         | 안드로이드 페이징 라이브러리                 |
| Mp Android Chart | 안드로이드 차트 통계 라이브러리              |
| Room             | 안드로이드 DB 라이브러리                     |
| Weather Api      | 공공데이터포털 기상청 날씨 라이브러리        |
| CalendarView     | 캘린더 라이브러리                            |

</br></br>

## 백엔드

### 백엔드 사용 라이브러리

| 사용라이브러리                              | 사용이유                                                                |
| ------------------------------------------- | ----------------------------------------------------------------------- |
| aop                                         | 서비스 입력 반환 로깅 처리                                              |
| firebase-admin                              | 구글 Oauth 토큰 검증, FCM 서비스 이용                                   |
| jackson-datatype-jsr310                     | LocalDateTime Json 변환,반환 포맷 지정                                  |
| coomons-fileupload ,commons-io              | 파일 업로드                                                             |
| spring-cloud-starter-aws                    | aws S3서비스 사용                                                       |
| jjwt-api,jjwt-impl,jjwt-jackson             | 인증 인가에 사용할 jwt토큰 생성 및 검증                                 |
| spring-boot-starter-data-jpa                | Mysql 에 관리,조작하기 위한 JPA API                                     |
| spring-boot-starter-security                | 인증,인가 특히 권한관리를 쉽게 해주는 스프링 하위 프레임워크            |
| springfox-swagger-ui,springfox-boot-starter | API 명세                                                                |
| mysql-connector-java                        | DBMS                                                                    |
| lombok                                      | 컴파일 타임 코드 자동 생성기                                            |
| querydsl-apt, querydsl-jpa                  | JPA 사용 시에 복잡한 쿼리 처리에 사용                                   |
| spring-boot-starter-validation              | 사용자의 입력에 대한 유효성 검사에 도움을 주는 라이브러리               |
| spring-boot-starter-data-redis              | 휘발성 인메모리 저장소, 중복처리에 사용                                 |
| spring boot batch                           | 포인트 정산, 알림 발송과 같은 scheduled로 지정된 시간의 일괄처리에 사용 |

### 패키지 다이어그램

![image](/uploads/e6cce9d354b34807ea2866c8ccbac751/image.png)
