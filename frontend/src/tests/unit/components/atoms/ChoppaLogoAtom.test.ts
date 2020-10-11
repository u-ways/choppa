import { shallowMount } from "@vue/test-utils";
import ChoppaLogoAtom from "../../../../components/atoms/ChoppaLogoAtom.vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

// TODO BT: Is testing-library/vue ported to vue-next, yet? If so refactor this to use that.
describe("ChoppaLogoAtom test", () => {

  test("Check css prop is applied as a class to the icon", () => {
    const wrapper = shallowMount(ChoppaLogoAtom , {
      global: {
        components: {
          "font-awesome": FontAwesomeIcon
        }
      },
      props: {
        css: "test-css-class-one test-css-class-two"
      }
    });

    expect(wrapper.html()).toBe(
      `<font-awesome-icon-stub class="test-css-class-one test-css-class-two">` +
      "</font-awesome-icon-stub>"
    );
  });

});
