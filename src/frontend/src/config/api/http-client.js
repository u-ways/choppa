import axios from "axios";

const httpClient = axios.create({
  baseURL: "/api",
  timeout: 1000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default httpClient;
