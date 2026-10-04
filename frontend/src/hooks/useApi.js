import { useEffect, useState } from "react";
import { getApi } from "../services/api.js";

export default function useApi(path) {
  const [attempt, setAttempt] = useState(0);

  const [result, setResult] = useState({
    key: null,
    status: "idle",
    data: null,
    error: null,
  });

  const key = path ? `${path}:${attempt}` : null;

  useEffect(() => {
    if (!path) return;

    const controller = new AbortController();

    getApi(path, controller.signal).then(
      (data) => {
        if (!controller.signal.aborted) {
          setResult({
            key,
            status: "success",
            data,
            error: null,
          });
        }
      },

      (error) => {
        if (!controller.signal.aborted) {
          setResult({
            key,
            status: "error",
            data: null,
            error,
          });
        }
      },
    );

    return () => controller.abort();
  }, [path, key]);

  const current =
    result.key === key
      ? result
      : {
          status: path ? "loading" : "idle",
          data: null,
          error: null,
        };

  return {
    status: current.status,
    data: current.data,
    error: current.error,

    retry: () => {
      setAttempt((value) => value + 1);
    },
  };
}
