<script setup lang="ts">
import {computed, CSSProperties, defineEmits, onMounted, reactive, ref, watch} from "vue";
import {DrawerPlacement, UploadFileInfo, useDialog, useMessage} from "naive-ui";
import {FormatListBulletedRound, PlaylistAddOutlined,} from "@vicons/material"
import Empty from "../components/Empty.vue";
import {allProject, delProject} from "../api/projects/projects.ts";
import {APKInfo, ProjectInfo} from "../CommonModel.ts";
import UpsertProjectDialog from "../components/UpsertProjectDialog.vue";
import {ArchiveOutline as ArchiveIcon} from "@vicons/ionicons5";
import {addAPKInfo, allAPKInfo, uploadApk} from "../api/apk/apk.ts";
import {useResizeObserver} from '@vueuse/core'
import APKAPIDialog from "../components/APKAPIDialog.vue";

const emit = defineEmits(['useDarkTheme'])
const naiveDialog = useDialog()
const naiveMsg = useMessage()
const emptyAPKInfo = {
  version: "",
  versionCode: 0,
  updateContent: "",
  updateTime: 0,
  projectId: ""
}
const apkInfoFormRules = {
  version: {
    required: true,
    message: '请输入版本号',
    trigger: ['input']
  },
  versionCode: [
    {
      required: true,
      message: '请作答',
      type: 'number',
      trigger: ['input', 'blur']
    },
    {
      trigger: ['input', 'blur'],
      level: 'warning',
      validator(_rule: FormItemRule, value: number) {
        if (value === 0) {
          return new Error('你确定吗？')
        }
        return true
      }
    }
  ],
  updateContent: {
    required: true,
    message: '请输入更新内容',
    trigger: ['input']
  },

}
const nowReviewProject = reactive<ProjectInfo>({
  projectId: "",
  name: ""
})
const showProjects = ref(false)
const darkMode = ref(false)
const pageLoading = ref(false)
const pageLoadingMsg = ref<string>("")
const placement = ref<DrawerPlacement>('right')
const projectList = ref<Array<ProjectInfo>>([])
const newApkInfo = reactive<APKInfo>(emptyAPKInfo)
const upsertProjectDialog = ref(null)
const apkInfoFormRef = ref(null)
const apkAPIDialogRef = ref(null)
const apkInfoHisList = ref<Array<APKInfo>>([])
const apkAddCardRef = ref()
const apkHisListHeight = ref(0)
const apkFile = ref<File | unknown>(null)
const pageLoadingShow = (msg: string = "正在加载") => {
  pageLoading.value = true
  pageLoadingMsg.value = msg
}
const pageLoadingHide = () => {
  pageLoading.value = false
}

function delProjectConfirm(item: ProjectInfo) {
  naiveDialog.warning({
    title: '项目删除警告',
    content: `你确定要删除${item.name}嘛，删除后将清除该项目的所有服务器信息，包括APK文件和历史上传信息等`,
    positiveText: '确定',
    negativeText: '不确定',
    onPositiveClick: () => {
      pageLoadingShow("正在删除")
      delProject(item.projectId).then(_ => {
        naiveMsg.success("删除成功")
        updateProjectList()
        pageLoadingHide()
      }).catch(err => {
        naiveMsg.error(err)
      })

    },
    onNegativeClick: () => {

    }
  })
}

function selectProject(item: ProjectInfo) {
  Object.assign(nowReviewProject, item)
  Object.assign(newApkInfo, {
    version: "",
    versionCode: 0,
    updateContent: "",
    updateTime: 0,
    projectId: ""
  })
  updateAPKHisList()
  // showProjects.value = false
}

function submitApkInfo(e: MouseEvent) {
  e.preventDefault()
  apkInfoFormRef.value?.validate((errors) => {
    if (!errors) {
      if (apkFile.value == null) {
        naiveMsg.warning("请选择上传的文件")
        return
      }
      naiveDialog.info({
        title: 'APK信息上传',
        content: `上传项目${nowReviewProject.name}`,
        positiveText: '确认上传',
        negativeText: '不确定',
        onPositiveClick: () => {
          const data: APKInfo = {
            projectId: nowReviewProject.projectId,
            updateContent: newApkInfo.updateContent,
            updateTime: new Date().getTime(),
            version: newApkInfo.version,
            versionCode: newApkInfo.versionCode
          }
          pageLoadingShow("正在上传")
          addAPKInfo(data).then(_ => {
            updateAPKHisList()
            uploadApk(apkFile.value as File, nowReviewProject.projectId).then(_ => {
              naiveMsg.success("新增成功")
              pageLoadingHide()
            }).catch(err => {
              console.log(err)
              pageLoadingHide()
              naiveMsg.error("文件上传错误请查看控制台打印")
            })
          }).catch(err => {
            console.log(err)
            pageLoadingHide()
            naiveMsg.error("请求错误请看控制台错误打印")
          })

        },
      })
    }
  })
}

function apkSelect(fileList: UploadFileInfo[]) {
  if (fileList.length != 0) {
    apkFile.value = fileList[0].file
  } else {
    apkFile.value = null
  }
}

async function updateAPKHisList() {
  naiveMsg.info("正在获取APK历史信息")
  await allAPKInfo(nowReviewProject.projectId).then(res => {
    apkInfoHisList.value = res.data
    if (apkInfoHisList.value.length != 0) {
      newApkInfo.versionCode = apkInfoHisList.value[0]!!.versionCode
    }
    naiveMsg.success("获取成功")
  }).catch(_ => {
    naiveMsg.error("获取apk历史失败")
  })
}

async function updateProjectList() {

  pageLoadingShow()
  await allProject().then(res => {
    projectList.value = res.data
  }).catch(err=>{
    naiveMsg.error("项目列表获取失败请查看控制台报错")
    console.log(err)
  })
  pageLoadingHide()

}

const timestampFormat = computed(() => {
  return (timestamp: number) => {
    const date = new Date(timestamp); // 将时间戳转换为毫秒
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2); // 月份从0开始，所以需要加1
    const day = ("0" + date.getDate()).slice(-2);
    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);
    const seconds = ("0" + date.getSeconds()).slice(-2);

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }
})
useResizeObserver(apkAddCardRef, (entries: any) => {
  const entry = entries[0]
  const {height} = entry.contentRect
  apkHisListHeight.value = height
})
onMounted(async () => {
  await updateProjectList()
})
watch(darkMode, (newVal, _) => {
  emit("useDarkTheme", newVal)
})
const pageDarkModeSwitchStyle = (info: { focused: boolean, checked: boolean }) => {
  const style: CSSProperties = {}
  console.log(info.checked)
  if (info.checked) {
    style.background = '#002a1a'
    if (info.focused) {
      style.boxShadow = '0 0 0 2px #d0305040'
    }
  } else {
    style.background = '#ff9100'
    if (info.focused) {
      style.boxShadow = '0 0 0 2px #2080f040'
    }
  }
  return style
}
</script>

<template>
  <!--  项目列表-->
  <n-drawer v-model:show="showProjects" :width="502" :placement="placement">
    <n-drawer-content closable title="项目列表">
      <template #footer>
        <n-button @click="upsertProjectDialog?.showDialog(false)" strong round type="primary">
          <template #icon>
            <n-icon>
              <PlaylistAddOutlined/>
            </n-icon>
          </template>
          新增项目
        </n-button>
      </template>
      <div class="project-list">
        <n-el
            :class="[item.projectId==nowReviewProject.projectId?'project-item-active':'']"
            class="project-item"
            v-for="item in projectList" @click="selectProject(item)"
            tag="div"
        >
          <span>{{ item.name }}</span>
          <n-popover trigger="hover">
            <template #trigger>
              <n-button style="margin-right: 5px" strong secondary type="primary">
                查看ID
              </n-button>
            </template>
            <span> {{ item.projectId }}</span>
          </n-popover>
          <n-button style="margin-right: 5px" @click="upsertProjectDialog?.showDialog(true,item)" strong secondary
                    type="info">编辑
          </n-button>
          <n-button @click="delProjectConfirm(item)" strong secondary
                    type="error">删除
          </n-button>
        </n-el>
      </div>
    </n-drawer-content>
  </n-drawer>
  <!--  内容-->
  <n-el tag="div" class="card" :style="`box-shadow: 0 0 20px ${darkMode?'#052318':'#32969b'};`">
    <div class="head">
      <img src="../assets/apk.svg" class="logo" alt="">
      <n-el
          class="title"
          tag="p"
      >
        项目APP版本管理
      </n-el>

      <div class="operation">
        <n-gradient-text size="20" type="success" style="font-weight: bold">
          {{ nowReviewProject.name }}
        </n-gradient-text>
        <n-button  v-if="nowReviewProject.projectId!=''" @click="apkAPIDialogRef!!.showDialog(nowReviewProject)" class="list-button" size="small" :round="true"
                  type="primary">
          <template #icon>
            <n-icon>
              <FormatListBulletedRound/>
            </n-icon>
          </template>
          APK相关获取
        </n-button>
        <n-button  @click="showProjects=true" class="list-button" size="small" :round="true"
                  type="primary">
          <template #icon>
            <n-icon>
              <FormatListBulletedRound/>
            </n-icon>
          </template>
          项目列表
        </n-button>
        <n-switch :rail-style="pageDarkModeSwitchStyle" v-model:value="darkMode" size="large">
          <template #icon>
            <span v-if="darkMode">🌓</span>
            <span v-else>☀️</span>
          </template>
          <template #checked>
            深色
          </template>
          <template #unchecked>
            浅色
          </template>
        </n-switch>
      </div>
    </div>
    <Empty v-if="nowReviewProject.projectId==''" msg="内容为空，请选择项目进行操作"/>
    <div v-else class="content">
      <n-el ref="apkAddCardRef" tag="div" class="add-apk-card">
        <n-form
            class="form"
            ref="apkInfoFormRef"
            size="large"
            label-width="auto"
            require-mark-placement="right-hanging"
            :model="newApkInfo"
            :rules="apkInfoFormRules"
        >
          <n-form-item label="APK版本号" path="version">
            <n-input placeholder="请输入更新版本号" v-model:value="newApkInfo.version"></n-input>
          </n-form-item>
          <n-form-item label="APK版本代码" path="versionCode">
            <n-input-number :min="apkInfoHisList.length==0?0:apkInfoHisList[0].versionCode"
                            placeholder="请输入版本代码号"
                            v-model:value="newApkInfo.versionCode"></n-input-number>
          </n-form-item>
          <n-form-item label="APK更新内容" path="updateContent">
            <n-input placeholder="请输入更新内容" v-model:value="newApkInfo.updateContent"
                     :autosize="{ minRows: 3,maxRows: 4}" type="textarea"></n-input>
          </n-form-item>
        </n-form>
        <n-upload
            accept=".apk"
            :on-update:file-list="apkSelect"
            :default-upload="false"
            multiple
            directory-dnd
            :max="1"
        >
          <n-upload-dragger>
            <div style="margin-bottom: 12px">
              <n-icon size="48" :depth="3">
                <archive-icon/>
              </n-icon>
            </div>
            <n-text style="font-size: 16px">
              点击或者拖动APK文件到该区域来上传
            </n-text>
            <n-p depth="3" style="margin: 8px 0 0 0">
              上传的文件确保是.apk结尾的文件，确保服务器存储正常
            </n-p>
          </n-upload-dragger>
        </n-upload>
        <n-button attr-type="button" type="info" size="large" @click="submitApkInfo">提交</n-button>
      </n-el>
      <n-el tag="div" class="line"/>
      <div class="apk-history" :style="{height:`${apkHisListHeight}px`}">
        <n-timeline v-if="apkInfoHisList.length!=0" class="his-list">
          <n-timeline-item
              v-for="item in apkInfoHisList"
              type="success"
              line-type="dashed"
          >
            <template #default>
              <n-el tag="span" style="font-size: 18px">
                {{ item.updateContent }}
              </n-el>
            </template>
            <template #footer>
              <span style="font-size: 15px">
                {{ timestampFormat(item.updateTime) }}
              </span>
            </template>
            <template #header>
              <n-el tag="span" class="title">
                版本信息: {{ item.version }} ({{ item.versionCode }})
              </n-el>
            </template>
          </n-timeline-item>
        </n-timeline>
        <Empty v-else msg="没有历史信息"/>
      </div>
    </div>

  </n-el>
  <!--  加载页面-->
  <div v-show="pageLoading" class="loading">
    <n-spin size="large" :description="pageLoadingMsg">

    </n-spin>
  </div>

  <UpsertProjectDialog @upsertFinish="updateProjectList" ref="upsertProjectDialog"/>
  <APKAPIDialog ref="apkAPIDialogRef"/>
</template>

<style scoped lang="scss">
@import "Home";
</style>
