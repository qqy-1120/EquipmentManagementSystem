import { message } from 'antd';
import dayjs from 'dayjs';
const beforeUpload = (file) => {
    const isLt2M = file.size / 1024 / 1024 < 10;
    if (!isLt2M) {
        message.error('图片大小不能超过10MB');
    }
    const isPic = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isPic) {
        message.error('请上传jpg/png格式图片');
    }
    return isLt2M && isPic;
}
const equiFormat = (data) => {
    const records = data.map(item => {
        return {
            ...item,
            key: item.id,
            receive_time: item.receive_time && item.receive_time !== '' ? dayjs(item.receive_time) : '',
            buy_time: item.buy_time && item.buy_time !== '' ? dayjs(item.buy_time) : '',
            location: item.location ? item.location : '',
            username: item.username ? item.username : '',
        }
    })
    return records;
}
export { beforeUpload, equiFormat };