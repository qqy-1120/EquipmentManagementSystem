import axios from 'axios';
import { localeMessage } from './utils';
export const HOST = 'https://10.177.44.94:9091';
export default function fetch(option = {}) {
  const { url, byteResponse = false, id,...rest } = option;
  return axios({
    url:url,
    withCredentials: true,
    headers: {
      'token': localStorage.getItem('token'),
    },
    ...rest,
  }).then(res => {    
    const { code, data } = res.data;
    const msg=res.data.message;
    if (code === '200') {
      return data;
    }
    if (byteResponse) {
      return data;
    }
    // message.error(msg || '服务器错误，请重试');
    return Promise.reject(new Error(localeMessage(msg)));
  });
}