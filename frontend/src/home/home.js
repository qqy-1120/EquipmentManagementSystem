import { Form, Image, Upload, Badge, DatePicker, Input, Popconfirm, Table, Select, Divider, Space, Row, Col, Tag, Button, message, Tooltip } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useState, useRef, useEffect } from 'react';
import './home.css';
import { useNavigate } from 'react-router-dom';
import { Logout, AddFour } from '@icon-park/react'
import { state, classfyInput, selectStateOptions, fallback, tableRules } from './components/config';
import { updateEquipment, addEquipment, uploadPhoto, deleteEquipment, getItems, addItems } from './service';
import dayjs from 'dayjs';
import { beforeUpload, equiFormat } from '../component/utils';
import { pageSize } from '../component/config';
import CollectionCreateForm from './components/collectionCreateForm';
import { shortInputLength, longInputLength, middleInputLength } from '../component/config';
import UploadCsv from './components/uploadCsv';
import ExportRecords from './components/exportRecords';
const { TextArea } = Input;
const uploadTip = (
    <div>
        <a>上传</a>
    </div>
)
//TODO:重构
const Home = () => {
    const navigate = useNavigate();
    const [form] = Form.useForm();
    const [categories, setCategories] = useState([]);
    const [locations, setLocations] = useState([]);
    const [userFilter, setUserFilter] = useState([]);
    const [data, setData] = useState([]);
    const getAllEquipments = async () => {
        try {
            const records = await getItems('equipments');
            const result = equiFormat(records);
            setData(result);
        }
        catch (error) {
            console.log(error, 'get all equipments error');
            message.error(error.message);
        }
    }
    const getUserList = async () => {
        try {
            const users = await getItems('users');
            const res = users.map((item) => { return item.username });
            setUserFilter(res);
        } catch (error) {
            console.log(error, 'get user list error');
            message.error(error.message);
        }
    }
    const getCategories = async () => {
        try {
            const categoryList = await getItems('categories');
            const cateArray = categoryList.map((item) => { return item.category });
            setCategories(cateArray);
        } catch (error) {
            console.log(error, 'get categories error');
            message.error(error.message);
        }
    };
    const getLocations = async () => {
        try {
            const locationsList = await getItems('locations');
            const locations = locationsList.map((item) => { return item.location });
            setLocations(locations);
        } catch (error) {
            console.log(error, 'get locations error');
            message.error(error.message);
        }
    };
    useEffect(() => {
        getAllEquipments();
        getCategories();
        getLocations();
        getUserList();
    }, []);

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
            console.log(error, 'restore error');
            message.error(error.message);
        } finally {
            getAllEquipments();
        }
    }
    const receive = async (key) => {
        try {
            const newData = [...data];
            const index = newData.findIndex((item) => key === item.key);
            if (index > -1) {
                const item = newData[index];
                item.user_id = localStorage.getItem('user_id');
                item.username = localStorage.getItem('username');
                item.is_receive = 1;
                item.state = 1;
                item.receive_time = new Date();
                await updateEquipment({ id: key, ...item })
            }
        } catch (error) {
            console.log(error, 'receive error');
            message.error(error.message);
        } finally {
            getAllEquipments();
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
        let inputNode = <Input style={{ width: 100 }} maxLength={middleInputLength} />;
        if (inputType === 'shortTextArea') {
            inputNode = <TextArea style={{ width: 100 }} maxLength={shortInputLength} autoSize={{ minRows: 1, maxRows: 3 }} />;
        }
        else if (inputType === 'middleTextArea') {
            inputNode = <TextArea style={{ width: 100 }} maxLength={middleInputLength} autoSize={{ minRows: 1, maxRows: 3 }} />;
        }
        else if (inputType === 'longTextArea') {
            inputNode = <TextArea style={{ width: 100 }} maxLength={longInputLength} autoSize={{ minRows: 1, maxRows: 3 }} />;
        }
        else if (inputType === 'upload') {
            const props = {
                name: 'file',
                beforeUpload: beforeUpload,
                customRequest: async detail => {
                    try {
                        setFile(detail.file);
                        const reader = new FileReader();
                        reader.readAsDataURL(detail.file);
                        reader.onload = function () {
                            setImgUrl(reader.result);
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
                options={selectStateOptions}
            />
        }
        else if (inputType === 'selectLocation') {
            const onNameChange = (event) => {
                setNewLocation(event.target.value);
            };
            const addItem = async (e) => {
                try {
                    e.preventDefault();
                    if (newLocation === '') return message.error('新位置不能为空');
                    await addItems('location', { location: newLocation });
                    getLocations();
                    setNewLocation('');
                    setTimeout(() => {
                        locaInputRef.current?.focus();
                    }, 0);
                } catch (error) {
                    console.log(error, 'add location error');
                    message.error(error.message);
                }
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
                                maxLength={middleInputLength}
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
                try {
                    e.preventDefault();
                    if (newCategory === '') return message.error('新类别不能为空');
                    await addItems('category', { category: newCategory });
                    await getCategories();
                    setNewCategory('');
                    setTimeout(() => {
                        ctgInputRef.current?.focus();
                    }, 0);
                } catch (error) {
                    console.log(error, 'add category error');
                    message.error(error.message);
                }
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
                        {console.log(menu)}
                        <Divider style={{ margin: '8px 0', }} />
                        <Space style={{ padding: '0 8px 4px', }}>
                            <Input
                                placeholder="新类别"
                                ref={ctgInputRef}
                                value={newCategory}
                                onChange={onNameChange}
                                onKeyDown={(e) => e.stopPropagation()}
                                maxLength={shortInputLength}
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
                        rules={tableRules(title)}
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
        setFilteredInfo({ "categories": [], "usernames": [], "states": [], "locations": [] });
        setSortedInfo({});
        if (editingKey === '') {
            setOpen(true);
        }
        else {
            message.error('请先保存正在编辑的设备信息');
        }
    }
    const edit = async (record) => {
        form.setFieldsValue({
            // name: '',
            // category: '',
            // number: '',
            ...record,
        });
        // console.log(record, 'record')
        // const row=await form.validateFields();
        // console.log(row,'row')
        setEditingKey(record.key);
    };
    const cancel = () => {
        setImgUrl('');
        setEditingKey('');
    };
    const save = async (key) => {
        try {
            const row = await form.validateFields();
            console.log(row, 'row');
            if (file !== '') await uploadPhoto(file, key);
            if (row.state === 2 || row.state === 0) {
                row.is_receive = 0;
                row.receive_time = null;
                row.user_id = 0;
                row.username = '';
            }

            await updateEquipment({ id: key, ...row });
            message.success('保存成功');
        } catch (error) {
            console.log(error, 'save error');
            if (error.errorFields) message.error(error.errorFields[0].errors[0]);
            else message.error(error.message);
        } finally {
            getAllEquipments();
            setEditingKey('');
            setImgUrl('');
        }
    };
    async function del(key) {
        try {
            await deleteEquipment(key);
            await getAllEquipments();
            message.success('删除成功');
        } catch (error) {
            console.log(error, 'delete error');
            message.error(error.message);
        }
    }
    const [sortedInfo, setSortedInfo] = useState({});
    const [filteredInfo, setFilteredInfo] = useState({ "categories": [], "usernames": [], "states": [], "locations": [] });
    const columns = [{
        title: '名称',
        dataIndex: 'name',
        key: 'name',
        width: 150,
        editable: true,
        sortOrder: sortedInfo.columnKey === 'name' ? sortedInfo.order : null,
        sorter: (a, b) => {
            return a.name.localeCompare(b.name);
        },
        showSorterTooltip: false,
        className: 'dark',
        fixed: 'left'
    },
    {
        title: '类别',
        dataIndex: 'category',
        className: 'dark',
        width: 170,
        filters: categories.map((item) => { return { text: item, value: item } }),
        onFilter: (value, record) => record.category === value,
        filteredValue: filteredInfo.categories,
        sorter: (a, b) => {
            console.log(a.category, b.category, a.category.localeCompare(b.category))
            return a.category.localeCompare(b.category);
        },
        showSorterTooltip: false,
        sortOrder: sortedInfo.columnKey === 'categories' ? sortedInfo.order : null,
        filterSearch: true,
        key: 'categories',
        editable: true,
        fixed: 'left'
    },
    {
        title: '资产编号',
        dataIndex: 'number',
        key: 'number',
        // width: 150,
        editable: true,
        sortOrder: sortedInfo.columnKey === 'number' ? sortedInfo.order : null,
        sorter: (a, b) => {
            return a.number.localeCompare(b.number);
        },
        showSorterTooltip: false,
        className: 'dark'
    }, {
        title: '购入时间',
        dataIndex: 'buy_time',
        width: 200,
        className: 'dark',
        sortOrder: sortedInfo.columnKey === 'buy_time' ? sortedInfo.order : null,
        sorter: (a, b) => {
            return a.buy_time - b.buy_time;
        },
        showSorterTooltip: false,
        render: (buy_time) => {
            return dayjs(buy_time).isValid() ? dayjs(buy_time).format('YYYY-MM-DD') : '';
        },
        key: 'buy_time',
        editable: true,
    },
    {
        title: '使用者',
        dataIndex: 'username',
        sortOrder: sortedInfo.columnKey === 'usernames' ? sortedInfo.order : null,
        showSorterTooltip: false,
        // width: 150,
        filters: userFilter.map((item) => { return { text: item, value: item } }),
        onFilter: (value, record) => record.username === value,
        filteredValue: filteredInfo.usernames,
        filterSearch: true,
        sorter: (a, b) => {
            return a.username.localeCompare(b.username);
        },
        key: 'usernames',
        editable: false,
        className: 'dark'
    }, {
        title: '领用时间',
        dataIndex: 'receive_time',
        className: 'dark',
        key: 'receive_time',
        sortOrder: sortedInfo.columnKey === 'receive_time' ? sortedInfo.order : null,
        showSorterTooltip: false,
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
        key: 'states',
        className: 'dark',
        width: 130,
        sortOrder: sortedInfo.columnKey === 'states' ? sortedInfo.order : null,
        sorter: (a, b) => {
            return a.state - b.state;
        },
        showSorterTooltip: false,
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
        onFilter: (value, record) => record.state === value,
        filteredValue: filteredInfo.states,
        editable: true,
    }, {
        title: '位置',
        dataIndex: 'location',
        className: 'dark',
        key: 'locations',
        width: 160,
        sortOrder: sortedInfo.columnKey === 'locations' ? sortedInfo.order : null,
        sorter: (a, b) => {
            return a.location.localeCompare(b.location);
        },
        showSorterTooltip: false,
        filters: locations.map((item) => { return { text: item, value: item } }),
        onFilter: (value, record) => record.location === value,
        filteredValue: filteredInfo.locations,
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
            console.log(record.user_id, localStorage.getItem('user_id'), 'kc user')
            return localStorage.getItem('groupname') === 'ADMIN' ? editable ? (<span>
                <a
                    onClick={() => save(record.key)}
                    style={{
                        marginRight: 8,
                    }}
                >
                    保存
                </a>
                <Popconfirm okText="确认" cancelText="取消" title="确认取消？" onConfirm={cancel}>
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
                        }} ><a style={{ color: '#52c41a' }}>编辑</a></Tag>
                    </Col>
                    <Col span={8}>
                        <Popconfirm okText="确认" cancelText="取消" title="确认删除？" onConfirm={() => del(record.key)}>
                            <Tag color="red" ><a style={{ color: '#ff4d4f' }}>删除</a></Tag>
                        </Popconfirm>
                    </Col>
                </Row>
            ) : (record.user_id === parseInt(localStorage.getItem('user_id'), 10) ?
                <Row gutter={[8, 8]}>
                    <Col span={10}>
                        <Popconfirm okText="确认" cancelText="取消" title="确认归还？" onConfirm={() => restore(record.key)}>
                            <Tag color="magenta" ><a style={{ color: '#ff4d4f' }}>归还</a></Tag>
                        </Popconfirm>
                    </Col>
                </Row> : (record.state === 0 ? <Row gutter={[8, 8]}>
                    <Col span={10}>
                        <Popconfirm okText="确认" cancelText="取消" title="确认领用？" onConfirm={() => receive(record.key)}>
                            <Tag color="geekblue"><a style={{ color: '#1d39c4' }}>领用</a></Tag>
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
        console.log(filters, sorter, pagination)
        setSortedInfo(sorter);
        setFilteredInfo(filters);
    }
    const [open, setOpen] = useState(false);
    const [newImg, setNewImg] = useState('');
    const changeNewImgUrl = (url) => {
        setNewImg(url);
    }
    const [imageFile, setImageFile] = useState('');
    const changeImageFile = (file) => {
        setImageFile(file);
    }
    const onCreate = async (values) => {
        const newData = {
            name: values.name,
            category: values.category,
            number: values.number,
            buy_time: values.buy_time,
            state: 0,
            is_receive: 0,
            receive_time: '',
            user_id: 0,
            username: '',
            location: values.location,
            configuration: values.configuration
        };
        try {
            const id = await addEquipment({ ...newData });
            if (imageFile !== '') await uploadPhoto(imageFile, id);
            await getAllEquipments();
            message.success('添加成功');
        }
        catch (error) {
            console.log(error, 'create new equipment error');
            message.error(error.message);
        }
        finally {
            setOpen(false);
            setNewImg('');
            setImageFile('');
        }
    };
    return (
        <div className='home'>
            <Form form={form} component={false}>
                {localStorage.getItem('groupname') === 'ADMIN' ?
                    <div className='controlBut'>
                        <Space size='large'>
                        <div className='iconButton'><UploadCsv getAllEquipments={getAllEquipments} /></div>
                        <div className='iconButton'><ExportRecords getAllEquipments={getAllEquipments} /></div>
                        <div className='iconButton'>
                            <Tooltip title="添加设备">
                                <AddFour theme="filled" size="25" fill="#36304A" strokeLinejoin="bevel" onClick={handleAdd} />
                            </Tooltip>
                        </div>
                        <div className='iconButton'>
                            <Tooltip title="退出">
                                <Logout theme="outline" size="25" fill="#36304A" strokeLinejoin="miter" strokeLinecap="square" onClick={() => {
                                    localStorage.removeItem('token');
                                    localStorage.removeItem('groupname');
                                    localStorage.removeItem('username');
                                    localStorage.removeItem('user_id');
                                    navigate('../')
                                }} />
                            </Tooltip>
                        </div>
                        </Space>
                    </div>
                    : <div className='logout'>
                        <Tooltip title="退出">
                            <Logout theme="outline" size="25" fill="#36304A" strokeLinejoin="miter" strokeLinecap="square" onClick={() => {
                                localStorage.removeItem('token');
                                localStorage.removeItem('groupname');
                                localStorage.removeItem('username');
                                localStorage.removeItem('user_id');
                                navigate('../')
                            }} />
                        </Tooltip>
                    </div>}

                <CollectionCreateForm
                    open={open}
                    newImg={newImg}
                    changeNewImgUrl={changeNewImgUrl}
                    onCreate={onCreate}
                    changeImageFile={changeImageFile}
                    onCancel={() => {
                        setOpen(false);
                        setNewImg('');
                        setImageFile('');
                    }}
                />
                <div className={localStorage.getItem('groupname') === 'ADMIN' ? 'admin' : 'user'}>
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
                            pageSize: pageSize
                        }}
                    />
                </div>
            </Form>

        </div>
    );
};
export default Home;