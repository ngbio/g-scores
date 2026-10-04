import { useState } from "react";
import useApi from "../hooks/useApi.js";
import RequestState from "../components/RequestState.js";
import { formatCount } from "../utils/format.js";
import { endpoints } from "../services/api.js";

const levels = [
  {
    key: "atLeast8",
    label: "Từ 8 điểm",
    range: "8 ≤ điểm ≤ 10",
    color: "#0B1F33",
  },
  {
    key: "from6ToUnder8",
    label: "Từ 6 đến dưới 8",
    range: "6 ≤ điểm < 8",
    color: "#496278",
  },
  {
    key: "from4ToUnder6",
    label: "Từ 4 đến dưới 6",
    range: "4 ≤ điểm < 6",
    color: "#C6A15B",
  },
  { key: "under4", label: "Dưới 4 điểm", range: "Điểm < 4", color: "#dfcba4" },
];

export default function ScoreDistribution() {
  const request = useApi(endpoints["score-distribution"]);
  const [subjectCode, setSubjectCode] = useState("");
  const subjects = request.data || [];
  const selected =
    subjects.find((subject) => subject.subjectCode === subjectCode) ||
    subjects[0];
  const total = selected
    ? levels.reduce((sum, level) => sum + selected[level.key], 0)
    : 0;
  const max = selected
    ? Math.max(1, ...levels.map((level) => selected[level.key]))
    : 1;

  return (
    <section>
      <div className="page-heading">
        <p className="eyebrow">Phân bố kết quả</p>
        <h1>Thống kê theo môn</h1>
        <p>Số lượng thí sinh ở bốn mức điểm của từng môn thi.</p>
      </div>
      <RequestState {...request} />
      {request.status === "success" && !selected && (
        <div className="empty-state">
          <h2>Chưa có danh mục môn thi</h2>
          <p>Vui lòng thử lại sau khi dữ liệu được cập nhật.</p>
          <button className="button secondary" onClick={request.retry}>
            Tải lại
          </button>
        </div>
      )}
      {request.status === "success" && selected && (
        <>
          <div className="card">
            <div className="card-heading">
              <div>
                <h2>Phân bố điểm · {selected.subjectName}</h2>
                <p>
                  <strong>{formatCount(total)}</strong> thí sinh có điểm
                </p>
              </div>
              <div className="subject-filter">
                <label htmlFor="subject">Môn thi</label>
                <select
                  id="subject"
                  value={selected.subjectCode}
                  onChange={(event) => setSubjectCode(event.target.value)}
                >
                  {subjects.map((subject) => (
                    <option
                      key={subject.subjectCode}
                      value={subject.subjectCode}
                    >
                      {subject.subjectName}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            {total === 0 && (
              <p className="notice" role="status">
                Môn này chưa có dữ liệu điểm.
              </p>
            )}
            <figure
              className="chart"
              aria-label={`Biểu đồ phân bố điểm môn ${selected.subjectName}`}
            >
              <div className="chart-unit">
                Số thí sinh · Cột cao nhất:{" "}
                {formatCount(max === 1 && total === 0 ? 0 : max)}
              </div>
              <div className="bars">
                {levels.map((level) => (
                  <div className="bar-column" key={level.key}>
                    <span className="bar-value">
                      {formatCount(selected[level.key])}
                    </span>
                    <div className="bar-track">
                      <div
                        className="bar"
                        style={{
                          height: `${(selected[level.key] / max) * 100}%`,
                          backgroundColor: level.color,
                        }}
                      />
                    </div>
                    <span className="bar-label">{level.label}</span>
                    <span className="bar-range">{level.range}</span>
                  </div>
                ))}
              </div>
              <figcaption>
                Thống kê chỉ bao gồm các thí sinh có điểm môn đang chọn.
              </figcaption>
            </figure>
          </div>
          <div className="card table-card">
            <div className="card-heading">
              <h2>Tổng hợp tất cả môn</h2>
            </div>
            <div
              className="table-scroll"
              tabIndex={0}
              role="region"
              aria-label="Bảng thống kê theo môn"
            >
              <table>
                <caption className="sr-only">
                  Số thí sinh theo bốn mức điểm của từng môn
                </caption>
                <thead>
                  <tr>
                    <th scope="col">Môn thi</th>
                    {levels.map((level) => (
                      <th scope="col" key={level.key}>
                        {level.label}
                      </th>
                    ))}
                    <th scope="col">Tổng</th>
                  </tr>
                </thead>
                <tbody>
                  {subjects.map((subject) => (
                    <tr key={subject.subjectCode}>
                      <th scope="row">{subject.subjectName}</th>
                      {levels.map((level) => (
                        <td key={level.key}>
                          {formatCount(subject[level.key])}
                        </td>
                      ))}
                      <td>
                        {formatCount(
                          levels.reduce(
                            (sum, level) => sum + subject[level.key],
                            0,
                          ),
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </section>
  );
}
