import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://i7d101.p.ssafy.io:8080/api",
});

export default axiosClient;
