import { vi } from 'vitest';

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // deprecated
    removeListener: vi.fn(), // deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

const useSearchParamsMock = [{ entries: () => [] }];

vi.mock('react-router-dom', () => ({
  useParams: () => ({}),
  useSearchParams: vi.fn().mockImplementation(() => (useSearchParamsMock)),
  BrowserRouter: vi.fn().mockImplementation((props) => props.children),
  Link: vi.fn().mockImplementation((props) => props.children),
}));