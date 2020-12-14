<template>
  <div class="w-full h-full flex flex-col">
    <div class="hidden">
      <slot></slot>
    </div>
    <div class="relative h-full overflow-hidden"
         @mouseover="doTransitions = false"
         @mouseleave="doTransitions = true">
      <template v-for="slideElement in slideElements">
        <div v-html="slideElement.html"
             v-bind:key="slideElement.id"
             class="absolute top-0 w-full h-full"
             :class="[ slideElementClass(slideElement) ]"
             :style="{ ...slideElementStyle(slideElement) }"
        />
      </template>
    </div>
    <!-- <div class="flex flex-row w-full justify-center">
      <button v-for="slideElement in slideElements"
              v-bind:key="slideElement.id"
              v-on:click="
              showSlide(slideElements[slideElement.id]);"
              class="h-1 w-6 md:h-2 md:w-9 mx-3 mt-2
              rounded-full bg-gray-100 hover:ring focus:ring focus:outline-none"/>
    </div> -->
  </div>
</template>
<script>
/* eslint-disable no-param-reassign */

const VISIBLE_DURATION_MS = 4000;
const TRANSITION_DURATION_MS = 1000;

export default {
  data() {
    return {
      state: Object.freeze({
        hidden: Object.freeze("hidden"),
        entering: Object.freeze("entering"),
        exiting: Object.freeze("exiting"),
        visible: Object.freeze("visible"),
      }),
      slideElements: [],
      slideVisibleTimer: 0,
      slideAnimationTimer: 0,
      doTransitions: true,
      currentVisibleSlide: undefined,
      previousVisibleSlide: undefined,
    };
  },
  mounted() {
    this.slideElements = this.$slots.default.map((child, index) => this.createSlideElements(
      index,
      child.elm.outerHTML,
    ));
    this.showSlide(this.slideElements[0]);
    window.requestAnimationFrame(this.onUpdate);
  },
  methods: {
    createSlideElements(index, slideHtml) {
      return {
        id: index,
        html: slideHtml,
        state: this.state.hidden,
      };
    },
    slideElementClass(slideElement) {
      switch (slideElement.state) {
        case this.state.entering:
        case this.state.exiting:
          return "animate-slide-left";
        default:
          return [];
      }
    },
    slideElementStyle(slideElement) {
      switch (slideElement.state) {
        case this.state.entering:
        case this.state.hidden:
          return { left: "100%" };
        default:
          return { left: "0%" };
      }
    },
    resetVisibilityTimer() {
      this.slideVisibleTimer = window.performance.now() + VISIBLE_DURATION_MS;
    },
    resetAnimationTimer() {
      this.slideAnimationTimer = window.performance.now() + TRANSITION_DURATION_MS;
    },
    onUpdate() {
      if (this.doTransitions === false) {
        if (window.performance.now() > this.slideAnimationTimer) {
          this.setPreviousAndCurrentStates();
        }
      }
      if (this.doTransitions === true) {
        if (window.performance.now() >= this.slideAnimationTimer) {
          this.setPreviousAndCurrentStates();
        }
        if (window.performance.now() >= this.slideVisibleTimer) {
          this.showNextSlide();
          this.resetAnimationTimer();
          this.resetVisibilityTimer();
        }
      }
      window.requestAnimationFrame(this.onUpdate);
    },
    showNextSlide() {
      if (this.currentVisibleSlide.id >= this.slideElements.length - 1) {
        this.showSlide(this.slideElements[0]);
      } else {
        this.showSlide(this.slideElements[this.currentVisibleSlide.id + 1]);
      }
    },
    showSlide(slide) {
      if (this.currentVisibleSlide) {
        this.hideSlide(this.currentVisibleSlide);
        slide.state = this.state.entering;
      } else {
        slide.state = this.state.visible;
      }

      this.currentVisibleSlide = slide;
      this.resetVisibilityTimer();
      this.resetAnimationTimer();
    },
    hideSlide(slide) {
      this.previousVisibleSlide = slide;
      slide.state = this.state.exiting;
    },
    setPreviousAndCurrentStates() {
      this.currentVisibleSlide.state = this.state.visible;
      if (this.previousVisibleSlide) {
        this.previousVisibleSlide.state = this.state.hidden;
      }
      this.resetAnimationTimer();
    },
  },
};

</script>
