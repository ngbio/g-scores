export default function RequestState({ status, error, retry }) {
  if (status === "loading")
    return (
      <div className="notice" role="status">
        <span className="spinner" aria-hidden="true" />
        Đang tải dữ liệu…
      </div>
    );
  if (status === "error")
    return (
      <div className="notice error" role="alert">
        <div>
          <strong>
            {error.status === 404
              ? "Không tìm thấy kết quả"
              : "Chưa thể tải dữ liệu"}
          </strong>
          <p>{error.message}</p>
        </div>
        <button className="button secondary" onClick={retry}>
          Thử lại
        </button>
      </div>
    );
  return null;
}
