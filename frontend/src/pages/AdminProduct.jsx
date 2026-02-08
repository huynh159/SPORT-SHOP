import React, { useEffect, useState } from 'react';
import axiosClient from '../api/axiosClient';
import { toast } from 'react-toastify';

const AdminProduct = () => {
  // --- 1. CÁC BIẾN TRẠNG THÁI (STATE) ---
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState({ name: '', brand: '', categoryId: 1 });
  const [imageFile, setImageFile] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [currentId, setCurrentId] = useState(null);

  // --- 2. HÀM GỌI DANH SÁCH (LẤY DỮ LIỆU) ---
  const fetchProducts = async () => {
    try {
      const response = await axiosClient.get('/products');
      setProducts(response.data);
    } catch (error) {
      toast.error("Không thể lấy danh sách sản phẩm!");
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // --- 3. HÀM THÊM SẢN PHẨM MỚI ---
  const handleSubmit = async (e) => {
  e.preventDefault();
  const formData = new FormData();
  formData.append('product', new Blob([JSON.stringify(newProduct)], { type: 'application/json' }));
  if (imageFile) {
    formData.append('image', imageFile);
  } 

  try {
    if (isEditing) {
      // Gọi API Cập nhật (PUT)
      await axiosClient.put(`/products/${currentId}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      toast.success("Cập nhật thành công!");
    } else {
      // Gọi API Thêm mới (POST)
      if (!imageFile) return toast.error("Vui lòng chọn ảnh!");
      await axiosClient.post('/products', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      toast.success("Thêm mới thành công!");
    }
    fetchProducts();
    cancelEdit(); // Reset form về trạng thái Thêm mới
  } catch (error) {
    toast.error("Thao tác thất bại!");
  }
};

  // --- 4. HÀM XÓA SẢN PHẨM ---
  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa?")) {
      try {
        await axiosClient.delete(`/products/${id}`);
        toast.success("Xóa thành công!");
        fetchProducts();
      } catch (error) {
        toast.error("Lỗi: Bạn không có quyền xóa!");
      }
    }
  };

  const handleEditClick = (product) => {
  setIsEditing(true);
  setCurrentId(product.id);
  // Đổ dữ liệu cũ vào form (trừ file ảnh vì lý do bảo mật trình duyệt)
  setNewProduct({
    name: product.name,
    brand: product.brand,
    categoryId: product.category?.id || 1
  });
  // Cuộn trang lên đầu để người dùng thấy form
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

// Thêm hàm để hủy trạng thái sửa
const cancelEdit = () => {
  setIsEditing(false);
  setCurrentId(null);
  setNewProduct({ name: '', brand: '', categoryId: 1 });
  setImageFile(null);
};
  // --- 5. GIAO DIỆN (HIỂN THỊ) ---
  return (
    <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
      <h2>Hệ thống quản lý S-Mart</h2>

      {/* --- PHẦN FORM THÊM MỚI (NẰM Ở TRÊN) --- */}
      <div style={{ marginBottom: '40px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
        {/* Thay đổi tiêu đề Form */}
<h3>{isEditing ? `Đang sửa sản phẩm ID: ${currentId}` : "+ Thêm sản phẩm mới"}</h3>

<form onSubmit={handleSubmit}>
  {/* 1. Ô nhập tên sản phẩm - CỰC KỲ QUAN TRỌNG */}
  <input 
    type="text" 
    placeholder="Tên sản phẩm" 
    value={newProduct.name} // Ràng buộc dữ liệu để khi bấm Sửa nó hiện tên lên đây
    required 
    style={{ padding: '8px', marginRight: '10px' }}
    onChange={(e) => setNewProduct({...newProduct, name: e.target.value})} 
  />

  {/* 2. Ô nhập thương hiệu */}
  <input 
    type="text" 
    placeholder="Thương hiệu" 
    value={newProduct.brand} // Ràng buộc dữ liệu
    required 
    style={{ padding: '8px', marginRight: '10px' }}
    onChange={(e) => setNewProduct({...newProduct, brand: e.target.value})} 
  />
  
  {/* 3. Ô chọn file ảnh */}
  <input
    type="file"
    accept="image/*"
    style={{ marginRight: '10px' }}
    onChange={(e) => setImageFile(e.target.files?.[0] || null)}
  />

  {/* 4. Các nút bấm */}
  <button type="submit" style={{ background: isEditing ? '#ffc107' : '#28a745', color: 'white', padding: '8px 15px', border: 'none', cursor: 'pointer' }}>
    {isEditing ? "Cập nhật sản phẩm" : "Lưu vào hệ thống"}
  </button>

  {isEditing && (
    <button type="button" onClick={cancelEdit} style={{ marginLeft: '10px', padding: '8px 15px' }}>
      Hủy
    </button>
  )}
</form>
      </div>

      {/* --- PHẦN BẢNG DANH SÁCH (NẰM Ở DƯỚI) --- */}
      <table border="1" style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'center' }}>
        <thead>
          <tr style={{ background: '#333', color: 'white' }}>
            <th>ID</th>
            <th>Ảnh</th>
            <th>Tên</th>
            <th>Thương hiệu</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>
                <img 
                  src={`http://localhost:8080/uploads/${p.imageUrl}`} 
                  alt={p.name} 
                  style={{ width: '60px', height: '60px', objectFit: 'cover', borderRadius: '4px' }} 
                />
              </td>
              <td>{p.name}</td>
              <td>{p.brand}</td>
              <td>
                <button 
    onClick={() => handleEditClick(p)} // Gọi hàm khi bấm nút
    style={{ color: 'blue', border: 'none', background: 'none', cursor: 'pointer' }}
  >
    Sửa
  </button>
                
                
                
                <button onClick={() => handleDelete(p.id)} style={{ color: 'red', border: 'none', background: 'none', cursor: 'pointer', marginLeft: '10px' }}>
                  Xóa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminProduct;