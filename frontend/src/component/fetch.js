import axios from 'axios';
import { message } from 'antd';
import dayjs from 'dayjs';

export const HOST = 'https://10.177.44.94:9091';
const urlRegex = /page|querypage/;
export default function fetch(option = {}) {
  const { url, byteResponse = false, id,...rest } = option;
  return axios({
    url:url,
    // url: `${HOST}${url}`,
    withCredentials: true,
    ...rest,
  }).then(res => {    
    const { code, data } = res.data;
    const msg=res.data.message;
    if (code === '200') {
      if(urlRegex.test(url)){
        const records = data.records.map(item => {
          return {
            ...item,
            key: item.id,
            receive_time : item.receive_time&&item.receive_time!=='' ? dayjs(item.receive_time) : '',
            buy_time : item.buy_time&&item.buy_time!=='' ? dayjs(item.buy_time) : '',
          }
        })
        return records;
      }
      else if(/user/.test(url)){
        const username=data.map(item => {
          return item.username;
        });
        return username;
        }
      return data;
    }
    if (byteResponse) {
      return data;
    }
    message.error(msg || '服务器错误，请重试');
    return Promise.reject(new Error(msg));
  });
}