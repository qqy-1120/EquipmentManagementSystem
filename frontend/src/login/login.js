import './login.css';
import { message, Form, Input, Button } from 'antd';
import { useNavigate, Navigate } from 'react-router-dom';
import { Dropbox } from '@icon-park/react';
import { login } from './service';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
const onFinishFailed = (errorInfo) => {
  console.log('Failed:', errorInfo);
  message.error('登录失败')
};
function Login() {
  const navigate = useNavigate();
  const onFinish = async (values) => {
    var userrole = values.username === 'admin' ? 1 : 0;
    try {
      const { user_id, username, groupname, token } = await login({ is_manager: userrole, ...values })
      if (user_id) {
        localStorage.setItem('token', token)
        localStorage.setItem('groupname', groupname)
        localStorage.setItem('username', username)
        localStorage.setItem('user_id', user_id)
        navigate('home')
      }
    } catch (error) {
      console.log(error.message)
      message.error(error.message)
    }
  };
  return (
    localStorage.getItem('token') ? <Navigate to="/home" /> :
      <div className="App">
        <div className='logo'>
          <Dropbox theme="outline" size="45" fill="#333" />
        </div>

        <Form
          name="basic"
          // style={{
          //   maxWidth: 600,
          // }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          <Form.Item
          className='formItem'
            style={{
              width:'100%',
            }}
            name="username"
            rules={[
              {
                required: true,
                message: '输入用户名',
              },
              { type: 'string' },
              {
                max: 20,
                message: '用户名至多20位'
              },
              {
                whitespace: true,
                message: '输入不能全为空格'
              }

            ]}
          >
            <Input bordered={false} style={{height:'45px', backgroundColor: 'rgb(239, 239, 239)',}} prefix={<UserOutlined className="site-form-item-icon" />} placeholder="用户名" />
          </Form.Item>

          <Form.Item
             style={{
              width:'100%'
            }}
            className='formItem'
            name="password"
            rules={[
              {
                required: true,
                message: '输入密码',
              },
              {
                whitespace: true,
                message: '输入不能全为空格'
              },
              { type: 'string' },
              {
                max: 20,
                message: '密码至多20位'
              }
            ]}
          >
            <Input.Password 
            bordered={false} style={{height:'45px', backgroundColor: 'rgb(239, 239, 239)',}}
              prefix={<LockOutlined className="site-form-item-icon" />}
              // type="password"
              placeholder="密码"
            />
          </Form.Item>
          <Form.Item
          >
            <Button style={{ backgroundColor: '#36304A',fontWeight:'bold'}} className='loginButton' type='primary' htmlType="submit">
              登录
            </Button>
          </Form.Item>
        </Form>
      </div>


  );
}
export default Login;
