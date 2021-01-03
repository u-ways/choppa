import { describe, expect, it } from "@/tests/unit/test-imports";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";

describe("Toast Data test", () => {
  it("Constructor assigns properties correctly", () => {
    const toast = new ToastData({
      variant: toastVariants.SUCCESS,
      message: "test message",
    });

    expect(toast.variant).toBe(toastVariants.SUCCESS);
    expect(toast.message).toBe("test message");
  });

  it("No message passed to constructor will throw error", () => {
    expect(() => {
      // eslint-disable-next-line no-new
      new ToastData({ variant: toastVariants.SUCCESS });
    }).toThrowWithMessage(Error, "Expected variant and message");
  });

  it("No variant passed to constructor will throw error", () => {
    expect(() => {
      // eslint-disable-next-line no-new
      new ToastData({ message: "test message" });
    }).toThrowWithMessage(Error, "Expected variant and message");
  });

  it("Invalid variant passed to constructor will throw error", () => {
    expect(() => {
      // eslint-disable-next-line no-new
      new ToastData({
        variant: "invalidToastVariant",
        message: "test message",
      });
    }).toThrowWithMessage(Error, "Unsupported toast variant invalidToastVariant");
  });
});
