import react from "@vitejs/plugin-react";
import { defineConfig, loadEnv, transformWithOxc } from "vite";

export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, import.meta.dirname, "API_");
  const proxy = {
    "/api": {
      target: env.API_PROXY_TARGET || "http://localhost:8081",
      changeOrigin: true,
    },
  };
  return {
    plugins: [
      {
        name: "jsx-in-js",
        enforce: "pre",
        transform(code, id) {
          if (!/\/src\/.*\.js$/.test(id)) return null;
          return transformWithOxc(code, id, {
            lang: "jsx",
            jsx: { runtime: "automatic", development: command === "serve" },
          });
        },
      },
      react(),
    ],
    optimizeDeps: { rolldownOptions: { moduleTypes: { ".js": "jsx" } } },
    build: { rolldownOptions: { moduleTypes: { ".js": "jsx" } } },
    server: { port: 5173, proxy },
    preview: { proxy },
  };
});
