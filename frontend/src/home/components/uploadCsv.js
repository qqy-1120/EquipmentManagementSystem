import React from 'react';
import { Tooltip, message, Upload } from 'antd';
import { addEquipment } from '../service';
import { Export } from '@icon-park/react';
import dayjs from 'dayjs';
const beforeUpload = (file) => {
    const extension = file.name.split('.').pop().toLowerCase();
    if (extension !== 'csv') {
        message.error('请上传csv格式文件');
        return false;
    }
    return true;
}
const formatCsv = (str) => {
    let newStr = '';
    newStr = str?.substring(1, str.length - 2);
    return newStr
}
const csvToArray = (csv) => {
    const rows = csv.split('\n');
    const array = rows.map((row) => {
        return row.split(',');
    });
    array.pop();
    // 上传的csv文件一定要严格按照每个字段的顺序，否则会出错
    const json = array.map((item) => {
        return {
            name: formatCsv(item[0]),
            category: formatCsv(item[1]),
            number: formatCsv(item[2]),
            buy_time: dayjs(item[3]),
            state: parseInt(formatCsv(item[4])),
            is_receive: parseInt(formatCsv(item[5])),
            user_id: formatCsv(item[6]),
            username: formatCsv(item[7]),
            receive_time: dayjs(item[8]),
            location: formatCsv(item[9]),
            configuration: formatCsv(item[10])
        }
    });
    return json;
}

const UploadCsv = ({ getAllEquipments }) => {
    const onCreate = async (data) => {
        try {
            data.map(async item => { await addEquipment(item); })
            await getAllEquipments();
            message.success('添加成功');
        }
        catch (error) {
            console.log(error, 'create new equipment error');
            message.error(error.message);
        }
        finally {
            //setFile
        }
    };
    const props = {
        name: 'file',
        accept: '.csv',
        beforeUpload: beforeUpload,
        customRequest: async detail => {
            try {
                const reader = new FileReader();
                // 如果中文解析失败 GB2312
                reader.readAsText(detail.file, 'utf-8');
                reader.onload = async function () {
                    const res = csvToArray(reader.result);
                    await onCreate(res)

                };
                detail.onSuccess();
            }
            catch (error) {
                console.log(error, 'upload photo error');
                message.error('上传失败');
            }
        },
    };
    return (
        <Upload {...props}>
            <Tooltip title="批量导入">
                <Export theme="outline" size="25" fill="#36304A" strokeLinejoin="miter" strokeLinecap="square"
                />
            </Tooltip>
        </Upload>
    )
}
export default UploadCsv;