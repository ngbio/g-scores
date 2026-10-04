import useApi from "../hooks/useApi.js";
import RequestState from "../components/RequestState.js";
import { formatScore } from "../utils/format.js";
import { endpoints } from "../services/api.js";

export default function TopStudents() {
  const request = useApi(endpoints["top-students"]);
  const students = request.data || [];
  return (
    <section>
      <div className="page-heading">
        <p className="eyebrow">Kết quả nổi bật</p>
        <h1>Top 10 khối A</h1>
        <p>Thí sinh có tổng điểm Toán, Vật lí và Hóa học cao nhất.</p>
      </div>
      <RequestState {...request} />
      {request.status === "success" && (
        <div className="card table-card">
          <div className="card-heading">
            <h2>Danh sách thí sinh</h2>
            <span className="tag">Toán + Vật lí + Hóa học</span>
          </div>
          {students.length === 0 ? (
            <div className="empty-state">
              <h2>Chưa có thí sinh đủ điều kiện</h2>
              <p>Danh sách chỉ tính thí sinh có đủ điểm cả ba môn.</p>
              <button className="button secondary" onClick={request.retry}>
                Tải lại
              </button>
            </div>
          ) : (
            <div
              className="table-scroll"
              tabIndex={0}
              role="region"
              aria-label="Bảng top 10 khối A"
            >
              <table>
                <caption className="sr-only">
                  Top 10 khối A, sắp xếp theo tổng điểm giảm dần
                </caption>
                <thead>
                  <tr>
                    <th scope="col">STT</th>
                    <th scope="col">Số báo danh</th>
                    <th scope="col">Toán</th>
                    <th scope="col">Vật lí</th>
                    <th scope="col">Hóa học</th>
                    <th scope="col">Tổng điểm</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((student, index) => (
                    <tr key={student.registrationNumber}>
                      <td>{index + 1}</td>
                      <th scope="row" className="registration">
                        {student.registrationNumber}
                      </th>
                      <td>{formatScore(student.mathScore)}</td>
                      <td>{formatScore(student.physicsScore)}</td>
                      <td>{formatScore(student.chemistryScore)}</td>
                      <td className="total-score">
                        {formatScore(student.totalScore)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          <p className="table-note">
            Chỉ xét thí sinh có đủ điểm ba môn. Tổng điểm giảm dần; nếu bằng
            điểm, số báo danh tăng dần. Hiển thị tối đa 10 thí sinh.
          </p>
        </div>
      )}
    </section>
  );
}
