import React, { useState } from 'react';
import { UploadOutlined,ImportOutlined } from '@ant-design/icons';
import { Button, Tooltip,message, Upload } from 'antd';
import { addEquipment } from '../service';
import { Export } from '@icon-park/react'
const csvToArray = (csv) => {
    const rows = csv.split('\n');
    // console.log(rows,'kc rows');
    const array = rows.map((row) => {
        return row.split(',');
    });
    array.pop();
    const json = array.map((item) => {
        return {
            name: item[0],
            category: item[1],
            number: item[2],
            buy_time: item[3],
            state: item[4],
            is_receive: item[5],
            receive_time: item[6],
            user_id: item[7],
            username: item[8],
            location: item[9],
            configuration: item[10]
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
        // beforeUpload: beforeUpload,
        customRequest: async detail => {
            try {
                console.log(detail.file);
                // setFile(detail.file);
                const reader = new FileReader();
                // 中文解析失败
                reader.readAsText(detail.file, 'utf-8');
                reader.onload = async function () {
                    console.log(reader.result);
                    debugger
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