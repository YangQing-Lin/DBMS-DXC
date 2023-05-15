import request from "@/utils/request";

export default {
    getTabelInfo(data) {
        console.log("get table info: ", data)
        return request({
            url: '/dblink/get_table_info?tableName=' + data.tablename + '&dbLinkId=' + data.linkid,
            method: "get",
        });
    },
    getExportInfo(data) {
        console.log("partial export: ", data);
        return request({
            url: '/dblink/partial_export?' + data.path,
            method: "post",
            data: data.body
        });
    },
    getFullExport(data) {
        console.log("full export data body: ", data.body)
        return request({
            url: '/dblink/full_export?' + data.path,
            method: "post",
            data: data.body
        });
    },
    getHistory(data) {
        return request({
            url: '/history/all',
            method: "get",
        });
    },
    getHistoryByLinkId(linkId, tableName) {
        return request({
            url: '/history/get?linkId=' + linkId + '&tableName=' + tableName,
            method: "get",
        })
    },
    getHistoryDetail(data) {
        console.log("history id: ", data.historyid)
        return request({
            url: 'history_info/get?historyId=' + data.historyid,
            method: "get",
        });
    },
}
