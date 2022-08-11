import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://i7d101.p.ssafy.io/api",
});

export default axiosClient;
