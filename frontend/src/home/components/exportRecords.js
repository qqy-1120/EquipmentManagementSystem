import { Tooltip, message } from "antd";
import { Download } from "@icon-park/react";
import { getItems } from "../service";
import { equiFormat } from "../../component/utils";
import dayjs from "dayjs";
const formatDate = (date) => {
  const formatedDate=dayjs(date).format("YYYY-MM-DD")==='Invalid Date'?'':dayjs(date).format("YYYY-MM-DD");
  return formatedDate;
};
const jsonToCsv = (data) => {
  let csv = 'id,名称,类别,资产编号,购入时间,使用状态,使用者,领用时间,位置,配置\n';
  const records = equiFormat(data);
  records.map(item => {
    csv += `${item.id + '\t'},${item.name + '\t'},${item.category + '\t'},${item.number + '\t'},${formatDate(item.buy_time) + '\t'},${item.state + '\t'},${item.username + '\t'},${formatDate(item.receive_time) + '\t'},${item.location + '\t'},${item.configuration + '\t'}\n`;
  })
  return csv;
}
const ExportRecords = () => {
  const downloadRecords = async () => {
    try {
      const records = await getItems('equipments');
      debugger
      const result = jsonToCsv(records);
      let url = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(result);
      var link = document.createElement('a');
      link.href = url;
      link.download = 'records.csv';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
    catch (error) {
      console.log(error, 'download error');
      message.error(error.message);
    }
  }
  return (

    <Tooltip title="下载记录">
      <Download theme="outline" size="25" fill="#36304A" strokeLinejoin="miter" strokeLinecap="square" onClick={downloadRecords} />
    </Tooltip>

  )
}
export default ExportRecords;