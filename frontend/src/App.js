import { Navigate, Route, Routes } from "react-router-dom";

import MainLayout from "./components/MainLayout.js";

import ScoreLookup from "./features/ScoreLookup.js";
import ScoreDistribution from "./features/ScoreDistribution.js";
import TopStudents from "./features/TopStudents.js";

function App() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/tra-cuu" element={<ScoreLookup />} />

        <Route path="/thong-ke" element={<ScoreDistribution />} />

        <Route path="/top-10" element={<TopStudents />} />
      </Route>

      <Route path="/" element={<Navigate to="/tra-cuu" replace />} />

      <Route path="*" element={<Navigate to="/tra-cuu" replace />} />
    </Routes>
  );
}

export default App;
