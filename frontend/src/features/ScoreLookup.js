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
    const number = input.trim();
    if (!/^[0-9]{8,}$/.test(number) || number.length > 255) {
      setValidation(
        "Số báo danh phải gồm từ 8 đến 255 chữ số (0–9). Nếu thiếu số 0 ở đầu, hãy nhập đầy đủ.",
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
              placeholder="Ví dụ: 01000001"
              minLength={8}
              maxLength={255}
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
            Số báo danh gồm ít nhất 8 chữ số. Giữ nguyên số 0 ở đầu.
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
