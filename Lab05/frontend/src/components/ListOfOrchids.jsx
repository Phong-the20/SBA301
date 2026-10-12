import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getOrchids, deleteOrchid } from "../services/orchidApi";

export default function ListOfOrchids() {
  const [orchids, setOrchids] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await getOrchids();
      setOrchids(response.data);
      setError("");
    } catch (e) {
      setError(e.message || "Không thể kết nối đến Backend API");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleDelete = async (id, name) => {
    if (!window.confirm(`Bạn có chắc chắn muốn xóa hoa lan "${name || id}"?`)) return;
    try {
      await deleteOrchid(id);
      await loadData();
    } catch (e) {
      setError(e.message || "Lỗi khi xóa hoa lan");
    }
  };

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" style={{ width: "3rem", height: "3rem" }} role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 text-muted fs-5">Đang tải danh sách hoa lan từ Backend (GET /orchids)...</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
          <h2 className="fw-bold mb-1 fs-2">🌿 Danh Sách Hoa Lan (Orchids)</h2>
          <p className="text-muted mb-0 fs-6">Hiển thị {orchids.length} bản ghi lấy từ SQL Server qua Spring Boot JPA</p>
        </div>
        <div className="d-flex gap-2">
          <button className="btn btn-outline-secondary px-3 py-2 fs-6" onClick={loadData}>
            🔄 Làm mới
          </button>
          <Link className="btn btn-primary px-3 py-2 fs-6 fw-semibold" to="/orchids/new">
            ➕ Create Orchid
          </Link>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger alert-dismissible fade show fs-6" role="alert">
          <strong>⚠️ Lỗi:</strong> {error}
          <button type="button" className="btn-close" onClick={() => setError("")}></button>
        </div>
      )}

      {orchids.length === 0 ? (
        <div className="card text-center p-5 shadow-sm border-0 bg-light">
          <div className="display-3 mb-3">🌱</div>
          <h3 className="fw-bold">Chưa có dữ liệu hoa lan trong hệ thống</h3>
          <p className="text-muted fs-5">Bấm "Create Orchid" để thêm bản ghi đầu tiên vào SQL Server.</p>
          <div className="mt-3">
            <Link className="btn btn-primary px-4 py-2 fs-5" to="/orchids/new">
              Tạo hoa lan mới ngay
            </Link>
          </div>
        </div>
      ) : (
        <div className="card shadow-sm border-0 overflow-hidden">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-dark">
                <tr>
                  <th style={{ width: "70px" }}>ID</th>
                  <th style={{ width: "110px" }}>Hình ảnh</th>
                  <th>Tên hoa lan (Name)</th>
                  <th style={{ width: "180px" }}>Danh mục (Category)</th>
                  <th style={{ width: "140px" }} className="text-center">Tự nhiên</th>
                  <th style={{ width: "140px" }} className="text-center">Quyến rũ</th>
                  <th style={{ width: "200px" }} className="text-center">Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {orchids.map((o) => (
                  <tr key={o.orchidId}>
                    <td className="fw-bold text-secondary fs-5">#{o.orchidId}</td>
                    <td>
                      {o.orchidURL ? (
                        <img
                          src={o.orchidURL}
                          alt={o.orchidName}
                          referrerPolicy="no-referrer"
                          className="orchid-thumb"
                          onError={(e) => {
                            e.target.src = "https://images.unsplash.com/photo-1598880940371-c756e015fea1?w=200&auto=format&fit=crop&q=80";
                          }}
                        />
                      ) : (
                        <div className="orchid-thumb-placeholder">
                          🌸
                        </div>
                      )}
                    </td>
                    <td>
                      <div className="orchid-name">{o.orchidName}</div>
                      {o.orchidDescription && (
                        <div className="orchid-desc text-truncate" style={{ maxWidth: "340px" }}>
                          {o.orchidDescription}
                        </div>
                      )}
                    </td>
                    <td>
                      <span className="badge bg-secondary text-uppercase">{o.orchidCategory || "Chưa phân loại"}</span>
                    </td>
                    <td className="text-center">
                      {o.natural ? (
                        <span className="badge bg-success">🌿 Yes</span>
                      ) : (
                        <span className="badge bg-light text-dark border">🧪 No</span>
                      )}
                    </td>
                    <td className="text-center">
                      {o.attractive ? (
                        <span className="badge bg-warning text-dark">✨ Yes</span>
                      ) : (
                        <span className="badge bg-light text-muted border">No</span>
                      )}
                    </td>
                    <td className="text-center">
                      <div className="d-flex justify-content-center gap-2">
                        <Link className="btn btn-outline-warning btn-sm fw-semibold" to={`/orchids/${o.orchidId}/edit`}>
                          ✏️ Edit
                        </Link>
                        <button className="btn btn-outline-danger btn-sm fw-semibold" onClick={() => handleDelete(o.orchidId, o.orchidName)}>
                          🗑️ Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
