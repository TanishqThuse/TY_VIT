const express = require('express');
const app = express();

app.get('/', (req, res) => {
  res.send('Hello from Express!');
});

app.listen(3000, () => {
  console.log('Server running on http://localhost:3000');
});

// // Simple Hello World server
// const http = require('http');

// const server = http.createServer((req, res) => {
//   res.writeHead(200, {'Content-Type': 'text/plain'});
//   res.end('Hello, Node.js World!');
// });

// server.listen(3000, () => {
//   console.log('Server is running on http://localhost:3000');
// });