import { userName, pwd } from '../svg';
import './login.css';
import { message } from 'antd';
import { useNavigate,Navigate } from 'react-router-dom';
import { Dropbox } from '@icon-park/react';
import { useState } from 'react';
import { login } from './service';
function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  return (
    localStorage.getItem('token')?<Navigate to="/home" />:
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
      <div className='loginButtonBox'>
      <button className='loginButton'
        onClick={async () => {
          var userrole=username==='admin'?1:0;
          try {
            const { user_id, groupname,token } = await login({username: username, password:password,is_manager:userrole}) 
            if (user_id) {  
              localStorage.setItem('token',token)
              localStorage.setItem('groupname',groupname) 
              localStorage.setItem('username',username)
              localStorage.setItem('user_id',user_id)                                                                                  
              navigate('home')
            }
          } catch (error) {
            message.error('用户名或密码错误')
          }
        }}>登录</button>
</div>
</div>

  );
}

export default Login;
