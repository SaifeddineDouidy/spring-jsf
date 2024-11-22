/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/webapp/**/*.xhtml',
    './src/main/webapp/**/*.js',
    './src/main/webapp/**/*.css',
    './src/main/java/**/*.java',
    "./node_modules/flowbite/**/*.js"
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')({
      charts: true,
      datatables: true
    }),
  ],
}
