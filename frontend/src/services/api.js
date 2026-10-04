import axios from "axios";

const api = axios.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL || "/api").replace(/\/$/, ""),
  timeout: 60000,
  headers: {
    Accept: "application/json",
  },
});

export const endpoints = {
  "score-lookup": (registrationNumber) =>
    `/students/${encodeURIComponent(registrationNumber)}/scores`,

  "score-distribution": "/reports/score-distribution",

  "top-students": "/reports/top-students?group=A",
};

export async function getApi(path, signal) {
  let response;
  try {
    response = await api.get(path, { signal });
  } catch (cause) {
    if (axios.isCancel(cause)) throw cause;
    const status = cause.response?.status;
    const serverMessage = cause.response?.data?.message;
    const message =
      status >= 500
        ? "Máy chủ chưa sẵn sàng. Vui lòng thử lại sau."
        : status
          ? typeof serverMessage === "string"
            ? serverMessage
            : "Yêu cầu không hợp lệ. Vui lòng kiểm tra lại."
          : cause.code === "ECONNABORTED" || cause.code === "ETIMEDOUT"
            ? "Yêu cầu mất quá nhiều thời gian. Vui lòng thử lại."
            : "Không thể kết nối máy chủ. Vui lòng kiểm tra kết nối và thử lại.";
    const error = new Error(message, { cause });
    error.status = status;
    throw error;
  }
  if (!response.data || !Object.hasOwn(response.data, "data")) {
    throw new Error("Dữ liệu phản hồi không hợp lệ. Vui lòng thử lại.");
  }
  return response.data.data;
}

export default api;
