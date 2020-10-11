import { App, createApp } from "vue";
import ChoppaApp from "./App.vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserAlt, faHelicopter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { ComponentPublicInstance } from "@vue/runtime-core";
import router from "./router";
import Logger from "./utilities/Logger";

// Set up awesomefont
library.add(faUserAlt, faHelicopter);

const app: App = createApp(ChoppaApp);

// Loggers
window.onerror = (error: Event | string) => {
  Logger.error(`[Error] ${error}`);
  return true;
};

app.config.warnHandler = (
  msg: string,
  instance: ComponentPublicInstance | null,
  trace: string
) => Logger.warn(`[Vue Warn] ${msg} \n${trace}`);

app.config.errorHandler = (err: unknown) => Logger.error(`[Vue Error] ${(err as Error).message} \n${(err as Error).stack}`);

app.component("font-awesome", FontAwesomeIcon);
app.use(router);
app.mount("#app");
