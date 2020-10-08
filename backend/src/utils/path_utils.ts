import {dirname, fromFileUrl} from "../../deps.ts";

/**
 * Get the relative (to file) working directory path from import meta.
 * Usage Example:
 *
 * ```
 * const __dirname = rwd(import.meta)
 * // => ...choppa/backend/src/utils
 * ```
 *
 * @param meta
 */
export function rwd(meta: ImportMeta): string {
    return dirname(fromFileUrl(meta.url));
}

/**
 * Get the relative filename path from import meta.
 * Usage Example:
 *
 * ```
 * const __filename = rfn(import.meta)
 * // => ...choppa/backend/src/utils/path_utils.ts
 * ```
 *
 * @param url import.meta.url
 */
export function rfn(meta: ImportMeta): string {
    return fromFileUrl(meta.url);
}