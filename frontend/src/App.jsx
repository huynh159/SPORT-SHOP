import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css'; // Đừng quên dòng này để có CSS của thông báo
import Login from './pages/Login';

// Bạn có thể tạo thêm một trang Home đơn giản để test sau khi login
const Home = () => <h1 style={{ textAlign: 'center' }}>Chào mừng đến với S-Mart!</h1>;

function App() {
  return (
    <Router>
      {/* ToastContainer giúp hiển thị thông báo ở góc màn hình */}
      <ToastContainer position="top-right" autoClose={3000} />
      
      <Routes>
        {/* Trang mặc định sẽ tự chuyển hướng sang login */}
        <Route path="/" element={<Navigate to="/login" />} />
        
        <Route path="/login" element={<Login />} />
        
        {/* Sau này bạn sẽ thêm các Route cho Admin ở đây */}
        <Route path="/home" element={<Home />} />
        
        {/* Trang 404 nếu người dùng nhập sai URL */}
        <Route path="*" element={<h1>404 - Không tìm thấy trang</h1>} />
      </Routes>
    </Router>
  );
}

export default App;