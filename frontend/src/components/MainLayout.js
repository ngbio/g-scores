import { useEffect, useRef } from "react";
import { NavLink, Outlet, useLocation } from "react-router-dom";

export default function MainLayout() {
  const { pathname } = useLocation();
  const contentRef = useRef(null);
  useEffect(() => {
    const titles = {
      "/tra-cuu": "Tra cứu điểm",
      "/thong-ke": "Thống kê theo môn",
      "/top-10": "Top 10 khối A",
    };
    document.title = `${titles[pathname] || "Điểm thi THPT 2024"} · G-Scores`;
    contentRef.current?.scrollTo({ top: 0, behavior: "instant" });
  }, [pathname]);
  return (
    <div className="app">
      <a
        className="skip-link"
        href="#content"
        onClick={(event) => {
          event.preventDefault();
          document.getElementById("content").focus();
        }}
      >
        Đến nội dung chính
      </a>

      <header className="header">
        <div className="header-inner">
          <NavLink className="brand" to="/tra-cuu">
            <span className="brand-icon" aria-hidden="true">
              G
            </span>
            G-Scores
          </NavLink>

          <span className="year-tag">Kỳ thi THPT 2024</span>
        </div>
      </header>

      <div className="body-layout">
        <aside className="sidebar">
          <nav className="sidebar-nav" aria-label="Điều hướng chính">
            <NavLink
              to="/tra-cuu"
              className={({ isActive }) =>
                isActive ? "nav-item active" : "nav-item"
              }
            >
              <span aria-hidden="true">01</span>
              Tra cứu điểm
            </NavLink>

            <NavLink
              to="/thong-ke"
              className={({ isActive }) =>
                isActive ? "nav-item active" : "nav-item"
              }
            >
              <span aria-hidden="true">02</span>
              Thống kê theo môn
            </NavLink>

            <NavLink
              to="/top-10"
              className={({ isActive }) =>
                isActive ? "nav-item active" : "nav-item"
              }
            >
              <span aria-hidden="true">03</span>
              Top 10 khối A
            </NavLink>
          </nav>
        </aside>

        <main className="main-content" id="content" ref={contentRef} tabIndex={0} aria-label="Nội dung trang">
          <div className="page-content" key={pathname}>
            <Outlet />
          </div>
        </main>
      </div>

      <footer className="footer">
        <span>G-Scores</span>

        <span>Tra cứu & thống kê điểm thi THPT 2024</span>
      </footer>
    </div>
  );
}
