import fetch from "../component/fetch";
export async function getEquipmentList(pageNum) {
  const pageSize = 10;
  return fetch({
    url: "/api/equipment/page/"+pageNum+"/"+pageSize,
    method: "get",
    // params: {
    //     pageNum,
    //     pageSize,
    // },
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
        url: "/api/equipment/"+id,
        method: "delete",
        // data:id,
    });
}
export async function uploadPhoto(file,id) {
    const uploadImg = new window.FormData();
    uploadImg.append('file', file);
    const res = await fetch({
        url: "/api/equipment/upload/"+id,
        method: "post",
        data: uploadImg,
    });
    return res;
  }

export async function getCategoriesList() {
    return fetch({
        url: "/api/category/list",
        method: "get",
    });
  }
  export async function addCategory(category) {
    return fetch({
        url: "/api/category/create",
        method: "post",
        params: category,
    });
  }
export async function getLocationsList(){
    return fetch({
        url: "/api/location/list",
        method: "get",
    });
}
export async function register(user){
    return fetch({
        url: "/api/register",
        method: "post",
        data: user,
    });
}
export async function getSelectEquipments(pageNum,select){
  
  const pageSize = 10;
  debugger
  return fetch({
    url: "/api/equipment/querypage/"+pageNum+"/"+pageSize+'?'+select,
    method: "get",
    // params: select,
  });
}
export async function getUsers(){
  return fetch({
    url: "/api/user/list",
    method: "get",
  });
}
export async function addLocation(location){
  return fetch({
    url: "/api/location/create",
    method: "post",
    params: location,
  });
}