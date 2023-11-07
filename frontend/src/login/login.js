import { userName, pwd } from '../svg';
import './login.css';
import { Button, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { Dropbox } from '@icon-park/react';
import { useState } from 'react';
import { login } from './service';
function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  return (
    <div className="App">
      <div className='logo'>
        <Dropbox theme="outline" size="45" fill="#333" />
      </div>
      <div className='loginBox'>
        <div className='inputBox' >
          {userName}
          <input placeholder="用户名" value={username}
            onChange={(e) => {
              setUsername(e.target.value)
            }} style={{ marginLeft: "5%", backgroundColor: '#EFEFEF', border: 'none', outline: 'none' }} />
        </div>
        <div className='inputBox' style={{ marginTop: "8%" }}>
          {pwd}
          <input placeholder="密码"
            type="password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value)
            }} style={{ marginLeft: "5%", backgroundColor: '#EFEFEF', border: 'none', outline: 'none' }} />
        </div>
      </div>
      <Button type="primary" style={{ backgroundColor: '#5134AB', borderRadius: '40px', width: '20%', height: '50px', fontSize: '18px', marginTop: '5%' }}
        onClick={async () => {
          var userrole=username==='admin'?1:0;
          try {
            const { user_id, is_manager } = await login({username: username, password:password,is_manager:userrole}) 
            if (user_id) {
              navigate('home', { state: { user_id: user_id, is_manager: is_manager, username: username } })
            }
          } catch (error) {
            message.error('用户名或密码错误')
          }
        }}>登录</Button>

    </div>
  );
}

export default Login;
