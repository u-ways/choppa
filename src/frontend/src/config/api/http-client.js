/* eslint-disable no-param-reassign */
import axios from "axios";
import Vue from "vue";

const httpClient = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

httpClient.interceptors.request.use(async (config) => {
  config.headers.Authorization = `Bearer ${await Vue.prototype.$auth.getAccessToken()}`;
  return config;
});

export default httpClient;
