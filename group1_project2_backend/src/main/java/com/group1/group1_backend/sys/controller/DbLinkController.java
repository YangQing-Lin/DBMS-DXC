package com.group1.group1_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.group1.group1_backend.comm.vo.Result;
import com.group1.group1_backend.sys.entity.DbLink;
import com.group1.group1_backend.sys.entity.History;
import com.group1.group1_backend.sys.entity.HistoryGatherField;
import com.group1.group1_backend.sys.service.IDbLinkService;
import com.group1.group1_backend.sys.service.IHistoryGatherFieldService;
import com.group1.group1_backend.sys.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linqingchuan
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/dblink")
public class DbLinkController {
    @Autowired
    private IDbLinkService dbLinkService;
    @Autowired
    private IHistoryService historyService;
    @Autowired
    private IHistoryGatherFieldService historyGatherFieldService;

    @GetMapping("/all")
    public Result<List<DbLink>> getAllDbLink() {
        List<DbLink> list = dbLinkService.list();
        list.forEach(System.out::println);
        return Result.success("查询成功", list);
    }

    @GetMapping("/get_my_link")
    public Result<List<DbLink>> getMyLink(String userId) {
        LambdaQueryWrapper<DbLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DbLink::getUserId, userId);
        List<DbLink> list = dbLinkService.list(wrapper);
        if (list.size() == 0) {
            return Result.fail("查询失败");
        }
        list.forEach(System.out::println);
        return Result.success("查询成功", list);
    }

    /**
     * 测试数据库连接，失败会返回报错信息
     * @param dbLink
     * @return
     */
    @PostMapping("/test")
    public Result<?> dbLinkTest(@RequestBody DbLink dbLink) throws SQLException {
        if (dbLink.toString().length() == 0) {
            return Result.fail("参数错误");
        }
        Result<?> result = getConnect(dbLink);
        if (result.getCode() == 20001) {
            return result;
        }
        Connection conn = (Connection) result.getData();
        conn.close();
        return Result.success("连接成功");
    }

    /**
     * 添加数据库连接信息，并返回列表内所有的连接，用于前端更新数据
     * @param dbLink
     * @return
     */
    @PostMapping("/add")
    public Result<List<DbLink>> addLink(@RequestBody DbLink dbLink) {
        dbLinkService.save(dbLink);
        List<DbLink> list = dbLinkService.list();
        return Result.success("添加成功", list);
    }

    @DeleteMapping("/{dbLinkId}")
    public Result<?> deleteLink(@PathVariable("dbLinkId") Integer dbLinkId) {
        boolean b = dbLinkService.removeById(dbLinkId);
        if (b) {
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    @DeleteMapping("/delete_by_id")
    public Result<?> deleteLink(String dbLinkId) {
        boolean b = dbLinkService.removeById(Integer.valueOf(dbLinkId));
        if (b) {
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    /**
     * 以id列表来删除，【废弃】
     * @param dbLinkIds
     * @return
     */
    @DeleteMapping("/delete")
    public Result<?> deleteLinks(@RequestBody List<String> dbLinkIds) {
        boolean b = dbLinkService.removeByIds(dbLinkIds);
        if (b) {
            return Result.success("删除成功");
        }
        return Result.fail("删除失败");
    }

    /**
     * 按照id修改数据库连接的信息
     * @param linkId 前端传入需要修改的连接的id
     * @param dbLink 修改之后的各种数据（不包括linkId）
     * @return
     */
    @PutMapping("/update_link")
    public Result<?> updateDbLink(String dbLinkId, @RequestBody DbLink dbLink) {
        System.out.println("@@@@@@linkId = " + dbLinkId);
        System.out.println("######dbLink = " + dbLink);
        dbLink.setLinkId(null);
        LambdaQueryWrapper<DbLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DbLink::getLinkId, dbLinkId);
        boolean flag = dbLinkService.update(dbLink, wrapper);
        if (flag) {
            return Result.success("修改连接信息成功");
        } else {
            return Result.fail("修改连接信息失败");
        }
    }

    @GetMapping("/sql")
    public Result<?> SQLSelect(String SQL, String dbLinkId) throws SQLException {
        DbLink dbLink = dbLinkService.getById(dbLinkId);
        System.out.println(dbLinkId);
        Result<?> result = getConnect(dbLink);
        if (result.getCode() != 20000) {
            return Result.fail("连接数据库失败");
        }
        Connection conn = (Connection) result.getData();
        Statement stmt = conn.createStatement();
        String[] split = SQL.split(";");
        Map<String, List<Map<String, String>>> requestData = new HashMap<>();
        for (int i = 0; i < split.length; i ++ ) {
            String sql = split[i];
            System.out.println("sql = [" + sql + "]");
            ResultSet rs = stmt.executeQuery(sql);
            List<Map<String, String>> list = getDataList(rs);
            requestData.put("结果" + (i + 1), list);
            rs.close();
        }
        for (String sql : split) {
            sql = sql.trim();
        }
        stmt.close();
        conn.close();
        return Result.success("SQL语句查询成功", requestData);
    }

    private List<Map<String, String>> getDataList(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        List<Map<String, String>> dataList = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> e = new HashMap<>();
            for (int i = 1; i <= columnCount; i ++ ) {
                String columnName = metadata.getColumnName(i);
                String value = rs.getString(i);
                System.out.print(columnName + ": " + value + "\t");
                e.put(columnName, value);
            }
            dataList.add(e);
            System.out.println();
        }
        return dataList;
    }

    /**
     * 输入数据库连接信息，自动连接，获取所有的表信息
     * @param dbLink
     * @return
     */
    @GetMapping("/get_tables")
    public Result<?> getDbLinkTables(String dbLinkId) throws SQLException {
        DbLink dbLink = dbLinkService.getById(dbLinkId);
        Result<?> result = getConnect(dbLink);
        if (result.getCode() != 20000) {
            return Result.fail("连接数据库失败");
        }
        System.out.println(1);
        Connection conn = (Connection) result.getData();
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        String sql = "";
        if ("mysql".equals(dbLink.getLinkDbType())) {
            System.out.println(2);
            DatabaseMetaData metadata = conn.getMetaData();
            System.out.println(3);
            rs = metadata.getTables(null, null, null, new String[] {"TABLE"});
        } else if ("oracle".equals(dbLink.getLinkDbType())) {
            sql = "SELECT TABLE_NAME, COMMENTS AS REMARKS\n" +
                    "FROM user_tab_comments";
            System.out.println("sql = " + sql);
            rs = stmt.executeQuery(sql);
        } else if ("sqlserver".equals(dbLink.getLinkDbType())) {
            sql = "select\n" +
                    "a.name AS TABLE_NAME,\n" +
                    "CONVERT(NVARCHAR(100),isnull(g.[value],'-')) AS REMARKS\n" +
                    "from\n" +
                    "sys.tables a left join sys.extended_properties g\n" +
                    "on (a.object_id = g.major_id AND g.minor_id = 0)";
            System.out.println("sql = " + sql);
            rs = stmt.executeQuery(sql);
        } else if ("postgresql".equals(dbLink.getLinkDbType())) {
            sql = "SELECT\n" +
                    "relname AS TABLE_NAME,\n" +
                    "cast( obj_description ( relfilenode, 'pg_class' ) AS VARCHAR ) AS REMARKS \n" +
                    "FROM\n" +
                    "pg_class c \n" +
                    "WHERE\n" +
                    "relkind = 'r' \n" +
                    "AND relname NOT LIKE 'pg_%' \n" +
                    "AND relname NOT LIKE 'sql_%' \n" +
                    "ORDER BY\n" +
                    "relname\n";
            System.out.println("sql = " + sql);
            rs = stmt.executeQuery(sql);
        }
        System.out.println(4);
        List<Map<String, Object>> dataList = new ArrayList<>();
        System.out.println(5);
        System.out.println("SQL = " + sql);
        while(rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String tableComment = rs.getString("REMARKS");
            Map<String, Object> e = new HashMap<>();
            e.put("tableName", tableName);
            e.put("tableComment", tableComment);
            dataList.add(e);
        }
        rs.close();
        stmt.close();
        conn.close();
        printDataList(dataList);
        return Result.success("查询数据库信息成功", dataList);
    }


    /**
     * 获取表每个字段的设计信息并返回
     *
     * @param dbLink    用于连接数据库
     * @param tableName 需要查询的表名
     * @param stmt
     * @return
     * @throws SQLException
     */
    @GetMapping("/get_table_info")
    public Result<?> getTableInfo(String tableName, String dbLinkId) throws SQLException {
        DbLink dbLink = dbLinkService.getById(dbLinkId);
        Result<?> result = getConnect(dbLink);
        if (result.getCode() != 20000) {
            return Result.fail("连接数据库失败");
        }
        Connection conn = (Connection) result.getData();
        Statement stmt = conn.createStatement();
        System.out.println(dbLink);
        String sql = "";
        if ("mysql".equals(dbLink.getLinkDbType())) {
            sql = "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, COLUMN_COMMENT " +
                    "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'";
        } else if ("oracle".equals(dbLink.getLinkDbType())) {
            sql = "select t.COLUMN_NAME AS COLUMN_NAME,\n" +
                    "s.DATA_TYPE AS DATA_TYPE,\n" +
                    "s.DATA_LENGTH AS CHARACTER_MAXIMUM_LENGTH,\n" +
                    "t.COMMENTS AS COLUMN_COMMENT\n" +
                    "  from all_col_comments t, all_tab_columns s\n" +
                    " where t.Table_Name = '" + tableName.toUpperCase() + "'\n" +
                    "   and s.Table_Name = '" + tableName.toUpperCase() + "'\n" +
                    "   and t.COLUMN_NAME = s.COLUMN_NAME\n" +
                    " order by t.column_name";
        } else if ("sqlserver".equals(dbLink.getLinkDbType())) {
            sql = " SELECT convert(varchar(100), a.COLUMN_NAME) AS COLUMN_NAME, \n" +
                        "convert(varchar(100), a.DATA_TYPE) AS DATA_TYPE, \n" +
                        "convert(varchar(100), a.CHARACTER_MAXIMUM_LENGTH) AS CHARACTER_MAXIMUM_LENGTH,\n" +
                        "convert(varchar(100), b.COLUMN_COMMENT) AS COLUMN_COMMENT" +
                    " FROM (\n" +
                    " SELECT  COLUMN_NAME,\n" +
                        "DATA_TYPE,\n" +
                        "CHARACTER_MAXIMUM_LENGTH\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'\n" +
                    ") AS a,\n" +
                    "(\n" +
                    "select a.name tabname,a1.name 'COLUMN_NAME',b.value 'COLUMN_COMMENT' \n" +
                    "from sysobjects a left join  sys.columns a1 on a.id = a1.object_id\n" +
                    " left join sys.extended_properties b on b.major_id = a.id and b.minor_id = a1.column_id\n" +
                    " where a.name= '" + tableName.toUpperCase() + "'\n" +
                    " ) AS b\n" +
                    " WHERE a.COLUMN_NAME = b.COLUMN_NAME";
        } else if ("postgresql".equals(dbLink.getLinkDbType())) {
            sql = "SELECT\n" +
                    "a.attname AS COLUMN_NAME,\n" +
                    "format_type ( a.atttypid, a.atttypmod ) AS DATA_TYPE,\n" +
                    "a.atttypmod - 4 as CHARACTER_MAXIMUM_LENGTH,\n" +
                    "col_description ( a.attrelid, a.attnum ) AS COLUMN_COMMENT\n" +
                    "FROM\n" +
                    "pg_class AS c,\n" +
                    "pg_attribute AS a \n" +
                    "WHERE\n" +
                    "c.relname = '" + tableName + "' \n" +
                    "AND a.attrelid = c.oid \n" +
                    "AND a.attnum >0\n";
        }
        System.out.println("sql = " + sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Map<String, Object>> columnList = new ArrayList<>();
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("DATA_TYPE");
            int maxLength = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
            String columnComment = rs.getString("COLUMN_COMMENT");
            Map<String, Object> e = new HashMap<>();
            e.put("columnComment", columnComment);
            e.put("columnName", columnName);
            e.put("dataType", dataType);
            e.put("maxLength", maxLength);
            columnList.add(e);
        }
        printDataList(columnList);
        Map<String, Object> data = new HashMap<>();
        data.put("columnList", columnList);
        data.put("tableName", tableName);
        data.put("dataCount", getCount(stmt, tableName));
        data.put("columnCount", columnList.size());

        rs.close();
        stmt.close();
        conn.close();
        return Result.success("获取表信息成功", data);
    }


    /**
     * 全量导出：返回表中的所有数据，或选定字段的数据
     * @param linkId
     * @param tableName
     * @param tableComment
     * @param gatherState
     * @param gatherType
     * @param exportType
     * @param exportPath
     * @param filterCriteria
     * @param data
     * @return
     * @throws SQLException
     */
    @PostMapping("/full_export")
    public Result<?> fullExport(
            String dbLinkId, String tableName,
            String tableComment, String gatherState, String gatherType, String exportType, String exportPath,
            String filterCriteria, @RequestBody Map<String, Object> data
    ) throws SQLException {
        System.out.println(data + " : " + data.get("linkPort"));
        DbLink dbLink = dbLinkService.getById(dbLinkId);
        System.out.println(dbLink.toString());
        List<String> columnNames = (List<String>) data.get("columnNames");
        System.out.println("columnNames = " + columnNames);
        Result<?> result = getConnect(dbLink);
        if (result.getCode() != 20000) {
            return Result.fail("连接数据库失败");
        }
        Connection conn = (Connection) result.getData();
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        String sql = null;
        if (columnNames == null || columnNames.size() == 0) {
            if (filterCriteria == null || filterCriteria.length() == 0) {
                sql = "SELECT * FROM " + tableName;
            } else {
                sql = "SELECT * FROM " + tableName + " WHERE " + filterCriteria  + "";
            }
        } else {
            System.out.println(columnNames);
            String nameStr = columnNames.stream().collect(Collectors.joining(", "));
            System.out.println("name str = " + nameStr);
            if (filterCriteria == null || filterCriteria.length() == 0) {
                sql = "SELECT " + nameStr + " FROM " + tableName;
            } else {
                sql = "SELECT " + nameStr + " FROM " + tableName + " WHERE " + filterCriteria  + "";
            }
        }
        System.out.println("sql = " + sql);
        rs = stmt.executeQuery(sql);
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        Map<String, List<Map<String, String>>> requestData = new HashMap<>();
        List<Map<String, String>> dataList = getDataList(rs);
        requestData.put("requestData", dataList);
        System.out.println(dataList.toString());
        // 先保存一次历史记录
        History history = new History(dbLinkId, tableName, tableComment, columnCount, dataList.size(), gatherState, gatherType, exportType, exportPath, filterCriteria);
        System.out.println("history = " + history.toString());
        historyService.save(history);
        // 报错数据后获取它的history_id
        LambdaQueryWrapper<History> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(History::getGatherTime).last("LIMIT 1");
        History one = historyService.getOne(wrapper);
        System.out.println("ONE = " + one.toString());
        Integer historyId = one.getHistoryId();
        rs.close();
        stmt.close();
        conn.close();
        // 使用history_id存储history_gather_field信息
        Result<?> result1 = saveHistoryGatherField(
                historyId,
                tableName,
                columnNames,
                dbLinkId
        );
        System.out.println("result1 = " + result1.toString());
        return Result.success("获取表信息成功", requestData);
    }

    private Result<?> saveHistoryGatherField(
            int historyId,
            String tableName,
            List<String> columnNames,
            String dbLinkId) throws SQLException {

        System.out.println("in get table info");
        Result<?> result = getTableInfo(tableName, dbLinkId);
        System.out.println("get table info DONE");
        if (result.getCode() != 20000) {
            return result;
        }
        Map<String, Object> data = (Map<String, Object>) result.getData();
        List<Map<String, Object>> columnList = (List<Map<String, Object>>) data.get("columnList");
        System.out.println("history ID = " + historyId);
        List<String> newColumnNames = new ArrayList<>();
        for (String columnName : columnNames) {
            newColumnNames.add(columnName.replace("\"", ""));
        }
        System.out.println("newColumnNames: " + newColumnNames.toString());
        boolean flag = false;
        for (Map<String, Object> column : columnList) {
            String columnName = (String) column.get("columnName");
            System.out.println("1: " + columnName + "\t\t2: " + newColumnNames);
            if (newColumnNames.contains(columnName.replace("\"", "")) || newColumnNames.contains(columnName.replace("\"", "").toLowerCase())) {
                HistoryGatherField historyGatherField = new HistoryGatherField(historyId, (String) column.get("columnComment"), (String) column.get("columnName"), (String) column.get("dataType"), (Integer) column.get("maxLength"));
                boolean save = historyGatherFieldService.save(historyGatherField);
                System.out.println("save: " + save);
                if (save) {
                    flag = true;
                }
            }
        }
        if (flag) {
            return Result.success("保存历史记录详细信息成功");
        }
        return Result.fail("保存历史记录详细信息失败");
    }

    /**
     * 增量导出：相比于全量导出，多了一个根据字段值区间限制结果的查询条件
     * @param tableName
     * @param columnName 需要做限制的字段名
     * @param start 开始值
     * @param end 结束值
     * @param filterCriteria 其他过滤条件
     * @param data
     * @return
     * @throws SQLException
     */
    @PostMapping("/partial_export")
    public Result<?> partialExport(
            String dbLinkId,
            String tableName, String tableComment, String columnName,
            String start, String end, String filterCriteria,
            String gatherState, String gatherType, String exportType, String exportPath,
            @RequestBody Map<String, Object> data
    ) throws SQLException {
        System.out.println("info: " + columnName + " " + start + " " + end);
        String newFilterCriteria = "";
        if (columnName != null && columnName.length() > 0 && start != null && start.length() > 0 && end != null && end.length() > 0) {
            newFilterCriteria = columnName + " BETWEEN " + start + " AND " + end + ((filterCriteria == null || filterCriteria.length() == 0) ? "" : " AND " + filterCriteria);
        } else {
            newFilterCriteria = filterCriteria;
        }
        System.out.println("### newFilterCriteria = " + newFilterCriteria);
        return fullExport(dbLinkId, tableName, tableComment, gatherState, gatherType, exportType, exportPath, newFilterCriteria, data);
    }

    /**
     * 获取一个数据表一共有多少行
     * @param stmt 可使用sql查询的数据库对象
     * @param tableName 表名
     * @return 返回数据行数，-1表示查询失败
     * @throws SQLException
     */
    private int getCount(Statement stmt, String tableName) throws SQLException {
        ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + "");
        if (countRs.next()) {
            int count = countRs.getInt(1);
            return count;
        }
        return -1;
    }

    /**
     * 方便的输出返回信息
     * @param dataList
     */
    private void printDataList(List<Map<String, Object>> dataList) {
        for (Map<String, Object> map : dataList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 使用数据库连接信息去连接数据库，连接成功后返回connection对象
     * @param dbLink
     * @return
     */
    private Result<?> getConnect(DbLink dbLink) {
        System.out.println("in getConnect");
        System.out.println(dbLink.toString());
        try {
            String url = "";
            System.out.println(dbLink.getLinkDbType());
            if ("mysql".equals(dbLink.getLinkDbType())) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                // jdbc:oracle:thin:localhost:1521:orcl
                url = "jdbc:" + dbLink.getLinkDbType() + "://" + dbLink.getLinkDbIp() + ":" + dbLink.getLinkPort() + "/" + dbLink.getLinkDbName();
            } else if ("oracle".equals(dbLink.getLinkDbType())) {
                // Oracle服务默认端口：1521
                Class.forName("oracle.jdbc.driver.OracleDriver");
                url = "jdbc:" + dbLink.getLinkDbType() + ":thin:" + dbLink.getLinkDbIp() + ":" + dbLink.getLinkPort() + ":orcl";
            } else if ("sqlserver".equals(dbLink.getLinkDbType())) {
                // sqlserver服务默认端口：1433。如果这个端口连接不上，可以打开sqlserver的配置管理器
                // 打开“SQL Server 服务”选单，可以看到“SQL Server (MSSQL SERVER)”服务的进程ID：6964
                // 打开cmd，输入netstat -aon|findstr "6964"就可以查看这个服务监听的端口号了（查到的是1434）
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // jdbc:sqlserver://localhost:1434;DatabaseName=order_system
                url = "jdbc:" + dbLink.getLinkDbType() + "://" + dbLink.getLinkDbIp() + ":" + dbLink.getLinkPort() + ";DatabaseName=" + dbLink.getLinkDbName();
            } else if ("postgresql".equals(dbLink.getLinkDbType())) {
                Class.forName("org.postgresql.Driver");
                // jdbc:postgresql://127.0.0.1:5432/postgres
                url = "jdbc:" + dbLink.getLinkDbType() + "://" + dbLink.getLinkDbIp() + ":" + dbLink.getLinkPort() + "/" + dbLink.getLinkDbName();
            }
            Connection connection = DriverManager.getConnection(url, dbLink.getLinkUserName(), dbLink.getLinkPassword());
            System.out.println(connection);
            return Result.success(connection);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return Result.fail("连接数据库失败");
        }
    }
}
