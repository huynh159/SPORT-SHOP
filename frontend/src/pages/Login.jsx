import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Thêm cái này
import axiosClient from '../api/axiosClient';
import { toast } from 'react-toastify'; // Nếu bạn đã cài react-toastify

const Login = () => {
  const [credentials, setCredentials] = useState({ username: '', password: '' });
  const navigate = useNavigate(); // Khởi tạo navigate

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axiosClient.post('/auth/login', credentials);
      
      // Lưu token vào localStorage
      localStorage.setItem('token', response.data); 
      
      // Thông báo đẹp hơn thay vì alert
      console.log("Đăng nhập thành công, Token:", response.data);
      
      // Chuyển hướng mượt mà không load lại trang
      navigate('/admin/products'); 
    } catch (error) {
      console.error("Lỗi đăng nhập:", error.response?.data);
      alert('Sai tài khoản hoặc mật khẩu hoặc tài khoản chưa kích hoạt!');
    }
  };

  return (
    <div className="login-container" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '50px' }}>
      <h2>S-Mart Login</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
        <input 
          type="text" 
          placeholder="Username" 
          required
          onChange={(e) => setCredentials({...credentials, username: e.target.value})} 
        />
        <input 
          type="password" 
          placeholder="Password" 
          required
          onChange={(e) => setCredentials({...credentials, password: e.target.value})} 
        />
        <button type="submit" style={{ cursor: 'pointer', padding: '10px', background: '#007bff', color: 'white', border: 'none', borderRadius: '4px' }}>
          Đăng nhập
        </button>
      </form>
    </div>
  );
};

export default Login;