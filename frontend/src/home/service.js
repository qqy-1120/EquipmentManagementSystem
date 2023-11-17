import fetch from "../component/fetch";
export async function getEquipmentList(pageNum) {
  const pageSize = 10;
  return fetch({
    url: "/api/equipment/page/" + pageNum + "/" + pageSize,
    method: "get",
  });
}
export async function getSelectEquipments(pageNum, select) {
  const pageSize = 10;
  return fetch({
    url: "/api/equipment/querypage/" + pageNum + "/" + pageSize + '?' + select,
    method: "get",
  });
}
export async function updateEquipment(equipment) {
  return fetch({
    url: "/api/equipment/update",
    method: "put",
    data: equipment,
  });
}
export async function addEquipment(equipment) {
  return fetch({
    url: "/api/equipment/create",
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
    url: "/api/equipment/upload/" + id,
    method: "post",
    data: uploadImg,
  });
  return res;
}

export async function getFilters(name) {
  return fetch({
    url: "/api/" + name + "/list",
    method: "get",
  });
}

export async function addItems(name, content) {
  return fetch({
    url: "/api/" + name + "/create",
    method: "post",
    params: content,
  });
}
