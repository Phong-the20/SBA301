import axios from "axios";

// Step 8.1 & 8.2 - Cấu hình Axios Client
const getBaseUrl = () => {
  const envUrl = import.meta.env.VITE_API_URL;
  if (envUrl && !envUrl.includes("localhost")) {
    return envUrl;
  }
  // Tự động dùng đúng hostname đang truy cập (localhost, 127.0.0.1, hoặc IP mạng LAN)
  const host = typeof window !== "undefined" && window.location.hostname ? window.location.hostname : "localhost";
  return `http://${host}:8080/orchids`;
};

const api = axios.create({
  baseURL: getBaseUrl(),
  headers: { "Content-Type": "application/json" },
});

export const getOrchids = () => api.get("/");
export const getOrchid = (id) => api.get(`/${id}`);
export const createOrchid = (orchid) => api.post("/", orchid);
export const updateOrchid = (id, orchid) => api.put(`/${id}`, orchid);
export const deleteOrchid = (id) => api.delete(`/${id}`);
