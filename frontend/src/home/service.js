import fetch from "../component/fetch";
import { pageSize } from "../component/config";
export async function getEquipmentList(pageNum) {
  return fetch({
    url: "/api/equipment/page/" + pageNum + "/" + pageSize,
    method: "get",
  });
}
export async function getSelectEquipments(pageNum, select) {
  return fetch({
    url: "/api/equipment/querypage/" + pageNum + "/" + pageSize + '?' + select,
    method: "get",
  });
}
export async function updateEquipment(equipment) {
  return fetch({
    url: "/api/equipment",
    method: "put",
    data: equipment,
  });
}
export async function addEquipment(equipment) {
  return fetch({
    url: "/api/equipment",
    method: "post",
    data: equipment,
  });
}
export async function deleteEquipment(id) {
  return fetch({
    url: "/api/equipment/" + id,
    method: "delete",
  });
}
export async function uploadPhoto(file, id) {
  const uploadImg = new window.FormData();
  uploadImg.append('file', file);
  const res = await fetch({
    url: "/api/equipment/image/" + id,
    method: "post",
    data: uploadImg,
  });
  return res;
}
export async function getItems(name) {
  return fetch({
    url: "/api/" + name,
    method: "get",
  });
}
export async function addItems(name, content) {
  return fetch({
    url: "/api/" + name,
    method: "post",
    params: content,
  });
}
