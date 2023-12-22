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
            configuration: item.configuration ? item.configuration : ''
        }
    })
    return records;
}
const isValidStr = (str, maxLength) => {
    // Check if the string is all whitespace
    // Check if the string is too long
    return str.trim().length === 0 || str.length > maxLength ? false : true;
}
const localeMessage = (message) => {
    switch (message) {
        case 'Invalid parameters.':
            return '参数错误';
        case 'Fail to save the requested item to database.':
            return '数据库保存失败';
        case 'Fail to remove the requested item from database.':
            return '删除失败';
        case 'Fail to retrieve the requested item from database.':
            return '获取失败';
        case 'User not found.':
            return '用户不存在';
        case 'Wrong user name or password.':
            return '用户名或密码错误';
        case 'User already exists.':
            return '用户已存在';
        case 'Unsupported file format (file ext. error)':
            return '文件格式错误';
        case 'Please log in to your account.':
            return '请登录';
        case 'Login failed, please try again.':
            return '登录失败，请重试';
        case 'Token has expired, please log in again.':
            return '登录已过期，请重新登录';
        case 'Token is illegal, please log in again.':
            return '非法登录，请重新登录';
        case 'Logout failed, please try again.':
            return '登出失败，请重试';
        case 'Redis connection failed.':
            return 'Redis连接失败';
        case 'Too many failed login attempts, please try again after 5 minutes.':
            return '登录失败次数过多，请5分钟后重试';
        case 'Insufficient permissions, access denied.':
            return '权限不足，拒绝访问';
        default:
            return '服务器错误';
    }
}
const localeUsername = (username) => {
    switch (username) {
        case 'kangchun':
            return '康春';
        case 'qiuqingyang':
            return '邱清扬';
        case 'luoxuchuan':
            return '罗旭川';
        case 'zengruiying':
            return '曾瑞莹';
        case 'huyongxiang':
            return '胡永祥';
        case 'hushuqing':
            return '胡述清';
        case 'wangxuan':
            return '王轩';
        case 'wangyingchuan':
            return '王颖川'
        case 'duyuxuan':
            return '杜雨轩'
        case 'caimingxuan':
            return '蔡铭轩'
        case 'sunhan':
            return '孙晗'
        case 'wushengnan':
            return '吴胜男'
        case 'liujianing':
            return '柳嘉宁'
        case 'maopenglei':
            return '毛鹏磊'
        case 'zhaowenxuan':
            return '赵文轩'
        case 'raozihao':
            return '饶子豪'
        case 'cuimohan':
            return '崔漠寒'
        case 'admin':
            return '管理员'
        case 'bz':
            return 'bz公用'
        default:
            return username;
    }

}
export { beforeUpload, equiFormat, isValidStr, localeMessage, localeUsername }