/** Commonly used HTTP constants */
export const HOSTNAME = "127.0.0.1";

/**
 * Generates an HTTP abort controller and signal
 */
export function generateAbortController() {
  const controller = new AbortController();
  const { signal } = controller;
  return { controller, signal };
}
