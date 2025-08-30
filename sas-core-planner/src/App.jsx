import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import HomePage from "./HomePage.jsx";
import CoursesPage from "./CoursesPage.jsx";
import RegisterPage from "./RegisterPage.jsx";
import LoginPage from "./LoginPage.jsx";

function App() {
  return (
    <BrowserRouter basename="/sas_core_planner/sas-core-planner">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/courses" element={<CoursesPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;