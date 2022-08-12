import axios from "axios";

const axiosClient = axios.create({
  baseURL: process.env.REACT_APP_API_PATH,
  //localhost:8080/api
});

export default axiosClient;
