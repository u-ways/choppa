<template>
      <div class="text-center pt-6 pl-4 w-full lg:w-1/2">
        <p class="text-xl sm:text-2xl lg:text-3xl font-bold">
          <slot name="heading"/>
        </p>
        <div class="text-lg sm:text-xl lg:text-2xl">
          <slot name="body"/>
          <slot name="extBody"/>
        </div>
      </div>
</template>
