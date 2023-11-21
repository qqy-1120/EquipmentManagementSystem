import React, { useRef, useEffect, useState } from 'react';
import { Button, Form, Input, Modal, DatePicker, Select, Divider, Space, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { getItems, addItems } from '../service';
const CollectionCreateForm = ({ open, onCreate, onCancel }) => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState('');
  const ctgInputRef = useRef(null);
  const getCategories = async () => {
    const categoryList = await getItems('categories');
    const cateArray = categoryList.map((item) => { return item.category });
    setCategories(cateArray);
  };
  const addItem = async (e) => {
    e.preventDefault();
    if (newCategory === '') return message.error('新类别不能为空');
    await addItems('category', { category: newCategory });
    getCategories();
    setNewCategory('');
    setTimeout(() => {
      ctgInputRef.current?.focus();
    }, 0);
  }
  useEffect(() => {
    getCategories();
  }, []);
  const onNameChange = (event) => {
    setNewCategory(event.target.value);
  }
  const [form] = Form.useForm();
  return (
    <Modal
      open={open}
      title="录入设备信息"
      okText="确认"
      cancelText="取消"
      onCancel={onCancel}
      onOk={async () => {
        form
          .validateFields()
          .then((values) => {
            form.resetFields();
            onCreate(values);
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
                    onChange={onNameChange}
                    onKeyDown={(e) => e.stopPropagation()}
                  />
                  <Button type="text" icon={<PlusOutlined />} onClick={addItem}>
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
          rules={[
            {
              required: true,
              message: '输入设备类别',
            },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item name="configuration" label="配置" >
          <Input />
        </Form.Item>
        <Form.Item name="buy_time" label="购入时间">
          <DatePicker />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default CollectionCreateForm;