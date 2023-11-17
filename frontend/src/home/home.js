import { Form, Image, Upload, Badge, DatePicker, Input, Popconfirm, Table, Select, Divider, Space, Row, Col, Tag, Tooltip, Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useState, useRef, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './home.css';
import { state, classfyInput, selectStateOptions, fallback } from './components/config';
import { updateEquipment, getSelectEquipments, addEquipment, uploadPhoto, getEquipmentList, deleteEquipment, getFilters, addItems } from './service';
import dayjs from 'dayjs';
const { TextArea } = Input;
const uploadTip = (
    <div>
        <a>上传</a>
    </div>
)
//TODO:重构

const Home = () => {
    const location = useLocation();
    const { user_id, is_manager, username } = location.state;
    const [newEquipment, setNewEquipment] = useState(0);
    const [form] = Form.useForm();
    const [categories, setCategories] = useState([]);
    const [locations, setLocations] = useState([]);
    const [userFilter, setUserFilter] = useState([]);
    const [data, setData] = useState([]);
    const getEquipments = async (pageNum) => {
        const equipmentList = await getEquipmentList(pageNum);
        setData(equipmentList);
    }
    const getSelectList = async (pageNum, params) => {
        const equipmentList = await getSelectEquipments(pageNum, params);
        setData(equipmentList);
    }
    const getUserList = async () => {
        const username = await getFilters('user');
        const filter = username.map((item) => { return { text: item, value: item } });
        setUserFilter(filter);
    }
    const getCategories = async () => {
        const categoryList = await getFilters('category');
        const cateArray = categoryList.map((item) => { return item.category });
        setCategories(cateArray);
    };
    const getLocations = async () => {
        const locationsList = await getFilters('location');
        const locations = locationsList.map((item) => { return item.location });
        setLocations(locations);
    };
    useEffect(() => {
        getEquipments(1);
        getCategories();
        getLocations();
        getUserList();
    }, []);

    const [page, setPage] = useState(1);
    const [editingKey, setEditingKey] = useState('');
    const [file, setFile] = useState('');
    const [imageUrl, setImgUrl] = useState('');
    const restore = async (key) => {
        try {
            const newData = [...data];
            const index = newData.findIndex((item) => key === item.key);
            if (index > -1) {
                const item = newData[index];
                item.user_id = 0;
                item.username = '';
                item.is_receive = 0;
                item.state = 0;
                item.receive_time = '';
                await updateEquipment({ id: key, ...item })
            }
        } catch (error) {
            message.error('归还失败');
        } finally {
            getEquipments(page);
        }
    }
    const receive = async (key) => {
        try {
            const newData = [...data];
            const index = newData.findIndex((item) => key === item.key);
            if (index > -1) {
                const item = newData[index];
                item.user_id = user_id;
                item.username = username;
                item.is_receive = 1;
                item.state = 1;
                item.receive_time = new Date();
                await updateEquipment({ id: key, ...item })
            }
        } catch (error) {
            message.error('领用失败');
        } finally {
            getEquipments(page);
        }
    }
    const isEditing = (record) => {
        return record.key === editingKey;
    };
    const EditableCell = ({
        editing,
        dataIndex,
        title,
        inputType,
        record,
        index,
        children,
        ...restProps
    }) => {
        const [newCategory, setNewCategory] = useState('');
        const ctgInputRef = useRef(null);
        const [newLocation, setNewLocation] = useState('');
        const locaInputRef = useRef(null);
        let inputNode = <Input style={{ width: 100 }} />;
        if (inputType === 'textArea') {
            inputNode = <TextArea style={{ width: 100 }} autoSize={{ minRows: 1, maxRows: 3 }} />;
        }
        else if (inputType === 'upload') {
            const beforeUpload = (file) => {
                const isLt2M = file.size / 1024 / 1024 < 10;
                if (!isLt2M) {
                    message.error('图片大小不能超过10MB');
                }
                return isLt2M;
            }
            const props = {
                name: 'file',
                beforeUpload: beforeUpload,
                customRequest: async detail => {
                    setFile(detail.file);
                    const reader = new FileReader();
                    reader.readAsDataURL(detail.file);
                    reader.onload = function () {
                        setImgUrl(reader.result);
                    };
                    detail.onSuccess();
                },
                headers: {
                    authorization: 'authorization-text',
                },
                showUploadList: false,
                fileList: [],
            };
            inputNode = <div className='inputPhoto'>
                <div style={{ display: 'inline-block', verticalAlign: 'middle' }}>{imageUrl && <Image src={imageUrl} alt="pic" style={{ width: '20px' }} />}</div>
                <div style={{ display: 'inline-block' }}><Upload
                    {...props}
                    name="avatar"
                    className="avatar-uploader"
                    showUploadList={false}
                >
                    {uploadTip}
                </Upload></div>
            </div>
        }
        else if (inputType === 'selectState') {
            inputNode = <Select
                defaultValue="闲置"
                style={{ width: 100, }}
                options={selectStateOptions.map((item) => ({
                    label: item.label,
                    value: item.value,
                }))}
            />
        }
        else if (inputType === 'selectLocation') {
            const onNameChange = (event) => {
                setNewLocation(event.target.value);
            };
            const addItem = async (e) => {
                e.preventDefault();
                if (newLocation === '') return message.error('新位置不能为空');
                await addItems('location', { location: newLocation });
                getLocations();
                setNewLocation('');
                setTimeout(() => {
                    locaInputRef.current?.focus();
                }, 0);
            };
            inputNode = <Select
                style={{
                    width: 140,
                }}
                placeholder="输入位置"
                dropdownRender={(menu) => (
                    <>
                        {menu}
                        <Divider style={{ margin: '8px 0', }} />
                        <Space style={{ padding: '0 8px 4px', }}>
                            <Input
                                placeholder="新位置"
                                ref={locaInputRef}
                                value={newLocation}
                                onChange={onNameChange}
                            />
                            <Button type="text" icon={<PlusOutlined />} onClick={addItem}>
                            </Button>
                        </Space>
                    </>
                )}
                options={locations.map((item) => ({
                    label: item,
                    value: item,
                }))}
            />
        }
        else if (inputType === 'selectCategory') {
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
            const onNameChange = (event) => {
                setNewCategory(event.target.value);
            }
            inputNode = <Select
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
        }
        else if (inputType === 'DatePicker') {
            inputNode = <DatePicker
                style={{ width: 130 }} />
        }
        return (
            <td {...restProps}>
                {editing ? (
                    <Form.Item
                        name={dataIndex}
                        style={{
                            margin: 0,
                        }}
                    // rules={[
                    //     {
                    //         required: true,
                    //         message: `请输入${title}!`,
                    //     },
                    // ]}
                    >
                        {inputNode}
                    </Form.Item>
                ) : (
                    children
                )}
            </td>
        );
    };
    const handleAdd = async () => {
        if (editingKey === '') {
            const maxKey = Math.max(...data.map(item => item.key));
            const newData = {
                key: maxKey + 1,
                name: '',
                category: '',
                number: '',
                buy_time: '',
                state: 0,
                is_receive: 0,
                receive_time: '',
                user_id: 0,
                username: '',
                photo_url: '',
                location: '',
                configuration: ''
            };
            setData([...data, newData]);
            setNewEquipment(1);
        }
        else {
            message.error('请先保存正在编辑的设备信息');
        }
    }
    const edit = (record) => {
        form.setFieldsValue({
            name: '',
            category: '',
            number: '',
            ...record,
        });
        setEditingKey(record.key);
    };
    const cancel = () => {
        if (newEquipment === 1) {
            setData(data.slice(0, data.length - 1));
        }
        setImgUrl('');
        setEditingKey('');
        setNewEquipment(0);
    };
    const save = async (key) => {
        try {
            const row = await form.validateFields();
            if (newEquipment === 1) {
                const newEquiId = await addEquipment({ ...row });
                if (file !== '') await uploadPhoto(file, newEquiId);
                setNewEquipment(0);
            }
            else {
                if (file !== '') await uploadPhoto(file, key);
                if (row.state === 2) {
                    row.is_receive = 0;
                    row.receive_time = null;
                    row.user_id = 0;
                    row.username = '';
                }
                await updateEquipment({ id: key, ...row });
            }
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
            message.error('修改失败');
        } finally {
            getEquipments(page);
            setEditingKey('');
            setImgUrl('');
        }
    };
    async function del(key) {
        if (newEquipment) {
            setData(data.slice(0, data.length - 1));
            setNewEquipment(0)
        }
        else {
            const is_del = await deleteEquipment(key);
            if (is_del)
                await getEquipments(page);
            else
                message.error('删除失败');
        }
    }
    async function changePage(pageNum) {
        setPage(pageNum);
        const equipmentList = await getEquipmentList(pageNum);
        setData(equipmentList);
    }

    const columns = [{
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        width: 150,
        editable: true,
        sorter: (a, b) => {
            return a.name.localeCompare(b.name);
        },
        className: 'dark',
        fixed: 'left'
    },
    {
        title: '类别',
        dataIndex: 'category',
        className: 'dark',
        width: 170,
        filters: categories.map((item) => { return { text: item, value: item } }),
        sorter: (a, b) => {
            return a.category.localeCompare(b.category);
        },
        filterSearch: true,
        key: 'category',
        editable: true,
        fixed: 'left'
    },
    {
        title: '资产编号',
        dataIndex: 'number',
        key: 'number',
        // width: 150,
        editable: true,
        sorter: (a, b) => {
            return a.number.localeCompare(b.number);
        },
        className: 'dark'
    }, {
        title: '购入时间',
        dataIndex: 'buy_time',
        width: 200,
        className: 'dark',
        sorter: (a, b) => {
            return a.buy_time - b.buy_time;
        },
        render: (buy_time) => {
            return dayjs(buy_time).isValid() ? dayjs(buy_time).format('YYYY-MM-DD') : '';
        },
        key: 'buy_time',
        required: false,
        editable: true,
    },
    {
        title: '使用者',
        dataIndex: 'username',
        // width: 150,
        filters: userFilter,
        filterSearch: true,
        sorter: (a, b) => {
            if (!a.username) a.username = '';
            if (!b.username) b.username = '';
            return a.username.localeCompare(b.username);
        },
        key: 'username',
        editable: false,
        className: 'dark'
    }, {
        title: '领用时间',
        dataIndex: 'receive_time',
        className: 'dark',
        key: 'receive_time',
        sorter: (a, b) => {
            return a.receive_time - b.receive_time;
        },
        render: (receive_time) => {
            return dayjs(receive_time).isValid() ? dayjs(receive_time).format('YYYY-MM-DD') : '';
        },
        editable: false,
    }, {
        title: '使用状态',
        dataIndex: 'state',
        key: 'state',
        className: 'dark',
        width: 130,
        sorter: (a, b) => {
            return a.state - b.state;
        },
        render: (state) => {
            switch (state) {
                case 0:
                    return <Badge status="warning" text="闲置" />
                case 2:
                    return <Badge status="error" text="已报废" />
                default:
                    return <Badge status="success" text="正在使用" />
            }
        },
        filters: state,
        editable: true,
    }, {
        title: '位置',
        dataIndex: 'location',
        className: 'dark',
        key: 'location',
        width: 160,
        sorter: (a, b) => {
            if (!a.location) a.location = '';
            if (!b.location) b.location = '';
            return a.location.localeCompare(b.location);
        },
        filters: locations.map((item) => { return { text: item, value: item } }),
        filterSearch: true,
        editable: true,
    }, {
        title: '配置',
        dataIndex: 'configuration',
        required: false,
        key: 'configuration',
        className: 'dark',
        editable: true,
    },
    {
        title: '照片',
        dataIndex: 'photo_url',
        key: 'photo_url',
        required: false,
        // width: 120,
        render: photo_url => {
            return <Image
                width={20}
                fallback={fallback}
                src={photo_url} />

        },
        editable: true,
        className: 'dark',
    },
    {
        title: '操作',
        dataIndex: 'operation',
        className: 'dark',
        width: 150,
        fixed: 'right',
        render: (text, record) => {
            const editable = isEditing(record);
            return is_manager === 1 ? editable ? (<span>
                <a
                    onClick={() => save(record.key)}
                    style={{
                        marginRight: 8,
                    }}
                >
                    保存
                </a>
                <Popconfirm title="确认取消？" onConfirm={cancel}>
                    <a>取消</a>
                </Popconfirm>
            </span>) : (
                <Row gutter={[8, 8]}>
                    <Col span={8}>

                        <Tag color={editingKey === '' ? 'green' : 'gray'} onClick={() => {
                            if (editingKey === '') {
                                edit(record);
                                record.photo_url ? setImgUrl(record.photo_url) : setImgUrl('');
                            }
                        }} >编辑</Tag>
                    </Col>
                    <Col span={8}>
                        <Popconfirm title="确认删除？" onConfirm={() => del(record.key)}>
                            <Tag color="red" >删除</Tag>
                        </Popconfirm>
                    </Col>
                </Row>
            ) : (record.user_id === user_id ?
                <Row gutter={[8, 8]}>
                    <Col span={10}>
                        <Popconfirm title="确认归还？" onConfirm={() => restore(record.key)}>
                            <Tag color="magenta" >归还</Tag>
                        </Popconfirm>
                    </Col>
                </Row> : (record.state === 0 ? <Row gutter={[8, 8]}>
                    <Col span={10}>
                        <Popconfirm title="确认领用？" onConfirm={() => receive(record.key)}>
                            <Tag color="geekblue">领用</Tag>
                        </Popconfirm>
                    </Col>
                </Row> : <></>)
            );
        },
    }];
    const mergedColumns = columns.map((col) => {
        if (!col.editable) {
            return col;
        }
        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: classfyInput(col.dataIndex),
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });



    const onTableChange = async (pagination, filters, sorter) => {
        try {
            class Params{
                constructor(){
                    this.name = '';
                }
                paramsFactory(prop) {
                    if (filters[prop]) {
                        filters[prop].map((item) => {
                            return this.name = this.name + prop+'=' + item + '&';
                        })
                    }
                    return this;
                } 
            }
            if (!filters.category && !filters.state && !filters.location && !filters.username) {
                await getEquipments(page);
            }
            else {
                const params=new Params().paramsFactory('category').paramsFactory('state').paramsFactory('location').paramsFactory('username').name.slice(0, -1);
                await getSelectList(pagination.current, params)
            }
        } catch (error) {
            message.error('查询失败');
        }
    };
    return (
        <div className='home'>
            <Form form={form} component={false}>
                {is_manager === 1 && <div className='addBtn'>
                    <Row justify="space-around" align="middle" gutter={[0, 14]}>
                        <Col span={2}>
                            <Tooltip placement="top" title={'数据将被添加至列表尾部'}>
                                <Button
                                    onClick={handleAdd}
                                    type="primary"
                                    style={{ backgroundColor: '#36304A', color: 'white', fontWeight: 'bolder' }}
                                >
                                    添加设备
                                </Button>
                            </Tooltip>
                        </Col>
                    </Row>
                </div>}
                <Table
                    components={{
                            body: {
                                cell: EditableCell,
                            },
                        }}
                    bordered={false}
                    dataSource={data}
                    onChange={onTableChange}
                    columns={mergedColumns}
                    scroll={{
                        x: window.screen.width,
                    }}
                    rowClassName={(record, index) => {
                        let className = index % 2 ? 'deep' : 'shallow';
                        return className
                    }}
                    pagination={{
                        onChange: changePage,
                        pageSize: 10
                    }}
                />
            </Form>
        </div>
    );
};
export default Home;