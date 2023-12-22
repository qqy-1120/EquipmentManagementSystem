import React, { useRef, useEffect, useState } from 'react';
import { Button, Form, Input, Modal, DatePicker, Select, Divider, Space, message,Image,Upload } from 'antd';
import { PlusOutlined} from '@ant-design/icons';
import { getItems, addItems } from '../service';
import { beforeUpload } from '../../component/utils';
const CollectionCreateForm = ({ open, onCreate,newImg,changeNewImgUrl, changeImageFile,onCancel }) => {
  const [categories, setCategories] = useState([]);
  const [locations, setLocations] = useState([]);
  const [newCategory, setNewCategory] = useState('');
  const [newLocation, setNewLocation] = useState('');
  // const [file, setFile] = useState('');
  // const [imageUrl, setImgUrl] = useState('');
  const ctgInputRef = useRef(null);
  const locInputRef = useRef(null);
  const uploadButton =  (
    <div>
        <a>上传</a>
    </div>
)
  const props = {
    name: 'file',
    beforeUpload: beforeUpload,
    customRequest: async detail => {
        try {
          changeImageFile(detail.file);
            // setFile(detail.file);
            const reader = new FileReader();
            reader.readAsDataURL(detail.file);
            reader.onload = function () {
              changeNewImgUrl(reader.result);
            };
            detail.onSuccess();
        }
        catch (error) {
            console.log(error, 'upload photo error');
            message.error('上传失败');
        }
    },
    headers: {
        authorization: 'authorization-text',
    },
    showUploadList: false,
    fileList: [],
};
  const getCategories = async () => {
    const categoryList = await getItems('categories');
    const cateArray = categoryList.map((item) => { return item.category });
    setCategories(cateArray);
  };
  const getLocations = async () => {  
    const locationList = await getItems('locations');
    const locaArray = locationList.map((item) => { return item.location });
    setLocations(locaArray);
  }
  const addCategory = async (e) => {
    e.preventDefault();
    if (newCategory === '') return message.error('新类别不能为空');
    await addItems('category', { category: newCategory });
    getCategories();
    setNewCategory('');
    setTimeout(() => {
      ctgInputRef.current?.focus();
    }, 0);
  }
  const addLocation = async (e) => {
    e.preventDefault();
    if (newLocation === '') return message.error('新位置不能为空');
    await addItems('location', { location: newLocation });
    getLocations();
    setNewLocation('');
    setTimeout(() => {
      locInputRef.current?.focus();
    }, 0);
  }
  useEffect(() => {
    getCategories();
    getLocations();
  }, []);
  const onCategoryChange = (event) => {
    setNewCategory(event.target.value);
  }
  const onLocationChange = (event) => {
    setNewLocation(event.target.value);
  }
  const [form] = Form.useForm();
  return (
    <Modal
      open={open}
      title="录入设备信息"
      okText="确认"
      cancelText="取消"
      onCancel={async ()=>{
        onCancel();
        form.resetFields();
      }  }
      onOk={async () => {
        form
          .validateFields()
          .then(async (values) => {
            await onCreate(values);
            form.resetFields();     
          })
          .catch((info) => {
            console.log('Validate Failed:', info);
          });
      }}
    >
      <Form
        form={form}
        layout="vertical"
        name="form_in_modal"
      >
        <Form.Item
          name="name"
          label="名称"
          rules={[
            {
              required: true,
              message: '输入设备名称',
            },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="category"
          label="类别"
          rules={[
            {
              required: true,
              message: '输入设备类别',
            },
          ]}
        >
          <Select
            style={{
              width: 145,
            }}
            placeholder="选择类别"
            dropdownRender={(menu) => (
              <>
                {menu}
                <Divider style={{ margin: '8px 0', }} />
                <Space style={{ padding: '0 8px 4px', }}>
                  <Input
                    placeholder="新类别"
                    ref={ctgInputRef}
                    value={newCategory}
                    onChange={onCategoryChange}
                    onKeyDown={(e) => e.stopPropagation()}
                  />
                  <Button type="text" icon={<PlusOutlined />} onClick={addCategory}>
                  </Button>
                </Space>
              </>
            )}
            options={categories.map((item) => ({
              label: item,
              value: item,
            }))}
          />
        </Form.Item>
        <Form.Item
          name="number"
          label="资产编号"
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="location"
          label="位置"
        >
          <Select
            style={{
              width: 145,
            }}
            placeholder="选择位置"
            dropdownRender={(menu) => (
              <>
                {menu}
                <Divider style={{ margin: '8px 0', }} />
                <Space style={{ padding: '0 8px 4px', }}>
                  <Input
                    placeholder="新位置"
                    ref={locInputRef}
                    value={newLocation}
                    onChange={onLocationChange}
                    onKeyDown={(e) => e.stopPropagation()}
                  />
                  <Button type="text" icon={<PlusOutlined />} onClick={addLocation}>
                  </Button>
                </Space>
              </>
            )}
            options={locations.map((item) => ({
              label: item,
              value: item,
            }))}
          />
        </Form.Item>
        <Form.Item name="configuration" label="配置" >
          <Input />
        </Form.Item>
        <Form.Item name="buy_time" label="购入时间">
          <DatePicker placeholder="选择日期" />
        </Form.Item>
        <Form.Item
          name="photo"
          label="照片"
        >
             <div className='inputPhoto'>
                <div style={{ display: 'inline-block', verticalAlign: 'middle' }}>{newImg && <Image src={newImg} alt="pic" style={{ width: '40px' }} />}</div>
                <div style={{ display: 'inline-block' }}><Upload
                    {...props}
                    name="avatar"
                    className="avatar-uploader"
                    showUploadList={false}
                >
                    {uploadButton}
                </Upload></div>
            </div>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default CollectionCreateForm;