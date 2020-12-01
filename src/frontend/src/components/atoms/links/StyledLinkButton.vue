<template>
  <button @click="() => this.$emit('click')" class="hover:underline focus:underline active:outline-none
  focus:outline-none text-choppa-light hover:text-choppa-light-hover dark:text-choppa-dark
  dark:hover:text-choppa-dark-hover" >
    <slot></slot>
  </button>
</template>
