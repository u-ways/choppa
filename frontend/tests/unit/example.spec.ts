import { shallowMount } from "@vue/test-utils";
import example from "@/components/Example.vue";

describe("Example.vue", () => {
  it("renders props.msg when passed", () => {
    const msg = "new message";
    const wrapper = shallowMount(example, {
      props: { msg }
    });
    expect(wrapper.text()).toMatch(msg);
  });
});
