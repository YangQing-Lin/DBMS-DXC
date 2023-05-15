import request from "@/utils/request";

export default {
    usesql(sql) {
        return request({
            url: "/dblink/sql?dbLinkId=" + sql.id + "&SQL=" + sql.note,
            method: "get",

        });
    },
    gettabel(id) {
        console.log(id)
        return request({
            url: '/dblink/get_tables?dbLinkId=' + id,
            method: "get",
        });
    },
    gettabelinfo(data) {
        return request({
            url: '/dblink/get_table_info?tableName=' + data.tablename + '&dbLinkId=' + data.linkid,
            method: "get",
        });
    },

}