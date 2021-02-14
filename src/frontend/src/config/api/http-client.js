import axios from "axios";
import Vue from "vue";

const httpClient = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

httpClient.interceptors.response.use(undefined, async (error) => {
  if (error.response.status === 401) {
    await Vue.prototype.$store.dispatch("logOut");
  }

  return Promise.reject(error);
});

export default httpClient;
