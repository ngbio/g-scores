import { useState } from "react";
import useApi from "../hooks/useApi.js";
import RequestState from "../components/RequestState.js";
import { formatScore } from "../utils/format.js";
import { endpoints } from "../services/api.js";

export default function ScoreLookup() {
  const [input, setInput] = useState("");
  const [registrationNumber, setRegistrationNumber] = useState("");
  const [validation, setValidation] = useState("");
  const request = useApi(
    registrationNumber ? endpoints["score-lookup"](registrationNumber) : null,
  );

  function submit(event) {
    event.preventDefault();
    const raw = input.trim();
    const number = String(Number(raw));
    if (!/^[0-9]{1,10}$/.test(raw) || Number(raw) < 1 || Number(raw) > 2147483647) {
      setValidation(
        "Số báo danh phải là số nguyên từ 1 đến 2147483647.",
      );
      return;
    }
    setValidation("");
    setInput(number);
    if (number === registrationNumber) request.retry();
    else setRegistrationNumber(number);
  }

  return (
    <section>
      <div className="page-heading">
        <p className="eyebrow">Kết quả kỳ thi</p>
        <h1>Tra cứu điểm thi</h1>
        <p>Nhập số báo danh để xem kết quả từng môn của thí sinh.</p>
      </div>
      <div className="card search-card">
        <form onSubmit={submit} noValidate>
          <label htmlFor="registration-number">Số báo danh</label>
          <div className="search-row">
            <input
              id="registration-number"
              name="registrationNumber"
              type="text"
              inputMode="numeric"
              autoComplete="off"
              placeholder="Ví dụ: 1000001"
              minLength={1}
              maxLength={10}
              value={input}
              onChange={(event) => {
                setInput(event.target.value);
                setValidation("");
              }}
              aria-invalid={Boolean(validation)}
              aria-describedby={validation ? "sbd-error sbd-hint" : "sbd-hint"}
            />
            <button className="button" disabled={request.status === "loading"}>
              {request.status === "loading" ? "Đang tra cứu…" : "Tra cứu điểm"}
            </button>
          </div>
          <p id="sbd-hint" className="hint">
            Nhập số báo danh, ví dụ 1000001.
          </p>
          {validation && (
            <p id="sbd-error" className="field-error" role="alert">
              {validation}
            </p>
          )}
        </form>
      </div>
      <RequestState {...request} />
      {request.status === "idle" && (
        <div className="empty-state">
          <span className="empty-symbol" aria-hidden="true">
            ⌕
          </span>
          <h2>Sẵn sàng tra cứu</h2>
          <p>Kết quả điểm thi sẽ xuất hiện tại đây.</p>
        </div>
      )}
      {request.status === "success" && request.data && (
        <section
          className="card results"
          aria-label="Kết quả tra cứu"
          aria-live="polite"
        >
          <div className="card-heading">
            <div>
              <p className="eyebrow">Kết quả tra cứu</p>
              <h2>Số báo danh {request.data.registrationNumber}</h2>
            </div>
            {request.data.foreignLanguageCode && (
              <span className="tag">
                Mã ngoại ngữ: {request.data.foreignLanguageCode}
              </span>
            )}
          </div>
          <div className="score-grid">
            {request.data.scores.map((subject) => (
              <div className="score-item" key={subject.subjectCode}>
                <span>{subject.subjectName}</span>
                <strong>{formatScore(subject.score)}</strong>
              </div>
            ))}
          </div>
        </section>
      )}
    </section>
  );
}
