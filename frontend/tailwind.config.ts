import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
          brand: {
              DEFAULT: 'var(--brand)',
              light: '#e8efe9' // This was in prototype, but not in globals.css
          },
          accent: 'var(--brand-2)',
          surface: 'var(--card)',
          muted: 'var(--muted)',
          ink: 'var(--ink)',
          line: 'var(--line)',
          bg: 'var(--bg)',
      }
    },
  },
  plugins: [],
}
export default config
