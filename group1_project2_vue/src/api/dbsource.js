import request from "@/utils/request";

export default {
  getdball() {
    return request({
      url: "/dblink/all",
      method: "get",
    });
  },
  adddbsource(data) {
    return request({
      url: "/dblink/add",
      method: "post",
      data,
    });
  },
  changedbsource(data) {
    return request({
      url: "/dblink/update_link?dbLinkId=" + data.linkId,
      method: "put",
      data,
    });
  },
  testdb(data) {
    return request({
      url: "/dblink/test",
      method: "post",
      data,
    });
  },
  deletedb(data) {
    return request({
      url: "/dblink/" + data,
      method: "delete",
    });
  },
};
