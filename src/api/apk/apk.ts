import request from "../request.ts";
import {APKInfo} from "../../CommonModel.ts";

export function addAPKInfo(data:APKInfo) {
    return request({
        url: `apkManage`,
        method: "post",
        data
    });
}


export function allAPKInfo(projectId:string) {
    return request({
        url: `apkManage/${projectId}`,
        method: "get",
    });
}
export function newestAPKInfo(projectId:string) {
    return request({
        url: `apkManage/newest/${projectId}`,
        method: "get",
    });
}

export function uploadApk(file:File,projectId:string) {
    let formData = new FormData()
    formData.append('apkFile', file)
    return request({
        url: `apkManage/upload/${projectId}`,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        method: "post",
        data: formData
    });
}
