import { message } from 'antd';
const beforeUpload = (file) => {
    const isLt2M = file.size / 1024 / 1024 < 10;
    if (!isLt2M) {
        message.error('图片大小不能超过10MB');
    }
    return isLt2M;
}
export {beforeUpload};