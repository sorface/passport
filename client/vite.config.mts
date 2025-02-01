import { defineConfig, ViteUserConfig } from 'vitest/config';
import react from '@vitejs/plugin-react-swc';

// https://vitejs.dev/config/
export default defineConfig({
  base: '/',
  plugins: [react()] as ViteUserConfig['plugins'],
  build: {
    outDir: 'build',
  },
  server: {
    port: 3001,
    proxy: {
      '/api': {
        target:
          process.env['VITE_PROXY_TARGET'] || 'http://localhost:8080/',
        changeOrigin: true,
        secure: true,
        ws: true,
      },
      '/oauth2': {
        target:
          process.env['VITE_PROXY_TARGET'] || 'http://localhost:8080/',
        changeOrigin: true,
        secure: true,
        ws: true,
      },
    },
  },
  test: {
    include: ['src/**/*.test.(ts|tsx)'],
    environment: 'jsdom',
    globals: true,
    setupFiles: './tests/setup.ts',
  },
});
