/* eslint-disable no-shadow, no-param-reassign */
import { ADD_NEW_TOAST, REMOVE_TOAST } from "@/config/store/mutation-types";

export const toastState = {
  toastUniqueCounter: 0,
  toasts: [],
};

export const toastGetters = {
  toasts: (state) => state.toasts,
  toastUniqueCounter: (state) => state.toastUniqueCounter,
};

export const toastMutations = {
  [ADD_NEW_TOAST](state, toast) {
    state.toasts.push(toast);
    state.toastUniqueCounter += 1;
  },
  [REMOVE_TOAST](state, toast) {
    state.toasts = state.toasts.filter((otherToast) => toast.id !== otherToast.id);
  },
};

export const toastActions = {
  newToast(context, toast) {
    context.commit(ADD_NEW_TOAST, toast);
    toast.timer = window.setTimeout(() => context.commit(REMOVE_TOAST, toast), toast._time);
  },
  removeToast(context, toast) {
    context.commit(REMOVE_TOAST, toast);
    window.clearTimeout(toast.timer);
  },
};
