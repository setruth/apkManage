import request from "../request.ts";

export function allProject() {
    return request({
        url: `projects`,
        method: "get",
    });
}

export function createProject(projectName:string) {
    console.log(`projects/${projectName}`)
    return request({
        url: `projects/${projectName}`,
        method: "post",
    });
}
export function updateProject(projectId:string,projectName:string) {
    return request({
        url: `projects/${projectId}/${projectName}`,
        method: "post",
    });
}
export function delProject(projectId:string) {
    return request({
        url: `projects/${projectId}`,
        method: "delete",
    });
}
