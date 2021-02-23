package com.atguigu.app;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.atguigu.constant.GmallConstants;
import com.atguigu.utils.MyKafkaSender;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */
public class CanalClient {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        //1.获取连接对象
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("hadoop102", 11111), "example", "", "");

        while (true){
            canalConnector.connect();

            canalConnector.subscribe("gmall.*");
            //获取message,一次抓取则有一条message
            Message message = canalConnector.get(100);
            //获取entry，每个sql命令对应一个entry，一条massage里面可能有多个entry
            List<CanalEntry.Entry> entries = message.getEntries();
            //没有数据则规避
            if (entries.size() <=0){
                System.out.println("没有数据，休息");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //遍历每个entry
            for (CanalEntry.Entry entry : entries) {
                //获取entry的类型，主要想要的类型为ROWDATA
                CanalEntry.EntryType entryType = entry.getEntryType();
                //获取entry所在的表名tablename
                String tableName = entry.getHeader().getTableName();
                //判断entry类型是否为ROWDATA，如果是则进行操作
                if (CanalEntry.EntryType.ROWDATA.equals(entryType)){
                    //entry是序列化的，不可以直接使用
                    ByteString storeValue = entry.getStoreValue();
                    //对entry进行反序列化，使用RowChange 接口
                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                    //获取事件的类型
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    //反序列化后获取每一行的值，一行可能有多个列
                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();

                    hanler(tableName,eventType,rowDatasList);

                }

            }

        }

    }

    private static void hanler(String tableName, CanalEntry.EventType entryType,
                               List<CanalEntry.RowData> rowDatasList) {
        //根据表名，及事件类型获取数据
        if (tableName.equals("order_info") && CanalEntry.EventType.INSERT.equals(entryType)){
            for (CanalEntry.RowData rowData : rowDatasList) {

                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

                JSONObject jsonObject = new JSONObject();

                for (CanalEntry.Column column : afterColumnsList) {
                    jsonObject.put(column.getName(),column.getValue());
                }
                System.out.println(jsonObject.toString());
                MyKafkaSender.send(GmallConstants.KAFKA_TOPIC_ORDER,jsonObject.toString());
            }
        }
    }
}





























