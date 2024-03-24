<script setup lang="ts">
import VanillaTilt from 'vanilla-tilt';
import {onMounted} from "vue";
//It also supports NodeList

defineProps({
  msg:{
    type:String,
    default:"内容为空"
  }
})
onMounted(()=>{
  VanillaTilt.init(document.querySelector(".tipCard") as HTMLElement, {
    max: 20, // 最大倾斜角度
    speed: 10, // 倾斜速度
    // 其他选项...
  });
})
</script>

<template>
  <div class="page">
    <div class="tipCard">
      <img src="../assets/empty.png">
      <p>{{msg}}</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
.page{
  height: 100%;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  .tipCard{
    cursor: pointer;
    img{
      height: 300px;
      width: 300px;
    }
    p{
      text-align: center;
      color: #a2beb4;
      font-weight: bold;
      font-size: 25px;
    }
  }

}
</style>
